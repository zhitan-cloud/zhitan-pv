package com.ruoyi.pvadmin.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
* 设备类型新建或者更新实体类
*/
@Data
public class DeviceTypeSubmitDTO {
    /**
     * 主键
     */
    private String id;

    /**
     * 类型名称
     */
    @NotBlank(message = "设备类型名称不能为空")
    private String name;

    /**
     * 设备描述
     */
    private String description;
}
