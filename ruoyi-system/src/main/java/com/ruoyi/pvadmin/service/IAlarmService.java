package com.ruoyi.pvadmin.service;


import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.pvadmin.domain.dto.AlarmHandlingDTO;
import com.ruoyi.pvadmin.domain.dto.AlarmQueryDTO;
import com.ruoyi.pvadmin.domain.vo.AlarmLevelAnalysisVO;
import com.ruoyi.pvadmin.domain.vo.AlarmVO;
import com.ruoyi.pvadmin.domain.vo.HomeAlarmVO;

import java.util.List;

/**
 * 报警Service接口
 */
 public interface IAlarmService {
    /**
     * 查询报警
     *
     * @param id 报警主键
     * @return 报警
     */
     AlarmVO selectAlarmById(String id);

    /**
     * 查询报警列表
     *
     * @param dto 报警
     * @return 报警集合
     */
     List<AlarmVO> selectAlarmList(AlarmQueryDTO dto);

    /**
     * 处理报警信息
     *
     * @param dto 处理的报警信息
     */
    void alarmHandling(AlarmHandlingDTO dto);

    /**
     * 首页-查询报警信息
     *
     * @return 结果
     */
    HomeAlarmVO listHomeAlarm(BaseEntity entity);

    /**
     * 查询报警等级分析
     *
     * @return 结果
     */
    AlarmLevelAnalysisVO getAlarmLevelAnalysis();

    /**
     * 根据id设置报警等级
     *
     * @param alarmId 报警信息id
     * @param level   级别
     * @return 结果
     */
    int editAlarmLevel(String alarmId, Integer level);
}
