package com.ruoyi.web.controller.pvadmin;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateTimeUtil;
import com.ruoyi.pvadmin.domain.dto.HomeQueryDTO;
import com.ruoyi.pvadmin.domain.dto.PeakValleyQueryDTO;
import com.ruoyi.pvadmin.domain.model.GenerationStatisticsItemModel;
import com.ruoyi.pvadmin.domain.vo.PeakAndValleyReportVO;
import com.ruoyi.pvadmin.service.IElectricityDataItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 电力-峰平谷数据Controller
 */
@RestController
@RequestMapping("/peakValley")
@Api(value = "峰平谷数据", tags = "峰平谷数据")
public class ElectricityDataItemController extends BaseController {

    @Autowired
    private IElectricityDataItemService service;


    /**
     * 首页-查询时段发电占比
     */
    @GetMapping("/getPeriodGenerationPercentage")
    @ApiOperation(value = "首页-查询时段发电占比", notes = "首页-查询时段发电占比")
    public AjaxResult getPeriodGenerationPercentage(@Validated HomeQueryDTO dto) {
        return AjaxResult.success(service.getPeriodGenerationPercentage(dto));
    }

    /**
     * 查询峰平谷报表
     */
    @GetMapping("/report")
    @ApiOperation(value = "尖峰平谷时段统计查询报表", notes = "尖峰平谷时段统计查询报表")
    public AjaxResult report(@Validated PeakValleyQueryDTO dto) {
        return AjaxResult.success(service.getReport(dto));
    }

    /**
     * 导出峰平谷报表
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response, @Validated PeakValleyQueryDTO dto) throws IOException {
        List<PeakAndValleyReportVO> list = service.getReport(dto);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(String.format("尖峰平谷%s报表", DateTimeUtil.getMonth(dto.getDateTime())));
        // 创建标题行
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("用电类型");
        headerRow.createCell(1).setCellValue("时段");
        List<GenerationStatisticsItemModel> timeList = list.get(0).getTimeList();
        for (int i = 0; i < timeList.size(); i++) {
            headerRow.createCell(i + 2).setCellValue(timeList.get(i).getTime());
            if ((i + 1) == timeList.size()) {
                headerRow.createCell(i + 3).setCellValue("合计");
            }
        }
        // 填充数据
        int rowIndex = 1;
        for (PeakAndValleyReportVO report : list) {
            Row dataRow = sheet.createRow(rowIndex++);
            dataRow.createCell(0).setCellValue(report.getTimeNameCN());
            dataRow.createCell(1).setCellValue(report.getTimePeriod());
            for (int i = 0; i < timeList.size(); i++) {
                BigDecimal value = null;
                for (GenerationStatisticsItemModel item : report.getTimeList()) {
                    if (item.getTime().equals(timeList.get(i).getTime())) {
                        value = item.getValue();
                        break;
                    }
                }
                if (value != null) {
                    dataRow.createCell(i + 2).setCellValue(value.doubleValue());
                } else {
                    dataRow.createCell(i + 2).setCellValue("");
                }
            }
            dataRow.createCell(timeList.size() + 2).setCellValue(report.getSumValue().doubleValue());
        }

        // 调整列宽
        for (int i = 0; i <= timeList.size(); i++) {
            sheet.autoSizeColumn(i);
        }
        // 将文件名设置为 "尖峰平谷报表.xlsx"
        String fileName = "尖峰平谷报表.xls";
        // 设置响应头信息
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        // 将 Excel 文件写入响应流中
        workbook.write(response.getOutputStream());
        // 关闭并释放资源
        IOUtils.closeQuietly(workbook);
    }


    /**
     * 尖峰平谷时段统计
     */
    @GetMapping("/segment")
    @ApiOperation(value = "尖峰平谷时段统计查询列表", notes = "尖峰平谷时段统计")
    public AjaxResult segment(@Validated PeakValleyQueryDTO dto) {
        return AjaxResult.success(service.getPeakAndValleyPieAndDataList(dto));
    }

}