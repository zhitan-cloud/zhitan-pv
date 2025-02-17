package com.ruoyi.web.controller.pvadmin;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.pvadmin.domain.dto.DeviceQueryDTO;
import com.ruoyi.pvadmin.domain.dto.LoadAnalysisDTO;
import com.ruoyi.pvadmin.domain.dto.PowerFactorAnalysisDTO;
import com.ruoyi.pvadmin.domain.dto.ThreePhaseUnbalanceAnalysisDTO;
import com.ruoyi.pvadmin.service.IRealtimeDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 实时数据 控制层
 */
@RestController
@Api(value = "实时数据管理", tags = "实时数据管理")
@RequestMapping("/realTime")
public class RealTimeController extends BaseController {

    @Autowired
    private IRealtimeDataService realtimeDataService;


    /**
     * 查询电表实时数据
     */
    @ApiOperation("查询实时数据")
    @GetMapping("/listRealTime")
    public AjaxResult listRealTime(@RequestParam(value = "powerStationId", defaultValue = "-1") String powerStationId,
                                   @RequestParam(value = "ammeter", required = false) Boolean ammeter) {

        DeviceQueryDTO queryDto = new DeviceQueryDTO();
        queryDto.setAmmeter(ammeter);
        queryDto.setPowerStationId(powerStationId);
        return AjaxResult.success(realtimeDataService.listRealTime(queryDto));
    }

    /**
     * 查询负荷分析信息
     */
    @ApiOperation("查询负荷分析信息")
    @GetMapping("/listLoadAnalysis")
    public AjaxResult listLoadAnalysis(@Validated LoadAnalysisDTO dto) {

        return AjaxResult.success(realtimeDataService.listLoadAnalysis(dto));
    }

    /**
     * 查询负荷分析详情
     */
    @ApiOperation("查询负荷分析详情")
    @GetMapping("/getLoadAnalysisDetail")
    public AjaxResult getLoadAnalysisDetail(@Validated LoadAnalysisDTO dto) {

        return AjaxResult.success(realtimeDataService.getLoadAnalysisDetail(dto));
    }

    /**
     * 查询三相不平衡信息
     */
    @ApiOperation("查询三相不平衡信息")
    @GetMapping("/listThreePhaseUnbalanceAnalysis")
    public AjaxResult listThreePhaseUnbalanceAnalysis(@Validated ThreePhaseUnbalanceAnalysisDTO dto) {

        return AjaxResult.success(realtimeDataService.listThreePhaseUnbalanceAnalysis(dto));
    }

    /**
     * 查询三相不平衡详细信息
     */
    @ApiOperation("查询负荷分析详情")
    @GetMapping("/getThreePhaseUnbalanceAnalysisDetail")
    public AjaxResult getThreePhaseUnbalanceAnalysisDetail(@Validated ThreePhaseUnbalanceAnalysisDTO dto) {

        return AjaxResult.success(realtimeDataService.getThreePhaseUnbalanceAnalysisDetail(dto));
    }

    /**
     * 查询功率因数分析信息
     */
    @ApiOperation("查询功率因数分析信息")
    @GetMapping("/getPowerFactorAnalysis")
    public AjaxResult getPowerFactorAnalysis(@Validated PowerFactorAnalysisDTO dto) {

        return AjaxResult.success(realtimeDataService.getPowerFactorAnalysis(dto));
    }

}