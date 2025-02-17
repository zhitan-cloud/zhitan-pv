package com.ruoyi.pvadmin.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 峰平谷配置对象 electricity_type_setting
 */
@Data
public class ElectricityTypeSettingQueryDTO {

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

}