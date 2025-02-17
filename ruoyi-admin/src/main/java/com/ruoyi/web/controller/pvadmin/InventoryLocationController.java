package com.ruoyi.web.controller.pvadmin;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.pvadmin.domain.dto.InventoryLocationDTO;
import com.ruoyi.pvadmin.service.IInventoryLocationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 电站维护Controller
 */
@Api(value = "库存地点", tags = "库存地点")
@RestController
@RequestMapping("/inv-loc")
public class InventoryLocationController extends BaseController {

    @Resource
    IInventoryLocationService service;

    @GetMapping("/list")
    @ApiOperation("查询库存地点维护列表")
    public AjaxResult list() {
        return success(service.getList());
    }

    @PostMapping("/create")
    @ApiOperation("创建库存地点")
    public AjaxResult create(@RequestBody InventoryLocationDTO dto) {
        service.create(dto);
        return success();
    }

    @PutMapping("/edit")
    @ApiOperation("编辑库存地点")
    public AjaxResult edit(@RequestBody InventoryLocationDTO dto) {
        service.edit(dto);
        return success();
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除库存地点")
    public AjaxResult delete(@PathVariable("id") String id) {
        service.delete(id);
        return success();
    }
}
