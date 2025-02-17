package com.ruoyi.pvadmin.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 备品备件对象 spare_parts
 *
 * @author ruoyi
 * @date 2023-08-31
 */
@Data
public class SparePartsVO extends BaseEntity {

    /**
     * $column.columnComment
     */
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
     * 库存数量
     */
    @Excel(name = "库存数量")
    private Long amount;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;

    /**
     * 电站id
     */
    @Excel(name = "电站id")
    private String powerStationId;

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
    private String locationId;

    /**
     * 出入库日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出入库日期")
    private Date movementDate;

}