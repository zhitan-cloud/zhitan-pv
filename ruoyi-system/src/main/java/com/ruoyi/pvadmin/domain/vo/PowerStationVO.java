package com.ruoyi.pvadmin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 电站维护对象 vo
 */
@Data
public class PowerStationVO {

    /**
     * $column.columnComment
     */
    private String id;

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
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}