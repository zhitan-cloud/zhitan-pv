package com.ruoyi.pvadmin.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.pvadmin.domain.enums.ElectricityTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 峰平谷配置子项对象 electricity_type_setting_item
 */
@Data
public class ElectricityTypeSettingItemSubmitDTO {

    /**
     * $column.columnComment
     */
    private String id;

    /**
     * 父级id
     */
    @NotBlank(message = "请设置父级")
    private String parentId;

    /**
     * 用电类型（尖、峰、平、谷）
     */
    @NotBlank(message = "请设置用电类型")
    private ElectricityTypeEnum type;

    /**
     * 开始时间
     */
    @NotBlank(message = "请设置开始时间")
    @JsonFormat(pattern = "HH:mm")
    private Date beginTime;

    /**
     * 截止时间
     */
    @NotBlank(message = "请设置截止时间")
    @JsonFormat(pattern = "HH:mm")
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