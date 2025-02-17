package com.ruoyi.pvadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.pvadmin.domain.entity.ElectricityDataItem;
import com.ruoyi.pvadmin.domain.model.ElectricModel;
import com.ruoyi.pvadmin.domain.vo.CumulativeGenerationVO;

import java.util.List;

/**
 * 电力-峰平谷数据Mapper接口
 *
 * @author ruoyi
 * @date 2023-08-23
 */
public interface ElectricityDataItemMapper extends BaseMapper<ElectricityDataItem> {

    /**
     * 获取累计电量、收益
     *
     * @return 结果
     */
    CumulativeGenerationVO getTotalCumulativeGeneration();

    List<ElectricityDataItem> listDataItemByPowerStationIds(ElectricModel electricModel);

    List<ElectricityDataItem> listDataItemByDeviceIds(ElectricModel electricModel);
}