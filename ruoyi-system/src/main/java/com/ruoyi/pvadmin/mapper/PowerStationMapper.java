package com.ruoyi.pvadmin.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.pvadmin.domain.dto.PowerStationQueryDTO;
import com.ruoyi.pvadmin.domain.entity.PowerStation;
import com.ruoyi.pvadmin.domain.vo.PowerStationVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 电站维护Mapper接口
 */
public interface PowerStationMapper extends BaseMapper<PowerStation> {
    /**
     * 查询电站维护
     *
     * @param id 电站维护主键
     * @return 电站维护
     */
    public List<PowerStation> selectPowerStationById(String id);

    /**
     * 查询电站维护列表
     *
     * @param powerStation 电站维护
     * @return 电站维护集合
     */
    public List<PowerStation> selectPowerStationList(PowerStation powerStation);

    /**
     * 新增电站维护
     *
     * @param powerStation 电站维护
     * @return 结果
     */
    public int insertPowerStation(PowerStation powerStation);

    /**
     * 修改电站维护
     *
     * @param powerStation 电站维护
     * @return 结果
     */
    public int updatePowerStation(PowerStation powerStation);

    /**
     * 删除电站维护
     *
     * @param id 电站维护主键
     * @return 结果
     */
    public int deletePowerStationById(String id);

    /**
     * 批量删除电站维护
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePowerStationByIds(String[] ids);

    /**
     * 根据传入id查询电站信息
     *
     * @param id 电站id
     * @return 结果
     */
    PowerStation listEnergyAnalysisById(@Param("id") String id);

    /**
     * 根据id查询电站信息
     *
     * @param id 电站id
     * @return 结果
     */
    PowerStation selectInfoByid(String id);

    /**
     * 根据查询条件查询电站列表
     *
     * @param dto 查询条件
     * @return 结果
     */
    List<PowerStationVO> listByQueryDto(PowerStationQueryDTO dto);

    /**
     * 根据查询条件查询电站列表
     *
     * @param powerStationId 电站id
     * @param params         权限
     * @return 结果
     */
    List<PowerStation> listByPermissions(@Param("id") String powerStationId,
                                         @Param("params") Map<String, Object> params);
}
