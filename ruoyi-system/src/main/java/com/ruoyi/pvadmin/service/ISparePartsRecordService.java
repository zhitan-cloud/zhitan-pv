package com.ruoyi.pvadmin.service;

import com.ruoyi.pvadmin.domain.entity.SparePartsRecord;

import java.util.List;


/**
 * 备品备件-操作记录Service接口
 *
 * @author ruoyi
 * @date 2023-08-31
 */
 public interface ISparePartsRecordService {
    /**
     * 查询备品备件-操作记录
     *
     * @param id 备品备件-操作记录主键
     * @return 备品备件-操作记录
     */
     SparePartsRecord selectSparePartsRecordById(String id);

    /**
     * 查询备品备件-操作记录列表
     *
     * @param sparePartsRecord 备品备件-操作记录
     * @return 备品备件-操作记录集合
     */
     List<SparePartsRecord> selectSparePartsRecordList(SparePartsRecord sparePartsRecord);

    /**
     * 新增备品备件-操作记录
     *
     * @param sparePartsRecord 备品备件-操作记录
     * @return 结果
     */
     int insertSparePartsRecord(SparePartsRecord sparePartsRecord);

    /**
     * 修改备品备件-操作记录
     *
     * @param sparePartsRecord 备品备件-操作记录
     * @return 结果
     */
     int updateSparePartsRecord(SparePartsRecord sparePartsRecord);

    /**
     * 批量删除备品备件-操作记录
     *
     * @param ids 需要删除的备品备件-操作记录主键集合
     * @return 结果
     */
     int deleteSparePartsRecordByIds(String[] ids);

    /**
     * 删除备品备件-操作记录信息
     *
     * @param id 备品备件-操作记录主键
     * @return 结果
     */
     int deleteSparePartsRecordById(String id);
}
