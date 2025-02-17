package com.ruoyi.pvadmin.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.pvadmin.domain.entity.ElectricityTypeSetting;
import com.ruoyi.pvadmin.domain.entity.ElectricityTypeSettingItem;
import com.ruoyi.pvadmin.mapper.ElectricityTypeSettingItemMapper;
import com.ruoyi.pvadmin.service.IElectricityTypeSettingItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 峰平谷配置子项Service业务层处理
 */
@Service
public class ElectricityTypeSettingItemServiceImpl extends ServiceImpl<ElectricityTypeSettingItemMapper, ElectricityTypeSettingItem>
        implements IElectricityTypeSettingItemService {

    @Autowired
    private ElectricityTypeSettingItemMapper electricityTypeSettingItemMapper;

    /**
     * 查询峰平谷配置子项
     *
     * @param id 峰平谷配置子项主键
     * @return 峰平谷配置子项
     */
    @Override
    public ElectricityTypeSettingItem selectElectricityTypeSettingItemById(String id) {
        return electricityTypeSettingItemMapper.selectElectricityTypeSettingItemById(id);
    }

    /**
     * 查询峰平谷配置子项列表
     *
     * @param electricityTypeSettingItem 峰平谷配置子项
     * @return 峰平谷配置子项
     */
    @Override
    public List<ElectricityTypeSettingItem> selectElectricityTypeSettingItemList(ElectricityTypeSettingItem electricityTypeSettingItem) {
        return electricityTypeSettingItemMapper.selectElectricityTypeSettingItemList(electricityTypeSettingItem);
    }

    /**
     * 新增峰平谷配置子项
     *
     * @param electricityTypeSettingItem 峰平谷配置子项
     * @return 结果
     */
    @Override
    public int insertElectricityTypeSettingItem(ElectricityTypeSettingItem electricityTypeSettingItem) {
        electricityTypeSettingItem.setCreateTime(DateUtils.getNowDate());
        return electricityTypeSettingItemMapper.insertElectricityTypeSettingItem(electricityTypeSettingItem);
    }

    /**
     * 修改峰平谷配置子项
     *
     * @param electricityTypeSettingItem 峰平谷配置子项
     * @return 结果
     */
    @Override
    public int updateElectricityTypeSettingItem(ElectricityTypeSettingItem electricityTypeSettingItem) {
        electricityTypeSettingItem.setUpdateTime(DateUtils.getNowDate());
        return electricityTypeSettingItemMapper.updateElectricityTypeSettingItem(electricityTypeSettingItem);
    }

    /**
     * 批量删除峰平谷配置子项
     *
     * @param ids 需要删除的峰平谷配置子项主键
     * @return 结果
     */
    @Override
    public int deleteElectricityTypeSettingItemByIds(String[] ids) {
        return electricityTypeSettingItemMapper.deleteElectricityTypeSettingItemByIds(ids);
    }

    /**
     * 删除峰平谷配置子项信息
     *
     * @param id 峰平谷配置子项主键
     * @return 结果
     */
    @Override
    public int deleteElectricityTypeSettingItemById(String id) {
        return electricityTypeSettingItemMapper.deleteElectricityTypeSettingItemById(id);
    }
}
