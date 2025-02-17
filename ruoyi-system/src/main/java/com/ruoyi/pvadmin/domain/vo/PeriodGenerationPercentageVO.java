package com.ruoyi.pvadmin.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 首页-时段发电占比返回  VO
 */
@Data
public class PeriodGenerationPercentageVO {

    /**
     * 时段类型（尖、峰、平、谷、深谷）
     */
    private String name;

    /**
     * 值
     */
    private BigDecimal value;

    /**
     * 占比
     */
    private BigDecimal ratio;
}