package com.ruoyi.web.controller.pvadmin;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.TimeTypeEnum;
import com.ruoyi.pvadmin.domain.dto.GenerationStatisticsDTO;
import com.ruoyi.pvadmin.domain.dto.PowerStationQueryDTO;
import com.ruoyi.pvadmin.domain.dto.PowerStationSubmitDTO;
import com.ruoyi.pvadmin.domain.vo.DeviceGenerationStatsVO;
import com.ruoyi.pvadmin.domain.vo.GenerationStatisticsVO;
import com.ruoyi.pvadmin.domain.vo.PowerStationVO;
import com.ruoyi.pvadmin.service.IPowerStationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 电站维护Controller
 */
@Api(value = "电站管理", tags = "电站管理")
@RestController
@RequestMapping("/powerStation")
public class PowerStationController extends BaseController {

    @Autowired
    private IPowerStationService powerStationService;

    /**
     * 查询电站维护列表
     */
    @GetMapping("/list")
    @ApiOperation("查询电站维护列表")
    @PreAuthorize("@ss.hasPermi('operations:powerStation:list')")
    public TableDataInfo list(PowerStationQueryDTO dto) {
        startPage();
        List<PowerStationVO> list = powerStationService.selectPowerStationList(dto);
        return getDataTable(list);
    }

    /**
     * 首页-电站发电排名
     */
    @GetMapping("/listPowerStationRank")
    @ApiOperation("首页-电站发电排名")
//    @PreAuthorize("@ss.hasPermi('operations:powerStation:list')")
    public AjaxResult listPowerStationRank() {
        return AjaxResult.success(powerStationService.listPowerStationRank());
    }

    /**
     * 首页-电站信息
     */
    @GetMapping("/getHomePowerStationInfo")
    @ApiOperation("首页-电站信息")
//    @PreAuthorize("@ss.hasPermi('operations:powerStation:query')")
    public AjaxResult getHomePowerStationInfo() {
        return AjaxResult.success(powerStationService.getHomePowerStationInfo(new BaseEntity()));
    }

    /**
     * 获取电站维护详细信息
     */
    @GetMapping(value = "/{id}")
    @ApiOperation("获取电站维护详细信息")
//    @PreAuthorize("@ss.hasPermi('operations:powerStation:query')")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return success(powerStationService.selectPowerStationById(id));
    }

    /**
     * 新增电站维护
     */
    @PostMapping
    @ApiOperation("新增电站维护")
    @PreAuthorize("@ss.hasPermi('operations:powerStation:add')")
    @Log(title = "新增电站维护", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated @RequestBody PowerStationSubmitDTO dto) {
        return toAjax(powerStationService.insertPowerStation(dto));
    }

    /**
     * 修改电站维护
     */
    @PutMapping
    @ApiOperation("修改电站维护")
    @Log(title = "电站维护", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('operations:powerStation:edit')")
    public AjaxResult edit(@Validated @RequestBody PowerStationSubmitDTO dto) {
        if (StringUtils.isBlank(dto.getId())) {
            return AjaxResult.error("未找到修改数据");
        }
        return toAjax(powerStationService.updatePowerStation(dto));
    }

    /**
     * 删除电站维护
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除电站维护")
    @Log(title = "删除电站维护", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('operations:powerStation:remove')")
    public AjaxResult remove(@PathVariable String id) {
        return toAjax(powerStationService.deletePowerStationByIds(id));
    }

    /**
     * 电站发电统计
     */
    @GetMapping("/listGenerationStatistics")
    @ApiOperation("查询电站发电统计")
//    @PreAuthorize("@ss.hasPermi('operations:powerStation:list')")
    public TableDataInfo listGenerationStatistics(@Validated GenerationStatisticsDTO dto) {

        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<GenerationStatisticsVO> page = PageMethod.startPage(pageDomain.getPageNum(), pageDomain.getPageSize());

        List<GenerationStatisticsVO> statisticsVOList = powerStationService.listGenerationStatistics(dto);

        TableDataInfo dataTable = getDataTable(statisticsVOList);
        dataTable.setTotal(page.getTotal());

        return dataTable;
    }

    /**
     * 电站状态-根据id查询电站信息
     */
    @GetMapping("/getPowerStationInfoById")
    @ApiOperation("电站状态-查询电站发电统计")
//    @PreAuthorize("@ss.hasPermi('operations:powerStation:query')")
    public AjaxResult getPowerStationInfoById(@RequestParam(value = "id", defaultValue = "-1") String id) {

        return AjaxResult.success(powerStationService.getPowerStationInfoById(id));
    }

    /**
     * 根据电站id查询发电信息、收益信息
     */
    @GetMapping("/getPowerGenerationInfo")
    @ApiOperation("电站状态-根据电站id查询发电信息、收益信息")
//    @PreAuthorize("@ss.hasPermi('operations:powerStation:query')")
    public AjaxResult getPowerGenerationInfo(@RequestParam(value = "id", defaultValue = "-1") String id) {
        return AjaxResult.success(powerStationService.getPowerGenerationInfo(id));
    }

    /**
     * 根据电站id获取发电趋势信息
     */
    @GetMapping("/getImplementedPower")
    @ApiOperation("电站状态-根据电站id获取发电趋势信息")
//    @PreAuthorize("@ss.hasPermi('operations:powerStation:query')")
    public AjaxResult getImplementedPower(@RequestParam(value = "id") String id,
                                          @RequestParam(value = "timeType") TimeTypeEnum timeType) {
        return AjaxResult.success(powerStationService.getImplementedPower(id, timeType));
    }

    /**
     * 根据电站id获取设备信息
     */
    @GetMapping("/listDeviceById")
    @ApiOperation("电站状态-根据电站id获取设备信息")
//    @PreAuthorize("@ss.hasPermi('operations:powerStation:list')")
    public AjaxResult listDeviceById(@RequestParam(value = "id", defaultValue = "-1") String id) {
        return AjaxResult.success(powerStationService.listDeviceById(id));
    }
}