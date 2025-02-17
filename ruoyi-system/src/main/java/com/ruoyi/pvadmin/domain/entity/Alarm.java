package com.ruoyi.pvadmin.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 报警对象 alarm
 */
@Data
@TableName(value = "alarm")
public class Alarm extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 发生时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发生时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dataTime;

    /**
     * 错误码
     */
    @Excel(name = "错误码")
    private String errCode;

    /**
     * 设备编号
     */
    @Excel(name = "设备编号")
    private String deviceCode;

    /**
     * 错误描述
     */
    @Excel(name = "错误描述")
    private String errorDescription;

    /**
     * 建议解决方案
     */
    @Excel(name = "建议解决方案")
    private String solution;

    /**
     * 报警等级（1：一级，2：二级，3：三级）
     */
    @Excel(name = "报警等级（1：一级，2：二级，3：三级）")
    private Integer level;

    /**
     * 状态（1未解决、2已解决）
     */
    @Excel(name = "状态", readConverterExp = "1=未解决、2已解决")
    private String status;

    /**
     * 处理人
     */
    @Excel(name = "处理人")
    private String handlers;

    /**
     * 处理人姓名
     */
    @Excel(name = "处理人姓名")
    private String handlersName;

    /**
     * 处理时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "处理时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date processingTime;

    /**
     * 反馈图片地址 多张图片中间使用逗号分割
     * 最多三张
     */
    @Excel(name = "处理图片", width = 30)
    private String imageUrl;
}