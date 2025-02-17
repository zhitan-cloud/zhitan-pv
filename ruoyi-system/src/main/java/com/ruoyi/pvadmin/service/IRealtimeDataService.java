package com.ruoyi.pvadmin.service;

import com.ruoyi.pvadmin.domain.dto.DeviceQueryDTO;
import com.ruoyi.pvadmin.domain.dto.LoadAnalysisDTO;
import com.ruoyi.pvadmin.domain.dto.PowerFactorAnalysisDTO;
import com.ruoyi.pvadmin.domain.dto.ThreePhaseUnbalanceAnalysisDTO;
import com.ruoyi.pvadmin.domain.model.ListElectricLoadDetail;
import com.ruoyi.pvadmin.domain.model.ListElectricLoadItem;
import com.ruoyi.pvadmin.domain.model.SensorParamVO;
import com.ruoyi.pvadmin.domain.vo.ElectricPowerFactorVO;
import com.ruoyi.pvadmin.domain.vo.ElectricThreePhaseDetail;
import com.ruoyi.pvadmin.domain.vo.ElectricThreePhaseItem;

import java.util.List;

/**
 * 实时数据 service
 */
public interface IRealtimeDataService {


    /**
     * 查询负荷分析信息
     *
     * @param dto 查询条件
     * @return 结果
     */
    /**
     * 记录列表
     */
    List<ListElectricLoadItem> listLoadAnalysis(LoadAnalysisDTO dto);

    /**
     * 查询负荷分析详情
     *
     * @param dto 查询条件
     * @return 结果
     */
    ListElectricLoadDetail getLoadAnalysisDetail(LoadAnalysisDTO dto);

    /**
     * 查询三项不平衡分析子项信息
     *
     * @param dto 查询条件
     * @return 结果
     */
    List<ElectricThreePhaseItem> listThreePhaseUnbalanceAnalysis(ThreePhaseUnbalanceAnalysisDTO dto);

    /**
     * 查询三项不平衡分析详细信息
     *
     * @param dto 查询条件
     * @return 结果
     */
    ElectricThreePhaseDetail getThreePhaseUnbalanceAnalysisDetail(ThreePhaseUnbalanceAnalysisDTO dto);

    /**
     * 查询功率因数分析信息
     *
     * @param dto 查询条件
     * @return 结果
     */
    ElectricPowerFactorVO getPowerFactorAnalysis(PowerFactorAnalysisDTO dto);

    /**
     * 查询电表实时数据
     *
     * @param queryDto 电站id
     * @return 结果
     */
    List<SensorParamVO> listRealTime(DeviceQueryDTO queryDto);
}