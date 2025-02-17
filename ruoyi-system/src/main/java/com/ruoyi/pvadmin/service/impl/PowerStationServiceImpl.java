package com.ruoyi.pvadmin.service.impl;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.enums.TimeTypeEnum;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateTimeUtil;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.pvadmin.domain.dto.GenerationStatisticsDTO;
import com.ruoyi.pvadmin.domain.dto.PowerStationQueryDTO;
import com.ruoyi.pvadmin.domain.dto.PowerStationSubmitDTO;
import com.ruoyi.pvadmin.domain.entity.DataItem;
import com.ruoyi.pvadmin.domain.entity.Device;
import com.ruoyi.pvadmin.domain.entity.ElectricityDataItem;
import com.ruoyi.pvadmin.domain.entity.PowerStation;
import com.ruoyi.pvadmin.domain.model.ElectricModel;
import com.ruoyi.pvadmin.domain.model.GenerationStatisticsItemModel;
import com.ruoyi.pvadmin.domain.model.PowerGenerationTrendModel;
import com.ruoyi.pvadmin.domain.model.TagValue;
import com.ruoyi.pvadmin.domain.vo.*;
import com.ruoyi.pvadmin.mapper.DataItemMapper;
import com.ruoyi.pvadmin.mapper.DeviceMapper;
import com.ruoyi.pvadmin.mapper.ElectricityDataItemMapper;
import com.ruoyi.pvadmin.mapper.PowerStationMapper;
import com.ruoyi.pvadmin.service.IPowerStationService;
import com.ruoyi.pvadmin.service.RealtimeDatabaseService;
import com.ruoyi.system.service.ISysConfigService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Console;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 电站维护Service业务层处理
 */
@Service
public class PowerStationServiceImpl extends ServiceImpl<PowerStationMapper, PowerStation> implements IPowerStationService {

    @Resource
    private DeviceMapper deviceMapper;

    @Resource
    private DataItemMapper dataItemMapper;

    @Resource
    private ISysConfigService configService;

    @Resource
    private PowerStationMapper powerStationMapper;

    @Resource
    private RealtimeDatabaseService realtimeDatabaseService;

    @Resource
    private ElectricityDataItemMapper electricityDataItemMapper;


    /**
     * 查询电站维护
     *
     * @param id 电站维护主键
     * @return 电站维护
     */
    @Override
    public PowerStationVO selectPowerStationById(String id) {
        PowerStationVO powerStationVO = new PowerStationVO();

        if (StringUtils.isBlank(id)) {
            return powerStationVO;
        }

        PowerStation powerStation = baseMapper.selectById(id);
        if (ObjectUtils.isNotEmpty(powerStation)) {
            BeanUtils.copyProperties(powerStation, powerStationVO);
        }
        return powerStationVO;
    }

    /**
     * 查询电站维护列表
     *
     * @param dto 电站维护
     * @return 电站维护
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<PowerStationVO> selectPowerStationList(PowerStationQueryDTO dto) {

        return baseMapper.listByQueryDto(dto);
    }

    /**
     * 新增电站维护
     *
     * @param dto 电站维护
     * @return 结果
     */
    @Override
    public int insertPowerStation(PowerStationSubmitDTO dto) {

        if (isDuplicate(dto)) {
            throw new ServiceException("电站信息已存在");
        }
        PowerStation powerStation = new PowerStation();

        BeanUtils.copyProperties(dto, powerStation);

        powerStation.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(powerStation);
    }

    /**
     * 修改电站维护
     *
     * @param dto 电站维护
     * @return 结果
     */
    @Override
    public int updatePowerStation(PowerStationSubmitDTO dto) {

        if (isDuplicate(dto)) {
            throw new ServiceException("电站信息已存在");
        }

        PowerStation powerStation = baseMapper.selectById(dto.getId());
        if (ObjectUtils.isEmpty(powerStation)) {
            throw new ServiceException("未找到修改数据");
        }
        BeanUtils.copyProperties(dto, powerStation);

        return baseMapper.updateById(powerStation);
    }

