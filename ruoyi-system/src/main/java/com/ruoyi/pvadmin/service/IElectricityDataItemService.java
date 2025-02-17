package com.ruoyi.pvadmin.service;

import com.ruoyi.pvadmin.domain.dto.HomeQueryDTO;
import com.ruoyi.pvadmin.domain.dto.PeakValleyQueryDTO;
import com.ruoyi.pvadmin.domain.vo.PeakAndValleyReportVO;
import com.ruoyi.pvadmin.domain.vo.PeakAndValleyVO;
import com.ruoyi.pvadmin.domain.vo.PeriodGenerationPercentageVO;

import java.util.List;

/**
 * 电力-峰平谷数据Service接口
 */
public interface IElectricityDataItemService {


    /**
     * 尖峰平谷时段统计
     *
     * @param dto 查询参数
     * @return 结果
     */
    PeakAndValleyVO getPeakAndValleyPieAndDataList(PeakValleyQueryDTO dto);

    /**
     * 查询峰平谷报表
     *
     * @param dto 查询条件
     * @return 结果
     */
    List<PeakAndValleyReportVO> getReport(PeakValleyQueryDTO dto);

    /**
     * 首页-查询时段发电占比
     *
     * @param dto 查询条件
     * @return 结果
     */
    List<PeriodGenerationPercentageVO> getPeriodGenerationPercentage(HomeQueryDTO dto);
}