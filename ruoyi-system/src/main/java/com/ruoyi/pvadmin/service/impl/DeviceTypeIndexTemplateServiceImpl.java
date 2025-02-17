package com.ruoyi.pvadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.pvadmin.domain.entity.DeviceTypeIndexTemplate;
import com.ruoyi.pvadmin.mapper.DeviceTypeIndexTemplateMapper;
import com.ruoyi.pvadmin.service.IDeviceTypeIndexTemplateService;
import org.springframework.stereotype.Service;

/**
 * 平台设备类型点位模板；平台预置的 指标模板，不提供租户，下面预设的采集点、计算点信息Service业务层处理
 */
@Service
public class DeviceTypeIndexTemplateServiceImpl extends ServiceImpl<DeviceTypeIndexTemplateMapper, DeviceTypeIndexTemplate> implements IDeviceTypeIndexTemplateService {

}