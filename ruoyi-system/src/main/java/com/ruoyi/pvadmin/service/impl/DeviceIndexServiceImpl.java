package com.ruoyi.pvadmin.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.pvadmin.domain.entity.DeviceIndex;
import com.ruoyi.pvadmin.mapper.DeviceIndexMapper;
import com.ruoyi.pvadmin.service.IDeviceIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 设备与点位关系Service业务层处理
 */
@Service
public class DeviceIndexServiceImpl extends ServiceImpl<DeviceIndexMapper, DeviceIndex> implements IDeviceIndexService {

    @Autowired
    private DeviceIndexMapper deviceIndexMapper;


}