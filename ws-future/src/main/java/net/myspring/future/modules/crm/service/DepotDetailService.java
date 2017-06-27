package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.crm.dto.DepotDetailDto;
import net.myspring.future.modules.crm.repository.DepotDetailRepository;
import net.myspring.future.modules.crm.web.query.DepotDetailQuery;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.time.LocalDateUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class DepotDetailService {

    @Autowired
    private DepotDetailRepository depotDetailRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<DepotDetailDto> findPage(Pageable pageable, DepotDetailQuery depotDetailQuery){

        Page<DepotDetailDto> page= depotDetailRepository.findPage(pageable,depotDetailQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public String export(DepotDetailQuery depotDetailQuery){
        List<DepotDetailDto> depotDetailDtoList=depotDetailRepository.findPage(new PageRequest(0,10000),depotDetailQuery).getContent();
        cacheUtils.initCacheInput(depotDetailDtoList);
        return export(depotDetailDtoList);
    }

    public String export(List<DepotDetailDto> depotDetailDtoList){
        Workbook workbook = new SXSSFWorkbook(10000);
        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();

        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "depotName", "门店"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "productName", "产品名称"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "hasIme", "包含串码"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "outQty", "财务数量"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "qty", "本地数量"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "isSame", "是否相同"));

        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("货品库存", depotDetailDtoList, simpleExcelColumnList);
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook, "货品库存" + LocalDateUtils.format(LocalDate.now()) + ".xlsx", simpleExcelSheet);
        ByteArrayInputStream byteArrayInputStream = ExcelUtils.doWrite(simpleExcelBook.getWorkbook(), simpleExcelBook.getSimpleExcelSheets());
        return null;
    }

    public void syn(){ }

}
