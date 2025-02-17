package com.ruoyi.pvadmin.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 获取设备点位
 */
@Data
public class PowerGenerationInfoVO {

    /**
     * 日发电量
     */
    private BigDecimal dayValue;

    /**
     * 月发电量
     */
    private BigDecimal monthValue;

    /**
     * 年发电量
     */
    private BigDecimal yearValue;

    /**
     * 日收益
     */
    private BigDecimal dayCost;

    /**
     * 月收益
     */
    private BigDecimal monthCost;

    /**
     * 年收益
     */
    private BigDecimal yearCost;

    public PowerGenerationInfoVO() {
    }

    public PowerGenerationInfoVO(BigDecimal dayValue, BigDecimal monthValue,
                                 BigDecimal yearValue, BigDecimal dayCost,
                                 BigDecimal monthCost, BigDecimal yearCost) {
        this.dayValue = dayValue;
        this.monthValue = monthValue;
        this.yearValue = yearValue;
        this.dayCost = dayCost;
        this.monthCost = monthCost;
        this.yearCost = yearCost;
    }
}