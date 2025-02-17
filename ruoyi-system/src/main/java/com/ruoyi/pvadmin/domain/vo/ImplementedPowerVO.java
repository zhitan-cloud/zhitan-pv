package com.ruoyi.pvadmin.domain.vo;

import com.ruoyi.pvadmin.domain.model.PowerGenerationTrendModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 获取设备点位
 */
@Data
public class ImplementedPowerVO {

    /**
     * 实时功率
     */
    private BigDecimal realTimePower;

    /**
     * 统计图信息
     */
    private List<PowerGenerationTrendModel> itemList;

}