package com.ruoyi.pvadmin.domain.vo;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;

@Data
public class DeviceTypeIndexVO {
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
