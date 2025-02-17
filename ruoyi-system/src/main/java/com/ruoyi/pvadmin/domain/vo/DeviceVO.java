package com.ruoyi.pvadmin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 设备管理对象 device
 */
@Data
public class DeviceVO {

    /**
     * $column.columnComment
     */
    private String id;

    /**
     * 电站名称
     */
    private String powerStationName;

    /**
     * 电站Id
     */
    private String powerStationId;

    /**
     * 设备编号
     */
    private String code;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 器具类型id
     */
    private String deviceTypeId;

    /**
     * 器具类型名称
     */
    private String deviceType;

    /**
     * 容量
     */
    private BigDecimal capacity;

    /**
     * 厂家
     */
    private String factory;

    /**
     * 额定交流功率
     */
    private BigDecimal ratedAcPower;

    /**
     * 电网类型
     */
    private String gridType;

    /**
     * 组件峰值功率
     */
    private BigDecimal modulePeakPower;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 是否是电表
     */
    private Boolean ammeter;
}