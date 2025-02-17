package com.ruoyi.pvadmin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 设备点检对象 device_inspection
 */
@Data
public class DeviceInspectionVO {


    /**
     * $column.columnComment
     */
    private String id;

    /**
     * 电站Id
     */
    private String powerStationId;

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
     * 点检/维修结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inspectionStartTime;

    /**
     * 点检/维修结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inspectionEndTime;

    /**
     * 时长
     */
    private double downtime;

    /**
     * 点检人员
     */
    private String inspectionStaff;

    /**
     * 点检结果
     */
    private String inspectionResult;

    /**
     * 类型（0：设备点检，1：设备维修，2：甲方停机）
     */
    private String inspectionType;

    /**
     * 备件名称/数量
     */
    private String sparePartNameOrNumber;

    /**
     * 预估损失电量
     */
    private double estimatedPowerLoss;

    /**
     * 附件
     */
    private String annex;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}