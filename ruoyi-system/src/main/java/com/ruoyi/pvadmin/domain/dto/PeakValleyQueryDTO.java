package com.ruoyi.pvadmin.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 尖峰平谷dto
 **/
@Data
public class PeakValleyQueryDTO extends BaseEntity {

    @NotNull(message = "统计时间不能为空")
    @JsonFormat(pattern = "yyyy-MM")
    @DateTimeFormat(pattern = "yyyy-MM")
    @ApiParam(value = "统计时间", defaultValue = "统计时间")
    private Date dateTime;

    @ApiParam(value = "电站id")
    private String powerStationId;

    @ApiParam(value = "设备id")
    private String deviceId;
}