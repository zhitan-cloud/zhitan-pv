package com.ruoyi.common.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.enums.TimeTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.ruoyi.common.enums.TimeTypeEnum.*;

/**
 * 时间工具类
 */
@Slf4j
public class DateTimeUtil {
    /**
     * 日期常用格式
     */
    public static final String COMMON_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * 格式化日期到分钟
     */
    public static final String COMMON_PATTERN_END_WITH_MINUTE = "yyyy-MM-dd HH:mm";
    /**
     * 日期格式 - 小时:分钟
     */
    public static final String COMMON_PATTERN_HOUR_MINUTE = "HH:mm";
    /**
     * 日期常用格式 - 年份
     */
    public static final String COMMON_PATTERN_YEAR = "yyyy";
    /**
     * 日期常用格式 - 某一年,
     */
    public static final String COMMON_PATTERN_CERTAIN_YEAR = "yy";

    /**
     * 日期常用格式 - 月份
     */
    public static final String COMMON_PATTERN_MONTH = "yyyyMM";
    /**
     * 日期常用格式 - 月份
     */
    public static final String COMMON_PATTERN_TO_MONTH = "yyyy-MM";
    /**
     * 日期常用格式 - 月份
     */
    public static final String COMMON_PATTERN_TO_MONTH_WORD = "yyyy-MM月";
    /**
     * 日期常用格式 - 月份
     */
    public static final String COMMON_PATTERN_TO_MONTH_ZH = "yyyy年MM月";
    /**
     * 日期常用格式 - 天
     */
    public static final String COMMON_PATTERN_DAY = "yyyyMMdd";
    /**
     * 日期常用格式 - 天
     */
    public static final String COMMON_PATTERN_TO_DAY = "yyyy-MM-dd";
    /**
     * 日期常用格式 - 天
     */
    public static final String COMMON_PATTERN_TO_DAY_WORD = "yyyy-MM-dd日";
    /**
     * 日期常用格式 - 月日
     */
    public static final String COMMON_PATTERN_MONTH_DAY = "MM-dd";
    /**
     * 日期常用格式 - 天某一天,
     */
    public static final String COMMON_PATTERN_DAY_OF_MONTH = "dd";
    /**
     * 日期常用格式 - 小时
     */
    public static final String COMMON_PATTERN_HOUR = "yyyyMMddHH";
    /**
     * 日期常用格式 - 小时
     */
    public static final String COMMON_PATTERN_TO_HOUR = "yyyy-MM-dd HH";
    /**
     * 日期常用格式 - 小时
     */
    public static final String COMMON_PATTERN_TO_HOUR_WORD = "yyyy-MM-dd HH时";
    /**
     * 日期常用格式 - 小时
     */
    public static final String COMMON_PATTERN_TO_HOUR_TEXT = "yyyy年MM月dd日 HH时";

    /**
     * 获取当前时间,时间格式：yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getNowDateTime() {
        return getNowDateTime(COMMON_PATTERN);
    }

    /**
     * 获取当前时间
     *
     * @param pattern 时间格式
     * @return
     */
    public static String getNowDateTime(String pattern) {
        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        String dateTime = df.format(new Date());
        return dateTime;
    }

    /**
     * 获取今年的年份值
     *
     * @return
     */
    public static String getNowYear() {
        return getNowDateTime(COMMON_PATTERN_YEAR);
    }

    /**
     * 获取今年的月份值
     *
     * @return
     */
    public static String getNowMonth() {
        return getNowDateTime(COMMON_PATTERN_MONTH);
    }

    /**
     * 字符串转成时间类型,默认格式：yyyy-MM-dd HH:mm:ss
     *
     * @param dateTimeStr
     * @return
     */
    public static Date toDateTime(String dateTimeStr) {
        DateTime dt = null;
        try {
            dt = DateTime.of(dateTimeStr, COMMON_PATTERN);
        } catch (Exception e) {

        }
        return dt;
    }

    /**
     * 字符串转成时间类型
     *
     * @param dateTimeStr
     * @return
     */
    public static Date toDateTime(String dateTimeStr, String pattern) {
        DateTime dt = null;
        try {
            dt = DateTime.of(dateTimeStr, pattern);
        } catch (Exception e) {

        }
        return dt;
    }

    /**
     * 字符串转成特定格式的时间字符串类型
     *
     * @param dateTimeStr   时间字符串
     * @param sourcePattern 字符串时间格式
     * @param toPattern     要转成什么格式的时间
     * @return
     */
    public static String toDateTimeStr(String dateTimeStr, String sourcePattern, String toPattern) {
        String str = Constants.EMPTY;
        try {
            DateTime dt = DateTime.of(dateTimeStr, sourcePattern);
            str = getDateTime(dt, toPattern);
        } catch (Exception e) {

        }
        return str;
    }

