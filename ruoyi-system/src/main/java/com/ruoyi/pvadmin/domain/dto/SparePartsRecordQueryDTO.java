package com.ruoyi.pvadmin.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.page.PageDomain;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.data.domain.PageRequest;

import java.util.Date;

/**
 * 备品备件 操作记录 dto
 **/
@Data
public class SparePartsRecordQueryDTO extends BaseEntity {


    @ApiParam(value = "名称")
    private String name;

    @ApiParam(value = "电站id")
    private String powerStationId;

    @ApiParam(value = "操作记录 出库、入库")
    private String operationType;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiParam(value = "出入库开始时间")
    private Date startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiParam(value = "出入库结束时间")
    private Date endDate;
}