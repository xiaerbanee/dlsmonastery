package net.myspring.future.modules.layout.service;

import com.google.common.collect.Lists;
import com.mongodb.gridfs.GridFSFile;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.layout.dto.AdGoodsOrderDetailDto;
import net.myspring.future.modules.layout.repository.AdGoodsOrderDetailRepository;
import net.myspring.future.modules.layout.web.query.AdGoodsOrderDetailQuery;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.text.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;

@Service
public class AdGoodsOrderDetailService {

    @Autowired
    private AdGoodsOrderDetailRepository adGoodsOrderDetailRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private GridFsTemplate tempGridFsTemplate;

    public Page<AdGoodsOrderDetailDto> findPage(Pageable pageable, AdGoodsOrderDetailQuery adGoodsOrderDetailQuery) {
        Page<AdGoodsOrderDetailDto> page = adGoodsOrderDetailRepository.findPage(pageable, adGoodsOrderDetailQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public String export(AdGoodsOrderDetailQuery adGoodsOrderDetailQuery) {

        Workbook workbook = new SXSSFWorkbook(10000);
        List<SimpleExcelSheet> simpleExcelSheetList = Lists.newArrayList();
        List<SimpleExcelColumn> adGoodsOrderDetailColumnList = Lists.newArrayList();
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "adGoodsOrderId", "单号"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "adGoodsOrderCreatedDate", "创建时间"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "adGoodsOrderBillDate", "开单时间"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "adGoodsOrderProcessStatus", "状态"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "adGoodsOrderShopAreaName", "办事处"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "adGoodsOrderShopName", "门店"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "productCode", "物料编码"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "productName", "货品"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "productPrice2", "价格"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "qty", "订货数"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "confirmQty", "确认数"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "billQty", "开单数"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "productPrice2", "金额"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "adGoodsOrderRemarks", "备注"));

        List<AdGoodsOrderDetailDto> adGoodsOrderDetailDtoList = findPage(new PageRequest(0,10000), adGoodsOrderDetailQuery).getContent();
        simpleExcelSheetList.add(new SimpleExcelSheet("柜台订货详情", adGoodsOrderDetailDtoList, adGoodsOrderDetailColumnList));

        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"柜台订货详情"+ LocalDate.now()+".xlsx", simpleExcelSheetList);
        ByteArrayInputStream byteArrayInputStream= ExcelUtils.doWrite(simpleExcelBook.getWorkbook(),simpleExcelBook.getSimpleExcelSheets());
        GridFSFile gridFSFile = tempGridFsTemplate.store(byteArrayInputStream,simpleExcelBook.getName(),"application/octet-stream; charset=utf-8", RequestUtils.getDbObject());
        return StringUtils.toString(gridFSFile.getId());

    }
}