    /**
     * 时间转成指定的格式
     *
     * @param pattern 时间格式
     * @return
     */
    public static String getDateTime(Date dt, String pattern) {
        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(dt);
    }

    /**
     * 时间转成yyyy-MM-dd HH:mm:ss格式
     *
     * @return
     */
    public static String getDateTime(Date dt) {
        if (ObjectUtil.isEmpty(dt)) {
            return Constants.EMPTY;
        }
        return getDateTime(dt, COMMON_PATTERN);
    }

    /**
     * 获取当前时间所属月份的最后一天
     *
     * @return
     */
    public static int getDateTimeLastDay(Date dt) {
        String month = getMonth(dt);
        String firstDate = month + "01";
        Date nextMonthFirstDate = addMonths(toDateTime(firstDate, COMMON_PATTERN_DAY), Constants.DIGIT_1);
        Date currentMonthLastDate = addDays(nextMonthFirstDate, Constants.DIGIT_MINUS_1);
        return Integer.parseInt(getDateTime(currentMonthLastDate, COMMON_PATTERN_DAY_OF_MONTH));
    }

    /**
     * 获取年份值
     *
     * @return
     */
    public static String getYear(Date dt) {
        return getDateTime(dt, COMMON_PATTERN_YEAR);
    }

    /**
     * 获取月份值 202202
     *
     * @return
     */
    public static String getMonth(Date dt) {
        return getDateTime(dt, COMMON_PATTERN_MONTH);
    }

    /**
     * 获取天,格式：yyyyMMdd
     *
     * @return
     */
    public static String toDay(Date dt) {
        return getDateTime(dt, COMMON_PATTERN_DAY);
    }

    /**
     * 获取小时:yyyyMMddHH
     *
     * @return
     */
    public static String toHour(Date dt) {
        return getDateTime(dt, COMMON_PATTERN_HOUR);
    }

    /**
     * 转成字符串类型值
     *
     * @return
     */
    public static String toString(Date dt) {
        return getDateTime(dt, COMMON_PATTERN);
    }

    /**
     * 时间增加对应的年数
     *
     * @param dateTime
     * @param years
     * @return
     */
    public static Date addYears(Date dateTime, int years) {
        return calcDate(dateTime, years, Calendar.YEAR);
    }

    /**
     * 时间增加对应的月数
     *
     * @param dateTime
     * @param months
     * @return
     */
    public static Date addMonths(Date dateTime, int months) {
        return calcDate(dateTime, months, Calendar.MONTH);
    }

    /**
     * 时间增加对应的天数
     *
     * @param dateTime
     * @param days
     * @return
     */
    public static Date addDays(Date dateTime, int days) {
        return calcDate(dateTime, days, Calendar.DATE);
    }

    /**
     * 时间增加对应的小时数
     *
     * @param dateTime
     * @param hours
     * @return
     */
    public static Date addHours(Date dateTime, int hours) {
        return calcDate(dateTime, hours, Calendar.HOUR);
    }

    /**
     * 时间增加对应的分钟数
     *
     * @param dateTime
     * @param minutes
     * @return
     */
    public static Date addMinutes(Date dateTime, int minutes) {
        return calcDate(dateTime, minutes, Calendar.MINUTE);
    }

    /**
     * 时间增加对应的小时数
     *
     * @param dateTime
     * @param seconds
     * @return
     */
    public static Date addSeconds(Date dateTime, int seconds) {
        return calcDate(dateTime, seconds, Calendar.SECOND);
    }

    /**
     * 计算日期通用方法
     *
     * @param dateTime
     * @param addValue
     * @param calendarType 计算类型：Calendar.YEAR，Calendar.MONTH,Calendar.DAY
     * @return
     */
    private static Date calcDate(Date dateTime, int addValue, int calendarType) {
        Date dt = null;
        try {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(dateTime);
            //把日期往后增加一年，整数往后推，负数往前移
            calendar.add(calendarType, addValue);
            // 获取相加或者相减之后的时间值
            Date tempDt = calendar.getTime();
            // 把时间转成所需要的格式
            String temp = getDateTime(tempDt, COMMON_PATTERN);
            dt = toDateTime(temp);
        } catch (Exception e) {

        }
        return dt;
    }

