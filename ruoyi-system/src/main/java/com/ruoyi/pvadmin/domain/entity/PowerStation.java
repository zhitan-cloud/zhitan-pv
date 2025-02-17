package com.ruoyi.pvadmin.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 电站维护对象 power_station
 */
@Data
@TableName(value = "power_station")
public class PowerStation extends BaseEntity {
    /**
    * serialVersionUID
    */
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 父级id
     */
    private String parentId;

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
     * 并网电压
     */
    @Excel(name = "并网电压")
    private BigDecimal gridVoltage;

    /**
     * 经度
     */
    @Excel(name = "经度")
    private BigDecimal lon;

    /**
     * 纬度
     */
    @Excel(name = "纬度")
    private BigDecimal lat;

    /**
     * 所属用户id
     */
    @Excel(name = "所属用户id")
    private String owningUserId;

}