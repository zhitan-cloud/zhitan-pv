package com.ruoyi.pvadmin.domain.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 报警等级分析对象
 */
@Data
public class AlarmLevelAnalysisItem {


    /**
     * 报警等级（1：一级，2：二级，3：三级）
     */
    private Integer level;

    /**
     * 错误码
     */
    private Integer count;

    /**
     * 比例
     */
    private BigDecimal ratio;
}