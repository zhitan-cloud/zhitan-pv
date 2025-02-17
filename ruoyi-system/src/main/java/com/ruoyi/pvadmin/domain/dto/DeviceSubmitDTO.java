package com.ruoyi.pvadmin.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 设备管理对象 device
 */
@Data
public class DeviceSubmitDTO {

    /**
     * $column.columnComment
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 电站id
     */
    @NotBlank(message = "电站不能为空")
    private String powerStationId;

    /**
     * 设备编号
     */
    @NotBlank(message = "设备编号不能为空")
    private String code;

    /**
     * 设备名称
     */
    @NotBlank(message = "设备名称不能为空")
    private String name;

    /**
     * 器具类型id
     */
    @NotNull(message = "设备类型不能为空")
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
    private boolean ammeter;
}