package com.ruoyi.pvadmin.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 平台能源折标模板；平台预置的 指标模板，不提供租户给，下面预设的采集点、计算点信息对象 index_template
 */
@Data
@TableName(value = "device_type_index_template")
public class DeviceTypeIndexTemplate extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
    * 设备类型id
    */
    @Excel(name = "设备类型id")
    private String deviceTypeId;

    /**
     * 设备类型名称
     */
    @Excel(name = "设备类型名称")
    private String deviceTypeName;

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
     * 点位类型（采集点 -COLLECT、计算点-CALCULATE）
     */
    @Excel(name = "点位类型", readConverterExp = "采=集点,-=COLLECT、计算点-CALCULATE")
    private String indexType;

    /**
     * 计量单位
     */
    @Excel(name = "计量单位")
    private String unit;

    /**
     * 网关用来获取对应数据
     */
    @Excel(name = "网关用来获取对应数据")
    private String tagKey;
}