    /**
     * 批量删除电站维护
     *
     * @param id 需要删除的电站维护主键
     * @return 结果
     */
    @Override
    public int deletePowerStationByIds(String id) {
        return baseMapper.deleteById(id);
    }

    /**
     * 电站发电统计
     *
     * @param dto 查询参数
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "p")
    public List<GenerationStatisticsVO> listGenerationStatistics(GenerationStatisticsDTO dto) {
        List<GenerationStatisticsVO> statisticsVOList = new ArrayList<>();

        List<PowerStation> powerStations = powerStationMapper.listByPermissions(dto.getPowerStationId(), dto.getParams());

        if (CollectionUtils.isEmpty(powerStations)) {
            throw new ServiceException("电站不存在");
        }

        List<String> powerStationIds = powerStations.stream().map(PowerStation::getId).collect(Collectors.toList());
        // 查询电站，以及电站下面的设备
        List<Device> deviceList = deviceMapper.listByPowerStationIds(powerStationIds);
        List<String> deviceIds = deviceList.stream().map(Device::getId).collect(Collectors.toList());
        // 处理设备与电站关系
        Map<String, List<String>> deviceMap = deviceList.stream().collect(Collectors.groupingBy(Device::getPowerStationId,
                Collectors.mapping(Device::getId, Collectors.toList())));

        // 处理日期
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
        List<DataItem> dataItemList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(deviceIds)) {

            dataItemList = dataItemMapper.selectList(Wrappers.<DataItem>lambdaQuery()
                    .select(DataItem::getDeviceId, DataItem::getDataTime, DataItem::getValue).in(DataItem::getDeviceId, deviceIds)
                    .between(DataItem::getDataTime, beginTime, endTime).eq(DataItem::getTimeType, dto.getDataItemTimeType())
            );
        }

        for (PowerStation powerStation : powerStations) {
            GenerationStatisticsVO statisticsVO = new GenerationStatisticsVO();
            statisticsVO.setPowerStationName(powerStation.getName());

            List<GenerationStatisticsItemModel> timeList = new ArrayList<>();

            List<String> deviceIdList = deviceMap.get(powerStation.getId());
            Map<String, BigDecimal> deviceIdMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(deviceIdList)) {
                deviceIdMap = dataItemList.stream().filter(li -> deviceIdList.contains(li.getDeviceId()))
                        .collect(Collectors.groupingBy(li -> DateUtil.formatDateTime(li.getDataTime()),
                                Collectors.mapping(DataItem::getValue, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
            }

            Date time = beginTime;
            BigDecimal sumValue = BigDecimal.ZERO;
            while (time.before(endTime)) {
                GenerationStatisticsItemModel itemVO = new GenerationStatisticsItemModel();

                String timeStr = DateUtil.formatDate(time);
                String key = DateUtil.formatDateTime(time);

                BigDecimal value = deviceIdMap.get(key);
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

            statisticsVO.setSumValue(sumValue);

            statisticsVO.setTimeList(timeList);
            statisticsVOList.add(statisticsVO);
        }

        return statisticsVOList;
    }

    @Override
    public List<PowerStationRankVO> listPowerStationRank() {
        List<PowerStationRankVO> rankList = new ArrayList<>();

        // 获取所有设备（设备id和电站id对应关系）
        Map<String, List<String>> deviceMap = deviceMapper.selectList(
                        Wrappers.<Device>lambdaQuery()
                                .select(Device::getPowerStationId, Device::getId))
                .stream().collect(Collectors.groupingBy(Device::getPowerStationId,
                        Collectors.mapping(Device::getId, Collectors.toList())));
        // 获取所有电站（电站id、经度、纬度）
        PowerStationQueryDTO dto = new PowerStationQueryDTO();
        List<PowerStationVO> powerStationList = powerStationMapper.listByQueryDto(dto);

        if (CollectionUtils.isEmpty(powerStationList)) {
            return rankList;
        }

        // 根据电站id集合查询峰平谷数据
        ElectricModel electricModel = new ElectricModel();

        Date beginTime = DateUtil.beginOfYear(new Date());
        Date endTime = DateUtil.endOfYear(beginTime);
        electricModel.setBeginTime(beginTime);
        electricModel.setEndTime(endTime);
        electricModel.setPowerStationIds(powerStationList.stream().map(PowerStationVO::getId).collect(Collectors.toList()));
        List<ElectricityDataItem> dataItemList = electricityDataItemMapper.listDataItemByPowerStationIds(electricModel);

        // 循环电站通过对应设备取dataitem数据中合计发电量
        powerStationList.forEach(n -> {
            PowerStationRankVO item = new PowerStationRankVO();
            item.setPowerStationId(n.getId());
            item.setPowerStationName(n.getName());
            item.setLon(n.getLon());
            item.setLat(n.getLat());

            // 日发电效率： 日发电量（kwh） / （装机容量（mw） * 1000 * 24（h） ） * 100
            // 年发电效率： 年发电量（kwh） / （装机容量（mw） * 1000 * 24（h） * 发电天数 ） * 100
            // 最新修改 用累积了除以装机容量
            List<String> powerStationDeviceIdList = deviceMap.get(n.getId());
            BigDecimal value = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(powerStationDeviceIdList)) {
                value = dataItemList.stream()
                        .filter(x -> powerStationDeviceIdList.contains(x.getDeviceId()))
                        .map(li -> {
                            if (n.getInstalledCapacity().compareTo(BigDecimal.ZERO) != 0) {
                                return li.getValue().divide(n.getInstalledCapacity().multiply(BigDecimal.valueOf(1000)), Constants.DIGIT_4, RoundingMode.HALF_UP);
                            }
                            return li.getValue();
                        }).reduce(BigDecimal.ZERO, BigDecimal::add)
                        .setScale(Constants.DIGIT_4, RoundingMode.HALF_UP);
            }

            item.setSumValue(String.valueOf(value));
            item.setSort(value);
            rankList.add(item);
        });
        // 按照发电量进行排名
        List<PowerStationRankVO> sortedList = rankList.stream()
                .sorted(Comparator.comparing(PowerStationRankVO::getSort).reversed())
                .collect(Collectors.toList());
        int line = 1;
        for (int i = 0; i < (long) sortedList.size(); i++) {
            sortedList.get(i).setRank(line);
            line++;
        }
        return sortedList;
    }

    /**
     * 首页-电站信息
     *
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "d")
    public HomePowerStationInfoVO getHomePowerStationInfo(BaseEntity entity) {
        HomePowerStationInfoVO homePowerStationInfoVO = new HomePowerStationInfoVO();

        // 电站数量
        PowerStationQueryDTO queryDto = new PowerStationQueryDTO();
        queryDto.setParams(entity.getParams());
        List<PowerStationVO> powerStationList = baseMapper.listByQueryDto(queryDto);
        List<String> powerStationIds = powerStationList.stream()
                .map(PowerStationVO::getId)
                .collect(Collectors.toList());

        // 总装机容量
        BigDecimal installedCapacity = powerStationList.stream()
                .map(PowerStationVO::getInstalledCapacity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        homePowerStationInfoVO.setInstalledCapacity(installedCapacity + Constants.ELECTRIC_LOAD_UNIT_MW);

        // 查询峰平谷数据
        ElectricModel electricModel = new ElectricModel();
        electricModel.setPowerStationIds(powerStationIds);
        electricModel.setParams(entity.getParams());

        Date beginTime = DateUtil.beginOfYear(new Date());
        Date endTime = DateUtil.endOfYear(beginTime);
        electricModel.setBeginTime(beginTime);
        electricModel.setEndTime(endTime);
        List<ElectricityDataItem> dataItemList = electricityDataItemMapper.listDataItemByPowerStationIds(electricModel);
        // 当前年
        String nowYear = DateTimeUtil.getNowYear();

        List<ElectricityDataItem> electricityDataItems = dataItemList.stream()
                .filter(li -> nowYear.equals(DateUtil.format(li.getDataTime(), Constants.COMMON_PATTERN_YEAR)))
                .collect(Collectors.toList());

        // 当年发电量
        BigDecimal cumulativeYear = electricityDataItems.stream()
                .map(ElectricityDataItem::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(Constants.DIGIT_2, RoundingMode.HALF_UP);
        if (cumulativeYear.compareTo(Constants.UNIT_CONVERSION_10000) > -1) {
            homePowerStationInfoVO.setCumulativeYear(
                    cumulativeYear.divide(Constants.UNIT_CONVERSION_10000, 2, RoundingMode.HALF_UP) + Constants.ELECTRIC_UNIT_W_KWH);
        } else {
            homePowerStationInfoVO.setCumulativeYear(cumulativeYear + Constants.ELECTRIC_UNIT_KWH);
        }

        // 当日发电量
        String newDay = DateUtil.formatDate(new Date());
        List<ElectricityDataItem> todayDataItemList = electricityDataItems.stream()
                .filter(li -> newDay.equals(DateUtil.formatDate(li.getDataTime()))).collect(Collectors.toList());

        BigDecimal cumulativeDay = BigDecimal.ZERO;
        BigDecimal earningsDay = BigDecimal.ZERO;
        for (ElectricityDataItem dataItem : todayDataItemList) {
            cumulativeDay = cumulativeDay.add(dataItem.getValue());
            earningsDay = earningsDay.add(dataItem.getCost());
        }

        if (cumulativeDay.compareTo(Constants.UNIT_CONVERSION_10000) > -1) {
            homePowerStationInfoVO.setCumulativeDay(
                    cumulativeDay.divide(Constants.UNIT_CONVERSION_10000, 2, RoundingMode.HALF_UP) + Constants.ELECTRIC_UNIT_W_KWH);
        } else {
            homePowerStationInfoVO.setCumulativeDay(cumulativeDay.setScale(2, RoundingMode.HALF_UP) + Constants.ELECTRIC_UNIT_KWH);
        }

        if (earningsDay.compareTo(Constants.UNIT_CONVERSION_10000) > -1) {
            homePowerStationInfoVO.setEarningsDay(
                    earningsDay.divide(Constants.UNIT_CONVERSION_10000, 2, RoundingMode.HALF_UP) + Constants.UNIT_W_YUAN);
        } else {
            homePowerStationInfoVO.setEarningsDay(earningsDay.setScale(2, RoundingMode.HALF_UP) + Constants.UNIT_YUAN);
        }

        // 累计发电量
        BigDecimal generation = dataItemList.stream()
                .map(ElectricityDataItem::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(Constants.DIGIT_2, RoundingMode.HALF_UP);
        if (generation.compareTo(Constants.UNIT_CONVERSION_10000) > -1) {
            homePowerStationInfoVO.setGeneration(
                    generation.divide(Constants.UNIT_CONVERSION_10000, 2, RoundingMode.HALF_UP) + Constants.ELECTRIC_UNIT_W_KWH);
        } else {
            homePowerStationInfoVO.setGeneration(generation + Constants.ELECTRIC_UNIT_KWH);
        }

        // 累计收益
        BigDecimal sumValue = dataItemList.stream()
                .map(ElectricityDataItem::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(Constants.DIGIT_2, RoundingMode.HALF_UP);
        if (sumValue.compareTo(Constants.UNIT_CONVERSION_10000) > -1) {
            homePowerStationInfoVO.setEarnings(
                    sumValue.divide(Constants.UNIT_CONVERSION_10000, 2, RoundingMode.HALF_UP) + Constants.UNIT_W_YUAN);
        } else {
            homePowerStationInfoVO.setEarnings(sumValue + Constants.UNIT_YUAN);
        }
        // 累计二氧化碳排放
        String configValue = configService.selectConfigByKey("sys.factor.carbonEmission");
        BigDecimal factor = StringUtils.isBlank(configValue) ? BigDecimal.ZERO : new BigDecimal(configValue);

        BigDecimal carbonEmissions = generation.multiply(factor).setScale(Constants.DIGIT_2, RoundingMode.HALF_UP);

        if (carbonEmissions.compareTo(Constants.UNIT_CONVERSION_1000) > -1) {
            homePowerStationInfoVO.setCarbonEmissions(
                    carbonEmissions.divide(Constants.UNIT_CONVERSION_1000, 2, RoundingMode.HALF_UP) + Constants.UNIT_T);
        } else {
            homePowerStationInfoVO.setCarbonEmissions(carbonEmissions + Constants.UNIT_KG);
        }

        return homePowerStationInfoVO;
    }

    /**
     * 电站状态-根据id查询电站信息
     *
     * @param id 电站id
     * @return 结果
     */
    @Override
    public PowerStationInfoVO getPowerStationInfoById(String id) {
        PowerStationInfoVO powerStationInfoVO = new PowerStationInfoVO();

        PowerStation powerStation = baseMapper.selectInfoByid(id);
        if (ObjectUtils.isEmpty(powerStation)) {
            return powerStationInfoVO;
        }

        BeanUtils.copyProperties(powerStation, powerStationInfoVO);

        // 逆变器数量
        List<Device> deviceList = deviceMapper.selectList(Wrappers.<Device>lambdaQuery().select(Device::getId)
                .eq(Device::getPowerStationId, id).eq(Device::getAmmeter, Boolean.FALSE)
        );
        powerStationInfoVO.setInverterCount(deviceList.size());

        // 总发电量
        BigDecimal sumValue = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(deviceList)) {
            List<String> deviceIds = deviceList.stream().map(Device::getId).collect(Collectors.toList());

            List<DataItem> dataItemList = dataItemMapper.selectList(Wrappers.<DataItem>lambdaQuery()
                    .select(DataItem::getValue).in(DataItem::getDeviceId, deviceIds).eq(DataItem::getTimeType, TimeTypeEnum.MONTH.name())
            );
            if (CollectionUtils.isNotEmpty(dataItemList)) {
                sumValue = dataItemList.stream().map(DataItem::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
            }
        }

        powerStationInfoVO.setSumValue(sumValue);
        return powerStationInfoVO;
    }

