package com.ruoyi.pvadmin.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 逆变器信息返回  vo
 */
@Data
public class InverterInfoVO {

    /**
     * id
     */
    private String id;

    /**
     * 厂家
     */
    private String factory;

    /**
     * 设备编号
     */
    private String code;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 额定交流功率
     */
    private BigDecimal ratedAcPower;

    /**
     * 总发电量
     */
    private BigDecimal sumValue;

}