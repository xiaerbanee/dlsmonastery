package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.dto.DepotShopDto;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.basic.web.query.DepotShopQuery;
import net.myspring.future.modules.crm.dto.AfterSaleProductAllotDto;
import net.myspring.future.modules.crm.dto.AfterSaleStoreAllotDto;
import net.myspring.future.modules.crm.repository.AfterSaleProductAllotRepository;
import net.myspring.future.modules.crm.repository.AfterSaleStoreAllotRepository;
import net.myspring.future.modules.crm.web.query.AfterSaleProductAllotQuery;
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
public class AfterSaleStoreAllotService {

    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private AfterSaleStoreAllotRepository afterSaleStoreAllotRepository;
    @Autowired
    private DepotManager depotManager;

    public Page<AfterSaleStoreAllotDto> findPage(Pageable pageable, AfterSaleStoreAllotQuery afterSaleStoreAllotQuery){
        afterSaleStoreAllotQuery.setDepotIdList(depotManager.filterDepotIds(RequestUtils.getAccountId()));
        Page<AfterSaleStoreAllotDto> afterSaleStoreAllotDtoPage=afterSaleStoreAllotRepository.findPage(pageable,afterSaleStoreAllotQuery);
        cacheUtils.initCacheInput(afterSaleStoreAllotDtoPage.getContent());
        return afterSaleStoreAllotDtoPage;
    }

    public SimpleExcelBook findSimpleExcelSheet(AfterSaleStoreAllotQuery afterSaleStoreAllotQuery)  {
        Workbook workbook = new SXSSFWorkbook(10000);
        List<AfterSaleStoreAllotDto> afterSaleStoreAllotDtos = afterSaleStoreAllotRepository.findFilter(afterSaleStoreAllotQuery);
        cacheUtils.initCacheInput(afterSaleStoreAllotDtos);
        List<SimpleExcelColumn> simpleExcelColumnList= Lists.newArrayList();

        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"businessId","售后单号"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"productName","货品"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"fromStoreName","调出仓库"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"toStoreName","调后仓库"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"outCode","财务编号"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"createdByName","创建人"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"createdDate","创建时间"));

        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("售后调拨",afterSaleStoreAllotDtos,simpleExcelColumnList);
        ExcelUtils.doWrite(workbook,simpleExcelSheet);
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"售后调拨"+ UUID.randomUUID()+".xlsx",simpleExcelSheet);
        return simpleExcelBook;
    }
}
