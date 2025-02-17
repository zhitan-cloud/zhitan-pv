package com.ruoyi.pvadmin.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 查询峰平谷累计发电量对象
 */
@Data
public class CumulativeGenerationVO {

    /**
     * 累计发电量
     */
    private BigDecimal sumValue;

    /**
     * 累计收益
     */
    private BigDecimal sumEarnings;
}