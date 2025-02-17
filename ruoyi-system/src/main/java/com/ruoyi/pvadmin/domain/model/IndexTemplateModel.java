package com.ruoyi.pvadmin.domain.model;

import lombok.Data;

/**
 * 点位模板
 */
@Data
public class IndexTemplateModel {

    /**
     * 主键
     */
    private String id;

    /**
     * 模板点位名称
     */
    private String name;

    /**
     * 模板点位编码
     */
    private String code;

    /**
     * 表具类型id
     */
    private String deviceTypeId;

    /**
     * 点位类型（采集点 -COLLECT、计算点-CALCULATE）
     */
    private String indexType;

    /**
     * 计量单位
     */
    private String unit;

    /**
     * 网关用来获取对应数据
     */
    private String tagKey;

}