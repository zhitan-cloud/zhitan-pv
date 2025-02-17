package com.ruoyi.pvadmin.domain.dto;

import lombok.Data;

/**
 * 设备因数DTO
 */
@Data
public class DeviceFactorDTO {
    /**
     * 点位模板的id
     */
    public String indexId;
    /**
     * 设备id
     */
    public String deviceId;
    /**
     * 倍率
     */
    public double factor;
}
