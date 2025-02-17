package com.ruoyi.pvadmin.domain.dto;

import lombok.Data;

@Data
public class InventoryLocationDTO {
    /**
     * id
     */
    private Long id;
    /**
     * 地点
     */
    private String location;
    /**
     * 备注
     */
    private String remark;
}
