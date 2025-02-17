package com.ruoyi.pvadmin.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 电力-峰平谷数据对象 electricity_data_item
 */
@Data
@TableName(value = "electricity_data_item")
public class ElectricityDataItem extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 时间具体到小时
     */
    private Date dataTime;

    /**
     * 用电类型峰、平、谷等
     */
    private String type;

    /**
     * 数据开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "数据开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date beginTime;

    /**
     * 数据结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "数据结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /**
     * 用电量
     */
    @Excel(name = "用电量")
    private BigDecimal value;

    /**
     * 电费
     */
    @Excel(name = "电费")
    private BigDecimal cost;

    /**
     * 单价
     */
    @Excel(name = "单价")
    private BigDecimal price;

}