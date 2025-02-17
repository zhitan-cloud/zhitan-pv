package com.ruoyi.pvadmin.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 设备类型
 */
@Data
@TableName(value = "device_type")
public class DeviceType extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 类型名称
     */
    @Excel(name = "设备类型名称")
    private String name;

    /**
     * 设备描述
     */
    @Excel(name = "设备描述")
    private String description;
}
