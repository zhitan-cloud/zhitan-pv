package com.ruoyi.pvadmin.domain.dto;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 设备管理对象 device
 */
@Data
public class DeviceQueryDTO extends BaseEntity {

    /**
     * 设备编号
     */
    private String code;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 电站id
     */
    private String powerStationId;

    /**
     * 是否是电表
     */
    private Boolean ammeter;

    /**
     * 器具类型（1:逆变器、2:电表、3:反送表）
     */
    private String deviceTypeId;

}