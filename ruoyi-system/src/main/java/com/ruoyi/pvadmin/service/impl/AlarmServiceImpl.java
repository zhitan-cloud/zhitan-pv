package com.ruoyi.pvadmin.service.impl;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.common.utils.CalcUtil;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.pvadmin.domain.dto.AlarmHandlingDTO;
import com.ruoyi.pvadmin.domain.dto.AlarmQueryDTO;
import com.ruoyi.pvadmin.domain.dto.DeviceQueryDTO;
import com.ruoyi.pvadmin.domain.entity.Alarm;
import com.ruoyi.pvadmin.domain.model.AlarmLevelAnalysisItem;
import com.ruoyi.pvadmin.domain.vo.AlarmLevelAnalysisVO;
import com.ruoyi.pvadmin.domain.vo.AlarmVO;
import com.ruoyi.pvadmin.domain.vo.HomeAlarmVO;
import com.ruoyi.pvadmin.mapper.AlarmMapper;
import com.ruoyi.pvadmin.mapper.DeviceMapper;
import com.ruoyi.pvadmin.service.IAlarmService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 报警Service业务层处理
 */
@Service
public class AlarmServiceImpl extends ServiceImpl<AlarmMapper, Alarm> implements IAlarmService {

    @Resource
    private DeviceMapper deviceMapper;

    /**
     * 查询报警
     *
     * @param id 报警主键
     * @return 报警
     */
    @Override
    public AlarmVO selectAlarmById(String id) {
        return baseMapper.getInfoById(id);
    }

    /**
     * 查询报警列表
     *
     * @param dto 报警
     * @return 报警
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<AlarmVO> selectAlarmList(AlarmQueryDTO dto) {

        return baseMapper.listByQueryDto(dto);
    }

    /**
     * 处理报警信息
     *
     * @param dto 处理的报警信息
     */
    @Override
    public void alarmHandling(AlarmHandlingDTO dto) {
        if (ObjectUtils.isEmpty(dto.getProcessingScenarios())) {
            throw new BaseException("处理意见不能为空");
        }
        if (ObjectUtils.isEmpty(dto.getImageUrl())) {
            throw new BaseException("反馈图片不能为空");
        }
        SysUser user = SecurityUtils.getLoginUser().getUser();

        Alarm alarm = baseMapper.selectById(dto.getId());
        if (ObjectUtils.isEmpty(alarm)) {
            throw new ServiceException("报警信息不存在");
        }
        alarm.setProcessingTime(new Date());
        alarm.setStatus(Constants.STR_NUMBER_2);
        alarm.setHandlersName(user.getNickName());
        alarm.setRemark(dto.getProcessingScenarios());
        alarm.setHandlers(String.valueOf(user.getUserId()));
        alarm.setImageUrl(dto.getImageUrl());
        baseMapper.updateById(alarm);
    }

