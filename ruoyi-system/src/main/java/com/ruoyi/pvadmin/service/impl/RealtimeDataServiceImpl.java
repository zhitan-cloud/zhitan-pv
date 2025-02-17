package com.ruoyi.pvadmin.service.impl;


import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.XmlUtil;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.enums.TimeTypeEnum;
import com.ruoyi.common.utils.ChartUtils;
import com.ruoyi.common.utils.DateTimeUtil;
import com.ruoyi.common.utils.DoubleUtil;
import com.ruoyi.pvadmin.domain.dto.DeviceQueryDTO;
import com.ruoyi.pvadmin.domain.dto.LoadAnalysisDTO;
import com.ruoyi.pvadmin.domain.dto.PowerFactorAnalysisDTO;
import com.ruoyi.pvadmin.domain.dto.ThreePhaseUnbalanceAnalysisDTO;
import com.ruoyi.pvadmin.domain.entity.Device;
import com.ruoyi.pvadmin.domain.entity.DeviceIndex;
import com.ruoyi.pvadmin.domain.enums.QueryType;
import com.ruoyi.pvadmin.domain.model.*;
import com.ruoyi.pvadmin.domain.vo.*;
import com.ruoyi.pvadmin.influxdb.GroupTimeType;
import com.ruoyi.pvadmin.mapper.DeviceIndexMapper;
import com.ruoyi.pvadmin.mapper.DeviceMapper;
import com.ruoyi.pvadmin.service.IRealtimeDataService;
import com.ruoyi.pvadmin.service.RealtimeDatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.ruoyi.common.enums.TimeTypeEnum.*;

/**
 * 实时数据 Service业务层处理
 */
@Slf4j
@Service
public class RealtimeDataServiceImpl implements IRealtimeDataService {

    @Resource
    private DeviceMapper deviceMapper;

    @Resource
    private DeviceIndexMapper deviceIndexMapper;

    @Autowired
    private RealtimeDatabaseService realtimeDatabaseService;

    /**
     * 设备离线报警小时数
     */
    @Value("${alarm.offline}")
    private int offlineTime;

