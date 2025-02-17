package com.ruoyi.pvadmin.domain.vo;


import lombok.Data;

import java.math.BigDecimal;

/**
 * 电站发电统计 返回VO
 */
@Data
public class PowerStationRankVO {

    /**
     * 电站id
     */
    private String powerStationId;

    /**
     * 电站名称
     */
    private String powerStationName;

    /**
     * 总发电量
     */
    private String sumValue;

    /**
     * 排序字段
     */
    private BigDecimal sort;

    /**
     * 经度
     */
    private BigDecimal lon;

    /**
     * 纬度
     */
    private BigDecimal lat;

    /**
     * 排名
     */
    private Integer rank;
}