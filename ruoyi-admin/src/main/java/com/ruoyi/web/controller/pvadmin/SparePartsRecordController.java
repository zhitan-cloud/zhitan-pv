package com.ruoyi.web.controller.pvadmin;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.pvadmin.domain.entity.SparePartsRecord;
import com.ruoyi.pvadmin.service.ISparePartsRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 备品备件-操作记录Controller
 *
 * @author ruoyi
 * @date 2023-08-31
 */
@RestController
@RequestMapping("/record")
public class SparePartsRecordController extends BaseController {
    @Autowired
    private ISparePartsRecordService sparePartsRecordService;

    /**
     * 查询备品备件-操作记录列表
     */
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('operations:record:list')")
    public TableDataInfo list(SparePartsRecord sparePartsRecord) {
        startPage();
        List<SparePartsRecord> list = sparePartsRecordService.selectSparePartsRecordList(sparePartsRecord);
        return getDataTable(list);
    }

    /**
     * 导出备品备件-操作记录列表
     */
    @Log(title = "备品备件-操作记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @PreAuthorize("@ss.hasPermi('operations:record:export')")
    public void export(HttpServletResponse response, SparePartsRecord sparePartsRecord) {
        List<SparePartsRecord> list = sparePartsRecordService.selectSparePartsRecordList(sparePartsRecord);
        ExcelUtil<SparePartsRecord> util = new ExcelUtil<>(SparePartsRecord.class);
        util.exportExcel(response, list, "备品备件-操作记录数据");
    }

    /**
     * 获取备品备件-操作记录详细信息
     */
    @GetMapping(value = "/{id}")
    @PreAuthorize("@ss.hasPermi('operations:record:query')")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return success(sparePartsRecordService.selectSparePartsRecordById(id));
    }

    /**
     * 新增备品备件-操作记录
     */
    @Log(title = "备品备件-操作记录", businessType = BusinessType.INSERT)
    @PostMapping
    @PreAuthorize("@ss.hasPermi('operations:record:add')")
    public AjaxResult add(@RequestBody SparePartsRecord sparePartsRecord) {
        return toAjax(sparePartsRecordService.insertSparePartsRecord(sparePartsRecord));
    }

    /**
     * 修改备品备件-操作记录
     */
    @Log(title = "备品备件-操作记录", businessType = BusinessType.UPDATE)
    @PutMapping
    @PreAuthorize("@ss.hasPermi('operations:record:edit')")
    public AjaxResult edit(@RequestBody SparePartsRecord sparePartsRecord) {
        return toAjax(sparePartsRecordService.updateSparePartsRecord(sparePartsRecord));
    }

    /**
     * 删除备品备件-操作记录
     */
    @Log(title = "备品备件-操作记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPermi('operations:record:remove')")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(sparePartsRecordService.deleteSparePartsRecordByIds(ids));
    }
}
