package com.ruoyi.pvadmin.domain.vo;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 尖峰平谷折线实体类
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PeakValleyTimeSharingLineStatisticsVO {

    /**
     * X轴数据（时间）
     **/
    @ApiParam(value = "X轴数据 时间字符串 10:00 或者 2000-01-01 10:00")
    private String xData;

    /**
     * Y轴数据 yTip 尖峰
     **/
    @ApiParam(value = "尖峰 Y轴数据 代表用量或者电费")
    private String yTip;

    /**
     * Y轴数据 yPeak 高峰
     **/
    @ApiParam(value = "高峰 Y轴数据 代表用量或者电费")
    private String yPeak;

    /**
     * Y轴数据 yFlat 平
     **/
    @ApiParam(value = "平段 Y轴数据 代表用量或者电费")
    private String yFlat;

    /**
     * Y轴数据 yTrough 谷
     **/
    @ApiParam(value = "谷段 Y轴数据 代表用量或者电费")
    private String yTrough;

    /**
     * Y轴数据 yDeep 深谷
     **/
    @ApiParam(value = "深谷段 Y轴数据 代表用量或者电费")
    private String yDeep;
}
