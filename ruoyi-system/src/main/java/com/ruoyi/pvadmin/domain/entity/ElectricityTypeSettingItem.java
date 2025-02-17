package com.ruoyi.pvadmin.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 峰平谷配置子项对象 electricity_type_setting_item
 */
@Data
@TableName(value = "electricity_type_setting_item")
public class ElectricityTypeSettingItem extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 父级id
     */
    @Excel(name = "父级id")
    private String parentId;

    /**
     * 用电类型（尖、峰、平、谷）
     */
    @Excel(name = "用电类型", readConverterExp = "尖、峰、平、谷")
    private String type;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date beginTime;

    /**
     * 截止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "截止时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /**
     * 电价
     */
    @Excel(name = "电价")
    private BigDecimal electricityPrice;

}