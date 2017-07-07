package net.myspring.tool.modules.oppo.service;

import com.google.common.collect.Lists;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.common.utils.CacheUtils;
import net.myspring.tool.modules.oppo.domain.OppoPlantSendImeiPpselDto;
import net.myspring.tool.modules.oppo.repository.OppoPlantSendImeiPpselRepository;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.time.LocalDateUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@LocalDataSource
public class OppoPlantSendImeiPpselService {

    @Autowired
    private OppoPlantSendImeiPpselRepository oppoPlantSendImeiPpselRepository;
    @Autowired
    private CacheUtils cacheUtils;

    @Transactional(readOnly = true)
    public SimpleExcelBook export(String date){
        Workbook workbook = new SXSSFWorkbook();
        LocalDate dateTimeStart = LocalDateUtils.parse(date);
        LocalDate dateTimeEnd = LocalDateUtils.parse(date).plusDays(1);
        List<OppoPlantSendImeiPpselDto> oppoPlantSendImeiPpselDtoList = oppoPlantSendImeiPpselRepository.findListByCreatedDate(dateTimeStart,dateTimeEnd);
        cacheUtils.initCacheInput(oppoPlantSendImeiPpselDtoList);

        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"billId","订单号"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"imei","串码"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"imei2","串码2"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"meid","meid"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"createdTime","创建时间"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"dlsProductId","物料编码"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"remark","备注"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"productName","产品名称"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"lxProductName","lx产品名称"));

        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("发货串码",oppoPlantSendImeiPpselDtoList,simpleExcelColumnList);
        ExcelUtils.doWrite(workbook,simpleExcelSheet);
        return new SimpleExcelBook(workbook,"发货串码"+dateTimeStart+".xlsx",simpleExcelSheet);
    }

}
