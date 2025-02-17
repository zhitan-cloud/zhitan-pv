package com.ruoyi.pvadmin.domain.model;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 传感器数据
 *
 * @Author: Zhujw
 * @Date: 2022/11/13
 */
@Data
public class SensorParamVO {

    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    /**
     * 离线报警
     */
    private boolean offline;

    @ApiModelProperty(value = "点位信息")
    private List<RealTimeIndexParam> indexArray;
}