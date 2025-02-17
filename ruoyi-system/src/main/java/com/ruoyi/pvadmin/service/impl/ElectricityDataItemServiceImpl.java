package com.ruoyi.pvadmin.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.enums.TimeTypeEnum;
import com.ruoyi.common.utils.CalcUtil;
import com.ruoyi.pvadmin.domain.dto.HomeQueryDTO;
import com.ruoyi.pvadmin.domain.dto.PeakValleyQueryDTO;
import com.ruoyi.pvadmin.domain.entity.ElectricityDataItem;
import com.ruoyi.pvadmin.domain.entity.ElectricityTypeSettingItem;
import com.ruoyi.pvadmin.domain.enums.ElectricityTypeEnum;
import com.ruoyi.pvadmin.domain.model.GenerationStatisticsItemModel;
import com.ruoyi.pvadmin.domain.vo.PeakAndValleyReportVO;
import com.ruoyi.pvadmin.domain.vo.PeakAndValleyVO;
import com.ruoyi.pvadmin.domain.vo.PeakValleyTimeSharingLineStatisticsVO;
import com.ruoyi.pvadmin.domain.vo.PeriodGenerationPercentageVO;
import com.ruoyi.pvadmin.mapper.DeviceMapper;
import com.ruoyi.pvadmin.mapper.ElectricityDataItemMapper;
import com.ruoyi.pvadmin.mapper.ElectricityTypeSettingMapper;
import com.ruoyi.pvadmin.mapper.PowerStationMapper;
import com.ruoyi.pvadmin.service.IElectricityDataItemService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * 电力-峰平谷数据Service业务层处理
 *
 * @author ruoyi
 * @date 2023-08-23
 */
