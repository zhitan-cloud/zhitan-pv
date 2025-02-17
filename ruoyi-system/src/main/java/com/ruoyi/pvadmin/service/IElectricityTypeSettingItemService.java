package com.ruoyi.pvadmin.service;

import com.ruoyi.pvadmin.domain.entity.ElectricityTypeSettingItem;

import java.util.List;

/**
 * 峰平谷配置子项Service接口
 */
public interface IElectricityTypeSettingItemService {
    /**
     * 查询峰平谷配置子项
     *
     * @param id 峰平谷配置子项主键
     * @return 峰平谷配置子项
     */
    public ElectricityTypeSettingItem selectElectricityTypeSettingItemById(String id);

    /**
     * 查询峰平谷配置子项列表
     *
     * @param electricityTypeSettingItem 峰平谷配置子项
     * @return 峰平谷配置子项集合
     */
    public List<ElectricityTypeSettingItem> selectElectricityTypeSettingItemList(ElectricityTypeSettingItem electricityTypeSettingItem);

    /**
     * 新增峰平谷配置子项
     *
     * @param electricityTypeSettingItem 峰平谷配置子项
     * @return 结果
     */
    public int insertElectricityTypeSettingItem(ElectricityTypeSettingItem electricityTypeSettingItem);

    /**
     * 修改峰平谷配置子项
     *
     * @param electricityTypeSettingItem 峰平谷配置子项
     * @return 结果
     */
    public int updateElectricityTypeSettingItem(ElectricityTypeSettingItem electricityTypeSettingItem);

    /**
     * 批量删除峰平谷配置子项
     *
     * @param ids 需要删除的峰平谷配置子项主键集合
     * @return 结果
     */
    public int deleteElectricityTypeSettingItemByIds(String[] ids);

    /**
     * 删除峰平谷配置子项信息
     *
     * @param id 峰平谷配置子项主键
     * @return 结果
     */
    public int deleteElectricityTypeSettingItemById(String id);
}
