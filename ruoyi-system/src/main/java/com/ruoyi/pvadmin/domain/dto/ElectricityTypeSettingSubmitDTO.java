package com.ruoyi.pvadmin.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 峰平谷配置对象 electricity_type_setting
 */
@Data
public class ElectricityTypeSettingSubmitDTO {

    /**
     * $column.columnComment
     */
    private String id;

    /**
     * 开始时间
     */
    @NotNull(message = "请设置配置项开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;

    /**
     * 截止时间
     */
    @NotNull(message = "请设置配置项截止时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    /**
     * 备注
     */
    private String remark;
}