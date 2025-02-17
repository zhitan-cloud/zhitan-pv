package com.ruoyi.pvadmin.service;


import com.ruoyi.pvadmin.domain.dto.ElectricityTypeSettingItemSubmitDTO;
import com.ruoyi.pvadmin.domain.dto.ElectricityTypeSettingQueryDTO;
import com.ruoyi.pvadmin.domain.dto.ElectricityTypeSettingSubmitDTO;
import com.ruoyi.pvadmin.domain.vo.ElectricityTypeSettingItemVO;
import com.ruoyi.pvadmin.domain.vo.ElectricityTypeSettingVO;

import java.util.List;

/**
 * 峰平谷配置Service接口
 */
public interface IElectricityTypeSettingService {
    /**
     * 查询峰平谷配置
     *
     * @param id 峰平谷配置主键
     * @return 峰平谷配置
     */
    public ElectricityTypeSettingVO selectElectricityTypeSettingById(String id);

    /**
     * 查询峰平谷配置列表
     *
     * @param electricityTypeSetting 峰平谷配置
     * @return 峰平谷配置集合
     */
    public List<ElectricityTypeSettingVO> selectElectricityTypeSettingList(ElectricityTypeSettingQueryDTO electricityTypeSetting);

    /**
     * 新增峰平谷配置
     *
     * @param dto 峰平谷配置
     * @return 结果
     */
    public String insertElectricityTypeSetting(ElectricityTypeSettingSubmitDTO dto);

    /**
     * 修改峰平谷配置
     *
     * @param dto 峰平谷配置
     * @return 结果
     */
    public String updateElectricityTypeSetting(ElectricityTypeSettingSubmitDTO dto);

    /**
     * 删除峰平谷配置信息
     *
     * @param id 峰平谷配置主键
     * @return 结果
     */
    public int deleteElectricityTypeSettingById(String id);

    /**
     * 新增峰平谷子项配置
     *
     * @param dtoList 配置子项
     * @return 结果
     */
    String insertElectricityTypeItemSetting(List<ElectricityTypeSettingItemSubmitDTO> dtoList);

    /**
     * 根据主项id查询峰平谷子项配置
     *
     * @param settingId 父级id
     * @return 结果
     */
    List<ElectricityTypeSettingItemVO> listBySettingId(String settingId);
}
