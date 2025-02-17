package com.ruoyi.pvadmin.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.enums.TimeTypeEnum;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 首页查询 dto
 */
@Data
public class HomeQueryDTO extends BaseEntity {

    /**
     * 电站id
     */
    private String powerStationId;

    /**
     * 时间类型
     */
    @NotNull(message = "时间类型不能为空")
    private TimeTypeEnum timeType;

    /**
     * 查询时间
     */
    @NotNull(message = "查询时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date queryTime;

    /**
     * 根据时间类型查询dataItem对应类型的数据
     *
     * @return 结果
     */
    public String getDataItemTimeType() {
        if (TimeTypeEnum.DAY.equals(this.timeType)) {
            return TimeTypeEnum.HOUR.name();
        } else if (TimeTypeEnum.MONTH.equals(this.timeType)) {
            return TimeTypeEnum.DAY.name();
        } else {
            return TimeTypeEnum.MONTH.name();
        }
    }

}