package com.ruoyi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 时间类型枚举
 */
@Getter
@AllArgsConstructor
public enum TimeTypeEnum {

    /**
     * 小时
     */
    HOUR,

    /**
     * 日
     */
    DAY,

    /**
     * 月
     */
    MONTH,

    /**
     * 年
     */
    YEAR;

}