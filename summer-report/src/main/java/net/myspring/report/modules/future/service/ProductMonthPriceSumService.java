package net.myspring.report.modules.future.service;

import com.google.common.collect.Lists;
import net.myspring.report.modules.future.dto.ProductMonthPriceSumDto;
import net.myspring.report.modules.future.dto.ProductTypeDto;
import net.myspring.report.modules.future.repository.ProductMonthPriceSumRepository;
import net.myspring.report.modules.future.repository.ProductTypeRepository;
import net.myspring.report.modules.future.web.query.ProductMonthPriceSumQuery;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductMonthPriceSumService {

    @Autowired
    private ProductTypeRepository productTypeRepository;
    @Autowired
    private ProductMonthPriceSumRepository productMonthPriceSumRepository;

    public SimpleExcelBook export(ProductMonthPriceSumQuery productMonthPriceSumQuery) {
        Workbook workbook = new SXSSFWorkbook(10000);
        List<SimpleExcelSheet> simpleExcelSheetList=Lists.newArrayList();
        List<ProductMonthPriceSumDto> list=productMonthPriceSumRepository.findReportData(productMonthPriceSumQuery);


        List<SimpleExcelColumn> simpleExcelColumns = Lists.newArrayList();
        List<ProductTypeDto> productTypeDtoList=productTypeRepository.findByMonth(productMonthPriceSumQuery.getMonth());
        simpleExcelColumns.add(new SimpleExcelColumn("areaName", "办事处"));
        simpleExcelColumns.add(new SimpleExcelColumn("areaName", "考核区域"));
        simpleExcelColumns.add(new SimpleExcelColumn("areaName", "门店"));
        simpleExcelColumns.add(new SimpleExcelColumn("areaName", "促销"));
        simpleExcelColumns.add(new SimpleExcelColumn("areaName", "身份证号"));
        for (ProductTypeDto productTypeDto:productTypeDtoList) {
            simpleExcelColumns.add(new SimpleExcelColumn(workbook,"extra", productTypeDto.getName(),productTypeDto.getName()));
        }
        simpleExcelColumns.add(new SimpleExcelColumn("areaName", "数量合计"));
        simpleExcelColumns.add(new SimpleExcelColumn("areaName", "保卡合计"));
        simpleExcelColumns.add(new SimpleExcelColumn("areaName", "销售合计"));
        simpleExcelColumns.add(new SimpleExcelColumn("areaName", "销售总合计"));
        SimpleExcelSheet sheet = new SimpleExcelSheet("保卡统计汇总", null,simpleExcelColumns);
        simpleExcelSheetList.add(sheet);

        simpleExcelColumns = Lists.newArrayList();
        simpleExcelColumns.add(new SimpleExcelColumn("","月份"));
        simpleExcelColumns.add(new SimpleExcelColumn("","办事处"));
        simpleExcelColumns.add(new SimpleExcelColumn("","考核区域"));
        simpleExcelColumns.add(new SimpleExcelColumn("","门店"));
        simpleExcelColumns.add(new SimpleExcelColumn("","岗位"));
        simpleExcelColumns.add(new SimpleExcelColumn("","促销员"));
        simpleExcelColumns.add(new SimpleExcelColumn("","促销备注"));
        simpleExcelColumns.add(new SimpleExcelColumn("","统计型号"));
        simpleExcelColumns.add(new SimpleExcelColumn("","型号"));
        simpleExcelColumns.add(new SimpleExcelColumn("","串码"));
        simpleExcelColumns.add(new SimpleExcelColumn("","上报门店"));
        simpleExcelColumns.add(new SimpleExcelColumn("","串码所在门店"));
        simpleExcelColumns.add(new SimpleExcelColumn("","串码发货门店"));
        simpleExcelColumns.add(new SimpleExcelColumn("","促销绑定门店"));
        SimpleExcelSheet detailSheet = new SimpleExcelSheet("保卡统计明细", null,simpleExcelColumns);
        simpleExcelSheetList.add(detailSheet);

        ExcelUtils.doWrite(workbook, sheet);
        return new SimpleExcelBook(workbook, "保卡统计"+productMonthPriceSumQuery.getMonth()+".xlsx", sheet);
    }

}
