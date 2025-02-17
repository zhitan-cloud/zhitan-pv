package com.ruoyi.pvadmin.domain.vo;


import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 电站信息 返回VO
 */
@Data
public class PowerStationInfoVO {

    /**
     * 编号
     */
    @Excel(name = "编号")
    private String code;

    /**
     * 名称
     */
    @Excel(name = "名称")
    private String name;

    /**
     * 补贴电价
     */
    @Excel(name = "补贴电价")
    private BigDecimal subsidizedPrices;

    /**
     * 电站装机容量
     */
    @Excel(name = "电站装机容量")
    private BigDecimal installedCapacity;

    /**
     * 逆变器数量
     */
    private Integer inverterCount;

    /**
     * 总发电量
     */
    private BigDecimal sumValue;

}