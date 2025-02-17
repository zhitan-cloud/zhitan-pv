package com.ruoyi.pvadmin.domain.vo;

import com.ruoyi.pvadmin.domain.model.AlarmLevelAnalysisItem;
import lombok.Data;

import java.util.List;

/**
 * 报警等级分析对象
 */
@Data
public class AlarmLevelAnalysisVO {


    /**
     * 所有报警条数
     */
    private Integer allCount;

    /**
     * 未处理报警条数
     */
    private Integer unhandledCount;

    /**
     * 比例集合
     */
    private List<AlarmLevelAnalysisItem> ratioList;
}