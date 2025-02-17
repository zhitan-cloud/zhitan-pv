package com.ruoyi.pvadmin.domain.vo;

import lombok.Data;

/**
 * 功率因数分析 列表信息
 */
@Data
public class ElectricPowerFactorItem {
    /**
     * 时间
     */
    private String timeCode;
    /**
     * 实时值
     */
    private String value;
}
