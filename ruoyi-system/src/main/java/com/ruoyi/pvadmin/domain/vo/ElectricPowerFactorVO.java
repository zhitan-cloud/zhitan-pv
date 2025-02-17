package com.ruoyi.pvadmin.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 功率因数分析 返回vo
 */
@Data
public class ElectricPowerFactorVO {
    /**
     * 记录列表
     */
    private List<ElectricPowerFactorItem> itemList;

    /**
     * 详情实体
     */
    private ElectricPowerFactorDetail detail;
}
