package com.ruoyi.pvadmin.domain.model;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;

/**
* 设备类型点位模板的ietm
*/
@Data
public class DeviceTypeIndexTemplateItem {
    /**
     * 主键
     */
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
     * 计算点公式（就是计算指标的累计量所需的采集点指标编码 indexTemplate.code)，这里有一个存储规则[富经理给] 这里会带到 energyIndex.calcText
     */
    @Excel(name = "计算点公式", readConverterExp = "计算点公式")
    private String calcIndex;

    /**
     * 计量单位
     */
    @Excel(name = "计量单位")
    private String unit;

    /**
     * 网关用来获取对应数据
     */
    @Excel(name = "网关用来获取对应数据")
    private String key;
}
