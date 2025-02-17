package com.ruoyi.pvadmin.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 设备点检附件
 */
@Data
@TableName(value = "device_inspection_annex")
public class DeviceInspectionAnnex {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 点检的id
     */
    private String deviceInspectionId;

    /**
     * 附件地址
     */
    private String annex;
}
