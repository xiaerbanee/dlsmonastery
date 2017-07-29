package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.dto.DemoPhoneTypeOfficeDto;
import net.myspring.future.modules.basic.repository.DemoPhoneTypeOfficeRepository;
import net.myspring.future.modules.basic.web.query.DemoPhoneTypeOfficeQuery;
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

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DemoPhoneTypeOfficeService {

    @Autowired
    private DemoPhoneTypeOfficeRepository demoPhoneTypeOfficeRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<DemoPhoneTypeOfficeDto> findPage(Pageable pageable, DemoPhoneTypeOfficeQuery demoPhoneTypeOfficeQuery){
        Page<DemoPhoneTypeOfficeDto> page = demoPhoneTypeOfficeRepository.findPage(pageable,demoPhoneTypeOfficeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public SimpleExcelBook export(DemoPhoneTypeOfficeQuery demoPhoneTypeOfficeQuery) {
        Workbook workbook = new SXSSFWorkbook(10000);

        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "officeName", "办事处"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "demoPhoneTypeName", "演示机型"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "qty", "数量"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "realQty", "实际数量"));

        List<DemoPhoneTypeOfficeDto> demoPhoneTypeOfficeDtos = findPage(new PageRequest(0,10000),demoPhoneTypeOfficeQuery).getContent();
        cacheUtils.initCacheInput(demoPhoneTypeOfficeDtos);
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("演示机型汇总", demoPhoneTypeOfficeDtos, simpleExcelColumnList);
        ExcelUtils.doWrite(workbook, simpleExcelSheet);
        return new SimpleExcelBook(workbook,"演示机型汇总"+ LocalDateUtils.format(LocalDate.now())+".xlsx",simpleExcelSheet);
    }

}
