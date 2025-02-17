package com.ruoyi.pvadmin.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 库存地点
 */
@Data
@TableName(value = "inventory_location")
public class InventoryLocation {

    private static final long serialVersionUID = 1L;
    /**
    * 主键
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 地点
     */
    private String location;
    /**
    * 备注
    */
    private String remark;
}
