package com.ruoyi.pvadmin.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.common.utils.DateTimeUtil;
import com.ruoyi.pvadmin.domain.dto.ElectricityTypeSettingItemSubmitDTO;
import com.ruoyi.pvadmin.domain.dto.ElectricityTypeSettingQueryDTO;
import com.ruoyi.pvadmin.domain.dto.ElectricityTypeSettingSubmitDTO;
import com.ruoyi.pvadmin.domain.entity.ElectricityTypeSetting;
import com.ruoyi.pvadmin.domain.entity.ElectricityTypeSettingItem;
import com.ruoyi.pvadmin.domain.vo.ElectricityTypeSettingItemVO;
import com.ruoyi.pvadmin.domain.vo.ElectricityTypeSettingVO;
import com.ruoyi.pvadmin.mapper.ElectricityTypeSettingItemMapper;
import com.ruoyi.pvadmin.mapper.ElectricityTypeSettingMapper;
import com.ruoyi.pvadmin.service.IElectricityTypeSettingService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 峰平谷配置Service业务层处理
 */
@Service
public class ElectricityTypeSettingServiceImpl extends ServiceImpl<ElectricityTypeSettingMapper, ElectricityTypeSetting>
        implements IElectricityTypeSettingService {

    @Autowired
    private ElectricityTypeSettingMapper settingMapper;

    @Autowired
    private ElectricityTypeSettingItemMapper settingItemMapper;

    /**
     * 查询峰平谷配置
     *
     * @param id 峰平谷配置主键
     * @return 峰平谷配置
     */
    @Override
    public ElectricityTypeSettingVO selectElectricityTypeSettingById(String id) {
        ElectricityTypeSettingVO typeSettingVO = new ElectricityTypeSettingVO();
        ElectricityTypeSetting setting = baseMapper.selectById(id);
        if (ObjectUtils.isEmpty(setting)) {
            throw new ServiceException("未找到峰平谷配置信息");
        }

        BeanUtils.copyProperties(setting, typeSettingVO);
        return typeSettingVO;
    }

    /**
     * 查询峰平谷配置列表
     *
     * @param dto 峰平谷配置
     * @return 峰平谷配置
     */
    @Override
    public List<ElectricityTypeSettingVO> selectElectricityTypeSettingList(ElectricityTypeSettingQueryDTO dto) {

        LambdaQueryWrapper<ElectricityTypeSetting> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(ElectricityTypeSetting::getId, ElectricityTypeSetting::getBeginTime, ElectricityTypeSetting::getEndTime,
                        ElectricityTypeSetting::getRemark, ElectricityTypeSetting::getCreateTime)
                .orderByAsc(ElectricityTypeSetting::getBeginTime);

        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        // 获取当前年份
        int currentYear = currentDate.getYear();
        // 指定时区（例如，使用系统默认时区）
        ZoneId zoneId = ZoneId.systemDefault();
        if (ObjectUtils.isEmpty(dto.getBeginTime())) {
            // 默认当年开始
            LocalDate startOfYear = LocalDate.of(currentYear, 1, 1);
            dto.setBeginTime(Date.from(startOfYear.atStartOfDay(zoneId).toInstant()));
        }

        wrapper.gt(ElectricityTypeSetting::getBeginTime, dto.getBeginTime());
        wrapper.gt(ElectricityTypeSetting::getEndTime, dto.getBeginTime());

        List<ElectricityTypeSetting> settingList = baseMapper.selectList(wrapper);
        List<ElectricityTypeSettingVO> settingVOList = new ArrayList<>(settingList.size());

        if (CollectionUtils.isEmpty(settingList)) {
            return settingVOList;
        }

        for (ElectricityTypeSetting setting : settingList) {
            ElectricityTypeSettingVO settingVO = new ElectricityTypeSettingVO();

            BeanUtils.copyProperties(setting, settingVO);
            settingVOList.add(settingVO);
        }
        return settingVOList;
    }

    /**
     * 新增峰平谷配置
     *
     * @param dto 峰平谷配置
     * @return 结果
     */
    @Override
    public String insertElectricityTypeSetting(ElectricityTypeSettingSubmitDTO dto) {
        if (dto.getEndTime().getTime() < dto.getBeginTime().getTime()) {
            return "截止时间不能小于开始时间";
        }
        // 校验开始-截止时间是否相交
        int count = baseMapper.isIntersecting(dto.getId(), dto.getBeginTime(), dto.getEndTime());
        if (count > Constants.DIGIT_0) {
            return "配置的时间段与其他配置存在交集，请更换后重试";
        }
        ElectricityTypeSetting typeSetting = new ElectricityTypeSetting();

        BeanUtils.copyProperties(dto, typeSetting);
        baseMapper.insert(typeSetting);
        return Constants.EMPTY;
    }

    /**
     * 修改峰平谷配置
     *
     * @param dto 峰平谷配置
     * @return 结果
     */
    @Override
    public String updateElectricityTypeSetting(ElectricityTypeSettingSubmitDTO dto) {
        ElectricityTypeSetting typeSetting = baseMapper.selectById(dto.getId());
        if (ObjectUtils.isEmpty(typeSetting)) {
            return "峰平谷信息不存在";
        }
        if (dto.getEndTime().getTime() < dto.getBeginTime().getTime()) {
            return "截止时间不能小于开始时间";
        }
        // 校验开始-截止时间是否相交
        int count = baseMapper.isIntersecting(dto.getId(), dto.getBeginTime(), dto.getEndTime());
        if (count > Constants.DIGIT_0) {
            return "配置的时间段与其他配置存在交集，请更换后重试";
        }
        Date now = new Date();
        Date oldBeginDate = typeSetting.getBeginTime();
        Date oldEndDate = typeSetting.getEndTime();
        Date newBeginDate = dto.getBeginTime();
        Date newEndDate = dto.getEndTime();
        // 已过去的时间不能修改年份
        if (oldBeginDate.before(now) &&
                !DateTimeUtil.getYear(oldBeginDate).equals(DateTimeUtil.getYear(newBeginDate))) {
            throw new BaseException("已过去的时间不能修改年份!");
        }
        if (oldEndDate.before(now) &&
                !DateTimeUtil.getYear(oldEndDate).equals(DateTimeUtil.getYear(newEndDate))) {
            throw new BaseException("已过去的时间不能修改年份!");
        }
        BeanUtils.copyProperties(dto, typeSetting);
        baseMapper.updateById(typeSetting);
        return Constants.EMPTY;
    }

    /**
     * 删除峰平谷配置信息
     *
     * @param id 峰平谷配置主键
     * @return 结果
     */
    @Override
    public int deleteElectricityTypeSettingById(String id) {
        ElectricityTypeSetting typeSetting = baseMapper.selectById(id);
        if (ObjectUtils.isEmpty(typeSetting)) {
            throw new BaseException("峰平谷信息不存在");
        }
        return settingMapper.deleteById(id);
    }

    /**
     * 新增峰平谷子项配置
     *
     * @param dtoList 配置子项
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertElectricityTypeItemSetting(List<ElectricityTypeSettingItemSubmitDTO> dtoList) {
        // 父级id
        String parentId = dtoList.get(Constants.DIGIT_0).getParentId();
        // 校验父级是否存在
        ElectricityTypeSetting electricityTypeSetting = baseMapper.selectById(parentId);
        if (ObjectUtils.isEmpty(electricityTypeSetting)) {
            return "峰平谷配置不存在";
        }
        // 判断时间是否相交
        if (checkIntersection(dtoList)) {
            return "配置时间存在相交";
        }
        // 判断时间是否整点、半点
        if (isOnTheHourOrHalfHour(dtoList)) {
            throw new ServiceException("峰平谷时间数据只能是整点或半点");
        }
        // 更新峰平谷
        settingItemMapper.delete(Wrappers.<ElectricityTypeSettingItem>lambdaQuery().eq(ElectricityTypeSettingItem::getParentId, parentId));
        for (ElectricityTypeSettingItemSubmitDTO dto : dtoList) {
            ElectricityTypeSettingItem settingItem = new ElectricityTypeSettingItem();

            BeanUtils.copyProperties(dto, settingItem);
            settingItem.setParentId(parentId);

            settingItem.setType(dto.getType().getCode());
            settingItemMapper.insert(settingItem);
        }
        return Constants.EMPTY;
    }

    /**
     * 根据主项id查询峰平谷子项配置
     *
     * @param settingId 父级id
     * @return 结果
     */
    @Override
    public List<ElectricityTypeSettingItemVO> listBySettingId(String settingId) {

        List<ElectricityTypeSettingItem> settingItemList = settingItemMapper.selectList(
                Wrappers.<ElectricityTypeSettingItem>lambdaQuery().select(ElectricityTypeSettingItem::getId,
                        ElectricityTypeSettingItem::getType, ElectricityTypeSettingItem::getBeginTime,
                        ElectricityTypeSettingItem::getEndTime, ElectricityTypeSettingItem::getElectricityPrice,
                        ElectricityTypeSettingItem::getRemark).eq(ElectricityTypeSettingItem::getParentId, settingId)
        );
        List<ElectricityTypeSettingItemVO> settingItemVOList = new ArrayList<>(settingItemList.size());
        if (CollectionUtils.isNotEmpty(settingItemList)) {
            for (ElectricityTypeSettingItem settingItem : settingItemList) {
                ElectricityTypeSettingItemVO itemVO = new ElectricityTypeSettingItemVO();

                BeanUtils.copyProperties(settingItem, itemVO);
                settingItemVOList.add(itemVO);
            }
        }
        return settingItemVOList;
    }

    /**
     * 校验峰平谷时段是否存在相交
     *
     * @param dtoList 校验的时间对象
     * @return 结果
     */
    public static boolean checkIntersection(List<ElectricityTypeSettingItemSubmitDTO> dtoList) {
        int length = dtoList.size();
        for (int i = 0; i < length; i++) {
            long startTimeI = dtoList.get(i).getBeginTime().getTime();
            long endTimeI = dtoList.get(i).getEndTime().getTime();

            for (int j = i + 1; j < length; j++) {
                long startTimeJ = dtoList.get(j).getBeginTime().getTime();
                long endTimeJ = dtoList.get(j).getEndTime().getTime();

                if (startTimeI < endTimeJ && startTimeJ < endTimeI) {
                    return true;  // 存在相交，返回true
                }
            }
        }
        return false;  // 不存在相交，返回false
    }

    /**
     * 判断时间是否整点、半点
     *
     * @param dtoList 判断时间
     * @return 结果
     */
    public static boolean isOnTheHourOrHalfHour(List<ElectricityTypeSettingItemSubmitDTO> dtoList) {
        for (ElectricityTypeSettingItemSubmitDTO dto : dtoList) {
            LocalTime beginTime = dto.getBeginTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            int beginMinute = beginTime.getMinute();

            LocalTime endTime = dto.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            int endMinute = endTime.getMinute();
            if ((beginMinute != 0 && beginMinute != 30) || (endMinute != 0 && endMinute != 30)) {
                return true;
            }
        }
        return false;
    }
}