    /**
     * 根据电站id查询发电信息、收益信息
     *
     * @param id 电站id
     * @return 结果
     */
    @Override
    public PowerGenerationInfoVO getPowerGenerationInfo(String id) {
        // 根据电站id查询设备
        List<String> deviceIds = deviceMapper.listByPowerStationId(id);
        if (CollectionUtils.isEmpty(deviceIds)) {
            return new PowerGenerationInfoVO();
        }
        // 获取数据
        Date date = new Date();
        Date beginTime = DateUtil.beginOfYear(date);
        Date endTime = DateUtil.endOfYear(date);
        List<ElectricityDataItem> dataItems = electricityDataItemMapper.selectList(Wrappers.<ElectricityDataItem>lambdaQuery()
                .select(ElectricityDataItem::getDataTime, ElectricityDataItem::getValue, ElectricityDataItem::getCost)
                .between(ElectricityDataItem::getDataTime, beginTime, endTime).in(ElectricityDataItem::getDeviceId, deviceIds)
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
     * 根据设备id获取发电趋势信息
     *
     * @param id       电站id
     * @param timeType 时间类型
     * @return 结果
     */
    @Override
    public List<PowerGenerationTrendModel> getImplementedPower(String id, TimeTypeEnum timeType) {
        // 根据电站id查询设备
        List<String> deviceIds = deviceMapper.listByPowerStationId(id);
        if (CollectionUtils.isEmpty(deviceIds)) {
            return Collections.emptyList();
        }

        // 查询时间
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
                .in(ElectricityDataItem::getDeviceId, deviceIds)
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
                trendModel.setValue(value.setScale(Constants.DIGIT_2, RoundingMode.HALF_UP));

                beginTime = DateUtil.offsetHour(beginTime, Constants.DIGIT_1);
            } else if (TimeTypeEnum.MONTH.equals(timeType)) {
                String formatDateTime = DateUtil.formatDate(beginTime);

                trendModel.setTime(formatDateTime);
                BigDecimal value = dataItemList.stream()
                        .filter(li -> formatDateTime.equals(DateUtil.formatDate(li.getDataTime())))
                        .map(ElectricityDataItem::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
                trendModel.setValue(value.setScale(Constants.DIGIT_2, RoundingMode.HALF_UP));

                beginTime = DateUtil.offsetDay(beginTime, Constants.DIGIT_1);
            } else if (TimeTypeEnum.YEAR.equals(timeType)) {
                String formatDateTime = DateUtil.format(beginTime, DateTimeUtil.COMMON_PATTERN_TO_MONTH);

                trendModel.setTime(formatDateTime);
                BigDecimal value = dataItemList.stream()
                        .filter(li -> formatDateTime.equals(DateUtil.format(li.getDataTime(), DateTimeUtil.COMMON_PATTERN_TO_MONTH)))
                        .map(ElectricityDataItem::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
                trendModel.setValue(value.setScale(Constants.DIGIT_2, RoundingMode.HALF_UP));

                beginTime = DateUtil.offsetMonth(beginTime, Constants.DIGIT_1);
            }
        }

        return modelList;
    }

    /**
     * 根据电站id获取设备信息
     *
     * @param id 电站id
     * @return 结果
     */
    @Override
    public PowerStationDeviceVO listDeviceById(String id) {
        PowerStationDeviceVO powerStationDeviceVO = new PowerStationDeviceVO();
        // 根据电站id查询设备
        List<Device> deviceList = deviceMapper.listInforByPowerStationId(id);
        if (CollectionUtils.isEmpty(deviceList)) {
            return powerStationDeviceVO;
        }

        // 根据设备编号查询实时数据
        List<String> deviceCodes = deviceList.stream().map(li -> {
            if (Boolean.TRUE.equals(li.getAmmeter())) {
                return li.getCode() + Constants.TAG_CODE_ACC;
            }
            return li.getCode() + Constants.TAG_CODE_CGL;
        }).collect(Collectors.toList());
        List<TagValue> tagValueList = realtimeDatabaseService.retrieve(deviceCodes);

        // 电表id
        List<InverterInfoVO> ammeterList = new ArrayList<>();
        // 逆变器id
        List<InverterInfoVO> inverterList = new ArrayList<>();
        powerStationDeviceVO.setAmmeterList(ammeterList);
        powerStationDeviceVO.setInverterList(inverterList);

        for (Device device : deviceList) {
            InverterInfoVO inverterInfoVO = new InverterInfoVO();

            BeanUtils.copyProperties(device, inverterInfoVO);

            Optional<TagValue> first = tagValueList.stream().filter(li -> li.getTagCode().contains(device.getCode())).findFirst();
            first.ifPresent(tagValue -> inverterInfoVO.setSumValue(BigDecimal.valueOf(tagValue.getValue())));

            if (ObjectUtils.isEmpty(inverterInfoVO.getSumValue())) {
                inverterInfoVO.setSumValue(BigDecimal.ZERO);
            }

            if (Boolean.TRUE.equals(device.getAmmeter())) {
                ammeterList.add(inverterInfoVO);
            } else {
                inverterList.add(inverterInfoVO);
            }
        }

        return powerStationDeviceVO;
    }

    /**
     * 校验是否已经存在
     *
     * @param dto 校验参数
     * @return 结果
     */
    private boolean isDuplicate(PowerStationSubmitDTO dto) {

        int count = baseMapper.selectCount(Wrappers.<PowerStation>lambdaQuery()
                .ne(StringUtils.isNotBlank(dto.getId()), PowerStation::getId, dto.getId())
                .eq(PowerStation::getCode, dto.getCode()).eq(PowerStation::getName, dto.getName())
        );
        return count > Constants.DIGIT_0;
    }
}