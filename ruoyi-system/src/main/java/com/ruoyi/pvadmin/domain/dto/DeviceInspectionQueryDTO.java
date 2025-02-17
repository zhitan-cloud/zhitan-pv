package com.ruoyi.pvadmin.domain.dto;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 设备点检查询对象
 */
@Data
public class DeviceInspectionQueryDTO extends BaseEntity {

    /**
     * 电站名称
     */
    private String powerStationName;

    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 类型（0：设备点检，1：设备维修，2：甲方停机）
     */
    private String inspectionType;

}