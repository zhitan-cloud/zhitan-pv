package com.ruoyi.pvadmin.domain.model;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ElectricModel extends BaseEntity {

    private Date beginTime;

    private Date endTime;

    private List<String> deviceIds;

    private List<String> powerStationIds;

}
