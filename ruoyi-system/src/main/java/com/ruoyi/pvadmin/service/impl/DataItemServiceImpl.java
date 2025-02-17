package com.ruoyi.pvadmin.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.enums.TimeTypeEnum;
import com.ruoyi.common.utils.DateTimeUtil;
import com.ruoyi.pvadmin.domain.dto.ComparativeAnalysisDTO;
import com.ruoyi.pvadmin.domain.dto.DeviceQueryDTO;
import com.ruoyi.pvadmin.domain.dto.HomeQueryDTO;
import com.ruoyi.pvadmin.domain.entity.DataItem;
import com.ruoyi.pvadmin.domain.entity.ElectricityDataItem;
import com.ruoyi.pvadmin.domain.entity.PowerStation;
import com.ruoyi.pvadmin.domain.model.ElectricModel;
import com.ruoyi.pvadmin.domain.vo.EnergyComparisonVO;
import com.ruoyi.pvadmin.domain.vo.HomepageGenerationStatsVO;
import com.ruoyi.pvadmin.mapper.DataItemMapper;
import com.ruoyi.pvadmin.mapper.DeviceMapper;
import com.ruoyi.pvadmin.mapper.ElectricityDataItemMapper;
import com.ruoyi.pvadmin.mapper.PowerStationMapper;
import com.ruoyi.pvadmin.service.IDataItemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 累积量  Service业务层处理
 *
 * @author ruoyi
 * @date 2023-08-23
 */
@Slf4j
@Service
public class DataItemServiceImpl extends ServiceImpl<DataItemMapper, DataItem> implements IDataItemService {

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private DataItemMapper dataItemMapper;

    @Autowired
    private PowerStationMapper powerStationMapper;

    @Resource
    private ElectricityDataItemMapper electricityDataItemMapper;

