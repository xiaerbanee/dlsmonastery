package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.crm.dto.ReportScoreOfficeDto;
import net.myspring.future.modules.crm.repository.ReportScoreOfficeRepository;
import net.myspring.future.modules.crm.web.query.ReportScoreOfficeQuery;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class ReportScoreOfficeService {

    @Autowired
    private ReportScoreOfficeRepository reportScoreOfficeRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<ReportScoreOfficeDto> findPage(Pageable pageable, ReportScoreOfficeQuery reportScoreOfficeQuery){
        Page<ReportScoreOfficeDto> page= reportScoreOfficeRepository.findPage(pageable,reportScoreOfficeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public SimpleExcelBook export(ReportScoreOfficeQuery reportScoreOfficeQuery) {

        List<ReportScoreOfficeDto> reportScoreOfficeDtos = reportScoreOfficeRepository.findPage(new PageRequest(0, 50000), reportScoreOfficeQuery).getContent();
        cacheUtils.initCacheInput(reportScoreOfficeDtos);

        Workbook workbook = new SXSSFWorkbook(50000);
        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();

        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "scoreDate", "日期"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "areaName", "办事处"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "officeName", "考核区域"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "score", "当日打分"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "monthScore", "月累计打分"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "dateRank", "当日排名"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "monthRank", "月累计排名"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "saleQty", "当日销量"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "monthSaleQty", "当月销量"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "recentMonthSaleQty", "最近销量"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "officePoint", "点位"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "monthSaleMoney", "月累计销售额"));

        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("每日排名", reportScoreOfficeDtos, simpleExcelColumnList);
        ExcelUtils.doWrite(workbook, simpleExcelSheet);

        return new SimpleExcelBook(workbook, "每日排名.xlsx", simpleExcelSheet);
    }

}
