package com.ruoyi.pvadmin.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 备品备件操作记录返回 VO
 *
 * @author ruoyi
 * @date 2023-08-31
 */
@Data
public class SparePartsOperationVO extends BaseEntity {
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
     * 库存数量
     */
    @Excel(name = "库存数量")
    private Long amount;

    /**
     * 地点
     */
    @Excel(name = "库存地点")
    private String location;
    /**
     * 地点 地点id
     */
    private String locationId;

}