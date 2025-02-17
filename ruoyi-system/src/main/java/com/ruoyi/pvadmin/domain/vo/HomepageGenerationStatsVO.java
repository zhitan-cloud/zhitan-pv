package com.ruoyi.pvadmin.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 首页查询发电量返回对象
 */
@Data
public class HomepageGenerationStatsVO {

    /**
     * 日期
     */
    private String time;

    /**
     * 值
     */
    private BigDecimal value;

}