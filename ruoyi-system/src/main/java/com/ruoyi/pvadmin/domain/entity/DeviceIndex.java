package com.ruoyi.pvadmin.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 设备与点位关系对象 device_index
 */
@Data
@TableName(value = "device_index")
public class DeviceIndex extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 点位id
     */
    private String indexId;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 点位编号
     */
    private String indexCode;

    /**
     * 点位类型（采集点 -COLLECT、计算点-CALCULATE）
     */
    private String indexType;

    /**
     * 计算点公式
     */
    private String calcIndex;

    /**
     * 单位
     */
    private String unit;

    /**
     * 网关用来获取对应数据
     */
    private String tagKey;

    /**
     * 系数/变比/倍率等
     */
    private float factor;


    @TableField(exist = false)
    private String indexName;
}