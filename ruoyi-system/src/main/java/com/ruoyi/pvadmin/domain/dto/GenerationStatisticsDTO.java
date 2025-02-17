package com.ruoyi.pvadmin.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.enums.TimeTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 电站发电统计 查询dto
 */
@Data
public class GenerationStatisticsDTO extends BaseEntity {

    /**
     * 时间类型
     */
    @NotNull(message = "时间类型不能为空")
    private TimeTypeEnum timeTypeEnum;

    /**
     * 电站id
     */
    private String powerStationId;

    /**
     * 查询时间
     */
    @NotNull(message = "查询时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dataTime;

    /**
     * 根据时间类型查询dataItem对应类型的数据
     *
     * @return 结果
     */
    public String getDataItemTimeType() {
        if (TimeTypeEnum.DAY.equals(this.timeTypeEnum)) {
            return TimeTypeEnum.HOUR.name();
        } else if (TimeTypeEnum.MONTH.equals(this.timeTypeEnum)) {
            return TimeTypeEnum.DAY.name();
        } else {
            return TimeTypeEnum.MONTH.name();
        }
    }

}