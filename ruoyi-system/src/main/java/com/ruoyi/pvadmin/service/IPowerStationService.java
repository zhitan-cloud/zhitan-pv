package com.ruoyi.pvadmin.service;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.enums.TimeTypeEnum;
import com.ruoyi.pvadmin.domain.dto.GenerationStatisticsDTO;
import com.ruoyi.pvadmin.domain.dto.PowerStationQueryDTO;
import com.ruoyi.pvadmin.domain.dto.PowerStationSubmitDTO;
import com.ruoyi.pvadmin.domain.model.PowerGenerationTrendModel;
import com.ruoyi.pvadmin.domain.vo.*;

import java.util.List;

/**
 * 电站维护Service接口
 */
public interface IPowerStationService {
    /**
     * 查询电站维护
     *
     * @param id 电站维护主键
     * @return 电站维护
     */
    public PowerStationVO selectPowerStationById(String id);

    /**
     * 查询电站维护列表
     *
     * @param dto 电站维护
     * @return 电站维护集合
     */
    public List<PowerStationVO> selectPowerStationList(PowerStationQueryDTO dto);

    /**
     * 新增电站维护
     *
     * @param powerStation 电站维护
     * @return 结果
     */
    public int insertPowerStation(PowerStationSubmitDTO powerStation);

    /**
     * 修改电站维护
     *
     * @param dto 电站维护
     * @return 结果
     */
    public int updatePowerStation(PowerStationSubmitDTO dto);

    /**
     * 批量删除电站维护
     *
     * @param id 需要删除的电站维护主键集合
     * @return 结果
     */
    public int deletePowerStationByIds(String id);

    /**
     * 电站发电统计
     *
     * @param dto 查询参数
     * @return 结果
     */
    List<GenerationStatisticsVO> listGenerationStatistics(GenerationStatisticsDTO dto);

    /**
     * 电站发电统计
     *
     * @return 结果
     */
    List<PowerStationRankVO> listPowerStationRank();

    /**
     * 首页-电站信息
     *
     * @return 结果
     */
    HomePowerStationInfoVO getHomePowerStationInfo(BaseEntity entity);

    /**
     * 电站状态-根据id查询电站信息
     *
     * @param id 电站id
     * @return 结果
     */
    PowerStationInfoVO getPowerStationInfoById(String id);

    /**
     * 根据电站id查询发电信息、收益信息
     *
     * @param id 电站id
     * @return 结果
     */
    PowerGenerationInfoVO getPowerGenerationInfo(String id);

    /**
     * 根据设备id获取发电趋势信息
     *
     * @param id       电站id
     * @param timeType 时间类型
     * @return 结果
     */
    List<PowerGenerationTrendModel> getImplementedPower(String id, TimeTypeEnum timeType);

    /**
     * 根据电站id获取设备信息
     *
     * @param id 电站id
     * @return 结果
     */
    PowerStationDeviceVO listDeviceById(String id);
}