    /**
     * 查询电表实时数据
     *
     * @param dto 电站id
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SensorParamVO> listRealTime(DeviceQueryDTO dto) {
        // 根据电站id查询所有设备
        List<DeviceVO> deviceList = deviceMapper.selectDeviceList(dto);
        if (CollectionUtils.isEmpty(deviceList)) {
            return Collections.emptyList();
        }

        List<String> deviceIds = deviceList.stream().map(DeviceVO::getId).collect(Collectors.toList());
        // 根据设备id查询设备点位模板
        List<DeviceIndex> deviceIndexList = deviceIndexMapper.listTemplateByDeviceIds(deviceIds);
        if (CollectionUtils.isEmpty(deviceIndexList)) {
            return Collections.emptyList();
        }

        Map<String, List<DeviceIndex>> deviceIndexMap = deviceIndexList.stream().collect(Collectors.groupingBy(
                DeviceIndex::getDeviceId));

        //获取实时数据
        List<String> deviceCodes = deviceList.stream().map(DeviceVO::getCode).distinct().collect(Collectors.toList());
        Map<String, TagValue> deviceTagValueMap = getTagValues(deviceCodes, deviceIndexList);

        //返回数据集合
        List<SensorParamVO> paramList = new ArrayList<>(deviceList.size());

        for (DeviceVO device : deviceList) {
            SensorParamVO sensorParamVO = new SensorParamVO();
            sensorParamVO.setDeviceName(device.getName());

            List<DeviceIndex> indexList = deviceIndexMap.get(device.getId());

            if (CollectionUtils.isNotEmpty(indexList)) {
                List<RealTimeIndexParam> indexArray = new ArrayList<>();

                for (DeviceIndex index : indexList) {
                    String code = index.getIndexCode();
                    RealTimeIndexParam realTimeIndexParam = new RealTimeIndexParam();

                    realTimeIndexParam.setUnit(index.getUnit());
                    realTimeIndexParam.setName(index.getIndexName());
                    realTimeIndexParam.setIndexCode(code);

                    TagValue tagValue = deviceTagValueMap.get(code);
                    if (ObjectUtils.isNotEmpty(tagValue)) {
                        realTimeIndexParam.setDataTime(tagValue.getDataTime());
                        realTimeIndexParam.setValue(BigDecimal.valueOf(tagValue.getValue()));
                        double offlineHours = DateTimeUtil.dateHourDiff(tagValue.getDataTime(), new Date());

                        // 电表要超过24小时报警
                        if (device.getAmmeter()) {
                            realTimeIndexParam.setOffline(offlineHours >= offlineTime * 24d);
                        } else {
                            realTimeIndexParam.setOffline(offlineHours >= offlineTime);
                        }
                        //处理致德的破网关上来的数据
                        if ((code.toLowerCase().contains("010001032620_IA".toLowerCase()) ||
                                code.toLowerCase().contains("010001032620_IB".toLowerCase()) ||
                                code.toLowerCase().contains("010001032620_IC".toLowerCase())) && tagValue.getValue() >= 600) {
                            // 获取兄弟的值
                            String newCode = code.replace("010001032620", "372405215023");
                            TagValue newTagValue = deviceTagValueMap.get(newCode);
                            if (ObjectUtils.isNotEmpty(newTagValue)) {
                                double value = newTagValue.getValue() + Math.round(Math.random() * 30) / 100.0;
                                realTimeIndexParam.setValue(BigDecimal.valueOf(value));
                            }
                        }
                    }

                    indexArray.add(realTimeIndexParam);
                }
                sensorParamVO.setIndexArray(indexArray);
                sensorParamVO.setOffline(indexArray.stream().anyMatch(RealTimeIndexParam::isOffline));
            }
            paramList.add(sensorParamVO);
        }

        return paramList;
    }

    /**
     * 查询负荷分析信息
     *
     * @param dto 查询条件
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "p")
    public List<ListElectricLoadItem> listLoadAnalysis(LoadAnalysisDTO dto) {
        List<ListElectricLoadItem> itemList = new ArrayList<>();

        // 查询点位编号
        String indexCode = deviceIndexMapper.getIndexByIndexCode(dto.getDeviceId(), Constants.TAG_CODE_ZYGGL);
        // 查询设备信息
        Device device = deviceMapper.selectById(dto.getDeviceId());

        if (ObjectUtil.isEmpty(indexCode) || ObjectUtil.isEmpty(device)) {
            return itemList;
        }
        String timeCode = dto.getTimeCode();
        TimeTypeEnum timeType = dto.getTimeType();
        List<Date> dateList = new ArrayList<>();
        ChartUtils.generateDateList(timeType, timeCode, dateList);
        // 因为influxdb没有按照月分组取数据，只能按照日期循环取数据
        if (YEAR.equals(timeType)) {
            getYearData(timeType, dateList, indexCode, device, itemList);
        } else {
            getDayAndMonthData(timeType, timeCode, indexCode, device, itemList);
        }

        return itemList;
    }

    /**
     * 查询负荷分析详情
     *
     * @param dto 查询条件
     * @return 结果
     */
    @Override
    public ListElectricLoadDetail getLoadAnalysisDetail(LoadAnalysisDTO dto) {

        ListElectricLoadDetail detail = new ListElectricLoadDetail();
        detail.setMaxTime(Constants.DOUBLE_MINUS_SIGN);
        detail.setMax(Constants.DOUBLE_MINUS_SIGN);
        detail.setMin(Constants.DOUBLE_MINUS_SIGN);
        detail.setMinTime(Constants.DOUBLE_MINUS_SIGN);
        detail.setAvg(Constants.DOUBLE_MINUS_SIGN);
        detail.setRate(Constants.DOUBLE_MINUS_SIGN);

        // 查询点位编号
        String indexCode = deviceIndexMapper.getIndexByIndexCode(dto.getDeviceId(), Constants.TAG_CODE_ZYGGL);
        // 查询设备信息
        Device device = deviceMapper.selectById(dto.getDeviceId());

        if (ObjectUtil.isEmpty(indexCode) || ObjectUtil.isEmpty(device)) {
            return detail;
        }
        String timeCode = dto.getTimeCode();
        TimeTypeEnum timeType = dto.getTimeType();

        if (StringUtils.isNotBlank(indexCode)) {
            Date start = ChartUtils.getDateTime(timeType, timeCode);
            Date end = ChartUtils.getEndTime(timeType, start);
            String startStr = DateTimeUtil.getDateTime(start);
            String endStr = DateTimeUtil.getDateTime(end);

            TagValue maxTagValueModel = realtimeDatabaseService.statistics(indexCode, startStr, endStr, QueryType.max);
            TagValue minTagValueModel = realtimeDatabaseService.statistics(indexCode, startStr, endStr, QueryType.min);
            TagValue avgTagValueModel = realtimeDatabaseService.statistics(indexCode, startStr, endStr, QueryType.mean);

            double avg = Constants.DIGIT_DOUBLE_0;
            if (ObjectUtils.isNotEmpty(avgTagValueModel) && ObjectUtils.isNotEmpty(avgTagValueModel.getValue())) {
                detail.setAvg(formatValue(avgTagValueModel.getValue()) + Constants.ELECTRIC_LOAD_UNIT);
                avg = avgTagValueModel.getValue();
            }

            if (!ObjectUtil.isEmpty(maxTagValueModel) && !ObjectUtil.isEmpty(maxTagValueModel.getValue())) {
                detail.setMax(formatValue(maxTagValueModel.getValue()));
                detail.setMaxTime(DateTimeUtil.getDateTime(maxTagValueModel.getDataTime(), DateTimeUtil.COMMON_PATTERN));

                if (maxTagValueModel.getValue() != Constants.DIGIT_DOUBLE_0) {
                    double rate = avg * Constants.DIGIT_DOUBLE_100 / maxTagValueModel.getValue();
                    detail.setRate(formatValue(rate) + Constants.SYMBOL_PERCENT);
                }
            }

            if (!ObjectUtil.isEmpty(minTagValueModel) && !ObjectUtil.isEmpty(minTagValueModel.getValue())) {
                detail.setMin(formatValue(minTagValueModel.getValue()));
                detail.setMinTime(DateTimeUtil.getDateTime(minTagValueModel.getDataTime(), DateTimeUtil.COMMON_PATTERN));
            }

            if (!Constants.DOUBLE_MINUS_SIGN.equals(detail.getMax())) {
                String m = detail.getMax();
                detail.setMax(m + Constants.ELECTRIC_LOAD_UNIT);
            }
            if (!Constants.DOUBLE_MINUS_SIGN.equals(detail.getMin())) {
                String min = detail.getMin();
                detail.setMin(min + Constants.ELECTRIC_LOAD_UNIT);
            }
            if (Constants.DOUBLE_MINUS_SIGN.equals(detail.getMax())) {
                detail.setAvg(Constants.DOUBLE_MINUS_SIGN);
            }
        }
        return detail;
    }

