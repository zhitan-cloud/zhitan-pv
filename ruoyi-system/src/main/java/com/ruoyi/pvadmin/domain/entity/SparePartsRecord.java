package com.ruoyi.pvadmin.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 备品备件-操作记录对象 spare_parts_record
 *
 * @author ruoyi
 * @date 2023-08-31
 */
@Data
@TableName(value = "spare_parts_record")
public class SparePartsRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

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

    /**
     * 规格型号
     */
    @Excel(name = "规格型号")
    private String specs;

    /**
     * 电站id
     */
    @Excel(name = "电站id")
    private String powerStationId;

    /**
     * 操作数量
     */
    @Excel(name = "操作数量")
    private Long amount;

    /**
     * 状态（0入库，1出库）
     */
    @Excel(name = "状态", readConverterExp = "0=入库，1出库")
    private String status;

    /**
     * 用户id
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long userId;

    /**
     * 部门id
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long deptId;

    /**
     * 地点
     */
    @Excel(name = "库存地点")
    private String location;
    /**
     * 地点 地点id
     */
    private Long locationId;

    /**
    * 出入库日期
    */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出入库日期")
    private Date movementDate;
}