@Service
public class ElectricityDataItemServiceImpl extends ServiceImpl<ElectricityDataItemMapper, ElectricityDataItem>
        implements IElectricityDataItemService {

    @Resource
    private DeviceMapper deviceMapper;

    @Resource
    private PowerStationMapper powerStationMapper;

    @Resource
    private ElectricityDataItemMapper electricityDataItemMapper;

    @Resource
    private ElectricityTypeSettingMapper electricitySettingMapper;


    /**
     * 首页-查询时段发电占比
     *
     * @param dto 查询条件
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<PeriodGenerationPercentageVO> getPeriodGenerationPercentage(HomeQueryDTO dto) {
        // 处理查询时间
        List<PeriodGenerationPercentageVO> percentageVOList = new ArrayList<>();

        // 判断是否传入电站id，若传入则进行过滤
        List<String> deviceIds = deviceMapper.listHomeDeviceIdByPowerStationId(dto);
        if (CollectionUtils.isEmpty(deviceIds)) {
            deviceIds.add(Constants.STR_MINUS_1);
        }

        Date beginTime = DateUtil.beginOfMonth(dto.getQueryTime());
        Date endTime = DateUtil.endOfMonth(dto.getQueryTime());

        TimeTypeEnum timeType = dto.getTimeType();
        if (TimeTypeEnum.DAY.equals(timeType)) {
            beginTime = DateUtil.beginOfDay(dto.getQueryTime());
            endTime = DateUtil.endOfDay(beginTime);
        }

        List<ElectricityDataItem> dataItemList = electricityDataItemMapper.selectList(Wrappers.<ElectricityDataItem>lambdaQuery()
                .select(ElectricityDataItem::getType, ElectricityDataItem::getDataTime, ElectricityDataItem::getValue)
                .between(ElectricityDataItem::getDataTime, beginTime, endTime)
                .in(CollectionUtils.isNotEmpty(deviceIds), ElectricityDataItem::getDeviceId, deviceIds)
        );

        BigDecimal sumValue = BigDecimal.ZERO;
        BigDecimal tipValue = BigDecimal.ZERO;
        BigDecimal peakValue = BigDecimal.ZERO;
        BigDecimal flatValue = BigDecimal.ZERO;
        BigDecimal troughValue = BigDecimal.ZERO;
        BigDecimal deepValue = BigDecimal.ZERO;
        for (ElectricityDataItem dataItem : dataItemList) {
            BigDecimal value = dataItem.getValue();

            if (ElectricityTypeEnum.tip.getCode().equals(dataItem.getType())) {
                tipValue = tipValue.add(value);
            } else if (ElectricityTypeEnum.peak.getCode().equals(dataItem.getType())) {
                peakValue = peakValue.add(value);
            } else if (ElectricityTypeEnum.flat.getCode().equals(dataItem.getType())) {
                flatValue = flatValue.add(value);
            } else if (ElectricityTypeEnum.trough.getCode().equals(dataItem.getType())) {
                troughValue = troughValue.add(value);
            } else if (ElectricityTypeEnum.deep.getCode().equals(dataItem.getType())) {
                deepValue = deepValue.add(value);
            }
            sumValue = sumValue.add(value);
        }

        BigDecimal[] arr = new BigDecimal[]{tipValue, peakValue, flatValue, troughValue, deepValue};
        double[] percentValue = CalcUtil.getPercentValue(arr, Constants.DIGIT_2);
        List<BigDecimal> valueList = new ArrayList<>(percentValue.length);
        for (double v : percentValue) {
            valueList.add(BigDecimal.valueOf(v).setScale(Constants.DIGIT_2, RoundingMode.HALF_UP));
        }
        // 尖
        PeriodGenerationPercentageVO tipPercentageVO = new PeriodGenerationPercentageVO();
        tipPercentageVO.setName(ElectricityTypeEnum.tip.getName());
        tipPercentageVO.setValue(tipValue.setScale(Constants.DIGIT_2, RoundingMode.HALF_UP));
        tipPercentageVO.setRatio(valueList.get(Constants.DIGIT_0));
        percentageVOList.add(tipPercentageVO);
        // 峰
        PeriodGenerationPercentageVO peakPercentageVO = new PeriodGenerationPercentageVO();
        peakPercentageVO.setName(ElectricityTypeEnum.peak.getName());
        peakPercentageVO.setValue(peakValue.setScale(Constants.DIGIT_2, RoundingMode.HALF_UP));
        peakPercentageVO.setRatio(valueList.get(Constants.DIGIT_1));
        percentageVOList.add(peakPercentageVO);
        // 平
        PeriodGenerationPercentageVO flatPercentageVO = new PeriodGenerationPercentageVO();
        flatPercentageVO.setName(ElectricityTypeEnum.flat.getName());
        flatPercentageVO.setValue(flatValue.setScale(Constants.DIGIT_2, RoundingMode.HALF_UP));
        flatPercentageVO.setRatio(valueList.get(Constants.DIGIT_2));
        percentageVOList.add(flatPercentageVO);
        // 谷
        PeriodGenerationPercentageVO troughPercentageVO = new PeriodGenerationPercentageVO();
        troughPercentageVO.setName(ElectricityTypeEnum.trough.getName());
        troughPercentageVO.setValue(troughValue.setScale(Constants.DIGIT_2, RoundingMode.HALF_UP));
        troughPercentageVO.setRatio(valueList.get(Constants.DIGIT_3));
        percentageVOList.add(troughPercentageVO);
        // 深谷
        PeriodGenerationPercentageVO deepPercentageVO = new PeriodGenerationPercentageVO();
        deepPercentageVO.setName(ElectricityTypeEnum.deep.getName());
        deepPercentageVO.setValue(deepValue.setScale(Constants.DIGIT_2, RoundingMode.HALF_UP));
        deepPercentageVO.setRatio(valueList.get(Constants.DIGIT_4));
        percentageVOList.add(deepPercentageVO);

        return percentageVOList;
    }

    /**
     * 尖峰平谷时段统计
     *
     * @param dto 查询参数
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "d")
    public PeakAndValleyVO getPeakAndValleyPieAndDataList(PeakValleyQueryDTO dto) {
        PeakAndValleyVO result = new PeakAndValleyVO();

        List<PeakValleyTimeSharingLineStatisticsVO> costList = new ArrayList<>();
        List<PeakValleyTimeSharingLineStatisticsVO> powerConsumptionList = new ArrayList<>();

        Date beginTime = DateUtil.beginOfMonth(dto.getDateTime());
        Date endTime = DateUtil.endOfMonth(beginTime);

        Date start = beginTime;
        while (start.before(endTime)) {
            PeakValleyTimeSharingLineStatisticsVO statisticsVO = new PeakValleyTimeSharingLineStatisticsVO();
            statisticsVO.setXData(DateUtil.formatDate(start));

            powerConsumptionList.add(statisticsVO);

            start = DateUtil.offsetDay(start, Constants.DIGIT_1);
        }
        result.setCostList(costList);
        result.setPowerConsumptionList(powerConsumptionList);

        // 获取设备id集合
        List<String> deviceIds = new ArrayList<>();
        if (StringUtils.isNotBlank(dto.getDeviceId())) {
            deviceIds.add(dto.getDeviceId());
        } else {
            deviceIds = deviceMapper.listDeviceIdsByPowerStationId(dto.getPowerStationId(), dto.getParams());
        }
        if (CollectionUtils.isEmpty(deviceIds)) {
            deviceIds.add(Constants.STR_MINUS_1);
        }

        List<ElectricityDataItem> electricityList = electricityDataItemMapper.selectList(
                Wrappers.<ElectricityDataItem>lambdaQuery().select(ElectricityDataItem::getType,
                                ElectricityDataItem::getDataTime, ElectricityDataItem::getValue,
                                ElectricityDataItem::getCost)
                        .between(ElectricityDataItem::getDataTime, beginTime, endTime).in(ElectricityDataItem::getDeviceId, deviceIds)
        );

        getPeakAndValleyPercentage(result, electricityList);

        Map<String, List<ElectricityDataItem>> costElectricityMap = electricityList.stream()
                .collect(groupingBy(li -> DateUtil.formatDate(li.getDataTime())));

        for (PeakValleyTimeSharingLineStatisticsVO model : powerConsumptionList) {
            PeakValleyTimeSharingLineStatisticsVO costModel = new PeakValleyTimeSharingLineStatisticsVO();
            costModel.setXData(model.getXData());
            costList.add(costModel);

            List<ElectricityDataItem> itemList = costElectricityMap.get(model.getXData());
            if (CollectionUtils.isEmpty(itemList)) {
                continue;
            }
            // 费用
            BigDecimal tipCost = BigDecimal.ZERO;
            BigDecimal peakCost = BigDecimal.ZERO;
            BigDecimal flatCost = BigDecimal.ZERO;
            BigDecimal troughCost = BigDecimal.ZERO;
            BigDecimal deepCost = BigDecimal.ZERO;
            // 用电量
            BigDecimal tipElectricity = BigDecimal.ZERO;
            BigDecimal peakElectricity = BigDecimal.ZERO;
            BigDecimal flatElectricity = BigDecimal.ZERO;
            BigDecimal troughElectricity = BigDecimal.ZERO;
            BigDecimal deepElectricity = BigDecimal.ZERO;

            for (ElectricityDataItem electricity : itemList) {
                String type = electricity.getType();
                if (ElectricityTypeEnum.tip.getCode().equals(type)) {
                    tipCost = tipCost.add(electricity.getCost());
                    tipElectricity = tipElectricity.add(electricity.getValue());
                } else if (ElectricityTypeEnum.peak.getCode().equals(type)) {
                    peakCost = peakCost.add(electricity.getCost());
                    peakElectricity = peakElectricity.add(electricity.getValue());
                } else if (ElectricityTypeEnum.flat.getCode().equals(type)) {
                    flatCost = flatCost.add(electricity.getCost());
                    flatElectricity = flatElectricity.add(electricity.getValue());
                } else if (ElectricityTypeEnum.trough.getCode().equals(type)) {
                    troughCost = troughCost.add(electricity.getCost());
                    troughElectricity = troughElectricity.add(electricity.getValue());
                } else {
                    deepCost = deepCost.add(electricity.getCost());
                    deepElectricity = deepElectricity.add(electricity.getValue());
                }
            }

            model.setYTip(formatValue(tipElectricity));
            model.setYPeak(formatValue(peakElectricity));
            model.setYFlat(formatValue(flatElectricity));
            model.setYTrough(formatValue(troughElectricity));
            model.setYDeep(formatValue(deepCost));

            costModel.setYTip(formatValue(tipCost));
            costModel.setYPeak(formatValue(peakCost));
            costModel.setYFlat(formatValue(flatCost));
            costModel.setYTrough(formatValue(troughCost));
            costModel.setYDeep(formatValue(deepCost));
        }
        return result;
    }

    /**
     * 查询峰平谷报表
     *
     * @param dto 查询条件
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "p")
    public List<PeakAndValleyReportVO> getReport(PeakValleyQueryDTO dto) {
        List<PeakAndValleyReportVO> reportVOList = new ArrayList<>();

        List<String> deviceIds = new ArrayList<>();
        // 查询设备信息
        if (StringUtils.isNotBlank(dto.getDeviceId())) {
            deviceIds.add(dto.getDeviceId());
        } else {
            deviceIds = deviceMapper.listByPowerStationId(dto.getPowerStationId());
        }
        if (CollectionUtils.isEmpty(deviceIds)) {
            deviceIds.add(Constants.STR_MINUS_1);
        }

        Date beginTime = DateUtil.beginOfMonth(dto.getDateTime());
        Date endTime = DateUtil.endOfMonth(dto.getDateTime());

        // 查询峰平谷配置
        List<ElectricityTypeSettingItem> settingItemList = electricitySettingMapper.listSettingByDate(beginTime);
        if (CollectionUtils.isEmpty(settingItemList)) {
            return reportVOList;
        }

        List<ElectricityDataItem> electricityList = electricityDataItemMapper.selectList(
                Wrappers.<ElectricityDataItem>lambdaQuery().select(ElectricityDataItem::getDataTime, ElectricityDataItem::getValue)
                        .between(ElectricityDataItem::getDataTime, beginTime, endTime).in(ElectricityDataItem::getDeviceId, deviceIds)
        );

        for (ElectricityTypeSettingItem settingItem : settingItemList) {
            PeakAndValleyReportVO reportVO = new PeakAndValleyReportVO();

            Date settTingBeginTime = settingItem.getBeginTime();
            Date settTingEndTime = settingItem.getEndTime();

            String format = Constants.COMMON_PATTERN_HOUR_MINUTE;
            String timeName = DateUtil.format(settTingBeginTime, format) + Constants.STR_LINE +
                    DateUtil.format(settTingEndTime, format);
            reportVO.setTimePeriod(timeName);
            reportVO.setTimeName(settingItem.getType());
            reportVO.setTimeNameCN(ElectricityTypeEnum.getNameByCode(settingItem.getType()));

            // 过滤峰平谷数据
            List<ElectricityDataItem> itemList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(electricityList)) {
                itemList = electricityList.stream()
                        .filter(li -> isWithinTimeRange(li.getDataTime(), settTingBeginTime, settTingEndTime))
                        .collect(Collectors.toList());
            }

            BigDecimal sumValue = BigDecimal.ZERO;
            List<GenerationStatisticsItemModel> timeList = new ArrayList<>();
            Date startTime = beginTime;
            while (startTime.before(endTime)) {
                GenerationStatisticsItemModel itemVO = new GenerationStatisticsItemModel();


                Date finalStartTime = startTime;

                String timeFormat = DateUtil.formatDate(finalStartTime);

                BigDecimal itemValue = itemList.stream()
                        .filter(li -> timeFormat.equals(DateUtil.formatDate(li.getDataTime())))
                        .map(ElectricityDataItem::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal value = ObjectUtils.isEmpty(itemValue) ? BigDecimal.ZERO : itemValue;
                itemVO.setValue(value);
                itemVO.setTime(timeFormat);

                sumValue = sumValue.add(value);
                timeList.add(itemVO);
                startTime = DateUtil.offsetDay(startTime, Constants.DIGIT_1);
            }
            reportVO.setSumValue(sumValue);
            reportVO.setTimeList(timeList);

            reportVOList.add(reportVO);
        }
        return reportVOList;
    }

    /**
     * 判断时间是否在开始 截止时间范围内
     *
     * @param time      判断时间
     * @param startTime 开始时间
     * @param endTime   截止时间
     * @return 结果
     */
    public boolean isWithinTimeRange(Date time, Date startTime, Date endTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startTime);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endTime);

        // 将日期部分设置为相同，只比较时和分
        calendar.set(Calendar.YEAR, startCalendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, startCalendar.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, startCalendar.get(Calendar.DAY_OF_MONTH));

        Date start = startCalendar.getTime();
        Date end = endCalendar.getTime();

        long dateTime = calendar.getTime().getTime();

        return dateTime >= start.getTime() && dateTime < end.getTime();
    }

    /**
     * 保留两位小数
     */
    private String formatValue(BigDecimal value) {
        return value.setScale(Constants.DIGIT_2, RoundingMode.HALF_UP).toString();
    }

    /**
     * 获取尖峰平谷时段统计 各类型占比
     *
     * @param result               返回对象
     * @param electricityDataItems 峰平谷信息
     */
    private void getPeakAndValleyPercentage(PeakAndValleyVO result, List<ElectricityDataItem> electricityDataItems) {
        BigDecimal totalPowerCost;
        BigDecimal totalPowerConsumption;

        BigDecimal tipPowerCost = calculatePowerCost(electricityDataItems, ElectricityTypeEnum.tip.getCode());
        BigDecimal tipPowerConsumption = calculatePowerConsumption(electricityDataItems, ElectricityTypeEnum.tip.getCode());
        BigDecimal peakPowerCost = calculatePowerCost(electricityDataItems, ElectricityTypeEnum.peak.getCode());
        BigDecimal peakPowerConsumption = calculatePowerConsumption(electricityDataItems, ElectricityTypeEnum.peak.getCode());
        BigDecimal flatPowerCost = calculatePowerCost(electricityDataItems, ElectricityTypeEnum.flat.getCode());
        BigDecimal flatPowerConsumption = calculatePowerConsumption(electricityDataItems, ElectricityTypeEnum.flat.getCode());
        BigDecimal troughPowerCost = calculatePowerCost(electricityDataItems, ElectricityTypeEnum.trough.getCode());
        BigDecimal troughPowerConsumption = calculatePowerConsumption(electricityDataItems, ElectricityTypeEnum.trough.getCode());
        BigDecimal deepPowerCost = calculatePowerCost(electricityDataItems, ElectricityTypeEnum.deep.getCode());
        BigDecimal deepPowerConsumption = calculatePowerConsumption(electricityDataItems, ElectricityTypeEnum.deep.getCode());

        totalPowerCost = tipPowerCost.add(peakPowerCost).add(flatPowerCost).add(troughPowerCost).add(deepPowerCost);
        totalPowerConsumption = tipPowerConsumption.add(peakPowerConsumption).add(flatPowerConsumption)
                .add(troughPowerConsumption).add(deepPowerConsumption);
        // 尖
        result.setTipPowerCost(tipPowerCost);
        result.setTipPowerConsumption(tipPowerConsumption);
        BigDecimal tipPowerCostProportion = tipPowerCost.compareTo(BigDecimal.ZERO) > 0 ?
                tipPowerCost.divide(totalPowerCost, Constants.DIGIT_2, RoundingMode.HALF_UP) : tipPowerCost;
        BigDecimal tipPowerProportion = tipPowerConsumption.compareTo(BigDecimal.ZERO) > 0 ?
                tipPowerConsumption.divide(totalPowerConsumption, Constants.DIGIT_2, RoundingMode.HALF_UP) : tipPowerConsumption;
        // 峰
        result.setPeakPowerCost(peakPowerCost);
        result.setPeakPowerConsumption(peakPowerConsumption);
        BigDecimal peakPowerCostProportion = peakPowerCost.compareTo(BigDecimal.ZERO) > 0 ?
                peakPowerCost.divide(totalPowerCost, Constants.DIGIT_2, RoundingMode.HALF_UP) : peakPowerCost;
        BigDecimal peakPowerProportion = peakPowerConsumption.compareTo(BigDecimal.ZERO) > 0 ?
                peakPowerConsumption.divide(totalPowerConsumption, Constants.DIGIT_2, RoundingMode.HALF_UP) : peakPowerConsumption;
        // 平
        result.setFlatPowerCost(flatPowerCost);
        result.setFlatPowerConsumption(flatPowerConsumption);
        BigDecimal flatPowerCostProportion = flatPowerCost.compareTo(BigDecimal.ZERO) > 0 ?
                flatPowerCost.divide(totalPowerCost, Constants.DIGIT_2, RoundingMode.HALF_UP) : flatPowerCost;
        BigDecimal flatPowerProportion = flatPowerConsumption.compareTo(BigDecimal.ZERO) > 0 ?
                flatPowerConsumption.divide(totalPowerConsumption, Constants.DIGIT_2, RoundingMode.HALF_UP) : flatPowerConsumption;
        // 谷
        result.setTroughPowerCost(troughPowerCost);
        result.setTroughPowerConsumption(troughPowerConsumption);
        BigDecimal troughPowerCostProportion = troughPowerCost.compareTo(BigDecimal.ZERO) > 0 ?
                troughPowerCost.divide(totalPowerCost, Constants.DIGIT_2, RoundingMode.HALF_UP) : troughPowerCost;
        BigDecimal troughPowerProportion = troughPowerConsumption.compareTo(BigDecimal.ZERO) > 0 ?
                troughPowerConsumption.divide(totalPowerConsumption, Constants.DIGIT_2, RoundingMode.HALF_UP) : troughPowerConsumption;
        // 深谷
        result.setDeepPowerCost(deepPowerCost);
        result.setDeepPowerConsumption(deepPowerConsumption);
        BigDecimal deepPowerCostProportion = deepPowerCost.compareTo(BigDecimal.ZERO) > 0 ?
                deepPowerCost.divide(totalPowerCost, Constants.DIGIT_2, RoundingMode.HALF_UP) : deepPowerCost;
        BigDecimal deepPowerProportion = deepPowerConsumption.compareTo(BigDecimal.ZERO) > 0 ?
                deepPowerConsumption.divide(totalPowerConsumption, Constants.DIGIT_2, RoundingMode.HALF_UP) : deepPowerConsumption;

        // 合计
        result.setTotalPowerCost(totalPowerCost);
        result.setTotalPowerConsumption(totalPowerConsumption);
        // 处理费用占比
        List<BigDecimal> costRatioList = calcUnitPowerProportionData(tipPowerCostProportion, peakPowerCostProportion,
                flatPowerCostProportion, troughPowerCostProportion, deepPowerCostProportion);

        result.setTipPowerCostProportion(costRatioList.get(Constants.DIGIT_0));
        result.setPeakPowerCostProportion(costRatioList.get(Constants.DIGIT_1));
        result.setFlatPowerCostProportion(costRatioList.get(Constants.DIGIT_2));
        result.setTroughPowerCostProportion(costRatioList.get(Constants.DIGIT_3));
        result.setDeepPowerCostProportion(costRatioList.get(Constants.DIGIT_4));
        // 用电量占比
        List<BigDecimal> valueList = calcUnitPowerProportionData(tipPowerProportion, peakPowerProportion,
                flatPowerProportion, troughPowerProportion, deepPowerProportion);

        result.setTipPowerProportion(valueList.get(Constants.DIGIT_0));
        result.setPeakPowerProportion(valueList.get(Constants.DIGIT_1));
        result.setFlatPowerProportion(valueList.get(Constants.DIGIT_2));
        result.setTroughPowerProportion(valueList.get(Constants.DIGIT_3));
        result.setDeepPowerProportion(valueList.get(Constants.DIGIT_4));
    }

    /**
     * 根据电的尖、峰、平、谷进行费用的求和
     */
    private BigDecimal calculatePowerCost(List<ElectricityDataItem> electricityDataItems, String electricityType) {
        return electricityDataItems.stream()
                .filter(vo -> electricityType.equals(vo.getType()))
                .map(ElectricityDataItem::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 计算用能单元占比的数据
     *
     * @param tip    尖峰
     * @param peak   高峰
     * @param flat   平段
     * @param trough 谷段
     * @param deep   深谷段
     * @return 占比数组
     * @author Silence
     * @date 2022/3/8 17:52
     */
    private List<BigDecimal> calcUnitPowerProportionData(BigDecimal tip, BigDecimal peak, BigDecimal flat, BigDecimal trough, BigDecimal deep) {
        int[] arr = new int[]{
                getBigDecimalInterPart(tip),
                getBigDecimalInterPart(peak),
                getBigDecimalInterPart(flat),
                getBigDecimalInterPart(trough),
                getBigDecimalInterPart(deep)
        };
        double[] percentValue = CalcUtil.getPercentValue(arr, Constants.DIGIT_2);
        List<BigDecimal> valueList = new ArrayList<>(percentValue.length);
        for (double v : percentValue) {
            valueList.add(BigDecimal.valueOf(v).setScale(Constants.DIGIT_2, RoundingMode.HALF_UP));
        }
        return valueList;
    }

    /**
     * 根据电的尖、峰、平、谷进行用电量的求和
     */
    private BigDecimal calculatePowerConsumption(List<ElectricityDataItem> electricityDataItems, String electricityType) {
        return electricityDataItems.stream()
                .filter(vo -> electricityType.equals(vo.getType()))
                .map(ElectricityDataItem::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 获取BigDecimal的整数部分 默认精度是3
     *
     * @param data 要转换的数据
     * @author Silence
     * @date 2022/3/8 16:30
     **/
    private int getBigDecimalInterPart(BigDecimal data) {
        if (data.compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }
        return data.multiply(BigDecimal.valueOf(Math.pow(10, 3))).intValue();
    }

}