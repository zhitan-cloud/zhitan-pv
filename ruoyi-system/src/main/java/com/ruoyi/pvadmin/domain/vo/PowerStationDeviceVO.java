package com.ruoyi.pvadmin.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 设备管理对象 device
 */
@Data
public class PowerStationDeviceVO {

    /**
     * 电表集合
     */
    private List<InverterInfoVO> ammeterList;

    /**
     * 逆变器集合
     */
    private List<InverterInfoVO> inverterList;
}