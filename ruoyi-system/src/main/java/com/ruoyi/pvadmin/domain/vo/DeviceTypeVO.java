package com.ruoyi.pvadmin.domain.vo;

import com.ruoyi.pvadmin.domain.model.DeviceTypeIndexTemplateItem;
import lombok.Data;

import java.util.List;

/**
 * 设备类型的VO
 */
@Data
public class DeviceTypeVO {
    /**
     * id
     */
    private String id;
    /**
     * 设备类型名称
     */
    private String name;
    /**
     * 设备描述
     */
    private String description;
    /**
     * 点位模板项
     */
    private List<DeviceTypeIndexTemplateItem> indexTemplateItems;
}
