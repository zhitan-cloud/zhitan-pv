package com.ruoyi.pvadmin.domain.vo;

import lombok.Data;

/**
 * 首页-电站信息  vo
 */
@Data
public class HomePowerStationInfoVO {

    /**
     * 当年总发电量
     */
    private String cumulativeYear;

    /**
     * 当日总发电量
     */
    private String cumulativeDay;

    /**
     * 总装机容量
     */
    private String installedCapacity;

    /**
     * 累计发电量
     */
    private String generation;

    /**
     * 当日累计收益
     */
    private String earningsDay;

    /**
     * 累计收益
     */
    private String earnings;

    /**
     * 累计CO2减排量
     */
    private String carbonEmissions;

}