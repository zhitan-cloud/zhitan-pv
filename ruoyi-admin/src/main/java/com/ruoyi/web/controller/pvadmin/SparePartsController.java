package com.ruoyi.web.controller.pvadmin;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.pvadmin.domain.dto.SparePartsRecordQueryDTO;
import com.ruoyi.pvadmin.domain.entity.SpareParts;
import com.ruoyi.pvadmin.domain.vo.DeviceGenerationStatsVO;
import com.ruoyi.pvadmin.domain.vo.SparePartsVO;
import com.ruoyi.pvadmin.domain.vo.StockOperationRecordVO;
import com.ruoyi.pvadmin.service.ISparePartsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 备品备件Controller
 *
 * @author ruoyi
 * @date 2023-08-31
 */
@RestController
@RequestMapping("/parts")
public class SparePartsController extends BaseController {
    @Autowired
    private ISparePartsService sparePartsService;

    /**
     * 查询备品备件列表
     */
    @GetMapping("/list")
    @ApiOperation("查询备品备件列表")
    @PreAuthorize("@ss.hasPermi('operations:parts:list')")
    public TableDataInfo list(SparePartsVO spareParts) {
        startPage();
        List<SparePartsVO> list = sparePartsService.selectSparePartsList(spareParts);
        return getDataTable(list);
    }

    /**
     * 查询出库入库操作记录列表
     */
    @GetMapping("/listOperationRecords")
    @ApiOperation("查询出库入库操作记录列表")
    @PreAuthorize("@ss.hasPermi('operations:parts:list')")
    public TableDataInfo listOperationRecords(SparePartsRecordQueryDTO dto) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<DeviceGenerationStatsVO> page = PageMethod.startPage(pageDomain.getPageNum(), pageDomain.getPageSize());
        List<StockOperationRecordVO> list = sparePartsService.listOperationRecords(dto);
        TableDataInfo dataTable = getDataTable(list);
        dataTable.setTotal(page.getTotal());
        return dataTable;
    }

    /**
     * 导出备品备件列表
     */
    @Log(title = "备品备件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @PreAuthorize("@ss.hasPermi('operations:parts:export')")
    public void export(HttpServletResponse response, SparePartsVO spareParts) {
        List<SparePartsVO> list = sparePartsService.selectSparePartsList(spareParts);
        ExcelUtil<SparePartsVO> util = new ExcelUtil<>(SparePartsVO.class);
        util.exportExcel(response, list, "备品备件数据");
    }

    /**
     * 获取备品备件详细信息
     */
    @GetMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPermi('operations:parts:query')")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return success(sparePartsService.selectSparePartsById(id));
    }

    @Log(title = "更新备品备件", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    @ApiOperation("更新备品备件")
    @PreAuthorize("@ss.hasPermi('operations:parts:edit')")
    public AjaxResult edit(@RequestBody SparePartsVO spareParts) {
        sparePartsService.edit(spareParts);
        return success();
    }

    @Log(title = "删除备品备件", businessType = BusinessType.UPDATE)
    @DeleteMapping("/delete")
    @ApiOperation("删除备品备件")
    @PreAuthorize("@ss.hasPermi('operations:parts:remove')")
    public AjaxResult delete(@RequestParam(name = "id") String id) {
        sparePartsService.deleteSparePartsById(id);
        return success();
    }

    /**
     * 新增备品备件（新增/入库）
     */
    @Log(title = "备品备件", businessType = BusinessType.INSERT)
    @ApiOperation("备品备件入库")
    @PostMapping
    @PreAuthorize("@ss.hasPermi('operations:parts:add')")
    public AjaxResult inBound(@RequestBody SparePartsVO spareParts) {
        if (StringUtils.isEmpty(spareParts.getCode())) {
            return error("缺少编号！");
        }
        if (StringUtils.isEmpty(spareParts.getSpecs())) {
            return error("缺少规格型号！");
        }
        if (StringUtils.isEmpty(spareParts.getName())) {
            return error("缺少名称！");
        }
        if (ObjectUtils.isEmpty(spareParts.getAmount()) || spareParts.getAmount() <= 0) {
            return error("数量应该为正整数！");
        }
        return toAjax(sparePartsService.inBound(spareParts));
    }

    /**
     * 修改备品备件
     */
    @Log(title = "备品备件", businessType = BusinessType.UPDATE)
    @ApiOperation("备品备件出库")
    @PutMapping
    @PreAuthorize("@ss.hasPermi('operations:parts:edit')")
    public AjaxResult outBound(@RequestBody SparePartsVO spareParts) {
        return toAjax(sparePartsService.outBound(spareParts));
    }
}