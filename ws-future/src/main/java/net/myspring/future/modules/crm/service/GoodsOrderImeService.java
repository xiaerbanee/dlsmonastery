package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.crm.dto.GoodsOrderDto;
import net.myspring.future.modules.crm.dto.GoodsOrderImeDto;
import net.myspring.future.modules.crm.repository.GoodsOrderImeRepository;
import net.myspring.future.modules.crm.repository.GoodsOrderRepository;
import net.myspring.future.modules.crm.web.query.GoodsOrderImeQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.*;
import net.myspring.util.time.LocalDateUtils;
import org.apache.poi.ss.usermodel.CellStyle;
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
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class GoodsOrderImeService {

    @Autowired
    private DepotManager depotManager;
    @Autowired
    private GoodsOrderImeRepository goodsOrderImeRepository;
    @Autowired
    private GoodsOrderRepository goodsOrderRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<GoodsOrderImeDto> findPage(Pageable pageable, GoodsOrderImeQuery goodsOrderImeQuery) {

        goodsOrderImeQuery.setDepotIdList(depotManager.filterDepotIds(RequestUtils.getAccountId()));

        Page<GoodsOrderImeDto> page = goodsOrderImeRepository.findPage(pageable, goodsOrderImeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public SimpleExcelBook export(GoodsOrderImeQuery goodsOrderImeQuery) {

        Workbook workbook = new SXSSFWorkbook(10000);
        List<List<SimpleExcelColumn>> excelColumnList= Lists.newArrayList();
        Map<String, CellStyle> cellStyleMap=ExcelUtils.getCellStyleMap(workbook);
        CellStyle headCellStyle = cellStyleMap.get(ExcelCellStyle.HEADER.name());
        CellStyle dataCellStyle = cellStyleMap.get(ExcelCellStyle.DATA.name());

        List<SimpleExcelColumn> headColumnList=Lists.newArrayList();
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"单号"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"办事处"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"考核区域"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"发货仓库"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"门店"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"货品名称"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"串码"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"串码2"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"MEID码"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"状态"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"创建人"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"创建时间"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"开单时间"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"发货时间"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"天翼购订货"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"备注"));
        excelColumnList.add(headColumnList);

        List<GoodsOrderImeDto> goodsOrderImeDtoList=findPage(new PageRequest(0,10000), goodsOrderImeQuery).getContent();
        List<String> goodsOrderIdList= CollectionUtil.extractToList(goodsOrderImeDtoList,"goodsOrderId");
        List<GoodsOrderDto> goodsOrderDtoList = goodsOrderRepository.findDtoListByIdList(goodsOrderIdList);
        cacheUtils.initCacheInput(goodsOrderDtoList);
        Map<String, GoodsOrderDto> goodsOrderDtoMap = CollectionUtil.extractToMap(goodsOrderDtoList, "id");

        for(GoodsOrderImeDto goodsOrderImeDto : goodsOrderImeDtoList){
            GoodsOrderDto goodsOrderDto = goodsOrderDtoMap.get(goodsOrderImeDto.getGoodsOrderId());
            List<SimpleExcelColumn> simpleExcelColumnList=Lists.newArrayList();
            simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getBusinessId()));
            simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getShopAreaName()));
            simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getShopOfficeName()));
            simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getStoreName()));
            simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getShopName()));
            simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderImeDto.getProductName()));
            simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderImeDto.getProductImeIme()));
            simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderImeDto.getProductImeIme2()));
            simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderImeDto.getProductImeMeid()));
            simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getStatus()));
            simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getCreatedByName()));
            simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getCreatedDate()));
            simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getBillDate()));
            simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getShipDate()));
            simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getLxMallOrder()!=null&&goodsOrderDto.getLxMallOrder()?"是":"否"));
            simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getRemarks()));
            excelColumnList.add(simpleExcelColumnList);
        }

        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("货品订货串码列表", excelColumnList);
        ExcelUtils.doWrite(workbook, simpleExcelSheet);
        return new SimpleExcelBook(workbook,"货品订货串码列表"+ LocalDateUtils.format(LocalDate.now())+".xlsx",simpleExcelSheet);

    }
}
