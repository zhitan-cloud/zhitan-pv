package com.ruoyi.pvadmin.domain.vo;

import lombok.Data;

/**
 * 获取设备点位
 */
@Data
public class DeviceIndexVO {
    /**
     * 点位模板的id
     */
    private String indexId;
    /**
     * 设备id
     */
    private String deviceId;
    /**
     * code
     */
    private String indexCode;
    /**
     * 类型
     */
    private String indexType;
    /**
     * 计算内容
     */
    private String calcIndex;
    /**
    * 倍率
    */
    private double factor;
}
