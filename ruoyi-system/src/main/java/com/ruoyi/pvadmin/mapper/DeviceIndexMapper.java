package com.ruoyi.pvadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.pvadmin.domain.dto.DeviceFactorDTO;
import com.ruoyi.pvadmin.domain.entity.DeviceIndex;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 设备与点位关系Mapper接口
 */
public interface DeviceIndexMapper extends BaseMapper<DeviceIndex> {

    /**
     * 根据电表id与点位后缀查询点位编号
     *
     * @param deviceId  电表id
     * @param indexCode 点位编号
     * @return 结果
     */
    String getIndexByIndexCode(@Param("deviceId") String deviceId, @Param("indexCode") String indexCode);

    int updateFactor(@RequestParam("request") List<DeviceFactorDTO> request);

    /**
     * 根据电表id查询电表所有点位
     *
     * @param deviceId 电表id
     * @return 结果
     */
    List<String> listByDeviceId(@Param("deviceId") String deviceId);

    /**
     * 根据设备id查询采集点位模板
     *
     * @param deviceIds 设备id集合
     * @return 结果
     */
    List<DeviceIndex> listTemplateByDeviceIds(@Param("deviceIds") List<String> deviceIds);
}
