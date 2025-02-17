package com.ruoyi.pvadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.pvadmin.domain.dto.DeviceInspectionQueryDTO;
import com.ruoyi.pvadmin.domain.entity.DeviceInspection;
import com.ruoyi.pvadmin.domain.vo.DeviceInspectionVO;

import java.util.List;


/**
 * 设备点检Mapper接口
 */
 public interface DeviceInspectionMapper extends BaseMapper<DeviceInspection> {
    /**
     * 查询设备点检
     *
     * @param id 设备点检主键
     * @return 设备点检
     */
     DeviceInspectionVO selectDeviceInspectionById(String id);

    /**
     * 查询设备点检列表
     *
     * @param dto 设备点检
     * @return 设备点检集合
     */
     List<DeviceInspectionVO> selectDeviceInspectionList(DeviceInspectionQueryDTO dto);

    /**
     * 新增设备点检
     *
     * @param deviceInspection 设备点检
     * @return 结果
     */
     int insertDeviceInspection(DeviceInspection deviceInspection);

    /**
     * 修改设备点检
     *
     * @param deviceInspection 设备点检
     * @return 结果
     */
     int updateDeviceInspection(DeviceInspection deviceInspection);

    /**
     * 删除设备点检
     *
     * @param id 设备点检主键
     * @return 结果
     */
     int deleteDeviceInspectionById(String id);

    /**
     * 批量删除设备点检
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
     int deleteDeviceInspectionByIds(String[] ids);
}
