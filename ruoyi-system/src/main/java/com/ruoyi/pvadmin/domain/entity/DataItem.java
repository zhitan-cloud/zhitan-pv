package com.ruoyi.pvadmin.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 累积量数据表
 */
@Data
@TableName(value = "data_item")
public class DataItem extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 计量器具id
     */
    private String deviceId;

    /**
     * 时间编码
     */
    private String timeCode;

    /**
     * 时间类型（HOUR、DAY、MONTH、YEAR）
     */
    @Excel(name = "时间类型", readConverterExp = "HOUR、DAY、MONTH、YEAR")
    private String timeType;

    /**
     * 业务时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "业务时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dataTime;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date beginTime;

    /**
     * 截止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "截止时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /**
     * 值
     */
    @Excel(name = "值")
    private BigDecimal value;

}