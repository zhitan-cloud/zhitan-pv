package com.ruoyi.pvadmin.domain.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 实时数据-能源点位数据
 */
@Data
public class RealTimeIndexParam {

    /**
     * 名称
     */
    private String name;

    /**
     * 点位编码
     */
    private String indexCode;

    /**
     * 单位
     */
    private String unit;

    /**
     * 值
     */
    private BigDecimal value;

    /**
     * 报警状态 预留属性  0 不报警；1 一级报警；2二级报警
     */
    private Integer alarmStatus;

    /**
     * 业务时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dataTime;

    /**
     * 指标分组编码
     */
    private String groupCode;

    /**
     * 离线报警
     */
    private boolean offline;
}