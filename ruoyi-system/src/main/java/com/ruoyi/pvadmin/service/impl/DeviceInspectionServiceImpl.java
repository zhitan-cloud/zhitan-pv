package com.ruoyi.pvadmin.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateTimeUtil;
import com.ruoyi.pvadmin.domain.dto.DeviceInspectionQueryDTO;
import com.ruoyi.pvadmin.domain.dto.DeviceInspectionSubmitDTO;
import com.ruoyi.pvadmin.domain.entity.DeviceInspection;
import com.ruoyi.pvadmin.domain.vo.DeviceInspectionExportVO;
import com.ruoyi.pvadmin.domain.vo.DeviceInspectionVO;
import com.ruoyi.pvadmin.mapper.DeviceInspectionMapper;
import com.ruoyi.pvadmin.service.IDeviceInspectionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备点检Service业务层处理
 */
@Service
public class DeviceInspectionServiceImpl extends ServiceImpl<DeviceInspectionMapper, DeviceInspection> implements IDeviceInspectionService {

    @Autowired
    private DeviceInspectionMapper deviceInspectionMapper;

    /**
     * 查询设备点检
     *
     * @param id 设备点检主键
     * @return 设备点检
     */
    @Override
    public DeviceInspectionVO selectDeviceInspectionById(String id) {
        return deviceInspectionMapper.selectDeviceInspectionById(id);
    }

    /**
     * 查询设备点检列表
     *
     * @param dto 设备点检
     * @return 设备点检
     */
    @Override
    @DataScope(deptAlias = "p")
    public List<DeviceInspectionVO> selectDeviceInspectionList(DeviceInspectionQueryDTO dto) {
        List<DeviceInspectionVO> result = baseMapper.selectDeviceInspectionList(dto);
        for (DeviceInspectionVO vo : result) {
            vo.setDowntime(DateTimeUtil.dateHourDiff(vo.getInspectionStartTime(), vo.getInspectionEndTime()));
        }
        return result;
    }

    /**
     * 新增设备点检
     *
     * @param dto 设备点检
     * @return 结果
     */
    @Override
    public int insertDeviceInspection(DeviceInspectionSubmitDTO dto) {

        DeviceInspection deviceInspection = new DeviceInspection();

        BeanUtils.copyProperties(dto, deviceInspection);

        return deviceInspectionMapper.insert(deviceInspection);
    }

    /**
     * 修改设备点检
     *
     * @param dto 设备点检
     * @return 结果
     */
    @Override
    public int updateDeviceInspection(DeviceInspectionSubmitDTO dto) {

        DeviceInspection deviceInspection = new DeviceInspection();

        BeanUtils.copyProperties(dto, deviceInspection);

        return deviceInspectionMapper.updateById(deviceInspection);
    }

    /**
     * 删除设备点检信息
     *
     * @param id 设备点检主键
     * @return 结果
     */
    @Override
    public int deleteDeviceInspectionById(String id) {
        return deviceInspectionMapper.deleteDeviceInspectionById(id);
    }

    @Override
    public List<DeviceInspectionExportVO> exportList(DeviceInspectionQueryDTO dto) {
        List<DeviceInspectionExportVO> result = new ArrayList<>();
        List<DeviceInspectionVO> list = baseMapper.selectDeviceInspectionList(dto);
        for (DeviceInspectionVO vo : list) {
            DeviceInspectionExportVO model = new DeviceInspectionExportVO();
            BeanUtils.copyProperties(vo, model);
            model.setDowntime(DateTimeUtil.dateHourDiff(vo.getInspectionStartTime(), vo.getInspectionEndTime()));
            result.add(model);
        }
        return result;
    }
}