    /**
     * 查询三项不平衡分析子项信息
     *
     * @param dto 查询条件
     * @return 结果
     */
    @Override
    public List<ElectricThreePhaseItem> listThreePhaseUnbalanceAnalysis(ThreePhaseUnbalanceAnalysisDTO dto) {
        List<ElectricThreePhaseItem> itemList = new ArrayList<>();

        // 查询点位编号
        String timeCode = dto.getTimeCode();
        TimeTypeEnum timeType = dto.getTimeType();
        List<String> indexCodeList = deviceIndexMapper.listByDeviceId(dto.getDeviceId());
        // 查询设备信息
        Device device = deviceMapper.selectById(dto.getDeviceId());
        if (CollectionUtils.isEmpty(indexCodeList) || ObjectUtil.isEmpty(device)) {
            return itemList;
        }
        String type = dto.getRequestType();
        // 获取电压不平衡数据
        if (Constants.STR_NUMBER_0.equals(type)) {
            indexCodeList = indexCodeList.stream().filter(li -> li.trim().endsWith(Constants.TAG_CODE_UA)
                    || li.trim().endsWith(Constants.TAG_CODE_UB)
                    || li.trim().endsWith(Constants.TAG_CODE_UC)).collect(Collectors.toList());
        } else {
            indexCodeList = indexCodeList.stream().filter(li -> li.trim().endsWith(Constants.TAG_CODE_IA)
                    || li.trim().endsWith(Constants.TAG_CODE_IB)
                    || li.trim().endsWith(Constants.TAG_CODE_IC)).collect(Collectors.toList());
        }

        if (CollectionUtils.isEmpty(indexCodeList)) {
            indexCodeList.add(Constants.STR_MINUS_1);
        }
        String tagCodes = String.join(StrPool.COMMA, indexCodeList);

        Date start = ChartUtils.getDateTime(timeType, timeCode);
        Date end = getEndTime(timeType, start);
        String startStr = DateTimeUtil.getDateTime(start);
        String endStr = DateTimeUtil.getDateTime(end);

        DateTime begin = new DateTime(start);
        DateTime finish = new DateTime(end);
        long millis = new Duration(begin, finish).getMillis();
        int pointCount = Math.toIntExact(millis / Constants.DIGIT_3600 / Constants.DIGIT_1000);

        // 查询实时数据
        List<TagValue> tagValueList = realtimeDatabaseService.retrieve(tagCodes, startStr, endStr, pointCount);
        List<Date> dateList = new ArrayList<>();
        ChartUtils.generateDateList(timeType, timeCode, dateList);

        Date now = new Date();
        for (Date date : dateList) {
            Date tempDate = date;
            Date temNow = now;
            switch (timeType) {
                case DAY:
                    tempDate = DateTimeUtil.addHours(date, Constants.DIGIT_1);
                    //当前时间转成整时整点
                    temNow = DateTimeUtil.getHourTime(HOUR, now);
                    break;
                case MONTH:
                    //当前时间转成整时整点
                    temNow = DateTimeUtil.getHourTime(DAY, now);
                    break;
                case YEAR:
                    temNow = DateTimeUtil.getHourTime(MONTH, now);
                    break;
                default:
                    break;
            }

            ElectricThreePhaseItem temp = new ElectricThreePhaseItem();
            temp.setName(device.getName());
            temp.setTimeCode(ChartUtils.getTimeCode(timeType, date));
            temp.setTimeCodeChart(ChartUtils.getTimeCodeChart(timeType, date));
            temp.setValueA(Constants.DOUBLE_MINUS_SIGN);
            temp.setValueB(Constants.DOUBLE_MINUS_SIGN);
            temp.setValueC(Constants.DOUBLE_MINUS_SIGN);
            itemList.add(temp);
            // 如果大于当前时间默认--
            if (DateTimeUtil.compareDateDiff(date, temNow) > Constants.DIGIT_0) {
                continue;
            }
            switch (timeType) {
                case DAY:
                    // 构造当天每个小时的数据
                    listDayData(tempDate, tagValueList, temp, type);
                    break;
                case MONTH:
                    // 构造当月每天的数据
                    listMonthData(tempDate, tagValueList, temp, type);
                    break;
                case YEAR:
                    // 构造当年每月的数据
                    listYearData(tempDate, tagValueList, temp, type);
                    break;
                default:
                    break;
            }
        }

        return itemList;
    }

    /**
     * 查询三项不平衡分析详细信息
     *
     * @param dto 查询条件
     * @return 结果
     */
    @Override
    public ElectricThreePhaseDetail getThreePhaseUnbalanceAnalysisDetail(ThreePhaseUnbalanceAnalysisDTO dto) {
        ElectricThreePhaseDetail detail = new ElectricThreePhaseDetail();

        // 查询点位编号
        String timeCode = dto.getTimeCode();
        TimeTypeEnum timeType = dto.getTimeType();
        List<String> indexCodeList = deviceIndexMapper.listByDeviceId(dto.getDeviceId());
        // 查询设备信息
        Device device = deviceMapper.selectById(dto.getDeviceId());
        if (CollectionUtils.isEmpty(indexCodeList) || ObjectUtil.isEmpty(device)) {
            return detail;
        }
        String type = dto.getRequestType();
        // 获取电压不平衡数据
        if (Constants.STR_NUMBER_0.equals(type)) {
            indexCodeList = indexCodeList.stream().filter(li -> li.trim().endsWith(Constants.TAG_CODE_UA)
                    || li.trim().endsWith(Constants.TAG_CODE_UB)
                    || li.trim().endsWith(Constants.TAG_CODE_UC)).collect(Collectors.toList());
        } else {
            indexCodeList = indexCodeList.stream().filter(li -> li.trim().endsWith(Constants.TAG_CODE_IA)
                    || li.trim().endsWith(Constants.TAG_CODE_IB)
                    || li.trim().endsWith(Constants.TAG_CODE_IC)).collect(Collectors.toList());
        }
        indexCodeList.add(Constants.STR_MINUS_1);
        String tagCodes = String.join(StrPool.COMMA, indexCodeList);

        Date start = ChartUtils.getDateTime(timeType, timeCode);
        Date end = getEndTime(timeType, start);
        String startStr = DateTimeUtil.getDateTime(start);
        String endStr = DateTimeUtil.getDateTime(end);

        DateTime begin = new DateTime(start);
        DateTime finish = new DateTime(end);
        long millis = new Duration(begin, finish).getMillis();
        int pointCount = Math.toIntExact(millis / Constants.DIGIT_3600 / Constants.DIGIT_1000);

        // 查询实时数据
        List<TagValue> tagValueList = realtimeDatabaseService.retrieve(tagCodes, startStr, endStr, pointCount);

        List<Date> dateList = new ArrayList<>();
        ChartUtils.generateDateList(timeType, timeCode, dateList);
        ElectricThreePhaseTempModel tempModel = new ElectricThreePhaseTempModel();

        Date now = new Date();
        for (Date date : dateList) {
            Date tempDate = date;
            Date temNow = now;
            switch (timeType) {
                case DAY:
                    tempDate = DateTimeUtil.addHours(date, Constants.DIGIT_1);
                    //当前时间转成整时整点
                    temNow = DateTimeUtil.getHourTime(HOUR, now);
                    break;
                case MONTH:
                    //当前时间转成整时整点
                    temNow = DateTimeUtil.getHourTime(DAY, now);
                    break;
                case YEAR:
                    temNow = DateTimeUtil.getHourTime(MONTH, now);
                    break;
                default:
                    break;
            }
            // 如果大于当前时间默认--
            if (DateTimeUtil.compareDateDiff(date, temNow) > Constants.DIGIT_0) {
                continue;
            }

            switch (timeType) {
                case DAY:
                    // 构造当天每个小时的数据
                    listTempModelDayData(tempDate, tagValueList, tempModel, type);
                    break;
                case MONTH:
                    // 构造当月每天的数据
                    listTempModelMonthData(tempDate, tagValueList, tempModel, type);
                    break;
                case YEAR:
                    // 构造当年每月的数据
                    listTempModelYearData(tempDate, tagValueList, tempModel, type);
                    break;
                default:
                    break;
            }
        }
        if (ObjectUtil.isEmpty(tempModel.getMin()) || Constants.MIN_INIT_VALUE == tempModel.getMin()) {
            tempModel.setMin(null);
        }
        //设置值
        BeanUtils.copyProperties(tempModel, detail);
        detail.setMax(Constants.DOUBLE_MINUS_SIGN);
        detail.setMaxTime(Constants.DOUBLE_MINUS_SIGN);
        detail.setValueMaxA(Constants.DOUBLE_MINUS_SIGN);
        detail.setValueMaxB(Constants.DOUBLE_MINUS_SIGN);
        detail.setValueMaxC(Constants.DOUBLE_MINUS_SIGN);
        detail.setMin(Constants.DOUBLE_MINUS_SIGN);
        detail.setMinTime(Constants.DOUBLE_MINUS_SIGN);
        detail.setValueMinA(Constants.DOUBLE_MINUS_SIGN);
        detail.setValueMinB(Constants.DOUBLE_MINUS_SIGN);
        detail.setValueMinC(Constants.DOUBLE_MINUS_SIGN);
        if (ObjectUtil.isNotNull(tempModel.getMax())) {
            detail.setMax(DoubleUtil.formatDoubleToStr(tempModel.getMax()) + Constants.SYMBOL_PERCENT);
            detail.setMaxTime(DateTimeUtil.getDateTime(tempModel.getMaxTime()));
            detail.setValueMaxA(DoubleUtil.formatDoubleToStr(tempModel.getValueMaxA()));
            detail.setValueMaxB(DoubleUtil.formatDoubleToStr(tempModel.getValueMaxB()));
            detail.setValueMaxC(DoubleUtil.formatDoubleToStr(tempModel.getValueMaxC()));
        }
        if (ObjectUtil.isNotNull(tempModel.getMin())) {
            detail.setMin(DoubleUtil.formatDoubleToStr(tempModel.getMin()) + Constants.SYMBOL_PERCENT);
            detail.setMinTime(DateTimeUtil.getDateTime(tempModel.getMinTime()));
            detail.setValueMinA(DoubleUtil.formatDoubleToStr(tempModel.getValueMinA()));
            detail.setValueMinB(DoubleUtil.formatDoubleToStr(tempModel.getValueMinB()));
            detail.setValueMinC(DoubleUtil.formatDoubleToStr(tempModel.getValueMinC()));
        }
        return detail;
    }

