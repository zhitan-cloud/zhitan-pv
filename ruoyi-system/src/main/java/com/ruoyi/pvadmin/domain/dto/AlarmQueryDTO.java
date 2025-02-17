package com.ruoyi.pvadmin.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 报警对象 alarm
 */
@Data
public class AlarmQueryDTO extends BaseEntity {

    /**
     * 电站名称
     */
    private String powerStationName;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /**
     * 截止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 状态（1未解决、2已解决）
     */
    private String status;

    /**
     * 报警级别
     */
    private Integer level;

}