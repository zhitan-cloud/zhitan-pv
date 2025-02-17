package com.ruoyi.pvadmin.domain.dto;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * 电站维护提交 dto
 */
@Data
public class PowerStationSubmitDTO extends BaseEntity {

    /**
     * $column.columnComment
     */
    private String id;

    /**
     * 编号
     */
    @NotBlank(message = "编号不能为空")
    private String code;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    private String name;

    /**
     * 经度
     */
    private BigDecimal lon;

    /**
     * 纬度
     */
    private BigDecimal lat;

    /**
     * 补贴电价
     */
    private BigDecimal subsidizedPrices;

    /**
     * 电站装机容量
     */
    private BigDecimal installedCapacity;

    /**
     * 并网电压
     */
    private BigDecimal gridVoltage;

}