    /**
     * 获取该时间属于当天的第几个小时
     *
     * @param dateTime
     * @return
     */
    public static int getHourOfDay(Date dateTime) {
        return getDateValue(dateTime, Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取该时间属于当月的第几天
     *
     * @param dateTime
     * @return
     */
    public static int getDayOfMonth(Date dateTime) {
        return getDateValue(dateTime, Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取该时间属于当周的第几天
     * 一周的第一天是周日
     *
     * @param dateTime
     * @return
     */
    public static int getDayOfWeek(Date dateTime) {
        return getDateValue(dateTime, Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取该时间属于年的第几个月
     * 月份值+1是真实的当前月
     *
     * @param dateTime
     * @return 已经在系统中获取值的基础上加1了，现在是真实的当前月份值
     */
    public static int getMonthOfYear(Date dateTime) {
        return getDateValue(dateTime, Calendar.MONTH) + 1;
    }

    /**
     * 获取当天的第几个小时/当月的第几天/当年的第几个月
     *
     * @param dateTime     如果时间值为空，默认当前时间
     * @param calendarType
     * @return
     */
    private static int getDateValue(Date dateTime, int calendarType) {
        int value = 0;
        try {
            if (ObjectUtil.isEmpty(dateTime)) {
                dateTime = new Date();
            }
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(dateTime);
            value = calendar.get(calendarType);
        } catch (Exception e) {

        }
        return value;
    }

    /**
     * 对比time1 和 time2 的大小
     *
     * @param time1
     * @param time2
     * @return -1:time1小于time2;0:time1等于time2;1:time1大于time2;
     */
    public static int compareDateDiff(Date time1, Date time2) {
        long diff = time1.getTime() - time2.getTime();
        int res = 0;
        if (diff > 0) {
            res = 1;
        } else if (diff < 0) {
            res = -1;
        }
        return res;
    }

    /**
     * 获取日期相差的小时数
     */
    public static double dateHourDiff(Date start, Date end) {

        if (start == null || end == null) {
            return 0;
        }
        Instant startInstant = start.toInstant();
        Instant endInstant = end.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();

        LocalDateTime startLocalDateTime = startInstant.atZone(zoneId).toLocalDateTime();
        LocalDateTime endLocalDateTime = endInstant.atZone(zoneId).toLocalDateTime();
        long minutes = Duration.between(startLocalDateTime, endLocalDateTime).toMinutes();
        double hours = Math.abs((double) minutes / 60);
        DecimalFormat df = new DecimalFormat("#.00");
        String result = df.format(hours);
        return Double.parseDouble(result);
    }

    /**
     * 获取查询data_item所需要的timecode值
     *
     * @param timeType 日期类型
     * @param date     时间
     * @return
     */
    public static String getTimeCode(String timeType, Date date) {
        String timeCode = Constants.EMPTY;
        if (ObjectUtil.isEmpty(date)) {
            date = new Date();
        }
        timeType = StringUtils.defaultString(timeType).toUpperCase();

        if (HOUR.name().equals(timeType)) {
            timeCode = Constants.WORD_H + getDateTime(date, COMMON_PATTERN_HOUR);
        } else if (DAY.name().equals(timeType)) {
            timeCode = Constants.WORD_D + getDateTime(date, COMMON_PATTERN_DAY);
        } else if (MONTH.name().equals(timeType)) {
            timeCode = Constants.WORD_M + getDateTime(date, COMMON_PATTERN_MONTH);
        } else if (YEAR.name().equals(timeType)) {
            timeCode = Constants.WORD_Y + getDateTime(date, COMMON_PATTERN_YEAR);
        }
        return timeCode;
    }

    /**
     * 获取整点时间
     *
     * @param timeType HOUR/DAY/MONTH/YEAR
     * @param date     时间值
     * @return
     */
    public static Date getHourTime(TimeTypeEnum timeType, Date date) {
        Date dt = null;
        if (ObjectUtil.isEmpty(date)) {
            date = new Date();
        }
        String tempStr = null;
        switch (timeType) {
            case HOUR:
                tempStr = getDateTime(date, COMMON_PATTERN_TO_HOUR);
                dt = toDateTime(tempStr, COMMON_PATTERN_TO_HOUR);
                break;
            case DAY:
                tempStr = getDateTime(date, COMMON_PATTERN_TO_DAY);
                dt = toDateTime(tempStr, COMMON_PATTERN_TO_DAY);
                break;
            case MONTH:
                tempStr = getDateTime(date, COMMON_PATTERN_TO_MONTH);
                dt = toDateTime(tempStr, COMMON_PATTERN_TO_MONTH);
                break;
            case YEAR:
                tempStr = getDateTime(date, COMMON_PATTERN_YEAR);
                dt = toDateTime(tempStr, COMMON_PATTERN_YEAR);
                break;
            default:
                break;
        }
        return dt;
    }

}