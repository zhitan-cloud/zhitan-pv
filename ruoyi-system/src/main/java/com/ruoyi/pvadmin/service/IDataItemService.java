package com.ruoyi.pvadmin.service;


import com.ruoyi.pvadmin.domain.dto.ComparativeAnalysisDTO;
import com.ruoyi.pvadmin.domain.dto.HomeQueryDTO;
import com.ruoyi.pvadmin.domain.vo.EnergyComparisonVO;
import com.ruoyi.pvadmin.domain.vo.HomepageGenerationStatsVO;

import java.util.List;

/**
 * 累积量 接口层
 */
public interface IDataItemService {

    /**
     * 能耗统计分析-获取同比分析列表数据
     *
     * @param dto            查询参数
     * @param comparisonType 同比、环比
     * @return 结果
     */
    List<EnergyComparisonVO> listEnergyAnalysis(ComparativeAnalysisDTO dto, String comparisonType);

    /**
     * 首页-获取首页发电量数据
     *
     * @param dto 查询条件
     * @return 结果
     */
    List<HomepageGenerationStatsVO> getHomepageGenerationStats(HomeQueryDTO dto);
}