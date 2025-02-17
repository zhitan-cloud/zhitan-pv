package com.ruoyi.pvadmin.domain.dto;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;

/**
 * 点位模板查询dto
 */
@Data
public class IndexTemplateQueryDTO {


    /**
     * 模板点位名称
     */
    @Excel(name = "模板点位名称")
    private String name;

    /**
     * 模板点位编码
     */
    @Excel(name = "模板点位编码")
    private String code;

    /**
     * 表具类型id
     */
    @Excel(name = "表具类型id")
    private String deviceTypeId;
}