package com.ruoyi.pvadmin.domain.vo;

import lombok.Data;

/**
 * 设备点检对象 device_inspection
 */
@Data
public class DeviceInspectionInfoVO {

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
     * 点检人员
     */
    private String inspectionStaff;

    /**
     * 点检结果
     */
    private String inspectionResult;

    /**
     * 类型（0：点检，1：维修）
     */
    private String inspectionType;

    /**
     * 附件地址
     */
    private String annex;

}