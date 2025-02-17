package com.ruoyi.pvadmin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.pvadmin.domain.dto.PowerStationQueryDTO;
import com.ruoyi.pvadmin.domain.dto.SparePartsRecordQueryDTO;
import com.ruoyi.pvadmin.domain.entity.SpareParts;
import com.ruoyi.pvadmin.domain.entity.SparePartsRecord;
import com.ruoyi.pvadmin.domain.vo.PowerStationVO;
import com.ruoyi.pvadmin.domain.vo.SparePartsVO;
import com.ruoyi.pvadmin.domain.vo.StockOperationRecordVO;
import com.ruoyi.pvadmin.mapper.PowerStationMapper;
import com.ruoyi.pvadmin.mapper.SparePartsMapper;
import com.ruoyi.pvadmin.mapper.SparePartsRecordMapper;
import com.ruoyi.pvadmin.service.ISparePartsService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 备品备件Service业务层处理
 *
 * @author ruoyi
 * @date 2023-08-31
 */
@Service
public class SparePartsServiceImpl extends ServiceImpl<SparePartsMapper, SpareParts> implements ISparePartsService {

    @Autowired
    private SparePartsMapper sparePartsMapper;

    @Autowired
    private PowerStationMapper powerStationMapper;

    @Autowired
    private SparePartsRecordMapper sparePartsRecordMapper;

    /**
     * 查询备品备件
     *
     * @param id 备品备件主键
     * @return 备品备件
     */
    @Override
    public SparePartsVO selectSparePartsById(String id) {
        SpareParts parts = sparePartsMapper.selectSparePartsById(id);
        SparePartsVO partsVO = new SparePartsVO();
        BeanUtils.copyProperties(parts, partsVO);
        return partsVO;
    }

    /**
     * 查询备品备件列表
     *
     * @param spareParts 备品备件
     * @return 备品备件
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SparePartsVO> selectSparePartsList(SparePartsVO spareParts) {
        return sparePartsMapper.selectSparePartsList(spareParts);
    }

    /**
     * 新增备品备件
     *
     * @param dto 备品备件
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int inBound(SparePartsVO dto) {
        if (dto.getMovementDate().after(new Date())) {
            throw new BaseException("入库日期不能大于今天");
        }
        SpareParts spareParts = new SpareParts();
        BeanUtils.copyProperties(dto, spareParts);
        spareParts.setLocationId(Long.parseLong(dto.getLocationId()));
        int count;
        // 新增记录表
        SparePartsRecord record = new SparePartsRecord();
        BeanUtils.copyProperties(spareParts, record);
        record.setStatus("0");
        record.setId(null);
        record.setMovementDate(dto.getMovementDate());
        sparePartsRecordMapper.insert(record);
        // 如果新增的备品备件在数据库中能查到，则更新数量
        // 如果新增的备品备件在数据库中查询不到，则新增记录并新增备件库数据
        List<SpareParts> parts = sparePartsMapper.selectSparePartsListByParam(dto);
        if (!ObjectUtils.isEmpty(parts)) {
            SpareParts parts1 = parts.get(0);
            parts1.setAmount(parts1.getAmount() + spareParts.getAmount());
            parts1.setLocationId(spareParts.getLocationId());
            parts1.setLocation(spareParts.getLocation());
            count = sparePartsMapper.updateById(parts1);
        } else {
            spareParts.setCreateTime(DateUtils.getNowDate());
            spareParts.setLocationId(spareParts.getLocationId());
            spareParts.setLocation(spareParts.getLocation());
            count = sparePartsMapper.insert(spareParts);
        }
        return count;
    }

    /**
     * 修改备品备件(出库)
     *
     * @param spareParts 备品备件
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int outBound(SparePartsVO spareParts) {
        if (StringUtils.isBlank(spareParts.getCode())) {
            throw new BaseException("缺少编号！");
        }
        if (StringUtils.isBlank(spareParts.getSpecs())) {
            throw new BaseException("缺少规格型号！");
        }
        if (StringUtils.isBlank(spareParts.getName())) {
            throw new BaseException("缺少名称！");
        }
        if (ObjectUtils.isEmpty(spareParts.getAmount()) || spareParts.getAmount() <= 0) {
            throw new BaseException("数量应该为正整数！");
        }
        if (spareParts.getMovementDate().after(new Date())) {
            throw new BaseException("入库日期不能大于今天");
        }

        List<SpareParts> parts = sparePartsMapper.selectSparePartsListByParam(spareParts);
        if (ObjectUtils.isEmpty(parts)) {
            throw new BaseException("库存未找到");
        }
        if (parts.get(0).getAmount() < spareParts.getAmount()) {
            throw new BaseException("数量不足,无法操作！");
        }
        int count = 0;
        // 新增记录表
        SparePartsRecord record = new SparePartsRecord();
        BeanUtils.copyProperties(spareParts, record);
        record.setId(null);
        record.setStatus("1");
        record.setMovementDate(spareParts.getMovementDate());
        sparePartsRecordMapper.insert(record);

        // 如果出库的备品备件在数据库中能查到，则更新数量
        // 如果新增的备品备件在数据库中查询不到，则新增记录并新增备件库数据
        if (!ObjectUtils.isEmpty(parts)) {
            SpareParts parts1 = parts.get(0);
            parts1.setAmount(parts1.getAmount() - spareParts.getAmount());
            count = sparePartsMapper.updateById(parts1);
        }
        return count;
    }

    /**
     * 编辑
     */
    @Override
    public void edit(SparePartsVO spareParts) {
        if (StringUtils.isEmpty(spareParts.getCode())) {
            throw new BaseException("缺少编号！");
        }
        if (StringUtils.isEmpty(spareParts.getSpecs())) {
            throw new BaseException("缺少规格型号！");
        }
        if (StringUtils.isEmpty(spareParts.getName())) {
            throw new BaseException("缺少名称！");
        }
        if (ObjectUtils.isEmpty(spareParts.getAmount()) || spareParts.getAmount() <= 0) {
            throw new BaseException("数量应该为正整数！");
        }
        SpareParts older = sparePartsMapper.selectSparePartsById(spareParts.getId());
        if (ObjectUtils.isEmpty(older)) {
            throw new BaseException("数据不存在，编辑失败！");
        }
        older.setCode(spareParts.getCode());
        older.setName(spareParts.getName());
        older.setSpecs(spareParts.getSpecs());
        older.setAmount(spareParts.getAmount());
        older.setRemark(spareParts.getRemark());
        older.setLocation(spareParts.getLocation());
        older.setLocationId(Long.parseLong(spareParts.getLocationId()));
        sparePartsMapper.updateSpareParts(older);
    }


