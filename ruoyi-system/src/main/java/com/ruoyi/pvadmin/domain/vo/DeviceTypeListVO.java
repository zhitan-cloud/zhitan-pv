package com.ruoyi.pvadmin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
* 设备类型的列表VO
*/
@Data
public class DeviceTypeListVO {
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
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
