package com.ruoyi.pvadmin.domain.dto;

import com.ruoyi.common.enums.TimeTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 三项不平衡查询 dto
 */
@Data
public class ThreePhaseUnbalanceAnalysisDTO {

    /**
     * 电站id
     */
    @NotBlank(message = "电站不存在")
    private String powerStationId;

    /**
     * 电表id
     */
    @NotBlank(message = "电表不存在")
    private String deviceId;

    /**
     * 时间类型 DAY/MONTH/YEAR
     */
    @NotNull(message = "时间类型不存在")
    private TimeTypeEnum timeType;

    /**
     * 查询时间
     */
    @NotBlank(message = "请选择查询时间")
    private String timeCode;

    /**
     * 请求类型：0.电压；1.电流
     */
    @NotBlank(message = "请求类型不存在")
    private String requestType;
}