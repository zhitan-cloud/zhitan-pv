package com.ruoyi.pvadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.pvadmin.domain.dto.InventoryLocationDTO;
import com.ruoyi.pvadmin.domain.entity.InventoryLocation;
import com.ruoyi.pvadmin.domain.vo.InventoryLocationVO;
import com.ruoyi.pvadmin.mapper.InventoryLocationMapper;
import com.ruoyi.pvadmin.service.IInventoryLocationService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryLocationServiceImpl extends ServiceImpl<InventoryLocationMapper, InventoryLocation>
        implements IInventoryLocationService {
    @Override
    public List<InventoryLocationVO> getList() {
        LambdaQueryWrapper<InventoryLocation> queryWrapper = new LambdaQueryWrapper<>();
        List<InventoryLocation> list = baseMapper.selectList(queryWrapper);
        List<InventoryLocationVO> result = new ArrayList<>();
        for (InventoryLocation inventoryLocation : list) {
            InventoryLocationVO model = new InventoryLocationVO();
            model.setId(inventoryLocation.getId().toString());
            BeanUtils.copyProperties(inventoryLocation, model);
            result.add(model);
        }
        return result;
    }

    @Override
    public void create(InventoryLocationDTO dto) {
        LambdaQueryWrapper<InventoryLocation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InventoryLocation::getLocation, dto.getLocation());
        Integer count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BaseException("库存地点已存在，创建失败！");
        }
        InventoryLocation entity = new InventoryLocation();
        BeanUtils.copyProperties(dto, entity);
        baseMapper.insert(entity);
    }

    @Override
    public void edit(InventoryLocationDTO dto) {
        LambdaQueryWrapper<InventoryLocation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InventoryLocation::getLocation, dto.getLocation());
        queryWrapper.ne(InventoryLocation::getId, dto.getId());
        Integer count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BaseException("库存地点已存在，更新失败！");
        }
        InventoryLocation older = baseMapper.selectById(dto.getId());
        if (ObjectUtils.isEmpty(older)) {
            throw new BaseException("数据不存在，更新失败！");
        }
        older.setLocation(dto.getLocation());
        older.setRemark(dto.getRemark());
        baseMapper.updateById(older);
    }

    @Override
    public void delete(String id) {
        baseMapper.deleteById(id);
    }
}
