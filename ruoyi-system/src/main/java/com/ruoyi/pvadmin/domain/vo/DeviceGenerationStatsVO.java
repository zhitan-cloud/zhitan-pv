package com.ruoyi.pvadmin.domain.vo;


import com.ruoyi.pvadmin.domain.model.GenerationStatisticsItemModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 设备发电统计 返回VO
 */
@Data
public class DeviceGenerationStatsVO {

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 时间集合
     */
    private List<GenerationStatisticsItemModel> timeList;

    /**
     * 合计
     */
    private BigDecimal sumValue;

    /**
     * 是否电表
     */
    private Boolean ammeter;
}