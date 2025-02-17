package com.ruoyi.pvadmin.domain.vo;

import lombok.Data;

/**
 * 设备类型的VO
 */
@Data
public class ACMeasurementsVO {

    /**
     * A相电压
     */
    private Double aVoltage;

    /**
     * B相电压
     */
    private Double bVoltage;

    /**
     * C相电压
     */
    private Double cVoltage;

    /**
     * A相电流
     */
    private Double aCurrent;

    /**
     * B相电流
     */
    private Double bCurrent;

    /**
     * C相电流
     */
    private Double cCurrent;

    /**
     * 总有功功率
     */
    private Double totalActivePower;

    /**
     * 功率因数
     */
    private Double factor;

    /**
     * 频率
     */
    private Double frequency;

}