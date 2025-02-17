package com.ruoyi.web.controller.pvadmin;


import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.pvadmin.domain.dto.DeviceInspectionQueryDTO;
import com.ruoyi.pvadmin.domain.dto.DeviceInspectionSubmitDTO;
import com.ruoyi.pvadmin.domain.vo.DeviceInspectionExportVO;
import com.ruoyi.pvadmin.domain.vo.DeviceInspectionVO;
import com.ruoyi.pvadmin.domain.vo.SparePartsVO;
import com.ruoyi.pvadmin.service.IDeviceInspectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 设备点检Controller
 */
@Api(value = "设备点检管理", tags = "设备点检管理")
@RestController
@RequestMapping("/inspection")
public class DeviceInspectionController extends BaseController {

    @Autowired
    private IDeviceInspectionService deviceInspectionService;

    /**
     * 查询设备点检列表
     */
    @GetMapping("/list")
    @ApiOperation("查询设备点检列表")
    public TableDataInfo list(DeviceInspectionQueryDTO dto) {
        startPage();
        List<DeviceInspectionVO> list = deviceInspectionService.selectDeviceInspectionList(dto);
        return getDataTable(list);
    }

    /**
     * 获取设备点检详细信息
     */
    @GetMapping(value = "/{id}")
    @ApiOperation("获取设备点检详细信息")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return success(deviceInspectionService.selectDeviceInspectionById(id));
    }

    /**
     * 新增设备点检
     */
    @PostMapping
    @ApiOperation("新增设备点检")
    @Log(title = "设备点检", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated @RequestBody DeviceInspectionSubmitDTO dto) {
        return toAjax(deviceInspectionService.insertDeviceInspection(dto));
    }

    /**
     * 修改设备点检
     */
    @PutMapping
    @ApiOperation("修改设备点检")
    @Log(title = "设备点检", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated @RequestBody DeviceInspectionSubmitDTO dto) {
        if (StringUtils.isBlank(dto.getId())) {
            return AjaxResult.error("信息不存在");
        }
        return toAjax(deviceInspectionService.updateDeviceInspection(dto));
    }

    /**
     * 删除设备点检
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除设备点检")
    @Log(title = "设备点检", businessType = BusinessType.DELETE)
    public AjaxResult remove(@PathVariable String id) {
        return toAjax(deviceInspectionService.deleteDeviceInspectionById(id));
    }

    /**
     * 导出备品备件列表
     */
    @Log(title = "点检", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public void export(HttpServletResponse response, DeviceInspectionQueryDTO dto) {
        List<DeviceInspectionExportVO> list = deviceInspectionService.exportList(dto);
        ExcelUtil<DeviceInspectionExportVO> util = new ExcelUtil<>(DeviceInspectionExportVO.class);
        util.exportExcel(response, list, "点检数据");
    }
}