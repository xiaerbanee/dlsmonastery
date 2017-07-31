package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.crm.dto.AfterSaleDto;
import net.myspring.future.modules.crm.dto.AfterSaleProductAllotDto;
import net.myspring.future.modules.crm.repository.AfterSaleProductAllotRepository;
import net.myspring.future.modules.crm.web.query.AfterSaleProductAllotQuery;
import net.myspring.future.modules.crm.web.query.AfterSaleQuery;
import net.myspring.future.modules.crm.web.query.AfterSaleStoreAllotQuery;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


/**
 * Created by zhucc on 2017/7/4.
 */
@Service
@Transactional(readOnly=true)
public class AfterSaleProductAllotService {

    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private AfterSaleProductAllotRepository afterSaleProductAllotRepository;
    @Autowired
    private DepotManager depotManager;

    public Page<AfterSaleProductAllotDto> findPage(Pageable pageable, AfterSaleProductAllotQuery afterSaleProductAllotQuery){
        afterSaleProductAllotQuery.setDepotIdList(depotManager.filterDepotIds(RequestUtils.getAccountId()));
        Page<AfterSaleProductAllotDto> afterSaleProductAllotDtoPage=afterSaleProductAllotRepository.findPage(pageable,afterSaleProductAllotQuery);
        cacheUtils.initCacheInput(afterSaleProductAllotDtoPage.getContent());
        return afterSaleProductAllotDtoPage;
    }
    public SimpleExcelBook findSimpleExcelSheet(AfterSaleProductAllotQuery afterSaleProductAllotQuery){
        Workbook workbook = new SXSSFWorkbook(10000);
        List<AfterSaleProductAllotDto> afterSaleProductAllotDtos=afterSaleProductAllotRepository.findFilter(afterSaleProductAllotQuery);
        cacheUtils.initCacheInput(afterSaleProductAllotDtos);
        List<SimpleExcelColumn> simpleExcelColumnList= Lists.newArrayList();

        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"businessId","售后单号"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"storeName","仓库"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"fromProductName","换前货品"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"toProductName","换后货品"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"toOutCode","盘盈编号"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"fromOutCode","盘亏编号"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"createdByName","创建人"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"createdDate","创建时间"));

        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("售后盘点",afterSaleProductAllotDtos,simpleExcelColumnList);
        ExcelUtils.doWrite(workbook,simpleExcelSheet);
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"售后盘点"+ UUID.randomUUID()+".xlsx",simpleExcelSheet);
        return simpleExcelBook;
    }
}


