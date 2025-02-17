package com.ruoyi.pvadmin.domain.vo;

import com.ruoyi.pvadmin.domain.model.GenerationStatisticsItemModel;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 尖峰平谷报表 vo
 */
@Data
@ApiModel(value = "尖峰平谷报表 vo", description = "尖峰平谷报表 vo")
public class PeakAndValleyReportVO {

    /**
     * 时间类型
     */
    private String timeName;

    /**
     * 时间类型 中文
     */
    private String timeNameCN;

    /**
     * 时间段
     */
    private String timePeriod;

    /**
     * 时间集合
     */
    private List<GenerationStatisticsItemModel> timeList;

    /**
     * 合计
     */
    private BigDecimal sumValue;

}