    /**
     * 首页-查询报警信息
     *
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "d")
    public HomeAlarmVO listHomeAlarm(BaseEntity entity) {
        HomeAlarmVO homeAlarmVO = new HomeAlarmVO();

        DeviceQueryDTO queryDto = new DeviceQueryDTO();
        queryDto.setParams(entity.getParams());
        List<String> deviceCodeList = deviceMapper.listDeviceCode(queryDto);

        // 当日报警
        Date beginTime = DateUtil.beginOfDay(new Date());
        Date endTime = DateUtil.endOfDay(beginTime);

        List<Alarm> alarmList = baseMapper.selectList(Wrappers.<Alarm>lambdaQuery()
                .select(Alarm::getStatus, Alarm::getLevel)
                .in(ObjectUtils.isNotEmpty(deviceCodeList), Alarm::getDeviceCode, deviceCodeList)
                .between(Alarm::getDataTime, beginTime, endTime)
        );

        int todayProcessed = Constants.DIGIT_0;
        int todayUnprocessed = Constants.DIGIT_0;
        int todayLevel1 = Constants.DIGIT_0;
        int todayLevel2 = Constants.DIGIT_0;
        int todayLevel3 = Constants.DIGIT_0;

        for (Alarm alarm : alarmList) {
            // 已处理
            if (Constants.STR_NUMBER_2.equals(alarm.getStatus())) {
                todayProcessed++;
            }
            // 未处理
            if (Constants.STR_NUMBER_1.equals(alarm.getStatus())) {
                todayUnprocessed++;
            }
            // 级别
            if (Constants.DIGIT_1 == alarm.getLevel()) {
                todayLevel1++;
            }
            if (Constants.DIGIT_2 == alarm.getLevel()) {
                todayLevel2++;
            }
            if (Constants.DIGIT_3 == alarm.getLevel()) {
                todayLevel3++;
            }
        }

        homeAlarmVO.setTodayAlarmCount(alarmList.size());
        homeAlarmVO.setTodayProcessed(todayProcessed);
        homeAlarmVO.setTodayUnprocessed(todayUnprocessed);
        homeAlarmVO.setTodayLevel1(todayLevel1);
        homeAlarmVO.setTodayLevel2(todayLevel2);
        homeAlarmVO.setTodayLevel3(todayLevel3);

        // 报警列表
        AlarmQueryDTO alarmQueryDto = new AlarmQueryDTO();
        alarmQueryDto.setParams(entity.getParams());
        List<AlarmVO> alarmVOList = baseMapper.listByQueryDto(alarmQueryDto);

        int processed = Constants.DIGIT_0;
        int unprocessed = Constants.DIGIT_0;
        int level1 = Constants.DIGIT_0;
        int level2 = Constants.DIGIT_0;
        int level3 = Constants.DIGIT_0;

        for (AlarmVO alarm : alarmVOList) {
            // 已处理
            if (Constants.STR_NUMBER_2.equals(alarm.getStatus())) {
                processed++;
            }
            // 未处理
            if (Constants.STR_NUMBER_1.equals(alarm.getStatus())) {
                unprocessed++;
            }
            // 级别
            if (Constants.DIGIT_1 == alarm.getLevel()) {
                level1++;
            }
            if (Constants.DIGIT_2 == alarm.getLevel()) {
                level2++;
            }
            if (Constants.DIGIT_3 == alarm.getLevel()) {
                level3++;
            }
        }

        homeAlarmVO.setAlarmCount(alarmVOList.size());
        homeAlarmVO.setProcessed(processed);
        homeAlarmVO.setUnprocessed(unprocessed);
        homeAlarmVO.setLevel1(level1);
        homeAlarmVO.setLevel2(level2);
        homeAlarmVO.setLevel3(level3);

        return homeAlarmVO;
    }

    /**
     * 查询报警等级分析
     *
     * @return 结果
     */
    @Override
    public AlarmLevelAnalysisVO getAlarmLevelAnalysis() {
        AlarmLevelAnalysisVO levelAnalysisItem = new AlarmLevelAnalysisVO();

        List<AlarmLevelAnalysisItem> analysisVOList = new ArrayList<>(Constants.DIGIT_3);

        List<Alarm> alarmList = baseMapper.selectList(Wrappers.<Alarm>lambdaQuery()
                .select(Alarm::getLevel).eq(Alarm::getStatus, Constants.STR_NUMBER_1)
        );
        Map<Integer, Long> levelMap = alarmList.stream().collect(Collectors.groupingBy(Alarm::getLevel, Collectors.counting()));

        AlarmLevelAnalysisItem level1VO = new AlarmLevelAnalysisItem();
        Long level1Count = levelMap.get(Constants.DIGIT_1);
        int count1 = ObjectUtils.isEmpty(level1Count) ? Constants.DIGIT_0 : Math.toIntExact(level1Count);
        level1VO.setCount(count1);
        level1VO.setLevel(Constants.DIGIT_1);

        AlarmLevelAnalysisItem level2VO = new AlarmLevelAnalysisItem();
        Long level2Count = levelMap.get(Constants.DIGIT_2);
        int count2 = ObjectUtils.isEmpty(level2Count) ? Constants.DIGIT_0 : Math.toIntExact(level2Count);
        level2VO.setCount(count2);
        level2VO.setLevel(Constants.DIGIT_2);

        AlarmLevelAnalysisItem level3VO = new AlarmLevelAnalysisItem();
        Long level3Count = levelMap.get(Constants.DIGIT_3);
        int count3 = ObjectUtils.isEmpty(level3Count) ? Constants.DIGIT_0 : Math.toIntExact(level3Count);
        level3VO.setCount(count3);
        level3VO.setLevel(Constants.DIGIT_3);

        int[] arr = new int[]{count1, count2, count3};
        double[] percentValue = CalcUtil.getPercentValue(arr, Constants.DIGIT_2);

        level1VO.setRatio(BigDecimal.valueOf(percentValue[0]).setScale(Constants.DIGIT_2, RoundingMode.HALF_UP));
        level2VO.setRatio(BigDecimal.valueOf(percentValue[1]).setScale(Constants.DIGIT_2, RoundingMode.HALF_UP));
        level3VO.setRatio(BigDecimal.valueOf(percentValue[2]).setScale(Constants.DIGIT_2, RoundingMode.HALF_UP));

        analysisVOList.add(level1VO);
        analysisVOList.add(level2VO);
        analysisVOList.add(level3VO);

        levelAnalysisItem.setRatioList(analysisVOList);
        levelAnalysisItem.setUnhandledCount(alarmList.size());
        levelAnalysisItem.setAllCount(baseMapper.selectCount(Wrappers.lambdaQuery()));
        return levelAnalysisItem;
    }

    /**
     * 根据id设置报警等级
     *
     * @param alarmId 报警信息id
     * @param level   级别
     * @return 结果
     */
    @Override
    public int editAlarmLevel(String alarmId, Integer level) {

        Alarm alarm = new Alarm();
        alarm.setId(alarmId);
        alarm.setLevel(level);

        return baseMapper.updateById(alarm);
    }
}