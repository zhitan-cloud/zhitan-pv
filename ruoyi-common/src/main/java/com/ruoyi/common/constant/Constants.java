package com.ruoyi.common.constant;

import io.jsonwebtoken.Claims;

import java.math.BigDecimal;

/**
 * 通用常量信息
 *
 * @author ruoyi
 */
public class Constants {

    /**
     * 数字 0
     */
    public static final int DIGIT_0 = 0;

    /**
     * 数字 1
     */
    public static final int DIGIT_1 = 1;

    /**
     * 数字 2
     */
    public static final int DIGIT_2 = 2;

    /**
     * 数字 3
     */
    public static final int DIGIT_3 = 3;

    /**
     * 数字 4
     */
    public static final int DIGIT_4 = 4;

    /**
     * 数字 1000
     */
    public static final int DIGIT_1000 = 1000;

    /**
     * 数字 10000
     */
    public static final int DIGIT_10000 = 10000;

    /**
     * 数字 3600
     */
    public static final int DIGIT_3600 = 3600;


    /**
     * 数字 -1
     */
    public static final int DIGIT_MINUS_1 = -1;

    /**
     * 小数0
     */
    public static final double DIGIT_DOUBLE_0 = 0D;

    /**
     * 小数0
     */
    public static final double DIGIT_DOUBLE_00 = 0.00D;

    /**
     * 小数1
     */
    public static final double DIGIT_DOUBLE_1 = 1.0D;

    /**
     * 小数100
     */
    public static final double DIGIT_DOUBLE_100 = 100D;

    /**
     * 数字 100
     */
    public static final int DIGIT_100 = 100;

    /**
     * 最小值初始化
     */
    public static final double MIN_INIT_VALUE = -99;

    /**
     * 单位转换 100
     */
    public static final BigDecimal UNIT_CONVERSION_100 = new BigDecimal(100);

    /**
     * 单位转换 1000
     */
    public static final BigDecimal UNIT_CONVERSION_1000 = new BigDecimal(1000);

    /**
     * 单位转换 10000
     */
    public static final BigDecimal UNIT_CONVERSION_10000 = new BigDecimal(10000);

    /**
     * 字符串数字 0
     */
    public static final String STR_NUMBER_0 = "0";

    /**
     * 字符串数字 1
     */
    public static final String STR_NUMBER_1 = "1";

    /**
     * 字符串数字 2
     */
    public static final String STR_NUMBER_2 = "2";

    /**
     * 字符串 -1
     */
    public static final String STR_MINUS_1 = "-1";

    /**
     * 空 字符集
     */
    public static final String EMPTY = "";

    /**
     * 单减号 -
     */
    public static final String SINGLE_MINUS_SIGN = "-";

    /**
     * 双减号 --
     */
    public static final String DOUBLE_MINUS_SIGN = "--";

    /**
     * 下划线
     */
    public static final String STR_UNDERLINE = "_";

    /**
     * 横线
     */
    public static final String STR_LINE = "-";

    /**
     * 能源对比-同比
     */
    public static final String ENERGY_COMPARISON_YOY = "YOY";

    /**
     * 能源对比-环比
     */
    public static final String ENERGY_COMPARISON_MOM = "MOM";

    /**
     * 总有功功率点位模板code
     */
    public static final String TAG_CODE_ZYGGL = "_PW";

    /**
     * 总功率因数 点位模板code
     */
    public static final String TAG_CODE_Q = "_Q";

    /**
     * 电网频率 点位模板code
     */
    public static final String TAG_CODE_HZ = "_HZ";

    /**
     * A相电压
     */
    public static final String TAG_CODE_UA = "_UA";

    /**
     * B相电压
     */
    public static final String TAG_CODE_UB = "_UB";

    /**
     * C相电压
     */
    public static final String TAG_CODE_UC = "_UC";

    /**
     * A相电流
     */
    public static final String TAG_CODE_IA = "_IA";

