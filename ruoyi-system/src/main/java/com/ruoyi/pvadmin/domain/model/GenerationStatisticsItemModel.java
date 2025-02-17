package com.ruoyi.pvadmin.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 时间、值子项
 */
@Data
public class GenerationStatisticsItemModel {

    /**
     * 日期
     */
    private String time;

    /**
     * 值
     */
    private BigDecimal value;
}