    /**
     * 能耗统计分析-获取同比分析列表数据
     *
     * @param dto            查询参数
     * @param comparisonType 同比、环比
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<EnergyComparisonVO> listEnergyAnalysis(ComparativeAnalysisDTO dto, String comparisonType) {
        List<EnergyComparisonVO> comparisonVOList = new ArrayList<>();

        // 1. 查询电站下的所有设备
        PowerStation powerStation = new PowerStation();
        if (StringUtils.isNotBlank(dto.getPowerStationId())) {
            powerStation = powerStationMapper.listEnergyAnalysisById(dto.getPowerStationId());

            if (ObjectUtils.isEmpty(powerStation)) {
                return comparisonVOList;
            }
        }
        // 2. 处理查询时间
        TimeTypeEnum timeType = dto.getTimeType();

        boolean isYoy = comparisonType.equals(Constants.ENERGY_COMPARISON_YOY);
        Date currentTime = DateUtil.beginOfYear(dto.getQueryTime());
        Date endTime = DateUtil.endOfYear(currentTime);
        Date compareTime = DateUtil.offset(currentTime, DateField.YEAR, Constants.DIGIT_MINUS_1);
        Date compareEndTime = DateUtil.offset(endTime, DateField.YEAR, Constants.DIGIT_MINUS_1);

        // 截止时间
        if (TimeTypeEnum.MONTH.equals(timeType)) {
            currentTime = DateUtil.beginOfMonth(dto.getQueryTime());
            endTime = DateUtil.endOfMonth(currentTime);
            compareTime = DateUtil.offsetMonth(currentTime, Constants.DIGIT_MINUS_1);
            compareEndTime = DateUtil.offsetMonth(endTime, Constants.DIGIT_MINUS_1);
        } else if (TimeTypeEnum.DAY.equals(timeType)) {
            currentTime = DateUtil.beginOfDay(dto.getQueryTime());
            endTime = DateUtil.endOfDay(currentTime);
            compareTime = DateUtil.offsetDay(currentTime, Constants.DIGIT_MINUS_1);
            compareEndTime = DateUtil.offsetDay(endTime, Constants.DIGIT_MINUS_1);
        }
        if (isYoy) {
            compareTime = DateUtil.offset(currentTime, DateField.YEAR, Constants.DIGIT_MINUS_1);
        }
        try {
            String powerStationName = StringUtils.isBlank(powerStation.getName()) ? "全厂" : powerStation.getName();
            Date startTime = currentTime;
            Date copyCompareTime = compareTime;
            while (startTime.before(endTime)) {
                EnergyComparisonVO comparisonVO = new EnergyComparisonVO();
                comparisonVO.setCurrentTime(startTime);
                comparisonVO.setPowerStationName(powerStationName);

                comparisonVO.setCompareTime(copyCompareTime);
                comparisonVOList.add(comparisonVO);

                if (TimeTypeEnum.YEAR.equals(timeType)) {
                    startTime = DateUtil.offsetMonth(startTime, Constants.DIGIT_1);
                    copyCompareTime = DateUtil.offsetMonth(copyCompareTime, Constants.DIGIT_1);
                } else if (TimeTypeEnum.MONTH.equals(timeType)) {
                    startTime = DateUtil.offsetDay(startTime, Constants.DIGIT_1);
                    copyCompareTime = DateUtil.offsetDay(copyCompareTime, Constants.DIGIT_1);
                } else {
                    startTime = DateUtil.offsetHour(startTime, Constants.DIGIT_1);
                    copyCompareTime = DateUtil.offsetHour(copyCompareTime, Constants.DIGIT_1);
                }
            }
        } catch (Exception e) {
            return comparisonVOList;
        }

        // 3. 根据设备查询点位信息，通过点位查询data_item
        List<String> deviceIds = deviceMapper.listDeviceIdsByPowerStationId(dto.getPowerStationId(), dto.getParams());
        if (CollectionUtils.isEmpty(deviceIds)) {
            return comparisonVOList;
        }

        ElectricModel electricModel = new ElectricModel();
        electricModel.setBeginTime(currentTime);
        electricModel.setEndTime(endTime);
        electricModel.setDeviceIds(deviceIds);
        Map<String, BigDecimal> dataItemMap = electricityDataItemMapper.listDataItemByDeviceIds(electricModel)
                .stream().collect(Collectors.groupingBy(item -> DateTimeUtil.getTimeCode(dto.getDataItemTimeType(), item.getDataTime()),
                        Collectors.mapping(ElectricityDataItem::getValue, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));

        ElectricModel lastElectricModel = new ElectricModel();
        lastElectricModel.setBeginTime(compareTime);
        lastElectricModel.setEndTime(compareEndTime);
        lastElectricModel.setDeviceIds(deviceIds);
        Map<String, BigDecimal> lastDataItemMap = electricityDataItemMapper.listDataItemByDeviceIds(lastElectricModel)
                .stream().collect(Collectors.groupingBy(item -> DateTimeUtil.getTimeCode(dto.getDataItemTimeType(), item.getDataTime()),
                        Collectors.mapping(ElectricityDataItem::getValue, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));
        // 4. 根据设备类型进行分组处理结果
        for (EnergyComparisonVO comparisonVO : comparisonVOList) {
            // 转化时间
            String currentTimeStr = DateTimeUtil.getTimeCode(dto.getDataItemTimeType(), comparisonVO.getCurrentTime());
            String compareTimeStr = DateTimeUtil.getTimeCode(dto.getDataItemTimeType(), comparisonVO.getCompareTime());

            BigDecimal currentValue = dataItemMap.get(currentTimeStr);
            BigDecimal contrastValues = lastDataItemMap.get(compareTimeStr);

            currentValue = ObjectUtils.isEmpty(currentValue) ? BigDecimal.ZERO : currentValue;
            contrastValues = ObjectUtils.isEmpty(contrastValues) ? BigDecimal.ZERO : contrastValues;

            BigDecimal ratio = contrastValues.compareTo(BigDecimal.ZERO) > 0
                    ? currentValue.subtract(contrastValues).divide(contrastValues, Constants.DIGIT_2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            comparisonVO.setRatio(ratio.multiply(Constants.UNIT_CONVERSION_100));
            comparisonVO.setCurrentValue(currentValue.setScale(Constants.DIGIT_2, RoundingMode.HALF_UP));
            comparisonVO.setContrastValues(contrastValues.setScale(Constants.DIGIT_2, RoundingMode.HALF_UP));
        }
        return comparisonVOList;
    }

    /**
     * 首页-获取首页发电量数据
     *
     * @param dto 查询条件
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<HomepageGenerationStatsVO> getHomepageGenerationStats(HomeQueryDTO dto) {
        List<HomepageGenerationStatsVO> generationStatsVOList = new ArrayList<>();

        Date beginTime = DateUtil.beginOfMonth(dto.getQueryTime());
        Date endTime = DateUtil.endOfMonth(dto.getQueryTime());

        TimeTypeEnum timeType = dto.getTimeType();
        if (TimeTypeEnum.DAY.equals(timeType)) {
            beginTime = DateUtil.beginOfDay(beginTime);
            endTime = DateUtil.endOfDay(beginTime);
        } else if (TimeTypeEnum.YEAR.equals(timeType)) {
            beginTime = DateUtil.beginOfYear(beginTime);
            endTime = DateUtil.endOfYear(beginTime);
        }

        List<DataItem> dataItemList = new ArrayList<>();

        DeviceQueryDTO queryDTO = new DeviceQueryDTO();
        queryDTO.setParams(dto.getParams());
        List<String> deviceIds = deviceMapper.listDeviceId(queryDTO);
        if (CollectionUtils.isNotEmpty(deviceIds)) {
            dataItemList = dataItemMapper.selectList(Wrappers.<DataItem>lambdaQuery()
                    .select(DataItem::getDataTime, DataItem::getValue)
                    .between(DataItem::getDataTime, beginTime, endTime)
                    .in(DataItem::getDeviceId, deviceIds)
                    .eq(DataItem::getTimeType, dto.getDataItemTimeType())
            );
        }
        Map<String, BigDecimal> dataItemMap = new HashMap<>();
        if (TimeTypeEnum.DAY.equals(timeType)) {
            dataItemMap = dataItemList.stream()
                    .collect(Collectors.groupingBy(item -> DateUtil.formatDateTime(item.getDataTime()),
                            Collectors.mapping(DataItem::getValue, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
        } else if (TimeTypeEnum.MONTH.equals(timeType)) {
            dataItemMap = dataItemList.stream()
                    .collect(Collectors.groupingBy(item -> DateUtil.formatDate(item.getDataTime()),
                            Collectors.mapping(DataItem::getValue, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
        } else if (TimeTypeEnum.YEAR.equals(timeType)) {
            dataItemMap = dataItemList.stream()
                    .collect(Collectors.groupingBy(item ->
                                    DateUtil.format(item.getDataTime(), DateTimeUtil.COMMON_PATTERN_TO_MONTH),
                            Collectors.mapping(DataItem::getValue, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
        }

        while (beginTime.before(endTime)) {
            HomepageGenerationStatsVO generationStatsVO = new HomepageGenerationStatsVO();

            String format = DateUtil.formatDateTime(beginTime);
            if (TimeTypeEnum.DAY.equals(timeType)) {
                beginTime = DateUtil.offsetHour(beginTime, Constants.DIGIT_1);
            } else if (TimeTypeEnum.MONTH.equals(timeType)) {
                format = DateUtil.formatDate(beginTime);
                beginTime = DateUtil.offsetDay(beginTime, Constants.DIGIT_1);
            } else if (TimeTypeEnum.YEAR.equals(timeType)) {
                format = DateUtil.format(beginTime, DateTimeUtil.COMMON_PATTERN_TO_MONTH);
                beginTime = DateUtil.offsetMonth(beginTime, Constants.DIGIT_1);
            }
            BigDecimal value = dataItemMap.get(format);

            generationStatsVO.setTime(format);
            generationStatsVO.setValue(value);
            generationStatsVOList.add(generationStatsVO);
        }
        return generationStatsVOList;
    }
}