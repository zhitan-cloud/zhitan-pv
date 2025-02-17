package com.ruoyi.pvadmin.service;


import com.ruoyi.common.enums.TimeTypeEnum;
import com.ruoyi.pvadmin.domain.dto.DeviceFactorDTO;
import com.ruoyi.pvadmin.domain.dto.DeviceGenerationStatsDTO;
import com.ruoyi.pvadmin.domain.dto.DeviceQueryDTO;
import com.ruoyi.pvadmin.domain.dto.DeviceSubmitDTO;
import com.ruoyi.pvadmin.domain.vo.*;

import java.util.List;

/**
 * 设备管理Service接口
 */
public interface IDeviceService {
    /**
     * 查询设备管理
     *
     * @param id 设备管理主键
     * @return 设备管理
     */
    DeviceVO selectDeviceById(String id);

    /**
     * 查询设备管理列表
     *
     * @param device 设备管理
     * @return 设备管理集合
     */
    List<DeviceVO> selectDeviceList(DeviceQueryDTO device);

    /**
     * 新增设备管理
     *
     * @param device 设备管理
     * @return 结果
     */
    String insertDevice(DeviceSubmitDTO device);

    /**
     * 修改设备管理
     *
     * @param dto 设备管理
     * @return 结果
     */
    String updateDevice(DeviceSubmitDTO dto);

    /**
     * 删除设备管理信息
     *
     * @param id 设备管理主键
     * @return 结果
     */
    int deleteDeviceById(String id);

    /**
     * 获取设备点位
     *
     * @param id 设备id
     */
    List<DeviceIndexVO> getDeviceIndex(String id);

    /**
     * 编辑倍率
     *
     * @param request 请求实体
     */
    void updateFactor(List<DeviceFactorDTO> request);

    /**
     * 根据设备id查询发电信息、收益信息
     *
     * @param id 设备id
     * @return 结果
     */
    PowerGenerationInfoVO getPowerGenerationInfo(String id);

    /**
     * 根据设备id获取发电趋势信息
     *
     * @param id       设备id
     * @param timeType 时间类型
     * @return 结果
     */
    ImplementedPowerVO getImplementedPower(String id, TimeTypeEnum timeType);

    /**
     * 查询逆变器发电统计
     *
     * @param dto 查询条件
     * @return 结果
     */
    List<DeviceGenerationStatsVO> getInverterGenerationStats(DeviceGenerationStatsDTO dto);

    /**
     * 根据设备id获取交流测信息
     *
     * @param id 设备id
     * @return 结果
     */
    ACMeasurementsVO getACMeasurementsByDeviceId(String id);

    /**
     * 根据设备id查询逆变器信息
     *
     * @param id 逆变器id
     * @return 结果
     */
    InverterInfoVO getInverterInfo(String id);

    /**
     * 根据设备id获取点检与维修信息
     *
     * @param id 设备id
     * @return 结果
     */
    List<DeviceInspectionInfoVO> listDeviceInspectionByDeviceId(String id);
}
