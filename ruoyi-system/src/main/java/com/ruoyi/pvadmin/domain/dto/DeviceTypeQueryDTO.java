package com.ruoyi.pvadmin.domain.dto;

import lombok.Data;

/**
 * 设备类型查询接口
 */
@Data
public class DeviceTypeQueryDTO {
    /**
     * 设备类型名称
     */
    private String name;

    /**
     * 设备描述
     */
    private String description;
}
