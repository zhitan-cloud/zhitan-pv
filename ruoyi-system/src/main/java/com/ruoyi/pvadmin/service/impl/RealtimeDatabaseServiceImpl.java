package com.ruoyi.pvadmin.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.pvadmin.domain.enums.QueryType;
import com.ruoyi.pvadmin.domain.model.TagValue;
import com.ruoyi.pvadmin.influxdb.GroupTimeType;
import com.ruoyi.pvadmin.influxdb.InfluxDBRepository;
import com.ruoyi.pvadmin.service.RealtimeDatabaseService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

/**
 * 实时数据库取数服务实现类.
 */
@Service
public class RealtimeDatabaseServiceImpl implements RealtimeDatabaseService {

    private final InfluxDBRepository influxDB;

    public RealtimeDatabaseServiceImpl(InfluxDBRepository influxDB) {
        this.influxDB = influxDB;
    }

    @Override
    public TagValue retrieve(String tagCode) {
        return influxDB.query(tagCode);
    }

    @Override
    public List<TagValue> retrieve(List<String> tagCodes) {
        return influxDB.query(tagCodes);
    }

    @Override
    public TagValue retrieve(String tagCode, Date dataTime) {
        return influxDB.query(tagCode, dataTime);
    }

    @Override
    public List<TagValue> retrieve(List<String> tagCodes, Date dataTime) {
        return influxDB.query(tagCodes, dataTime);
    }

    @Override
    public List<TagValue> queryOneHour(List<String> tagCodes, Date dataTime) {
        return influxDB.queryOneHour(tagCodes, dataTime);
    }

    @Override
    public List<TagValue> retrieve(String tagCode, Date beginTime, Date endTime, int pointCount) {
        return retrieve(Collections.singletonList(tagCode), beginTime, endTime, pointCount);
    }

    @Override
    public List<TagValue> retrieve(String tagCode, Date beginTime, Date endTime, long interval) {
        return retrieve(Collections.singletonList(tagCode), beginTime, endTime, interval);
    }

    @Override
    public List<TagValue> retrieve(List<String> tagCodes, Date beginTime, Date endTime, int pointCount) {
        return influxDB.getHistoryData(tagCodes, beginTime, endTime, pointCount);
    }

    @Override
    public List<TagValue> retrieve(List<String> tagCodes, Date beginTime, Date endTime, long interval) {
        return influxDB.getHistoryData(tagCodes, beginTime, endTime, interval);
    }

    @Override
    public TagValue statistics(String tagCode, Date beginTime, Date endTime, QueryType queryType) {
        List<TagValue> tagValues = statistics(Collections.singletonList(tagCode), beginTime, endTime, queryType);
        return tagValues.size() > 0 ? tagValues.get(0) : null;
    }

    @Override
    public List<TagValue> statistics(List<String> tagCodes, Date beginTime, Date endTime, QueryType queryType) {
        return influxDB.statistics(tagCodes, beginTime, endTime, queryType);
    }

    @Override
    public List<TagValue> statistics(List<String> tagCodes, Date beginTime, Date endTime, QueryType queryType, GroupTimeType timeType) {
        return influxDB.statistics(tagCodes, beginTime, endTime, queryType, timeType);
    }

    @Override
    public void storeData(List<TagValue> tagValues) {
        influxDB.store(tagValues);
    }

    @Override
    public void insertData(List<TagValue> tagValues) {
        influxDB.store(tagValues);
    }

    @Override
    public TagValue statistics(String tagCode, String beginTime, String endTime, QueryType queryType) {
        try {

            List<TagValue> tagValues = statistics(Collections.singletonList(tagCode), DateUtil.parseDateTime(beginTime), DateUtil.parseDateTime(endTime), queryType);
            return CollectionUtils.isNotEmpty(tagValues) ? tagValues.get(0) : new TagValue();
        } catch (Exception e) {
            return new TagValue();
        }
    }

