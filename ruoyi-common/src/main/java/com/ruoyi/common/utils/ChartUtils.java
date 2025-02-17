package com.ruoyi.common.utils;


import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.enums.TimeTypeEnum;

import java.util.Date;
import java.util.List;

/**
 * @Description: 统计图相关数据工具类
 * @author: yxw
 * @date: 2022年04月28日 15:29
 */
public class ChartUtils {

    /**
     * 构造日期列表
     *
     * @param timeType
     * @param timeCode
     * @param dateList
     */
    public static void generateDateList(TimeTypeEnum timeType, String timeCode, List<Date> dateList) {

        if (TimeTypeEnum.DAY.equals(timeType)) {
            if (!timeCode.contains(Constants.SINGLE_MINUS_SIGN)) {
                timeCode = DateTimeUtil.toDateTimeStr(timeCode, DateTimeUtil.COMMON_PATTERN_DAY, DateTimeUtil.COMMON_PATTERN_TO_DAY);
            }
            int hour = 23;
            for (int i = Constants.DIGIT_0; i <= hour; i++) {
                String tempCode = timeCode + " 0" + i;
                if (i > 9) {
                    tempCode = timeCode + " " + i;
                }
                Date tempD = DateTimeUtil.toDateTime(tempCode, DateTimeUtil.COMMON_PATTERN_TO_HOUR);
                dateList.add(tempD);
            }
        } else if (TimeTypeEnum.MONTH.equals(timeType)) {
            if (!timeCode.contains(Constants.SINGLE_MINUS_SIGN)) {
                timeCode = DateTimeUtil.toDateTimeStr(timeCode, DateTimeUtil.COMMON_PATTERN_MONTH, DateTimeUtil.COMMON_PATTERN_TO_MONTH);
            }
            int max = DateTimeUtil.getDateTimeLastDay(DateTimeUtil.toDateTime(timeCode, DateTimeUtil.COMMON_PATTERN_TO_MONTH));

            for (int i = Constants.DIGIT_1; i <= max; i++) {
                String tempCode = timeCode + "-0" + i;
                if (i > 9) {
                    tempCode = timeCode + "-" + i;
                }
                Date tempD = DateTimeUtil.toDateTime(tempCode, DateTimeUtil.COMMON_PATTERN_TO_DAY);
                dateList.add(tempD);
            }
        } else if (TimeTypeEnum.YEAR.equals(timeType)) {
            int monthMax = 12;
            for (int i = Constants.DIGIT_1; i <= monthMax; i++) {
                String tempCode = timeCode + "-0" + i;
                if (i > 9) {
                    tempCode = timeCode + "-" + i;
                }
                Date tempD = DateTimeUtil.toDateTime(tempCode, DateTimeUtil.COMMON_PATTERN_TO_MONTH);
                dateList.add(tempD);
            }
        }
    }

    /**
     * 获取日期显示字符
     *
     * @param timeType
     * @param date
     * @return
     */
    public static String getTimeCode(TimeTypeEnum timeType, Date date) {
        String str = Constants.EMPTY;

        if (TimeTypeEnum.DAY.equals(timeType)) {
            str = DateTimeUtil.getDateTime(date, DateTimeUtil.COMMON_PATTERN_TO_HOUR);
        } else if (TimeTypeEnum.MONTH.equals(timeType)) {
            str = DateTimeUtil.getDateTime(date, DateTimeUtil.COMMON_PATTERN_TO_DAY);
        } else if (TimeTypeEnum.YEAR.equals(timeType)) {
            str = DateTimeUtil.getDateTime(date, DateTimeUtil.COMMON_PATTERN_TO_MONTH);
        }
        return str;
    }

    /**
     * 获取日期显示字符
     *
     * @param timeType
     * @param date
     * @return
     */
    public static String getTimeCodeChart(TimeTypeEnum timeType, Date date) {
        String str = Constants.EMPTY;

        if (TimeTypeEnum.DAY.equals(timeType)) {
            str = DateTimeUtil.getDateTime(date, DateTimeUtil.COMMON_PATTERN_HOUR_MINUTE);
        } else if (TimeTypeEnum.MONTH.equals(timeType)) {
            str = DateTimeUtil.getDateTime(date, DateTimeUtil.COMMON_PATTERN_MONTH_DAY);
        } else if (TimeTypeEnum.YEAR.equals(timeType)) {
            str = DateTimeUtil.getDateTime(date, DateTimeUtil.COMMON_PATTERN_TO_MONTH);
        }
        return str;
    }

    /**
     * 获取日期显示字符
     *
     * @param timeType
     * @param timeCode
     * @return
     */
    public static Date getDateTime(TimeTypeEnum timeType, String timeCode) {
        Date d1 = new Date();
        switch (timeType) {
            case DAY:
                if (!timeCode.contains(Constants.SINGLE_MINUS_SIGN)) {
                    timeCode = DateTimeUtil.toDateTimeStr(timeCode, DateTimeUtil.COMMON_PATTERN_DAY, DateTimeUtil.COMMON_PATTERN_TO_DAY);
                }
                d1 = DateTimeUtil.toDateTime(timeCode, DateTimeUtil.COMMON_PATTERN_TO_DAY);
                break;
            case MONTH:
                if (!timeCode.contains(Constants.SINGLE_MINUS_SIGN)) {
                    timeCode = DateTimeUtil.toDateTimeStr(timeCode, DateTimeUtil.COMMON_PATTERN_MONTH, DateTimeUtil.COMMON_PATTERN_TO_MONTH);
                }
                d1 = DateTimeUtil.toDateTime(timeCode, DateTimeUtil.COMMON_PATTERN_TO_MONTH);
                break;
            case YEAR:
                d1 = DateTimeUtil.toDateTime(timeCode, DateTimeUtil.COMMON_PATTERN_YEAR);
                break;
            default:
                break;
        }
        return d1;
    }

    /**
     * 获取对应的结束时间
     *
     * @param timeType
     * @param date
     * @return
     */
    public static Date getEndTime(TimeTypeEnum timeType, Date date) {
        Date d1 = new Date();
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

}