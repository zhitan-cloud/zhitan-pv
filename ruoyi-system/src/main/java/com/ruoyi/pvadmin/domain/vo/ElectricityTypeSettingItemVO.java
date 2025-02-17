package com.ruoyi.pvadmin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 峰平谷配置子项对象 electricity_type_setting_item
 */
@Data
public class ElectricityTypeSettingItemVO {

    /**
     * $column.columnComment
     */
    private String id;

    /**
     * 用电类型（尖、峰、平、谷）
     */
    private String type;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "HH:mm:ss")
    private Date beginTime;

    /**
     * 截止时间
     */
    @JsonFormat(pattern = "HH:mm:ss")
    private Date endTime;

    /**
     * 电价
     */
    private BigDecimal electricityPrice;

    /**
     * 备注
     */
    private String remark;

}