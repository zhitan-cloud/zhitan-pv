package com.ruoyi.pvadmin.domain.vo;

import lombok.Data;

/**
 * 三相不平衡分析 子项信息
 */
@Data
public class ElectricThreePhaseItem {
    /**
     * 电表名称
     */
    private String name;
    /**
     * 时间
     */
    private String timeCode;
    /**
     * 统计图显示时间
     */
    private String timeCodeChart;
    /**
     * 最大三相不平衡
     */
    private String max;
    /**
     * 最小三相不平衡
     */
    private String min;
    /**
     * A相电流/电压
     */
    private String valueA;
    /**
     * D相电流/电压
     */
    private String valueB;
    /**
     * C相电流/电压
     */
    private String valueC;
}