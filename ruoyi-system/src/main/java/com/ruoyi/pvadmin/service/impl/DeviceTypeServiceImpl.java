package com.ruoyi.pvadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.pvadmin.domain.dto.DeviceTypeIndexSubmitDTO;
import com.ruoyi.pvadmin.domain.dto.DeviceTypeQueryDTO;
import com.ruoyi.pvadmin.domain.dto.DeviceTypeSubmitDTO;
import com.ruoyi.pvadmin.domain.entity.Device;
import com.ruoyi.pvadmin.domain.entity.DeviceType;
import com.ruoyi.pvadmin.domain.entity.DeviceTypeIndexTemplate;
import com.ruoyi.pvadmin.domain.vo.DeviceTypeIndexVO;
import com.ruoyi.pvadmin.domain.vo.DeviceTypeListVO;
import com.ruoyi.pvadmin.mapper.DeviceMapper;
import com.ruoyi.pvadmin.mapper.DeviceTypeIndexTemplateMapper;
import com.ruoyi.pvadmin.mapper.DeviceTypeMapper;
import com.ruoyi.pvadmin.service.IDeviceTypeService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 设备类型的处理层
 */
@Service
public class DeviceTypeServiceImpl extends ServiceImpl<DeviceTypeMapper, DeviceType> implements IDeviceTypeService {

    @Resource
    private DeviceTypeIndexTemplateMapper templateMapper;

    @Resource
    private DeviceMapper deviceMapper;

    /**
     * 分页
     */
    @Override
    public List<DeviceTypeListVO> pagedList(DeviceTypeQueryDTO request) {
        List<DeviceType> list = baseMapper.selectList(
                Wrappers.<DeviceType>lambdaQuery()
                        .select(DeviceType::getId, DeviceType::getName, DeviceType::getDescription,
                                DeviceType::getCreateTime, DeviceType::getCreateBy,
                                DeviceType::getUpdateTime, DeviceType::getUpdateBy)
                        .like(StringUtils.isNotBlank(request.getName()), DeviceType::getName, request.getName())
                        .orderByDesc(DeviceType::getCreateTime)
        );
        List<DeviceTypeListVO> result = new ArrayList<>();
        list.forEach(m ->
        {
            DeviceTypeListVO model = new DeviceTypeListVO();
            BeanUtils.copyBeanProp(model, m);
            result.add(model);
        });
        return result;
    }

    /**
     * 二级列表获取index
     */
    @Override
    public List<DeviceTypeIndexVO> listIndex(String id) {
        if (StringUtils.isBlank(id)) {
            throw new ServiceException("设备类型不存在");
        }
        List<DeviceTypeIndexVO> result = new ArrayList<>();
        LambdaQueryWrapper<DeviceTypeIndexTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeviceTypeIndexTemplate::getDeviceTypeId, id);
        List<DeviceTypeIndexTemplate> list = templateMapper.selectList(queryWrapper);
        list.forEach(m ->
        {
            DeviceTypeIndexVO model = new DeviceTypeIndexVO();
            BeanUtils.copyBeanProp(model, m);
            result.add(model);
        });
        return result;
    }

    /**
     * 创建类型
     */
    @Override
    public int create(DeviceTypeSubmitDTO request) {
        LambdaQueryWrapper<DeviceType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeviceType::getName, request.getName());
        long count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new ServiceException("类型名称重复:" + request.getName());
        }
        DeviceType entity = new DeviceType();
        entity.setId(IdWorker.getIdStr());
        entity.setDescription(request.getDescription());
        entity.setName(request.getName());
        return baseMapper.insert(entity);
    }

    /**
     * 编辑类型
     */
    @Override
    public int edit(DeviceTypeSubmitDTO request) {
        if (StringUtils.isBlank(request.getId())) {
            throw new ServiceException("数据不存在");
        }
        LambdaQueryWrapper<DeviceType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(DeviceType::getId, request.getId())
                .eq(DeviceType::getName, request.getName());
        long count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new ServiceException("修改后类型名称重复:" + request.getName());
        }
        DeviceType entity = new DeviceType();
        BeanUtils.copyBeanProp(entity, request);
        return baseMapper.updateById(entity);
    }

    /**
     * 新增编辑指标
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editIndex(String id, List<DeviceTypeIndexSubmitDTO> indexes) {
        DeviceType deviceType = baseMapper.selectById(id);
        if (ObjectUtils.isEmpty(deviceType)) {
            throw new ServiceException("设备类型不存在，更新点位模板失败");
        }
        // 本次没有添加的点位，则直接删除数据库的数据
        if (CollectionUtils.isEmpty(indexes)) {
            templateMapper.delete(Wrappers.<DeviceTypeIndexTemplate>lambdaQuery().eq(DeviceTypeIndexTemplate::getDeviceTypeId, id));
            return;
        }
        // 查询旧数据
        List<DeviceTypeIndexTemplate> templateModelList = templateMapper.selectList(
                Wrappers.<DeviceTypeIndexTemplate>lambdaQuery()
                        .eq(DeviceTypeIndexTemplate::getDeviceTypeId, id)
        );

        Map<String, DeviceTypeIndexTemplate> templateMap = templateModelList.stream()
                .collect(Collectors.toMap(DeviceTypeIndexTemplate::getCode, prod -> prod));

        Map<String, DeviceTypeIndexSubmitDTO> submitMap = indexes.stream()
                .collect(Collectors.toMap(DeviceTypeIndexSubmitDTO::getCode, prod -> prod));

        // 寻找需要新增的数据
        for (DeviceTypeIndexSubmitDTO dto : indexes) {
            DeviceTypeIndexTemplate template = templateMap.get(dto.getCode());
            // 存在，则更新
            if (ObjectUtils.isNotEmpty(template)) {
                BeanUtils.copyBeanProp(template, dto);
                templateMapper.updateById(template);
            } else {
                // 否则，则新增
                DeviceTypeIndexTemplate model = new DeviceTypeIndexTemplate();
                BeanUtils.copyBeanProp(model, template);
                model.setId(IdWorker.getIdStr());
                model.setDeviceTypeId(id);
                model.setDeviceTypeName(deviceType.getName());

                templateMapper.insert(model);
            }
        }

        // 遍历新增活删除
        if (CollectionUtils.isEmpty(templateModelList)) {
            return;
        }
        List<String> delIdList = new ArrayList<>();
        for (DeviceTypeIndexTemplate template : templateModelList) {

            DeviceTypeIndexSubmitDTO dto = submitMap.get(template.getCode());
            if (ObjectUtils.isEmpty(dto)) {
                delIdList.add(template.getId());
            }
        }

        if (CollectionUtils.isNotEmpty(delIdList)) {
            templateMapper.delete(Wrappers.<DeviceTypeIndexTemplate>lambdaQuery().in(DeviceTypeIndexTemplate::getId, delIdList));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(String id) {
        DeviceType deviceType = baseMapper.selectById(id);
        if (ObjectUtils.isEmpty(deviceType)) {
            throw new ServiceException("类型数据不存在，请刷新后重试");
        }
        LambdaQueryWrapper<Device> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Device::getDeviceTypeId, id);
        long existCount = deviceMapper.selectCount(queryWrapper);
        if (existCount > 0) {
            throw new ServiceException("设备类型已被绑定使用，请解绑后重试");
        }
        LambdaQueryWrapper<DeviceTypeIndexTemplate> deleteTemplate = new LambdaQueryWrapper<>();
        deleteTemplate.eq(DeviceTypeIndexTemplate::getDeviceTypeId, id);
        templateMapper.delete(deleteTemplate);
        return baseMapper.deleteById(id);
    }
}
