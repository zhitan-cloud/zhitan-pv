package com.ruoyi.web.controller.pvadmin;


import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.pvadmin.domain.dto.ComparativeAnalysisDTO;
import com.ruoyi.pvadmin.domain.dto.HomeQueryDTO;
import com.ruoyi.pvadmin.service.IDataItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 能耗统计分析 Controller
 */
@RestController
@RequestMapping("/statisticsAnalysis")
@Api(value = "能耗统计分析管理", tags = "能耗统计分析管理")
public class StatisticsAnalysisController {

    @Autowired
    private IDataItemService dataItemService;


    /**
     * 首页-获取首页发电量数据
     */
    @GetMapping("/getHomepageGenerationStats")
    @ApiOperation("首页-获取首页发电量数据")
    public AjaxResult getHomepageGenerationStats(@Validated HomeQueryDTO dto) {
        return AjaxResult.success(dataItemService.getHomepageGenerationStats(dto));
    }

    /**
     * 能耗统计分析-获取同比分析列表数据
     */
    @GetMapping("/querySameCompareList")
    @ApiOperation("能耗统计分析-获取同比分析列表数据")
    public AjaxResult querySameCompareList(@Validated ComparativeAnalysisDTO dto) {
        return AjaxResult.success(dataItemService.listEnergyAnalysis(dto, Constants.ENERGY_COMPARISON_YOY));
    }

    /**
     * 能耗统计分析-获取环比分析列表数据
     */
    @GetMapping("/queryLoopCompareList")
    @ApiOperation("能耗统计分析-获取环比分析列表数据")
    public AjaxResult queryLoopCompareList(@Validated ComparativeAnalysisDTO dto) {
        return AjaxResult.success(dataItemService.listEnergyAnalysis(dto, Constants.ENERGY_COMPARISON_MOM));
    }

}