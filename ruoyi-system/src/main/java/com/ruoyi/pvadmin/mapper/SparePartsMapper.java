package com.ruoyi.pvadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.pvadmin.domain.entity.SpareParts;
import com.ruoyi.pvadmin.domain.vo.SparePartsVO;

import java.util.List;


/**
 * 备品备件Mapper接口
 *
 * @author ruoyi
 * @date 2023-08-31
 */
 public interface SparePartsMapper extends BaseMapper<SpareParts> {
    /**
     * 查询备品备件
     *
     * @param id 备品备件主键
     * @return 备品备件
     */
     SpareParts selectSparePartsById(String id);

    /**
     * 查询备品备件列表
     *
     * @param spareParts 备品备件
     * @return 备品备件集合
     */
     List<SparePartsVO> selectSparePartsList(SparePartsVO spareParts);

    /**
     * 查询备品备件列表
     *
     * @param spareParts 备品备件
     * @return 备品备件集合
     */
     List<SpareParts> selectSparePartsListByParam(SparePartsVO spareParts);

    /**
     * 新增备品备件
     *
     * @param spareParts 备品备件
     * @return 结果
     */
     int insertSpareParts(SpareParts spareParts);

    /**
     * 修改备品备件
     *
     * @param spareParts 备品备件
     * @return 结果
     */
     int updateSpareParts(SpareParts spareParts);

    /**
     * 删除备品备件
     *
     * @param id 备品备件主键
     * @return 结果
     */
     int deleteSparePartsById(String id);

    /**
     * 批量删除备品备件
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
     int deleteSparePartsByIds(String[] ids);
}
