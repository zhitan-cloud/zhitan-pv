package com.ruoyi.pvadmin.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 处理报警对象
 */
@Data
public class AlarmHandlingDTO {

    /**
     * $column.columnComment
     */
    @NotBlank(message = "报警对象未找到")
    private String id;

    /**
     * 处理方案
     */
    private String processingScenarios;

    /**
    * 处理图片
    */
    private String imageUrl;
}