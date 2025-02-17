package com.ruoyi.pvadmin.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 点位模板 提交dto
 */
@Data
public class IndexTemplateSubmitDTO {

    /**
     * 主键
     */
    private String id;

    /**
     * 模板点位名称
     */
    @NotBlank(message = "点位名称不能为空")
    private String name;

    /**
     * 模板点位编码
     */
    @NotBlank(message = "点位编号不能为空")
    private String code;

    /**
     * 点位类型（采集点 -COLLECT、计算点-CALCULATE）
     */
    @NotBlank(message = "点位类型不能为空")
    private String indexType;

    /**
     * 计算点公式
     */
    private String calcIndex;

    /**
     * 计量单位
     */
    private String unit;

    /**
     * 网关用来获取对应数据
     */
    private String key;
}