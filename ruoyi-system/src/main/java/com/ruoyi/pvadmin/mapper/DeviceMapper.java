package com.ruoyi.pvadmin.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.pvadmin.domain.dto.DeviceQueryDTO;
import com.ruoyi.pvadmin.domain.dto.HomeQueryDTO;
import com.ruoyi.pvadmin.domain.entity.Device;
import com.ruoyi.pvadmin.domain.vo.DeviceVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 设备管理Mapper接口
 *
 * @author ruoyi
 * @date 2023-08-24
 */
public interface DeviceMapper extends BaseMapper<Device> {

    /**
     * 删除设备管理
     *
     * @param id 设备管理主键
     * @return 结果
     */
    public int deleteDeviceById(String id);

    /**
     * 根据传入的电站id查询设备信息，若没传入则查询所有
     *
     * @param powerStationId 电站id
     * @return 结果
     */
    List<String> listByPowerStationId(@Param("powerStationId") String powerStationId);

    /**
     * 根据传入的设备id集合查询设备信息，若没传入则查询所有
     *
     * @param deviceId 电站id
     * @return 结果
     */
    List<Device> listDeviceById(@Param("deviceId") String deviceId);

    /**
     * 根据传入电站id集合查询电表信息
     *
     * @param powerStationId 电站id
     * @return 结果
     */
    List<Device> listAmmeterByPowerStationId(@Param("powerStationId") String powerStationId);

    /**
     * 根据传入电站id集合查询下边的设备信息
     *
     * @param powerStationId 电站id
     * @return 结果
     */
    List<Device> listAllDeviceByStationId(@Param("powerStationId") String powerStationId);

    /**
     * 根据传入的电站id查询设备信息，若没传入则查询所有
     *
     * @param powerStationIds 电站id集合
     * @return 结果
     */
    List<Device> listByPowerStationIds(@Param("powerStationIds") List<String> powerStationIds);

    /**
     * 通过电站id查询下面的设备信息
     *
     * @param id 电站id
     * @return 结果
     */
    List<Device> listInforByPowerStationId(String id);

    /**
     * 查询列表
     *
     * @param dto 查询条件
     * @return 结果
     */
    List<DeviceVO> selectDeviceList(DeviceQueryDTO dto);

    /**
     * 权限-查询设备点位
     *
     * @param dto 查询条件
     * @return 结果
     */
    List<String> listDeviceId(DeviceQueryDTO dto);

    /**
     * 根据传入的电站id查询设备信息，若没传入则查询所有
     *
     * @param dto 查询条件
     * @return 结果
     */
    List<String> listHomeDeviceIdByPowerStationId(HomeQueryDTO dto);

    /**
     * 权限-查询设备编号
     *
     * @param dto 查询条件
     * @return 结果
     */
    List<String> listDeviceCode(DeviceQueryDTO dto);

    /**
     * 根据权限查询设备信息
     *
     * @param deviceId id
     * @param params   权限
     * @return 结果
     */
    List<Device> listByPermissions(@Param("deviceId") String deviceId,
                                   @Param("ammeter") Integer ammeter,
                                   @Param("params") Map<String, Object> params);

    /**
     * 根据权限查询设备信息
     *
     * @param powerStationId 电站id
     * @param params         权限
     * @return 结果
     */
    List<String> listDeviceIdsByPowerStationId(@Param("powerStationId") String powerStationId,
                                               @Param("params") Map<String, Object> params);


    /**
     * 根据权限查询设备编号信息
     *
     * @param params 权限
     * @return 结果
     */
    List<String> listCodeByPermissions(@Param("params") Map<String, Object> params);

}