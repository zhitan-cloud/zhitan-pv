package com.ruoyi.pvadmin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 备品备件-操作记录对象 spare_parts_record
 *
 * @author ruoyi
 * @date 2023-08-31
 */
@Data
public class StockOperationRecordVO {

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 规格型号
     */
    private String specs;

    /**
     * 电站名称
     */
    private String powerStationName;

    /**
     * 操作数量
     */
    private Long amount;

    /**
     * 状态（0入库，1出库）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 地点
     */
    @Excel(name = "库存地点")
    private String location;

    /**
     * 地点 地点id
     */
    private String locationId;

    /**
     * 出入库日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date movementDate;
}