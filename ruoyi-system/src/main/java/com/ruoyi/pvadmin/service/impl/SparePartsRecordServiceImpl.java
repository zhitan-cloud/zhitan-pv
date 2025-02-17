package com.ruoyi.pvadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.pvadmin.domain.entity.SparePartsRecord;
import com.ruoyi.pvadmin.mapper.SparePartsRecordMapper;
import com.ruoyi.pvadmin.service.ISparePartsRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 备品备件-操作记录Service业务层处理
 *
 * @author ruoyi
 * @date 2023-08-31
 */
@Service
public class SparePartsRecordServiceImpl extends ServiceImpl<SparePartsRecordMapper, SparePartsRecord>
        implements ISparePartsRecordService {
    @Autowired
    private SparePartsRecordMapper sparePartsRecordMapper;

    /**
     * 查询备品备件-操作记录
     *
     * @param id 备品备件-操作记录主键
     * @return 备品备件-操作记录
     */
    @Override
    public SparePartsRecord selectSparePartsRecordById(String id) {
        return sparePartsRecordMapper.selectSparePartsRecordById(id);
    }

    /**
     * 查询备品备件-操作记录列表
     *
     * @param sparePartsRecord 备品备件-操作记录
     * @return 备品备件-操作记录
     */
    @Override
    public List<SparePartsRecord> selectSparePartsRecordList(SparePartsRecord sparePartsRecord) {
        return sparePartsRecordMapper.selectSparePartsRecordList(sparePartsRecord);
    }

    /**
     * 新增备品备件-操作记录
     *
     * @param sparePartsRecord 备品备件-操作记录
     * @return 结果
     */
    @Override
    public int insertSparePartsRecord(SparePartsRecord sparePartsRecord) {
        sparePartsRecord.setCreateTime(DateUtils.getNowDate());
        return sparePartsRecordMapper.insertSparePartsRecord(sparePartsRecord);
    }

    /**
     * 修改备品备件-操作记录
     *
     * @param sparePartsRecord 备品备件-操作记录
     * @return 结果
     */
    @Override
    public int updateSparePartsRecord(SparePartsRecord sparePartsRecord) {
        sparePartsRecord.setUpdateTime(DateUtils.getNowDate());
        return sparePartsRecordMapper.updateSparePartsRecord(sparePartsRecord);
    }

    /**
     * 批量删除备品备件-操作记录
     *
     * @param ids 需要删除的备品备件-操作记录主键
     * @return 结果
     */
    @Override
    public int deleteSparePartsRecordByIds(String[] ids) {
        return sparePartsRecordMapper.deleteSparePartsRecordByIds(ids);
    }

    /**
     * 删除备品备件-操作记录信息
     *
     * @param id 备品备件-操作记录主键
     * @return 结果
     */
    @Override
    public int deleteSparePartsRecordById(String id) {
        return sparePartsRecordMapper.deleteSparePartsRecordById(id);
    }
}
