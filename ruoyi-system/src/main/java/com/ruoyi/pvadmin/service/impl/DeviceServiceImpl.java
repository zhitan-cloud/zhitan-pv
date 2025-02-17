package com.ruoyi.pvadmin.service.impl;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.enums.TimeTypeEnum;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateTimeUtil;
import com.ruoyi.common.utils.DoubleUtil;
import com.ruoyi.pvadmin.domain.dto.DeviceFactorDTO;
import com.ruoyi.pvadmin.domain.dto.DeviceGenerationStatsDTO;
import com.ruoyi.pvadmin.domain.dto.DeviceQueryDTO;
import com.ruoyi.pvadmin.domain.dto.DeviceSubmitDTO;
import com.ruoyi.pvadmin.domain.entity.*;
import com.ruoyi.pvadmin.domain.model.GenerationStatisticsItemModel;
import com.ruoyi.pvadmin.domain.model.IndexTemplateModel;
import com.ruoyi.pvadmin.domain.model.PowerGenerationTrendModel;
import com.ruoyi.pvadmin.domain.model.TagValue;
import com.ruoyi.pvadmin.domain.vo.*;
import com.ruoyi.pvadmin.mapper.*;
import com.ruoyi.pvadmin.service.IDeviceService;
import com.ruoyi.pvadmin.service.RealtimeDatabaseService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 设备管理Service业务层处理
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

    @Resource
    private DeviceMapper deviceMapper;

    @Resource
    private DataItemMapper dataItemMapper;

    @Resource
    private DeviceIndexMapper deviceIndexMapper;

    @Resource
    private PowerStationMapper powerStationMapper;

    @Resource
    private DeviceInspectionMapper deviceInspectionMapper;

    @Autowired
    private RealtimeDatabaseService realtimeDatabaseService;

    @Resource
    private ElectricityDataItemMapper electricityDataItemMapper;

    @Resource
    private DeviceTypeIndexTemplateMapper deviceTypeIndexTemplateMapper;


    /**
     * 查询设备管理
     *
     * @param id 设备管理主键
     * @return 设备管理
     */
    @Override
    public DeviceVO selectDeviceById(String id) {
        DeviceVO deviceVO = new DeviceVO();

        Device device = baseMapper.selectById(id);

        if (ObjectUtils.isEmpty(device)) {
            return deviceVO;
        }
        BeanUtils.copyProperties(device, deviceVO);

        PowerStation powerStation = powerStationMapper.selectById(device.getPowerStationId());

        deviceVO.setPowerStationName(ObjectUtils.isEmpty(powerStation) ? Constants.EMPTY : powerStation.getName());
        return deviceVO;
    }

    /**
     * 查询设备管理列表
     *
     * @param dto 设备管理
     * @return 设备管理
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<DeviceVO> selectDeviceList(DeviceQueryDTO dto) {

        return baseMapper.selectDeviceList(dto);
    }

    /**
     * 新增设备管理
     *
     * @param dto 设备管理
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertDevice(DeviceSubmitDTO dto) {
        // 判断是否已经存在
        if (isDuplicate(dto)) {
            return "设备已存在";
        }
        dto.setDeviceTypeId(dto.getDeviceTypeId());
        // 找到对应的点位模板
        List<IndexTemplateModel> templateModelList = deviceTypeIndexTemplateMapper.listByType(dto.getDeviceTypeId());
        if (CollectionUtils.isEmpty(templateModelList)) {
            return "未找到模板信息";
        }

        Device device = new Device();
        BeanUtils.copyProperties(dto, device);
        baseMapper.insert(device);
        // 生成设备与点位的关系
        saveDeviceIndex(device, templateModelList);
        return Constants.EMPTY;
    }

    /**
     * 修改设备管理
     *
     * @param dto 设备管理
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateDevice(DeviceSubmitDTO dto) {
        // 判断是否已经存在
        if (isDuplicate(dto)) {
            throw new ServiceException("设备已存在");
        }

        Device device = deviceMapper.selectById(dto.getId());
        if (ObjectUtils.isEmpty(device)) {
            throw new ServiceException("设备信息不存在");
        }
        dto.setDeviceTypeId(dto.getDeviceTypeId());
        // 找到对应的点位模板
        List<IndexTemplateModel> templateModelList = deviceTypeIndexTemplateMapper.listByType(dto.getDeviceTypeId());
        if (CollectionUtils.isEmpty(templateModelList)) {
            return "未找到模板信息";
        }
        // 如果修改后的类型与原先类型不一致，则需要重新生成
        if (!dto.getDeviceTypeId().equals(device.getDeviceTypeId())) {
            deviceIndexMapper.delete(Wrappers.<DeviceIndex>lambdaQuery().eq(DeviceIndex::getDeviceId, device.getId()));

            saveDeviceIndex(device, templateModelList);
        }
        BeanUtils.copyProperties(dto, device);
        deviceMapper.updateById(device);

        return Constants.EMPTY;
    }

    /**
     * 删除设备管理信息
     *
     * @param id 设备管理主键
     * @return 结果
     */
    @Override
    public int deleteDeviceById(String id) {
        return deviceMapper.deleteDeviceById(id);
    }

    /**
     * 获取设备点位
     *
     * @param id 设备id
     */
    @Override
    public List<DeviceIndexVO> getDeviceIndex(String id) {
        LambdaQueryWrapper<DeviceIndex> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeviceIndex::getDeviceId, id);
        List<DeviceIndex> list = deviceIndexMapper.selectList(queryWrapper);
        List<DeviceIndexVO> result = new ArrayList<>();
        BeanUtils.copyProperties(list, result);
        return result;
    }

    /**
     * 编辑倍率
     *
     * @param request 请求实体
     */
    @Override
    public void updateFactor(List<DeviceFactorDTO> request) {
        request.forEach(m ->
        {
            if (m.getFactor() <= 1d) {
                m.setFactor(1d);
            }
        });
        deviceIndexMapper.updateFactor(request);
    }

    /**
     * 根据设备id查询发电信息、收益信息
     *
     * @param id 设备id
     * @return 结果
     */
    @Override
    public PowerGenerationInfoVO getPowerGenerationInfo(String id) {

        Date date = new Date();
        Date beginTime = DateUtil.beginOfYear(date);
        Date endTime = DateUtil.endOfYear(date);

        List<ElectricityDataItem> dataItems = electricityDataItemMapper.selectList(Wrappers.<ElectricityDataItem>lambdaQuery()
                .select(ElectricityDataItem::getDataTime, ElectricityDataItem::getValue, ElectricityDataItem::getCost)
                .between(ElectricityDataItem::getDataTime, beginTime, endTime)
                .eq(ElectricityDataItem::getDeviceId, id)
        );

        if (CollectionUtils.isEmpty(dataItems)) {
            return new PowerGenerationInfoVO();
        }

        BigDecimal dayValue = BigDecimal.ZERO;
        BigDecimal monthValue = BigDecimal.ZERO;
        BigDecimal yearValue = BigDecimal.ZERO;
        BigDecimal dayCost = BigDecimal.ZERO;
        BigDecimal monthCost = BigDecimal.ZERO;
        BigDecimal yearCost = BigDecimal.ZERO;

        for (ElectricityDataItem dataItem : dataItems) {
            // 当日发电量
            String dayFormat = DateUtil.formatDate(date);
            if (dayFormat.equals(DateUtil.formatDate(dataItem.getDataTime()))) {
                dayCost = dayCost.add(dataItem.getCost());
                dayValue = dayValue.add(dataItem.getValue());
            }
            // 当月发电量
            String mongthFormat = DateUtil.format(date, DateTimeUtil.COMMON_PATTERN_TO_MONTH);
            if (mongthFormat.equals(DateUtil.format(dataItem.getDataTime(), DateTimeUtil.COMMON_PATTERN_TO_MONTH))) {
                monthCost = monthCost.add(dataItem.getCost());
                monthValue = monthValue.add(dataItem.getValue());
            }
            yearCost = yearCost.add(dataItem.getCost());
            yearValue = yearValue.add(dataItem.getValue());
        }

        return new PowerGenerationInfoVO(dayValue, monthValue, yearValue, dayCost,
                monthCost, yearCost);
    }

    /**
     * 根据设备id获取实时功率，功率、发电趋势信息
     *
     * @param id       设备id
     * @param timeType 时间类型
     * @return 结果
     */
    @Override
    public ImplementedPowerVO getImplementedPower(String id, TimeTypeEnum timeType) {
        ImplementedPowerVO powerVO = new ImplementedPowerVO();

        BigDecimal realTimePower = BigDecimal.ZERO;
        String tagCode = deviceIndexMapper.getIndexByIndexCode(id, Constants.TAG_CODE_IE);
        if (StringUtils.isNotBlank(tagCode)) {
            TagValue retrieve = realtimeDatabaseService.retrieve(tagCode);

            realTimePower = ObjectUtils.isEmpty(retrieve.getValue()) ?
                    BigDecimal.ZERO : BigDecimal.valueOf(retrieve.getValue())
                    .setScale(Constants.DIGIT_2, RoundingMode.HALF_UP);
        }
        powerVO.setRealTimePower(realTimePower);

        // 查询
        Date date = new Date();
        Date beginTime = DateUtil.beginOfDay(date);
        Date endTime = DateUtil.endOfDay(date);

        if (TimeTypeEnum.MONTH.equals(timeType)) {
            beginTime = DateUtil.beginOfMonth(date);
            endTime = DateUtil.endOfMonth(date);
        } else if (TimeTypeEnum.YEAR.equals(timeType)) {
            beginTime = DateUtil.beginOfYear(date);
            endTime = DateUtil.endOfYear(date);
        }

        List<ElectricityDataItem> dataItemList = electricityDataItemMapper.selectList(Wrappers.<ElectricityDataItem>lambdaQuery()
                .select(ElectricityDataItem::getDataTime, ElectricityDataItem::getValue)
                .between(ElectricityDataItem::getDataTime, beginTime, endTime)
                .eq(ElectricityDataItem::getDeviceId, id)
        );

        List<PowerGenerationTrendModel> modelList = new ArrayList<>();
        while (beginTime.before(endTime)) {

            PowerGenerationTrendModel trendModel = new PowerGenerationTrendModel();
            modelList.add(trendModel);

            if (TimeTypeEnum.DAY.equals(timeType)) {
                String formatDateTime = DateUtil.format(beginTime, DateTimeUtil.COMMON_PATTERN_TO_HOUR);

                trendModel.setTime(formatDateTime);
                BigDecimal value = dataItemList.stream()
                        .filter(li -> formatDateTime.equals(DateUtil.format(li.getDataTime(), DateTimeUtil.COMMON_PATTERN_TO_HOUR)))
                        .map(ElectricityDataItem::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
                trendModel.setValue(value);

                beginTime = DateUtil.offsetHour(beginTime, Constants.DIGIT_1);
            } else if (TimeTypeEnum.MONTH.equals(timeType)) {
                String formatDateTime = DateUtil.formatDate(beginTime);

                trendModel.setTime(formatDateTime);
                BigDecimal value = dataItemList.stream()
                        .filter(li -> formatDateTime.equals(DateUtil.formatDate(li.getDataTime())))
                        .map(ElectricityDataItem::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
                trendModel.setValue(value);

                beginTime = DateUtil.offsetDay(beginTime, Constants.DIGIT_1);
            } else if (TimeTypeEnum.YEAR.equals(timeType)) {
                String formatDateTime = DateUtil.format(beginTime, DateTimeUtil.COMMON_PATTERN_TO_MONTH);

                trendModel.setTime(formatDateTime);
                BigDecimal value = dataItemList.stream()
                        .filter(li -> formatDateTime.equals(DateUtil.format(li.getDataTime(), DateTimeUtil.COMMON_PATTERN_TO_MONTH)))
                        .map(ElectricityDataItem::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
                trendModel.setValue(value);

                beginTime = DateUtil.offsetMonth(beginTime, Constants.DIGIT_1);
            }
        }

        powerVO.setItemList(modelList);
        return powerVO;
    }

    /**
     * 查询逆变器发电统计
     *
     * @param dto 查询条件
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<DeviceGenerationStatsVO> getInverterGenerationStats(DeviceGenerationStatsDTO dto) {
        List<DeviceGenerationStatsVO> generationStatsVOList = new ArrayList<>();

        // 查询设备
        List<Device> deviceList = deviceMapper.listByPermissions(dto.getDeviceId(),dto.getAmmeter(), dto.getParams());
        if (CollectionUtils.isEmpty(deviceList)) {
            return generationStatsVOList;
        }
        List<String> deviceIds = deviceList.stream().map(Device::getId).collect(Collectors.toList());

        // 查询时间
        Date beginTime = DateUtil.beginOfDay(dto.getDataTime());
        Date endTime = DateUtil.endOfDay(beginTime);
        if (TimeTypeEnum.YEAR.equals(dto.getTimeTypeEnum())) {
            beginTime = DateUtil.beginOfYear(dto.getDataTime());
            endTime = DateUtil.endOfYear(beginTime);
        } else if (TimeTypeEnum.MONTH.equals(dto.getTimeTypeEnum())) {
            beginTime = DateUtil.beginOfMonth(dto.getDataTime());
            endTime = DateUtil.endOfMonth(beginTime);
        }

        // 查询数据
        Map<String, List<DataItem>> dataItemDeviceMap = dataItemMapper.selectList(Wrappers.<DataItem>lambdaQuery()
                .select(DataItem::getDeviceId, DataItem::getDataTime, DataItem::getValue).in(DataItem::getDeviceId, deviceIds)
                .between(DataItem::getDataTime, beginTime, endTime).eq(DataItem::getTimeType, dto.getDataItemTimeType())
        ).stream().collect(Collectors.groupingBy(DataItem::getDeviceId));


        for (Device device : deviceList) {
            DeviceGenerationStatsVO generationStatsVO = new DeviceGenerationStatsVO();
            generationStatsVO.setDeviceName(device.getName());
            generationStatsVO.setAmmeter(device.getAmmeter());
            List<GenerationStatisticsItemModel> timeList = new ArrayList<>();
            generationStatsVO.setTimeList(timeList);

            List<DataItem> dataItemList = dataItemDeviceMap.get(device.getId());

            Map<String, BigDecimal> dataItemMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(dataItemList)) {
                dataItemMap = dataItemList.stream().collect(Collectors.groupingBy(
                        item -> DateUtil.formatDateTime(item.getDataTime()),
                        Collectors.mapping(DataItem::getValue, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));
            }

            Date time = beginTime;
            BigDecimal sumValue = BigDecimal.ZERO;
            while (time.before(endTime)) {
                GenerationStatisticsItemModel itemVO = new GenerationStatisticsItemModel();
                String timeStr = DateUtil.formatDate(time);

                String key = DateUtil.formatDateTime(time);

                BigDecimal value = dataItemMap.get(key);
                value = ObjectUtils.isEmpty(value) ? BigDecimal.ZERO : value;

                itemVO.setValue(value);
                sumValue = sumValue.add(value);

                if (TimeTypeEnum.YEAR.equals(dto.getTimeTypeEnum())) {
                    timeStr = DateUtil.format(time, DateTimeUtil.COMMON_PATTERN_TO_MONTH);
                    time = DateUtil.offsetMonth(time, Constants.DIGIT_1);
                } else if (TimeTypeEnum.MONTH.equals(dto.getTimeTypeEnum())) {
                    time = DateUtil.offsetDay(time, Constants.DIGIT_1);
                } else {
                    timeStr = DateUtil.format(time, DateTimeUtil.COMMON_PATTERN_TO_HOUR);
                    time = DateUtil.offsetHour(time, Constants.DIGIT_1);
                }
                itemVO.setTime(timeStr);
                timeList.add(itemVO);
            }
            generationStatsVO.setSumValue(sumValue);

            generationStatsVOList.add(generationStatsVO);
        }
        return generationStatsVOList;
    }

    /**
     * 根据设备id获取交流测信息
     *
     * @param id 设备id
     * @return 结果
     */
    @Override
    public ACMeasurementsVO getACMeasurementsByDeviceId(String id) {
        ACMeasurementsVO measurementsVO = new ACMeasurementsVO();

        List<String> indexCodeList = deviceIndexMapper.listByDeviceId(id);
        if (CollectionUtils.isEmpty(indexCodeList)) {
            return measurementsVO;
        }
        // 电压
        String tagCodeUa = Constants.TAG_CODE_UA;
        String tagCodeUb = Constants.TAG_CODE_UB;
        String tagCodeUc = Constants.TAG_CODE_UC;
        // 电流
        String tagCodeIa = Constants.TAG_CODE_IA;
        String tagCodeIb = Constants.TAG_CODE_IB;
        String tagCodeIc = Constants.TAG_CODE_IC;
        // 总有功功率
        String tagCodeZyggl = Constants.TAG_CODE_ZYGGL;
        // 功率因数
        String tagCodeQ = Constants.TAG_CODE_Q;
        //  频率
        String tagCodeHz = Constants.TAG_CODE_HZ;
        // 查询实时数据
        List<String> tageCodeList = indexCodeList.stream().filter(li -> li.endsWith(tagCodeUa) || li.endsWith(tagCodeUb) ||
                li.endsWith(tagCodeUc) || li.endsWith(tagCodeIa) || li.endsWith(tagCodeIb) ||
                li.endsWith(tagCodeIc) || li.endsWith(tagCodeZyggl) || li.endsWith(tagCodeQ) || li.endsWith(tagCodeHz)
        ).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(tageCodeList)) {
            return measurementsVO;
        }
        List<TagValue> tagValueList = realtimeDatabaseService.retrieve(tageCodeList);

        for (TagValue tagValue : tagValueList) {
            if (tagValue.getTagCode().endsWith(tagCodeUa)) {
                measurementsVO.setAVoltage(DoubleUtil.formatDouble(tagValue.getValue()));
            } else if (tagValue.getTagCode().endsWith(tagCodeUb)) {
                measurementsVO.setBVoltage(DoubleUtil.formatDouble(tagValue.getValue()));
            } else if (tagValue.getTagCode().endsWith(tagCodeUc)) {
                measurementsVO.setCVoltage(DoubleUtil.formatDouble(tagValue.getValue()));
            } else if (tagValue.getTagCode().endsWith(tagCodeIa)) {
                measurementsVO.setACurrent(DoubleUtil.formatDouble(tagValue.getValue()));
            } else if (tagValue.getTagCode().endsWith(tagCodeIb)) {
                measurementsVO.setBCurrent(DoubleUtil.formatDouble(tagValue.getValue()));
            } else if (tagValue.getTagCode().endsWith(tagCodeIc)) {
                measurementsVO.setCCurrent(DoubleUtil.formatDouble(tagValue.getValue()));
            } else if (tagValue.getTagCode().endsWith(tagCodeZyggl)) {
                measurementsVO.setTotalActivePower(DoubleUtil.formatDouble(tagValue.getValue()));
            } else if (tagValue.getTagCode().endsWith(tagCodeQ)) {
                measurementsVO.setFactor(DoubleUtil.formatDouble(tagValue.getValue()));
            } else if (tagValue.getTagCode().endsWith(tagCodeHz)) {
                measurementsVO.setFrequency(DoubleUtil.formatDouble(tagValue.getValue()));
            }
        }
        return measurementsVO;
    }

    /**
     * 根据设备id查询逆变器信息
     *
     * @param id 逆变器id
     * @return 结果
     */
    @Override
    public InverterInfoVO getInverterInfo(String id) {
        InverterInfoVO inverterInfoVO = new InverterInfoVO();

        Device device = deviceMapper.selectById(id);
        if (ObjectUtils.isEmpty(device)) {
            return inverterInfoVO;
        }

        BeanUtils.copyProperties(device, inverterInfoVO);
        // 查询累计发电量

        BigDecimal sumValue = BigDecimal.ZERO;
        if (Boolean.TRUE.equals(device.getAmmeter())) {
            List<DataItem> dataItemList = dataItemMapper.selectList(Wrappers.<DataItem>lambdaQuery()
                    .select(DataItem::getValue).eq(DataItem::getDeviceId, id).eq(DataItem::getTimeType, TimeTypeEnum.MONTH.name())
            );
            if (CollectionUtils.isNotEmpty(dataItemList)) {
                sumValue = dataItemList.stream().map(DataItem::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
            }
        } else {
            TagValue retrieve = realtimeDatabaseService.retrieve(device.getCode() + Constants.TAG_CODE_CGL);
            if (ObjectUtils.isNotEmpty(retrieve) && ObjectUtils.isNotEmpty(retrieve.getValue())) {
                sumValue = BigDecimal.valueOf(retrieve.getValue());
            }
        }

        inverterInfoVO.setSumValue(sumValue);
        return inverterInfoVO;
    }

    /**
     * 根据设备id获取点检与维修信息
     *
     * @param id 设备id
     * @return 结果
     */
    @Override
    public List<DeviceInspectionInfoVO> listDeviceInspectionByDeviceId(String id) {

        Device device = deviceMapper.selectById(id);

        if (ObjectUtils.isEmpty(device)) {
            return Collections.emptyList();
        }

        List<DeviceInspection> deviceInspectionList = deviceInspectionMapper.selectList(
                Wrappers.<DeviceInspection>lambdaQuery()
                        .select(DeviceInspection::getDeviceCode,
                                DeviceInspection::getDeviceName,
                                DeviceInspection::getInspectionStartTime,
                                DeviceInspection::getInspectionStaff,
                                DeviceInspection::getInspectionResult,
                                DeviceInspection::getInspectionType,
                                DeviceInspection::getAnnex)
                        .eq(DeviceInspection::getDeviceCode, device.getCode())
                        .orderByAsc(DeviceInspection::getInspectionStartTime)
        );

        List<DeviceInspectionInfoVO> inspectionInfoVOList = new ArrayList<>(deviceInspectionList.size());

        if (CollectionUtils.isNotEmpty(deviceInspectionList)) {
            for (DeviceInspection deviceInspection : deviceInspectionList) {

                DeviceInspectionInfoVO infoVO = new DeviceInspectionInfoVO();

                BeanUtils.copyProperties(deviceInspection, infoVO);

                inspectionInfoVOList.add(infoVO);
            }
        }

        return inspectionInfoVOList;
    }

    /**
     * 保存设备与点位关系
     *
     * @param device            设备信息
     * @param templateModelList 点位模型集合
     */
    private void saveDeviceIndex(Device device, List<IndexTemplateModel> templateModelList) {
        for (IndexTemplateModel templateModel : templateModelList) {

            DeviceIndex deviceIndex = new DeviceIndex();
            deviceIndex.setId(IdWorker.getIdStr());
            deviceIndex.setIndexId(templateModel.getId());
            deviceIndex.setDeviceId(device.getId());
            String indexCode = device.getCode() + Constants.STR_UNDERLINE + templateModel.getCode();
            deviceIndex.setIndexCode(indexCode);
            String unit = StringUtils.isNotBlank(templateModel.getUnit()) ? templateModel.getUnit() : Constants.EMPTY;
            deviceIndex.setUnit(unit);
            deviceIndex.setCalcIndex(Constants.EMPTY);
            deviceIndex.setIndexType(templateModel.getIndexType());
            deviceIndex.setTagKey(templateModel.getTagKey());
            deviceIndex.setFactor(1f);
            deviceIndexMapper.insert(deviceIndex);
        }
    }

    /**
     * 校验模板是否存在
     *
     * @param dto 判断参数
     * @return 结果
     */
    private boolean isDuplicate(DeviceSubmitDTO dto) {

        int count = baseMapper.selectCount(Wrappers.<Device>lambdaQuery()
                .ne(StringUtils.isNotBlank(dto.getId()), Device::getId, dto.getId())
                .eq(Device::getCode, dto.getCode())
        );
        return count > Constants.DIGIT_0;
    }

}