package com.ruoyi.pvadmin.service;


import com.ruoyi.pvadmin.domain.dto.DeviceInspectionQueryDTO;
import com.ruoyi.pvadmin.domain.dto.DeviceInspectionSubmitDTO;
import com.ruoyi.pvadmin.domain.vo.DeviceInspectionExportVO;
import com.ruoyi.pvadmin.domain.vo.DeviceInspectionVO;

import java.util.List;

/**
 * 设备点检Service接口
 */
public interface IDeviceInspectionService {
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
     * @param dto 设备点检
     * @return 结果
     */
    int insertDeviceInspection(DeviceInspectionSubmitDTO dto);

    /**
     * 修改设备点检
     *
     * @param dto 设备点检
     * @return 结果
     */
    int updateDeviceInspection(DeviceInspectionSubmitDTO dto);

    /**
     * 删除设备点检信息
     *
     * @param id 设备点检主键
     * @return 结果
     */
    int deleteDeviceInspectionById(String id);

    /**
    * 导出列表
    */
    List<DeviceInspectionExportVO> exportList(DeviceInspectionQueryDTO dto);
}
