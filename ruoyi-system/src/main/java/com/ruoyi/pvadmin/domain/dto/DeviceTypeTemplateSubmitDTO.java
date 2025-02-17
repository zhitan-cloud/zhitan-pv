package com.ruoyi.pvadmin.domain.dto;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 设备类型管理对象 device_type_template
 */
@Data
public class DeviceTypeTemplateSubmitDTO {

    /**
     * $column.columnComment
     */
    private String id;

    /**
     * 设备类型名称
     */
    @NotBlank(message = "类型名称不能为空")
    @Excel(name = "设备类型名称")
    private String name;

    /**
     * 设备描述
     */
    @Excel(name = "设备描述")
    private String description;

}