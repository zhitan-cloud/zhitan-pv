package com.ruoyi.pvadmin.domain.dto;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 电站维护 查询dto
 */
@Data
public class PowerStationQueryDTO extends BaseEntity {

    /**
     * 电站id
     */
    private String powerStationId;

    /**
     * 编号
     */
    @Excel(name = "编号")
    private String code;

    /**
     * 名称
     */
    @Excel(name = "名称")
    private String name;

}