    @Override
    public List<SpareParts> list(SparePartsVO spareParts) {
        return sparePartsMapper.selectSparePartsListByParam(spareParts);
    }

    /**
     * 批量删除备品备件
     *
     * @param ids 需要删除的备品备件主键
     * @return 结果
     */
    @Override
    public int deleteSparePartsByIds(String[] ids) {
        return sparePartsMapper.deleteSparePartsByIds(ids);
    }

    /**
     * 删除备品备件信息
     *
     * @param id 备品备件主键
     * @return 结果
     */
    @Override
    public int deleteSparePartsById(String id) {
        return sparePartsMapper.deleteSparePartsById(id);
    }

    /**
     * 查询出库入库操作记录列表
     *
     * @param dto 出库、入库
     * @return 结果
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<StockOperationRecordVO> listOperationRecords(SparePartsRecordQueryDTO dto) {

        List<SparePartsRecord> partsRecordList = sparePartsRecordMapper.selectList(
                Wrappers.<SparePartsRecord>lambdaQuery()
                        .eq(!StringUtils.isEmpty(dto.getOperationType()), SparePartsRecord::getStatus, dto.getOperationType())
                        .like(!StringUtils.isEmpty(dto.getName()), SparePartsRecord::getName, dto.getName())
                        .and(x -> x
                                .or().isNull(SparePartsRecord::getPowerStationId)
                                .or().eq(SparePartsRecord::getPowerStationId, ""))
                        .ge(!ObjectUtils.isEmpty(dto.getStartDate()), SparePartsRecord::getMovementDate, dto.getStartDate())
                        .le(!ObjectUtils.isEmpty(dto.getEndDate()), SparePartsRecord::getMovementDate, dto.getEndDate())
                        .orderByDesc(SparePartsRecord::getCreateTime)
        );

        if (CollectionUtils.isEmpty(partsRecordList)) {
            return Collections.emptyList();
        }

        // 查询电站
        PowerStationQueryDTO queryDto = new PowerStationQueryDTO();
        queryDto.setParams(dto.getParams());
        queryDto.setPowerStationId(dto.getPowerStationId());

        List<PowerStationVO> powerStationList = powerStationMapper.listByQueryDto(queryDto);

        Map<String, String> powerStationMap = powerStationList.stream()
                .collect(Collectors.toMap(PowerStationVO::getId, PowerStationVO::getName));

        List<StockOperationRecordVO> recordVOList = new ArrayList<>();
        for (SparePartsRecord partsRecord : partsRecordList) {
            StockOperationRecordVO operationRecordVO = new StockOperationRecordVO();
            BeanUtils.copyProperties(partsRecord, operationRecordVO);
            if (!ObjectUtils.isEmpty(partsRecord.getLocationId())) {
                operationRecordVO.setLocationId(partsRecord.getLocationId().toString());
            }
            String powerStationName = powerStationMap.get(partsRecord.getPowerStationId());
            if (StringUtils.isNotBlank(powerStationName)) {
                operationRecordVO.setPowerStationName(powerStationName);
            }

            recordVOList.add(operationRecordVO);
        }
        return recordVOList;
    }
}
