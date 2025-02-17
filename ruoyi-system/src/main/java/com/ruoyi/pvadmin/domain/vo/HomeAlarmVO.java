package com.ruoyi.pvadmin.domain.vo;

import lombok.Data;

/**
 * 首页-报警概览  vo
 */
@Data
public class HomeAlarmVO {

    /**
     * 全部报警数量
     */
    private Integer alarmCount;
    private Integer todayAlarmCount;

    /**
     * 已处理
     */
    private Integer processed;
    private Integer todayProcessed;

    /**
     * 未处理
     */
    private Integer unprocessed;
    private Integer todayUnprocessed;

    /**
     * 级别1
     */
    private Integer level1;
    private Integer todayLevel1;

    /**
     * 级别2
     */
    private Integer level2;
    private Integer todayLevel2;

    /**
     * 级别3
     */
    private Integer level3;
    private Integer todayLevel3;

}