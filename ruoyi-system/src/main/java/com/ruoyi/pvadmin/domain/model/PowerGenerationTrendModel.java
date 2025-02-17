package com.ruoyi.pvadmin.domain.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 获取设备点位
 */
@Data
public class PowerGenerationTrendModel {

    /**
     * 实时功率
     */
    private String time;

    /**
     * 月发电量
     */
    private BigDecimal value;

}