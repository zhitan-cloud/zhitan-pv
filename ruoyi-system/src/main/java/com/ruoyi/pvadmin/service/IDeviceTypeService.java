package com.ruoyi.pvadmin.service;

import com.ruoyi.pvadmin.domain.dto.DeviceTypeIndexSubmitDTO;
import com.ruoyi.pvadmin.domain.dto.DeviceTypeQueryDTO;
import com.ruoyi.pvadmin.domain.dto.DeviceTypeSubmitDTO;
import com.ruoyi.pvadmin.domain.vo.DeviceTypeIndexVO;
import com.ruoyi.pvadmin.domain.vo.DeviceTypeListVO;

import java.util.List;

/**
* 设备类型Service
*/
public interface IDeviceTypeService {
    List<DeviceTypeListVO> pagedList(DeviceTypeQueryDTO request);

    List<DeviceTypeIndexVO> listIndex(String id);

    int create(DeviceTypeSubmitDTO request);

    int edit(DeviceTypeSubmitDTO request);

    void editIndex(String id,List<DeviceTypeIndexSubmitDTO> indexes);

    int delete(String id);
}
