package com.ruoyi.pvadmin.service;


import com.ruoyi.pvadmin.domain.dto.SparePartsRecordQueryDTO;
import com.ruoyi.pvadmin.domain.entity.SpareParts;
import com.ruoyi.pvadmin.domain.vo.SparePartsVO;
import com.ruoyi.pvadmin.domain.vo.StockOperationRecordVO;

import java.util.List;

/**
 * 备品备件Service接口
 *
 * @author ruoyi
 * @date 2023-08-31
 */
 public interface ISparePartsService {
    /**
     * 查询备品备件
     *
     * @param id 备品备件主键
     * @return 备品备件
     */
    SparePartsVO selectSparePartsById(String id);

    /**
     * 查询备品备件列表
     *
     * @param spareParts 备品备件
     * @return 备品备件集合
     */
     List<SparePartsVO> selectSparePartsList(SparePartsVO spareParts);

    /**
     * 新增备品备件
     *
     * @param spareParts 备品备件
     * @return 结果
     */
     int inBound(SparePartsVO spareParts);

    /**
     * 修改备品备件
     *
     * @param spareParts 备品备件
     * @return 结果
     */
     int outBound(SparePartsVO spareParts);

     /**
     * 编辑
     */
     void edit(SparePartsVO spareParts);

    /**
     * 查询备品备件
     * @param spareParts
     * @return
     */
     List<SpareParts> list(SparePartsVO spareParts);

    /**
     * 批量删除备品备件
     *
     * @param ids 需要删除的备品备件主键集合
     * @return 结果
     */
     int deleteSparePartsByIds(String[] ids);

    /**
     * 删除备品备件信息
     *
     * @param id 备品备件主键
     * @return 结果
     */
     int deleteSparePartsById(String id);

    /**
     * 查询出库入库操作记录列表
     *
     * @param dto 出库、入库
     * @return 结果
     */
    List<StockOperationRecordVO> listOperationRecords(SparePartsRecordQueryDTO dto);
}
