package com.ruoyi.web.controller.pvadmin;


import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.pvadmin.domain.dto.ElectricityTypeSettingItemSubmitDTO;
import com.ruoyi.pvadmin.domain.dto.ElectricityTypeSettingQueryDTO;
import com.ruoyi.pvadmin.domain.dto.ElectricityTypeSettingSubmitDTO;
import com.ruoyi.pvadmin.domain.vo.ElectricityTypeSettingVO;
import com.ruoyi.pvadmin.service.IElectricityTypeSettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 峰平谷配置Controller
 */
@RestController
@RequestMapping("/electricityTypeSetting")
@Api(value = "峰平谷配置管理", tags = "峰平谷配置管理")
public class ElectricityTypeSettingController extends BaseController {

    @Autowired
    private IElectricityTypeSettingService electricityTypeSettingService;

    /**
     * 查询峰平谷配置列表
     */
    @GetMapping("/list")
    @ApiOperation("查询峰平谷配置列表")
    public TableDataInfo list(ElectricityTypeSettingQueryDTO dto) {
        startPage();
        List<ElectricityTypeSettingVO> list = electricityTypeSettingService.selectElectricityTypeSettingList(dto);
        return getDataTable(list);
    }

    /**
     * 获取峰平谷配置详细信息
     */
    @GetMapping(value = "/{id}")
    @ApiOperation("获取峰平谷配置详细信息")
    public AjaxResult getInfo(@PathVariable("id") String id) {

        if (StringUtils.isBlank(id)) {
            return AjaxResult.success(new ElectricityTypeSettingVO());
        }
        return success(electricityTypeSettingService.selectElectricityTypeSettingById(id));
    }

    /**
     * 新增峰平谷配置
     */
    @PostMapping
    @ApiOperation("新增峰平谷配置")
    @Log(title = "峰平谷配置", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('operations:electricityTypeSetting:add')")
    public AjaxResult add(@Validated @RequestBody ElectricityTypeSettingSubmitDTO dto) {
        String msg = electricityTypeSettingService.insertElectricityTypeSetting(dto);
        if (StringUtils.isNotBlank(msg)) {
            return AjaxResult.error(msg);
        }

        return AjaxResult.success();
    }

    /**
     * 修改峰平谷配置
     */
    @PutMapping
    @ApiOperation("修改峰平谷配置")
    @Log(title = "峰平谷配置", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('operations:electricityTypeSetting:edit')")
    public AjaxResult edit(@RequestBody ElectricityTypeSettingSubmitDTO dto) {
        if (StringUtils.isBlank(dto.getId())) {
            return AjaxResult.success();
        }
        String msg = electricityTypeSettingService.updateElectricityTypeSetting(dto);
        if (StringUtils.isNotBlank(msg)) {
            return AjaxResult.error(msg);
        }

        return AjaxResult.success();
    }

    /**
     * 删除峰平谷配置
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除峰平谷配置")
    @Log(title = "删除峰平谷配置", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('operations:electricityTypeSetting:delete')")
    public AjaxResult remove(@PathVariable String id) {
        return toAjax(electricityTypeSettingService.deleteElectricityTypeSettingById(id));
    }

    /**
     * 新增峰平谷子项配置
     */
    @PostMapping("/addItem")
    @ApiOperation("新增峰平谷子项配置")
    @Log(title = "新增峰平谷子项配置", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('operations:electricityTypeSetting:add')")
    public AjaxResult addItem(@Validated @RequestBody List<ElectricityTypeSettingItemSubmitDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return AjaxResult.success();
        }
        String msg = electricityTypeSettingService.insertElectricityTypeItemSetting(dtoList);
        if (StringUtils.isNotBlank(msg)) {
            return AjaxResult.error(msg);
        }

        return AjaxResult.success();
    }

    /**
     * 根据主项id查询峰平谷子项配置
     */
    @GetMapping("/listBySettingId/{settingId}")
    @ApiOperation("根据主项id查询峰平谷子项配置")
    @Log(title = "根据主项id查询峰平谷子项配置", businessType = BusinessType.INSERT)
    public AjaxResult listBySettingId(@PathVariable String settingId) {
        if (StringUtils.isBlank(settingId)) {
            return AjaxResult.success(new ArrayList<>());
        }

        return AjaxResult.success(electricityTypeSettingService.listBySettingId(settingId));
    }

}