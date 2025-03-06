package com.ruoyi.web.controller.pvadmin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.page.PageMethod;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.TimeTypeEnum;
import com.ruoyi.pvadmin.domain.dto.DeviceFactorDTO;
import com.ruoyi.pvadmin.domain.dto.DeviceGenerationStatsDTO;
import com.ruoyi.pvadmin.domain.dto.DeviceQueryDTO;
import com.ruoyi.pvadmin.domain.dto.DeviceSubmitDTO;
import com.ruoyi.pvadmin.domain.vo.DeviceGenerationStatsVO;
import com.ruoyi.pvadmin.domain.vo.DeviceIndexVO;
import com.ruoyi.pvadmin.domain.vo.DeviceVO;
import com.ruoyi.pvadmin.service.IDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备管理Controller
 */
@Api(value = "设备管理", tags = "设备管理")
@RestController
@RequestMapping("/device")
public class DeviceController extends BaseController {

    @Autowired
    private IDeviceService deviceService;

    /**
     * 查询设备管理列表
     */
    @GetMapping("/list")
    @ApiOperation("查询设备管理列表")
    public TableDataInfo list(DeviceQueryDTO dto) {
        startPage();
        List<DeviceVO> list = deviceService.selectDeviceList(dto);
        return getDataTable(list);
    }

    /**
     * 查询逆变器发电统计
     */
    @GetMapping("/getInverterGenerationStats")
    @ApiOperation("查询逆变器发电统计")
    public TableDataInfo getInverterGenerationStats(@Validated DeviceGenerationStatsDTO dto) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<DeviceGenerationStatsVO> page = PageMethod.startPage(pageDomain.getPageNum(), pageDomain.getPageSize());

        List<DeviceGenerationStatsVO> generationStatsVOList = deviceService.getInverterGenerationStats(dto);

        TableDataInfo dataTable = getDataTable(generationStatsVOList);
        dataTable.setTotal(page.getTotal());
        return dataTable;
    }

    /**
     * 获取设备管理详细信息
     */
    @GetMapping(value = "/{id}")
    @ApiOperation("获取设备管理详细信息")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        if (StringUtils.isBlank(id)) {
            return AjaxResult.success(new DeviceVO());
        }

        return success(deviceService.selectDeviceById(id));
    }

    /**
     * 新增设备管理
     */
    @PostMapping
    @ApiOperation("新增设备管理")
    @Log(title = "设备管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('operations:device:add')")
    public AjaxResult add(@Validated @RequestBody DeviceSubmitDTO dto) {

        String msg = deviceService.insertDevice(dto);

        if (StringUtils.isNotBlank(msg)) {
            return AjaxResult.error(msg);
        }
        return AjaxResult.success();
    }

    /**
     * 修改设备管理
     */
    @PutMapping
    @ApiOperation("修改设备管理")
    @Log(title = "设备管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('operations:device:edit')")
    public AjaxResult edit(@Validated @RequestBody DeviceSubmitDTO dto) {
        String msg = deviceService.updateDevice(dto);

        if (StringUtils.isNotBlank(msg)) {
            return AjaxResult.error(msg);
        }
        return AjaxResult.success();
    }

    /**
     * 删除设备管理
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除设备管理")
    @Log(title = "设备管理", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('operations:device:delete')")
    public AjaxResult remove(@PathVariable String id) {
        return toAjax(deviceService.deleteDeviceById(id));
    }

    /**
     * 获取设备点位
     */
    @GetMapping("/index/{id}")
    @ApiOperation("获取设备点位")
    public AjaxResult deviceIndex(@PathVariable("id") String id) {
        if (StringUtils.isBlank(id)) {
            AjaxResult.error("请先选择设备");
        }
        List<DeviceIndexVO> result = deviceService.getDeviceIndex(id);
        return AjaxResult.success(result);
    }

    /**
     * 编辑设备点位
     */
    @PutMapping("/factor")
    @ApiOperation("编辑设备点位")
    @Log(title = "编辑设备点位", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('operations:device:edit')")
    public AjaxResult updateFactor(@RequestBody List<DeviceFactorDTO> request) {
        deviceService.updateFactor(request);
        return AjaxResult.success();
    }

    /**
     * 根据设备id查询逆变器信息
     */
    @GetMapping("/getInverterInfo")
    @ApiOperation("设备状态-根据设备id查询逆变器信息")
    public AjaxResult getInverterInfo(@RequestParam(value = "id", defaultValue = "-1") String id) {
        return AjaxResult.success(deviceService.getInverterInfo(id));
    }

    /**
     * 根据设备id查询发电信息、收益信息
     */
    @GetMapping("/getPowerGenerationInfo")
    @ApiOperation("设备状态-根据设备id查询发电信息、收益信息")
    public AjaxResult getPowerGenerationInfo(@RequestParam(value = "id", defaultValue = "-1") String id) {
        return AjaxResult.success(deviceService.getPowerGenerationInfo(id));
    }

    /**
     * 根据设备id获取发电趋势信息
     */
    @GetMapping("/getImplementedPower")
    @ApiOperation("设备状态-根据设备id获取发电趋势信息")
    public AjaxResult getImplementedPower(@RequestParam(value = "id", defaultValue = "-1") String id,
                                          @RequestParam(value = "timeType") TimeTypeEnum timeType) {
        return AjaxResult.success(deviceService.getImplementedPower(id, timeType));
    }

    /**
     * 根据设备id获取交流测信息
     */
    @GetMapping("/getACMeasurementsByDeviceId")
    @ApiOperation("设备状态-根据设备id获取交流测信息")
    public AjaxResult getACMeasurementsByDeviceId(@RequestParam(value = "id", defaultValue = "-1") String id) {
        return AjaxResult.success(deviceService.getACMeasurementsByDeviceId(id));
    }

    /**
     * 根据设备id获取点检与维修信息
     */
    @GetMapping("/listDeviceInspectionByDeviceId")
    @ApiOperation("设备状态-根据设备id获取点检与维修信息")
    public AjaxResult listDeviceInspectionByDeviceId(@RequestParam(value = "id", defaultValue = "-1") String id) {
        return AjaxResult.success(deviceService.listDeviceInspectionByDeviceId(id));
    }

}