    /**
     * B相电流
     */
    public static final String TAG_CODE_IB = "_IB";

    /**
     * C相电流
     */
    public static final String TAG_CODE_IC = "_IC";

    /**
     * 电表累积量
     */
    public static final String TAG_CODE_ACC = "_Acc";

    /**
     * 逆变器累积量
     */
    public static final String TAG_CODE_CGL = "_CGL";

    /**
     * 逆变器效率
     */
    public static final String TAG_CODE_IE = "_IE";

    /**
     * 大写字母 M
     */
    public static final String WORD_M = "M";
    /**
     * 大写字母 Y
     */
    public static final String WORD_Y = "Y";
    /**
     * 大写字母 D
     */
    public static final String WORD_D = "D";
    /**
     * 大写字母 H
     */
    public static final String WORD_H = "H";

    /**
     * 电力负荷单位
     */
    public static final String ELECTRIC_LOAD_UNIT = "(kW)";

    /**
     * 电力负荷单位
     */
    public static final String ELECTRIC_LOAD_UNIT_SHOW = "kW";

    /**
     * 装机容量单位-兆瓦
     */
    public static final String ELECTRIC_LOAD_UNIT_MW = "MW";

    /**
     * 电力单位-kwh
     */
    public static final String ELECTRIC_UNIT_KWH = "kWh";

    /**
     * 电力单位-万千瓦时
     */
    public static final String ELECTRIC_UNIT_W_KWH = "万kWh";

    /**
     * 单位-个
     */
    public static final String UNIT_PCS = "个";

    /**
     * 单位-元
     */
    public static final String UNIT_YUAN = "元";

    /**
     * 单位-万元
     */
    public static final String UNIT_W_YUAN = "万元";

    /**
     * 单位-千克
     */
    public static final String UNIT_KG = "kg";

    /**
     * 单位-吨
     */
    public static final String UNIT_T = "吨";

    /**
     * 符号 - 百分号
     */
    public static final String SYMBOL_PERCENT = "%";

    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * www主域
     */
    public static final String WWW = "www.";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 注册
     */
    public static final String REGISTER = "Register";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * 用户ID
     */
    public static final String JWT_USERID = "userid";

    /**
     * 用户名称
     */
    public static final String JWT_USERNAME = Claims.SUBJECT;

    /**
     * 用户头像
     */
    public static final String JWT_AVATAR = "avatar";

    /**
     * 创建时间
     */
    public static final String JWT_CREATED = "created";

    /**
     * 用户权限
     */
    public static final String JWT_AUTHORITIES = "authorities";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";

    /**
     * RMI 远程方法调用
     */
    public static final String LOOKUP_RMI = "rmi:";

    /**
     * LDAP 远程方法调用
     */
    public static final String LOOKUP_LDAP = "ldap:";

    /**
     * LDAPS 远程方法调用
     */
    public static final String LOOKUP_LDAPS = "ldaps:";

    /**
     * 定时任务白名单配置（仅允许访问的包名，如其他需要可以自行添加）
     */
    public static final String[] JOB_WHITELIST_STR = {"com.ruoyi"};

    /**
     * 定时任务违规的字符
     */
    public static final String[] JOB_ERROR_STR = {"java.net.URL", "javax.naming.InitialContext", "org.yaml.snakeyaml",
            "org.springframework", "org.apache", "com.ruoyi.common.utils.file", "com.ruoyi.common.config"};


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
     * 日期常用格式 - 月份
     */
    public static final String COMMON_PATTERN_MONTH = "yyyyMM";
    /**
     * 日期常用格式 - 月份
     */
    public static final String COMMON_PATTERN_TO_MONTH = "yyyy-MM";
    /**
     * 日期常用格式 - 天
     */
    public static final String COMMON_PATTERN_DAY = "yyyyMMdd";
    /**
     * 日期常用格式 - 天
     */
    public static final String COMMON_PATTERN_TO_DAY = "yyyy-MM-dd";
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

}