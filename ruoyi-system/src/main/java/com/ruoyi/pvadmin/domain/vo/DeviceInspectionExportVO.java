package com.ruoyi.pvadmin.domain.vo;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 设备点检导出实体类
 */
@Data
public class DeviceInspectionExportVO {

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
    @Excel(name = "点检/维修开始时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date inspectionStartTime;

    /**
     * 点检/维修结束日期
     */
    @Excel(name = "点检/维修结束时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date inspectionEndTime;
    /**
     * 停机时长
     */
    @Excel(name = "停机时长", suffix = "小时")
    private double downtime;
    /**
     * 点检人员
     */
    @Excel(name = "操作人员")
    private String inspectionStaff;

    /**
     * 点检结果
     */
    @Excel(name = "检修结果")
    private String inspectionResult;

    /**
     * 类型（0：点检，1：维修）
     */
    @Excel(name = "类型", readConverterExp = "0=设备点检,1=设备维修,2=甲方停机")
    private String inspectionType;

    @Excel(name = "备件名称/数量")
    private String sparePartNameOrNumber;

    /**
     * 预估损失电量
     */
    @Excel(name = "预估损失电量")
    private double estimatedPowerLoss;

    /**
     * 附件
     */
    @Excel(name = "附件")
    private String annex;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;
}
