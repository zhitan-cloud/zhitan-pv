package com.ruoyi.pvadmin.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 备品备件对象 spare_parts
 *
 * @author ruoyi
 * @date 2023-08-31
 */
@Data
@TableName(value = "spare_parts")
public class SpareParts extends BaseEntity {
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
     * 库存数量
     */
    @Excel(name = "库存数量")
    private Long amount;

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
}