    @Override
    public List<TagValue> statistics(String tagCodes, String beginTime, String endTime, QueryType queryType, GroupTimeType timeType) {
        try {
            List<String> tagCodeList = Arrays.asList(tagCodes.split(StrUtil.COMMA).clone());
            List<TagValue> tagValues = influxDB.statistics(tagCodeList, DateUtil.parseDateTime(beginTime), DateUtil.parseDateTime(endTime), queryType, timeType);
            if (ObjectUtil.isEmpty(tagValues)) {
                tagValues = new ArrayList<>();
            }
            return tagValues;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * 获取一段时间内批量测点的历史数据.
     *
     * @param tagCodes   测点编号集合
     * @param beginTime  开始时间
     * @param endTime    结束时间
     * @param pointCount 每个测点得到的数据个数
     * @return 测点历史数据
     */
    @Override
    public List<TagValue> retrieve(String tagCodes, String beginTime, String endTime, int pointCount) {
        try {
            List<String> tagCodeList = Arrays.asList(tagCodes.split(StrUtil.COMMA).clone());
            List<TagValue> tagValues = influxDB.getHistoryData(tagCodeList, DateUtil.parseDateTime(beginTime), DateUtil.parseDateTime(endTime), pointCount);
            if (ObjectUtil.isEmpty(tagValues)) {
                tagValues = new ArrayList<>();
            }
            return tagValues;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * 获取测点在一段时间内的统计数据.
     *
     * @param tagCodes  测点编号集合
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @param queryType 统计类型
     * @return 测点统计结果
     */
    @Override
    public List<TagValue> listStatistics(String tagCodes, String beginTime, String endTime, QueryType queryType) {
        try {
            List<String> tagCodeList = Arrays.asList(tagCodes.split(StrUtil.COMMA).clone());

            List<TagValue> tagValues = influxDB.statistics(tagCodeList, DateUtil.parseDateTime(beginTime), DateUtil.parseDateTime(endTime), queryType);
            if (ObjectUtil.isEmpty(tagValues)) {
                tagValues = new ArrayList<>();
            }
            return tagValues;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * 获取一段时间内批量测点的历史数据.
     *
     * @param tagCodeList 测点编号集合
     * @param beginTime   开始时间
     * @param endTime     结束时间
     * @param pointCount  每个测点得到的数据个数
     * @return 测点历史数据
     */
    @Override
    public List<TagValue> retrieve(List<String> tagCodeList, Long beginTime, Long endTime, int pointCount) {
        try {
            LocalDateTime beginL = LocalDateTime.ofEpochSecond(beginTime, 0, ZoneOffset.ofHours(8));
            LocalDateTime endL = beginL.plusHours(1L);
            Date begin = Date.from(beginL.atZone(ZoneId.systemDefault()).toInstant());
            Date end = Date.from(endL.atZone(ZoneId.systemDefault()).toInstant());

            List<TagValue> tagValues = influxDB.getHistoryData(tagCodeList, begin, end, pointCount);
            if (ObjectUtil.isEmpty(tagValues)) {
                tagValues = new ArrayList<>();
            }
            return tagValues;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * 获取一个小时内所有数据
     *
     * @param tagCodes 测点编号
     * @param dataTime 开始时间
     * @return 测试历史时刻数据集合
     */
    @Override
    public List<TagValue> queryOneHour(String tagCodes, Long dataTime) {
        try {
            List<String> tagCodeList = Arrays.asList(StrUtil.split(tagCodes, StrUtil.COMMA));
            LocalDateTime beginL = LocalDateTime.ofEpochSecond(dataTime, 0, ZoneOffset.ofHours(8));
            Date begin = Date.from(beginL.atZone(ZoneId.systemDefault()).toInstant());
            List<TagValue> tagValues = influxDB.queryOneHour(tagCodeList, begin);
            if (ObjectUtil.isEmpty(tagValues)) {
                tagValues = new ArrayList<>();
            }
            return tagValues;
        } catch (Exception e) {
            return new ArrayList<>();
        }

    }

}