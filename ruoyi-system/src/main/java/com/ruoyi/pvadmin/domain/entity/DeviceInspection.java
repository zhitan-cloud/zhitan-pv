package com.ruoyi.pvadmin.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 设备点检对象 device_inspection
 */
@Data
@TableName(value = "device_inspection")
public class DeviceInspection extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 电站Id
     */
    @Excel(name = "电站Id")
    private String powerStationId;

    /**
    * 项目名称
    */
    @Excel(name = "项目名称")
    private String powerStationName;

    /**
     * 设备编号
     */
    @Excel(name = "设备编号")
    private String deviceCode;

    /**
     * 设备名称
     */
    @Excel(name = "设备名称")
    private String deviceName;

    /**
     * 点检/维修结束日期
     */
    @Excel(name = "点检日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inspectionStartTime;

    /**
     * 点检/维修结束日期
     */
    @Excel(name = "点检日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inspectionEndTime;

    /**
     * 点检人员
     */
    @Excel(name = "点检人员")
    private String inspectionStaff;

    /**
     * 点检结果
     */
    @Excel(name = "点检结果")
    private String inspectionResult;

    /**
     * 类型（0：点检，1：维修）
     */
    @Excel(name = "类型（0：设备点检，1：设备维修，2：甲方停机）")
    private String inspectionType;

    /**
     * 备件名称/数量
     */
    @Excel(name = "备件名称/数量")
    private String sparePartNameOrNumber;

    /**
     * 预估损失电量
     */
    @Excel(name = "预估损失电量")
    private double estimatedPowerLoss;

    /**
     * 附件地址
     */
    @Excel(name = "附件地址")
    private String annex;

    /**
     * 用户id
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long userId;

    /**
     * 部门id
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long deptId;
}