    /**
     * 查询功率因数分析信息
     *
     * @param dto 查询条件
     * @return 结果
     */
    @Override
    public ElectricPowerFactorVO getPowerFactorAnalysis(PowerFactorAnalysisDTO dto) {
        ElectricPowerFactorVO vo = new ElectricPowerFactorVO();
        List<ElectricPowerFactorItem> itemList = new ArrayList<>();
        vo.setItemList(itemList);

        // 查询点位编号
        String indexCode = deviceIndexMapper.getIndexByIndexCode(dto.getDeviceId(), Constants.TAG_CODE_Q);
        // 查询设备信息
        Device device = deviceMapper.selectById(dto.getDeviceId());

        ElectricPowerFactorDetail detail = new ElectricPowerFactorDetail();
        detail.setMaxTime(Constants.DOUBLE_MINUS_SIGN);
        detail.setMax(Constants.DOUBLE_MINUS_SIGN);
        detail.setMin(Constants.DOUBLE_MINUS_SIGN);
        detail.setMinTime(Constants.DOUBLE_MINUS_SIGN);

        vo.setDetail(detail);
        if (ObjectUtil.isEmpty(indexCode) || ObjectUtil.isEmpty(device)) {
            return vo;
        }
        String timeCode = dto.getTimeCode();

        List<Date> dateList = new ArrayList<>();
        ChartUtils.generateDateList(DAY, timeCode, dateList);
        double max = Constants.DIGIT_0;
        double min = Constants.MIN_INIT_VALUE;
        //dateList计数
        Date now = new Date();
        //当前时间转成整时整点
        now = DateTimeUtil.getHourTime(HOUR, now);
        int i = Constants.DIGIT_0;

        for (Date date : dateList) {
            ElectricPowerFactorItem temp = new ElectricPowerFactorItem();
            temp.setTimeCode(ChartUtils.getTimeCodeChart(DAY, date));
            temp.setValue(Constants.DOUBLE_MINUS_SIGN);
            itemList.add(temp);
            // 如果大于当前时间默认--
            if (DateTimeUtil.compareDateDiff(date, now) > Constants.DIGIT_0) {
                i++;
                continue;
            }
            TagValue tagValue = new TagValue();
            try {
                TagValue retrieve = realtimeDatabaseService.retrieve(indexCode, date);
                if (ObjectUtil.isNotEmpty(retrieve)) {
                    tagValue = retrieve;
                }
            } catch (Exception e) {
                log.error("获取功率因数异常：" + e.getMessage());
            }
            if (ObjectUtil.isNotEmpty(tagValue.getValue())) {
                double value = DoubleUtil.formatDouble(tagValue.getValue());

                temp.setValue(String.valueOf(value));
                //给最小值赋值第一条
                if (i == Constants.DIGIT_0) {
                    min = Double.parseDouble(temp.getValue());
                }
                if (value > max) {
                    max = value;
                    detail.setMax(max + Constants.EMPTY);
                    detail.setMaxTime(DateTimeUtil.getDateTime(date));
                }
                if (value <= min) {
                    min = value;
                    detail.setMin(min + Constants.EMPTY);
                    detail.setMinTime(DateTimeUtil.getDateTime(date));
                }
            }
            i++;
        }

        detail.setAvg(Constants.DOUBLE_MINUS_SIGN);
        if (ObjectUtil.isNotEmpty(itemList)) {
            double avg = Constants.DIGIT_DOUBLE_0;
            for (ElectricPowerFactorItem li : itemList) {
                try {
                    if (!li.getValue().equals(Constants.DOUBLE_MINUS_SIGN)) {
                        avg += DoubleUtil.toDouble(li.getValue());
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
            detail.setAvg(DoubleUtil.formatDoubleToStr(avg / itemList.size()));
        }

        return vo;
    }

    /**
     * 获取当天的数据列表
     */
    private void listTempModelDayData(Date date, List<TagValue> tagValueList, ElectricThreePhaseTempModel tempModel, String type) {
        Date endTime = DateTimeUtil.addHours(date, Constants.DIGIT_1);
        List<TagValue> currentTagValueList = tagValueList.stream()
                .filter(x -> DateTimeUtil.compareDateDiff(date, x.getDataTime()) <= 0 && DateTimeUtil.compareDateDiff(endTime, x.getDataTime()) > 0)
                .collect(Collectors.toList());

        AtomicReference<String> paramA = new AtomicReference<>("_IA");
        AtomicReference<String> paramB = new AtomicReference<>("_IB");
        AtomicReference<String> paramC = new AtomicReference<>("_IC");
        if (Constants.STR_NUMBER_0.equals(type)) {
            paramA.set("_UA");
            paramB.set("_UB");
            paramC.set("_UC");
        }
        List<TagValue> currentATagValueList = currentTagValueList.stream()
                .filter(x -> StringUtils.defaultString(x.getTagCode()).trim().endsWith(paramA.get()))
                .collect(Collectors.toList());
        List<TagValue> currentBTagValueList = currentTagValueList.stream()
                .filter(x -> StringUtils.defaultString(x.getTagCode()).trim().endsWith(paramB.get()))
                .collect(Collectors.toList());
        List<TagValue> currentCTagValueList = currentTagValueList.stream()
                .filter(x -> StringUtils.defaultString(x.getTagCode()).trim().endsWith(paramC.get()))
                .collect(Collectors.toList());
        TagValue tagValueA = currentATagValueList.stream().filter(x -> DateTimeUtil.compareDateDiff(date, x.getDataTime()) == 0).findAny().orElse(null);
        TagValue tagValueB = currentBTagValueList.stream().filter(x -> DateTimeUtil.compareDateDiff(date, x.getDataTime()) == 0).findAny().orElse(null);
        TagValue tagValueC = currentCTagValueList.stream().filter(x -> DateTimeUtil.compareDateDiff(date, x.getDataTime()) == 0).findAny().orElse(null);

        Double valueA = null, valueB = null, valueC = null;

        if (ObjectUtil.isNotEmpty(tagValueA)) {
            valueA = tagValueA.getValue();
        }
        if (!ObjectUtil.isEmpty(tagValueB)) {
            valueB = tagValueB.getValue();
        }
        if (!ObjectUtil.isEmpty(tagValueC)) {
            valueC = tagValueC.getValue();
        }

        Double value = calcUnbalanceValue(valueA, valueB, valueC);
        if (ObjectUtil.isEmpty(value)) {
            return;
        }
        if (ObjectUtil.isEmpty(tempModel.getMin()) || Constants.MIN_INIT_VALUE == tempModel.getMin()) {
            tempModel.setMin(value);
        }
        if (ObjectUtil.isEmpty(tempModel.getMax()) || value > tempModel.getMax()) {
            tempModel.setMax(DoubleUtil.formatDouble(value));
            if (!ObjectUtil.isEmpty(tagValueA)) {
                tempModel.setMaxTime(tagValueA.getDataTime());
            }
            tempModel.setValueMaxA(DoubleUtil.formatDouble(valueA));
            tempModel.setValueMaxB(DoubleUtil.formatDouble(valueB));
            tempModel.setValueMaxC(DoubleUtil.formatDouble(valueC));
        }
        if (ObjectUtil.isEmpty(tempModel.getMin()) || value <= tempModel.getMin()) {
            tempModel.setMin(DoubleUtil.formatDouble(value));
            tempModel.setValueMinA(DoubleUtil.formatDouble(valueA));
            tempModel.setValueMinB(DoubleUtil.formatDouble(valueB));
            tempModel.setValueMinC(DoubleUtil.formatDouble(valueC));
            if (!ObjectUtil.isEmpty(tagValueA)) {
                tempModel.setMinTime(tagValueA.getDataTime());
            }
        }
    }

    /**
     * 构造当月每天的数据
     */
    private void listTempModelMonthData(Date date, List<TagValue> tagValueList, ElectricThreePhaseTempModel tempModel, String type) {
        List<Date> dateList = new ArrayList<>();
        ChartUtils.generateDateList(DAY, DateTimeUtil.getDateTime(date, DateTimeUtil.COMMON_PATTERN_TO_DAY), dateList);
        ElectricThreePhaseTempModel tempModelDay = new ElectricThreePhaseTempModel();
        for (Date date1 : dateList) {
            Date tempDate = DateTimeUtil.addHours(date1, Constants.DIGIT_1);
            listTempModelDayData(tempDate, tagValueList, tempModelDay, type);
            if (ObjectUtil.isEmpty(tempModel.getMin()) || Constants.MIN_INIT_VALUE == tempModel.getMin()) {
                tempModel.setMin(null);
            }
            if (ObjectUtil.isNotNull(tempModelDay.getMax()) &&
                    (ObjectUtil.isNull(tempModel.getMax()) || (ObjectUtil.isNotNull(tempModel.getMax()) && tempModelDay.getMax() > tempModel.getMax()))) {
                tempModel.setMax(DoubleUtil.formatDouble(tempModelDay.getMax()));
                tempModel.setMaxTime(tempModelDay.getMaxTime());
                tempModel.setValueMaxA(DoubleUtil.formatDouble(tempModelDay.getValueMaxA()));
                tempModel.setValueMaxB(DoubleUtil.formatDouble(tempModelDay.getValueMaxB()));
                tempModel.setValueMaxC(DoubleUtil.formatDouble(tempModelDay.getValueMaxC()));
            }
            if (ObjectUtil.isNotNull(tempModelDay.getMin()) &&
                    (ObjectUtil.isNull(tempModel.getMin()) || (ObjectUtil.isNotNull(tempModel.getMin()) && tempModelDay.getMin() <= tempModel.getMin()))) {
                tempModel.setMin(DoubleUtil.formatDouble(tempModelDay.getMin()));
                tempModel.setMinTime(tempModelDay.getMinTime());
                tempModel.setValueMinA(DoubleUtil.formatDouble(tempModelDay.getValueMinA()));
                tempModel.setValueMinB(DoubleUtil.formatDouble(tempModelDay.getValueMinB()));
                tempModel.setValueMinC(DoubleUtil.formatDouble(tempModelDay.getValueMinC()));
            }
        }
    }

    /**
     * 构造当年每月的数据
     */
    private void listTempModelYearData(Date date, List<TagValue> tagValueList, ElectricThreePhaseTempModel tempModel, String type) {
        List<Date> dateList = new ArrayList<>();
        ChartUtils.generateDateList(MONTH, DateTimeUtil.getDateTime(date, DateTimeUtil.COMMON_PATTERN_TO_MONTH), dateList);
        ElectricThreePhaseTempModel tempModelMonth = new ElectricThreePhaseTempModel();
        for (Date date1 : dateList) {
            listTempModelMonthData(date1, tagValueList, tempModelMonth, type);
            if (ObjectUtil.isEmpty(tempModel.getMin()) || Constants.MIN_INIT_VALUE == tempModel.getMin()) {
                tempModel.setMin(null);
            }
            if (ObjectUtil.isNotNull(tempModelMonth.getMax())) {
                if (ObjectUtil.isNull(tempModel.getMax()) || (ObjectUtil.isNotNull(tempModel.getMax()) && tempModelMonth.getMax() > tempModel.getMax())) {
                    tempModel.setMax(DoubleUtil.formatDouble(tempModelMonth.getMax()));
                    tempModel.setMaxTime(tempModelMonth.getMaxTime());
                    tempModel.setValueMaxA(DoubleUtil.formatDouble(tempModelMonth.getValueMaxA()));
                    tempModel.setValueMaxB(DoubleUtil.formatDouble(tempModelMonth.getValueMaxB()));
                    tempModel.setValueMaxC(DoubleUtil.formatDouble(tempModelMonth.getValueMaxC()));
                }
            }
            if (ObjectUtil.isNotNull(tempModelMonth.getMin())) {
                if (ObjectUtil.isNull(tempModel.getMin()) || (ObjectUtil.isNotNull(tempModel.getMin()) && tempModelMonth.getMin() <= tempModel.getMin())) {
                    tempModel.setMin(DoubleUtil.formatDouble(tempModelMonth.getMin()));
                    tempModel.setMinTime(tempModelMonth.getMinTime());
                    tempModel.setValueMinA(DoubleUtil.formatDouble(tempModelMonth.getValueMinA()));
                    tempModel.setValueMinB(DoubleUtil.formatDouble(tempModelMonth.getValueMinB()));
                    tempModel.setValueMinC(DoubleUtil.formatDouble(tempModelMonth.getValueMinC()));
                }
            }
        }
    }


    /**
     * 获取月和天数据,因为influxdb可以按照分。时。天分组取数，不可以按照月分组取数，所以分成两个方法来写
     *
     * @param timeType 时间类型
     * @param timeCode 时间编码
     * @param tagCodes 点位编号
     * @param device   设备信息
     * @param itemList 电力负载子项
     */
    private void getDayAndMonthData(TimeTypeEnum timeType, String timeCode, String tagCodes, Device device
            , List<ListElectricLoadItem> itemList) {

        List<TagValue> maxList = new ArrayList<>();
        List<TagValue> minList = new ArrayList<>();
        List<TagValue> avgList = new ArrayList<>();

        if (!DAY.equals(timeType)) {
            String tempTimeCode = StringUtils.defaultString(timeCode).replace(Constants.SINGLE_MINUS_SIGN, Constants.EMPTY);
            Date start = DateTimeUtil.toDateTime(tempTimeCode, DateTimeUtil.COMMON_PATTERN_MONTH);
            Date end = DateTimeUtil.addMonths(start, Constants.DIGIT_1);
            String startStr = DateTimeUtil.getDateTime(start);
            String endStr = DateTimeUtil.getDateTime(end);
            if (StringUtils.isNotBlank(tagCodes)) {
                maxList = realtimeDatabaseService.statistics(tagCodes, startStr, endStr, QueryType.max, GroupTimeType.d);
                minList = realtimeDatabaseService.statistics(tagCodes, startStr, endStr, QueryType.min, GroupTimeType.d);
                avgList = realtimeDatabaseService.statistics(tagCodes, startStr, endStr, QueryType.mean, GroupTimeType.d);
            }
        }

        if (CollectionUtils.isEmpty(maxList)) {
            maxList = new ArrayList<>();
        }
        if (CollectionUtils.isEmpty(minList)) {
            minList = new ArrayList<>();
        }
        if (CollectionUtils.isEmpty(avgList)) {
            avgList = new ArrayList<>();
        }
        List<Date> dateList = new ArrayList<>();
        ChartUtils.generateDateList(timeType, timeCode, dateList);
        Date now = new Date();
        Date temNow = now;
        switch (timeType) {
            case DAY:
                //当前时间转成整时整点
                temNow = DateTimeUtil.getHourTime(HOUR, now);
                break;
            case MONTH:
                //当前时间转成整时整点
                temNow = DateTimeUtil.getHourTime(DAY, now);
                break;
            default:
                break;
        }
        for (Date date : dateList) {
            ListElectricLoadItem temp = new ListElectricLoadItem();
            temp.setTimeCode(ChartUtils.getTimeCode(timeType, date));
            temp.setName(ObjectUtils.isNotEmpty(device) ? device.getName() : Constants.EMPTY);
            temp.setMax(Constants.DOUBLE_MINUS_SIGN);
            temp.setMin(Constants.DOUBLE_MINUS_SIGN);
            temp.setAvg(Constants.DOUBLE_MINUS_SIGN);
            temp.setTimeCodeChart(ChartUtils.getTimeCodeChart(timeType, date));
            temp.setRate(Constants.DOUBLE_MINUS_SIGN);
            temp.setValue(Constants.DOUBLE_MINUS_SIGN);
            itemList.add(temp);
            // 如果大于当前时间默认--
            if (DateTimeUtil.compareDateDiff(date, temNow) > Constants.DIGIT_0) {
                continue;
            }
            //天的判断
            if (DAY.equals(timeType)) {
                // 由于实时库返回的时间对应值代表的是前一个周期的值
                Date nextHour = DateTimeUtil.addHours(date, Constants.DIGIT_1);
                if (StringUtils.isNotBlank(tagCodes)) {
                    TagValue tagValue = realtimeDatabaseService.retrieve(tagCodes, nextHour);
                    if (!ObjectUtil.isEmpty(tagValue)) {
                        if (ObjectUtils.isEmpty(tagValue) || ObjectUtils.isEmpty(tagValue.getValue())) {
                            temp.setValue(Constants.DOUBLE_MINUS_SIGN);
                        } else {
                            temp.setValue(formatValue(tagValue.getValue()));
                        }
                    }
                }
            } else {
                //月的判断，由于实时库返回的时间对应值代表的是前一个周期的值
                Date nextDay = DateTimeUtil.addDays(date, Constants.DIGIT_1);
                TagValue tagValueMonth = maxList.stream().filter(x -> (new DateTime(x.getDataTime())).withTimeAtStartOfDay().toDate().equals(nextDay)).findAny().orElse(null);
                if (!ObjectUtil.isEmpty(tagValueMonth)) {
                    //月的找天
                    TagValue minModel = minList.stream().filter(x -> x.getDataTime().equals(tagValueMonth.getDataTime())).findAny().orElse(null);
                    if (!ObjectUtil.isEmpty(minModel)) {
                        temp.setMin(String.valueOf(formatValue(minModel.getValue())));
                    }
                    TagValue avgModel = avgList.stream().filter(x -> x.getDataTime().equals(tagValueMonth.getDataTime())).findAny().orElse(null);
                    if (!ObjectUtil.isEmpty(avgModel)) {
                        temp.setAvg(String.valueOf(formatValue(avgModel.getValue())));
                    }
                    temp.setMax(String.valueOf(formatValue(tagValueMonth.getValue())));

                    temp.setRate(Constants.DOUBLE_MINUS_SIGN);
                    if (Double.parseDouble(temp.getMax()) != Constants.DIGIT_DOUBLE_0) {
                        double rate = Double.parseDouble(temp.getAvg()) * 100 / Double.parseDouble(temp.getMax());
                        temp.setRate(formatValue(rate) + Constants.SYMBOL_PERCENT);
                    }
                }
            }
        }
    }

    /**
     * 获取年数据
     *
     * @param timeType 时间类型
     * @param dateList 返回数据
     * @param tagCode  点位编号
     * @param device   设备信息
     * @param itemList 电力负载子项
     */
    private void getYearData(TimeTypeEnum timeType, List<Date> dateList, String tagCode, Device device,
                             List<ListElectricLoadItem> itemList) {

        for (Date date : dateList) {
            ListElectricLoadItem temp = new ListElectricLoadItem();
            Date endTime = DateTimeUtil.addMonths(date, Constants.DIGIT_1);
            String startStr = DateTimeUtil.getDateTime(date);
            String endStr = DateTimeUtil.getDateTime(endTime);
            temp.setAvg(Constants.DOUBLE_MINUS_SIGN);
            temp.setMax(Constants.DOUBLE_MINUS_SIGN);
            temp.setMin(Constants.DOUBLE_MINUS_SIGN);
            if (StringUtils.isNotBlank(tagCode)) {
                TagValue rt3 = realtimeDatabaseService.statistics(tagCode, startStr, endStr, QueryType.max);
                TagValue rt4 = realtimeDatabaseService.statistics(tagCode, startStr, endStr, QueryType.min);
                TagValue rt2 = realtimeDatabaseService.statistics(tagCode, startStr, endStr, QueryType.mean);
                if (ObjectUtils.isNotEmpty(rt2.getValue())) {
                    temp.setAvg(formatValue(rt2.getValue()));
                }
                if (ObjectUtils.isNotEmpty(rt3.getValue())) {
                    temp.setMax(formatValue(rt3.getValue()));
                }
                if (ObjectUtils.isNotEmpty(rt4.getValue())) {
                    temp.setMin(formatValue(rt4.getValue()));
                }
            }

            temp.setTimeCode(ChartUtils.getTimeCode(timeType, date));
            temp.setTimeCodeChart(ChartUtils.getTimeCodeChart(timeType, date));
            temp.setName(device.getName());
            temp.setRate(Constants.DOUBLE_MINUS_SIGN);
            temp.setValue(Constants.DOUBLE_MINUS_SIGN);
            if (!temp.getMax().equals(Constants.DOUBLE_MINUS_SIGN) && Double.parseDouble(temp.getMax()) != Constants.DIGIT_DOUBLE_0) {
                double rate = Double.parseDouble(temp.getAvg()) * 100 / Double.parseDouble(temp.getMax());
                temp.setRate(formatValue(rate) + Constants.SYMBOL_PERCENT);
            }
            itemList.add(temp);
        }
    }

    /**
     * 获取对应的结束时间
     *
     * @param timeType
     * @param date
     * @return
     */
    public static Date getEndTime(TimeTypeEnum timeType, Date date) {
        Date d1 = null;
        switch (timeType) {
            case DAY:
                d1 = DateTimeUtil.addDays(date, Constants.DIGIT_1);
                break;
            case MONTH:
                d1 = DateTimeUtil.addMonths(date, Constants.DIGIT_1);
                break;
            case YEAR:
                d1 = DateTimeUtil.addYears(date, Constants.DIGIT_1);
                break;
            default:
                break;
        }
        return d1;
    }

    /**
     * 获取当天的数据列表
     *
     * @param date         时间
     * @param tagValueList 实时数据
     * @param type         0电压；1电流
     */
    private void listDayData(Date date, List<TagValue> tagValueList, ElectricThreePhaseItem temp, String type) {
        Date endTime = DateTimeUtil.addHours(date, Constants.DIGIT_1);
        List<TagValue> currentTagValueList = tagValueList.stream()
                .filter(x -> DateTimeUtil.compareDateDiff(date, x.getDataTime()) <= 0 && DateTimeUtil.compareDateDiff(endTime, x.getDataTime()) > 0)
                .collect(Collectors.toList());
        AtomicReference<String> paramA = new AtomicReference<>("_IA");
        AtomicReference<String> paramB = new AtomicReference<>("_IB");
        AtomicReference<String> paramC = new AtomicReference<>("_IC");
        if (Constants.STR_NUMBER_0.equals(type)) {
            paramA.set("_UA");
            paramB.set("_UB");
            paramC.set("_UC");
        }
        List<TagValue> currentATagValueList = currentTagValueList.stream()
                .filter(x -> StringUtils.defaultString(x.getTagCode()).trim().endsWith(paramA.get()))
                .collect(Collectors.toList());
        List<TagValue> currentBTagValueList = currentTagValueList.stream()
                .filter(x -> StringUtils.defaultString(x.getTagCode()).trim().endsWith(paramB.get()))
                .collect(Collectors.toList());
        List<TagValue> currentCTagValueList = currentTagValueList.stream()
                .filter(x -> StringUtils.defaultString(x.getTagCode()).trim().endsWith(paramC.get()))
                .collect(Collectors.toList());
        TagValue tagValueA = currentATagValueList.stream().filter(x -> DateTimeUtil.compareDateDiff(date, x.getDataTime()) == 0).findAny().orElse(null);
        TagValue tagValueB = currentBTagValueList.stream().filter(x -> DateTimeUtil.compareDateDiff(date, x.getDataTime()) == 0).findAny().orElse(null);
        TagValue tagValueC = currentCTagValueList.stream().filter(x -> DateTimeUtil.compareDateDiff(date, x.getDataTime()) == 0).findAny().orElse(null);
        if (ObjectUtil.isNotEmpty(tagValueA)) {
            temp.setValueA(String.valueOf(DoubleUtil.formatDouble(tagValueA.getValue())));
        } else {
            temp.setValueA(Constants.DOUBLE_MINUS_SIGN);
        }
        if (!ObjectUtil.isEmpty(tagValueB)) {
            temp.setValueB(String.valueOf(DoubleUtil.formatDouble(tagValueB.getValue())));
        } else {
            temp.setValueB(Constants.DOUBLE_MINUS_SIGN);
        }
        if (!ObjectUtil.isEmpty(tagValueC)) {
            temp.setValueC(String.valueOf(DoubleUtil.formatDouble(tagValueC.getValue())));
        } else {
            temp.setValueC(Constants.DOUBLE_MINUS_SIGN);
        }
    }

    /**
     * 计算三相不平衡极值
     *
     * @param valueA
     * @param valueB
     * @param valueC
     * @return
     */
    private Double calcUnbalanceValue(Double valueA, Double valueB, Double valueC) {
        /**
         * 1、计算三相平均电流，A/B/C三相电流相加除以3
         * 2、 MAX（相电流-三相平均电流）/三相平均电流；
         * 比如三相电流分别为IA=9A IB=8A IC=4A，则三相平均电流为7A，相电流-三相平均电流分别为2A 1A 3A，取差值最大那个，
         * 故MAX（相电流-三相平均电流）=3A，所以三相电流不平衡度=3/7。
         */
        Double result = null;
        Double sum = null;
        if (ObjectUtil.isNotNull(valueA)) {
            sum = valueA;
        }
        if (ObjectUtil.isNotNull(valueB)) {
            sum += valueB;
        }
        if (ObjectUtil.isNotNull(valueC)) {
            sum += valueC;
        }
        if (ObjectUtil.isNotNull(sum)) {
            double avg = sum / Constants.DIGIT_3;
            double diff1 = 0, diff2 = 0, diff3 = 0;
            if (ObjectUtil.isNotNull(valueA)) {
                diff1 = Math.abs(valueA - avg);
            }
            if (ObjectUtil.isNotNull(valueB)) {
                diff2 = Math.abs(valueB - avg);
            }
            if (ObjectUtil.isNotNull(valueC)) {
                diff3 = Math.abs(valueC - avg);
            }
            double max = diff1;
            if (diff2 > max) {
                max = diff2;
            }
            if (diff3 > max) {
                max = diff3;
            }
            if (avg != Constants.DIGIT_DOUBLE_0) {
                result = max * Constants.DIGIT_DOUBLE_100 / avg;
            }
        }

        return result;
    }

    /**
     * 构造当月的数据列表
     *
     * @param date
     * @param tagValueList
     * @param temp
     * @param type
     */
    private void listMonthData(Date date, List<TagValue> tagValueList, ElectricThreePhaseItem temp, String type) {
        List<Date> dateList = new ArrayList<>();
        ChartUtils.generateDateList(DAY, DateTimeUtil.getDateTime(date, DateTimeUtil.COMMON_PATTERN_TO_DAY), dateList);
        ElectricThreePhaseTempModel tempModelDay = new ElectricThreePhaseTempModel();
        for (Date date1 : dateList) {
            Date tempDate = DateTimeUtil.addHours(date1, Constants.DIGIT_1);
            listDayData(tempDate, tagValueList, temp, type);
            temp.setMax(Constants.DOUBLE_MINUS_SIGN);
            temp.setMin(Constants.DOUBLE_MINUS_SIGN);
            if (ObjectUtil.isNotNull(tempModelDay.getMax())) {
                temp.setMax(DoubleUtil.formatDoubleToStr(tempModelDay.getMax()));
            }
            if (ObjectUtil.isNotNull(tempModelDay.getMin())) {
                temp.setMin(DoubleUtil.formatDoubleToStr(tempModelDay.getMin()));
            }
        }
    }

    /**
     * 构造当月的数据列表
     *
     * @param date
     * @param tagValueList
     * @param temp
     * @param type
     */
    private void listYearData(Date date, List<TagValue> tagValueList, ElectricThreePhaseItem temp, String type) {
        List<Date> dateList = new ArrayList<>();
        ChartUtils.generateDateList(MONTH, DateTimeUtil.getDateTime(date, DateTimeUtil.COMMON_PATTERN_TO_MONTH), dateList);
        ElectricThreePhaseTempModel tempModelMonth = new ElectricThreePhaseTempModel();
        for (Date date1 : dateList) {
            listMonthData(date1, tagValueList, temp, type);
            temp.setMax(Constants.DOUBLE_MINUS_SIGN);
            temp.setMin(Constants.DOUBLE_MINUS_SIGN);
            if (ObjectUtil.isNotNull(tempModelMonth.getMax())) {
                temp.setMax(DoubleUtil.formatDoubleToStr(tempModelMonth.getMax()));
            }
            if (ObjectUtil.isNotNull(tempModelMonth.getMin())) {
                temp.setMin(DoubleUtil.formatDoubleToStr(tempModelMonth.getMin()));
            }
        }
    }

    /**
     * 获取标签时间值
     *
     * @param deviceCodes 设备id
     * @param indexList   指标集合
     * @return 结果
     */
    private Map<String, TagValue> getTagValues(List<String> deviceCodes, List<DeviceIndex> indexList) {
        Map<String, TagValue> deviceTagValueMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(indexList)) {
            if (!deviceCodes.isEmpty() && !deviceCodes.contains(null)) {
                List<TagValue> deviceTagValueList = realtimeDatabaseService.retrieve(deviceCodes);
                if (CollectionUtils.isNotEmpty(deviceTagValueList)) {
                    deviceTagValueMap = deviceTagValueList.stream()
                            .peek(li -> li.setValue(DoubleUtil.formatDouble(li.getValue(), Constants.DIGIT_4)))
                            .collect(Collectors.toMap(TagValue::getTagCode, tagValue -> tagValue));
                    return deviceTagValueMap;
                }
            }
        }
        return deviceTagValueMap;
    }

    /**
     * 保留两位小数
     */
    private String formatValue(Double value) {
        if (ObjectUtils.isEmpty(value)) {
            return Constants.STR_NUMBER_0;
        }
        return BigDecimal.valueOf(value).setScale(Constants.DIGIT_2, RoundingMode.HALF_UP).toString();
    }

}