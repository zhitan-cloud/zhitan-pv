package com.ruoyi.pvadmin.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 设备管理对象 device
 */
@Data
@TableName(value = "device")
public class Device extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 电站id
     */
    private String powerStationId;

    /**
     * 设备编号
     */
    @Excel(name = "设备编号")
    private String code;

    /**
     * 设备名称
     */
    @Excel(name = "设备名称")
    private String name;

    /**
     * 器具类型id
     */
    @Excel(name = "器具类型id")
    private String deviceTypeId;

    /**
     * 容量
     */
    @Excel(name = "容量")
    private BigDecimal capacity;

    /**
     * 厂家
     */
    @Excel(name = "厂家")
    private String factory;

    /**
     * 额定交流功率
     */
    @Excel(name = "额定交流功率")
    private BigDecimal ratedAcPower;

    /**
     * 电网类型
     */
    @Excel(name = "电网类型")
    private String gridType;

    /**
     * 组件峰值功率
     */
    @Excel(name = "组件峰值功率")
    private BigDecimal modulePeakPower;

    /**
    * 是否是电表
    */
    @Excel(name = "是否是电表")
    private Boolean ammeter;

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