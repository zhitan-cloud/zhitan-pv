package com.ruoyi.pvadmin.domain.vo;


import com.ruoyi.pvadmin.domain.model.GenerationStatisticsItemModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 电站发电统计 返回VO
 */
@Data
public class GenerationStatisticsVO {

    /**
     * 电站名称
     */
    private String powerStationName;

    /**
     * 时间集合
     */
    private List<GenerationStatisticsItemModel> timeList;

    /**
     * 合计
     */
    private BigDecimal sumValue;
}