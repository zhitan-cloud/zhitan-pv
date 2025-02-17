package com.ruoyi.pvadmin.service;

import com.ruoyi.pvadmin.domain.dto.InventoryLocationDTO;
import com.ruoyi.pvadmin.domain.vo.InventoryLocationVO;

import java.util.List;

public interface IInventoryLocationService {
    List<InventoryLocationVO> getList();

    void create(InventoryLocationDTO dto);

    void edit(InventoryLocationDTO dto);

    void delete(String id);
}
