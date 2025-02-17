package com.ruoyi.pvadmin.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.pvadmin.domain.entity.ElectricityTypeSetting;
import com.ruoyi.pvadmin.domain.entity.ElectricityTypeSettingItem;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 峰平谷配置Mapper接口
 */
public interface ElectricityTypeSettingMapper extends BaseMapper<ElectricityTypeSetting> {
    /**
     * 查询峰平谷配置
     *
     * @param id 峰平谷配置主键
     * @return 峰平谷配置
     */
    public ElectricityTypeSetting selectElectricityTypeSettingById(String id);

    /**
     * 查询峰平谷配置列表
     *
     * @param electricityTypeSetting 峰平谷配置
     * @return 峰平谷配置集合
     */
    public List<ElectricityTypeSetting> selectElectricityTypeSettingList(ElectricityTypeSetting electricityTypeSetting);

    /**
     * 新增峰平谷配置
     *
     * @param electricityTypeSetting 峰平谷配置
     * @return 结果
     */
    public int insertElectricityTypeSetting(ElectricityTypeSetting electricityTypeSetting);

    /**
     * 修改峰平谷配置
     *
     * @param electricityTypeSetting 峰平谷配置
     * @return 结果
     */
    public int updateElectricityTypeSetting(ElectricityTypeSetting electricityTypeSetting);

    /**
     * 删除峰平谷配置
     *
     * @param id 峰平谷配置主键
     * @return 结果
     */
    public int deleteElectricityTypeSettingById(String id);

    /**
     * 批量删除峰平谷配置
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteElectricityTypeSettingByIds(String[] ids);

    /**
     * 校验时间是否存在相交
     *
     * @param id        排除id
     * @param beginTime 开始时间
     * @param endTime   截止时间
     * @return 结果
     */
    int isIntersecting(@Param("id") String id, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    /**
     * 根据查询日期查询峰平谷配置子项信息
     *
     * @param dateTime 查询时间
     * @return 结果
     */
    List<ElectricityTypeSettingItem> listSettingByDate(@Param("dateTime") Date dateTime);
}
