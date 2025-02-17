package com.ruoyi.pvadmin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 报警对象 alarm
 */
@Data
public class AlarmVO {

    /**
     * $column.columnComment
     */
    private String id;

    /**
     * 电站名称
     */
    private String powerStationName;

    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 发生时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dataTime;

    /**
     * 错误码
     */
    private String errCode;

    /**
     * 错误描述
     */
    private String errorDescription;

    /**
     * 错误级别
     */
    private Integer level;

    /**
     * 状态（1未解决、2已解决）
     */
    private String status;

    /**
     * 处理意见
     */
    private String remark;

    /**
     * 反馈图片地址 多张图片中间使用逗号分割
     * 最多三张
     */
    private String imageUrl;
}