package com.ruoyi.pvadmin.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 设备点检提交对象
 */
@Data
public class DeviceInspectionSubmitDTO {


    /**
     * $column.columnComment
     */
    private String id;

    /**
     * 电站id
     */
    private String powerStationId;

    @NotBlank(message = "项目不能为空")
    private String powerStationName;

    /**
     * 设备编号
     */
    @NotBlank(message = "设备编号不存在")
    private String deviceCode;

    /**
     * 设备名称
     */
    @NotBlank(message = "设备名称不存在")
    private String deviceName;

    /**
     * 点检/维修结束日期
     */
    @NotNull(message = "点检日期为空")
    @Excel(name = "点检日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inspectionStartTime;

    /**
     * 点检/维修结束日期
     */
    @NotNull(message = "点检日期为空")
    @Excel(name = "点检日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inspectionEndTime;

    /**
     * 点检人员
     */
    @NotBlank(message = "未找到点检人员")
    private String inspectionStaff;

    /**
     * 类型（0：设备点检，1：设备维修，2：甲方停机）
     */
    @NotBlank(message = "未找到操作类型")
    private String inspectionType;

    /**
     * 点检结果
     */
    private String inspectionResult;

    /**
     * 备件名称/数量
     */
    @NotBlank(message = "备件名称/数量不能为空")
    private String sparePartNameOrNumber;

    /**
     * 预估损失电量
     */
    @NotNull(message = "预估损失电量不能为空")
    private double estimatedPowerLoss;

    /**
     * 附件地址
     */
    private String annex;

    /**
     * 备注
     */
    private String remark;

}