package com.ruoyi.pvadmin.domain.enums;

/**
 * @author fanxinfu
 * <p>
 * 获取实时数据方式.
 */
public enum QueryType {
    last,
    /**
     * 平均值
     */
    mean,
    /**
     * 最小
     */
    min,
    /**
     * 最大
     */
    max,
    /**
     * 求和
     */
    sum
}