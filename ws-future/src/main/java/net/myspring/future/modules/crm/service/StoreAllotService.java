package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.future.modules.crm.domain.StoreAllot;
import net.myspring.future.modules.crm.domain.StoreAllotIme;
import net.myspring.future.modules.crm.mapper.StoreAllotImeMapper;
import net.myspring.future.modules.crm.mapper.StoreAllotMapper;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = false)
public class StoreAllotService {

    @Autowired
    private StoreAllotMapper storeAllotMapper;
    @Autowired
    private StoreAllotImeMapper storeAllotImeMapper;


    public StoreAllot findOne(String id) {
        StoreAllot storeAllot = storeAllotMapper.findOne(id);
        return storeAllot;
    }

    public StoreAllot shipBoxAndIme(StoreAllot storeAllot, String boxIme, String imeStr) {
        return null;
    }

    public void ship(StoreAllot storeAllot) {

    }

    public Integer getCloudQty(String productId,String fromStoreId){
        return null;
    }

    public StoreAllot update(StoreAllot storeAllot){
        storeAllotMapper.update(storeAllot);
        return storeAllot;
    }

    public StoreAllot save(StoreAllot storeAllot) {
        return storeAllot;
    }

    public void delete(StoreAllot storeAllot) {
    }

    public Page<StoreAllot> findPage(Pageable pageable, Map<String, Object> map) {
        Page<StoreAllot> page = storeAllotMapper.findPage(pageable, map);
        return page;
    }

    public List<SimpleExcelSheet> findSimpleExcelSheets(Workbook workbook, Map<String, Object> map) {
        List<SimpleExcelSheet> simpleExcelSheetList = Lists.newArrayList();
        List<SimpleExcelColumn> storeAllotColumnList = Lists.newArrayList();
        storeAllotColumnList.add(new SimpleExcelColumn(workbook, "businessId", "单据编号"));
        storeAllotColumnList.add(new SimpleExcelColumn(workbook, "fromStore.name", "调拨前"));
        storeAllotColumnList.add(new SimpleExcelColumn(workbook, "toStore.name", "调拨后"));
        storeAllotColumnList.add(new SimpleExcelColumn(workbook, "outCode", "外部编号"));
        storeAllotColumnList.add(new SimpleExcelColumn(workbook, "status", "状态"));
        storeAllotColumnList.add(new SimpleExcelColumn(workbook, "created.loginName", "创建人"));
        storeAllotColumnList.add(new SimpleExcelColumn(workbook, "createdDate", "创建时间"));
        storeAllotColumnList.add(new SimpleExcelColumn(workbook, "lastModified.loginName", "更新人"));
        storeAllotColumnList.add(new SimpleExcelColumn(workbook, "lastModifiedDate", "更新时间"));
        storeAllotColumnList.add(new SimpleExcelColumn(workbook, "remarks", "备注"));
        List<StoreAllot> storeAllotList = storeAllotMapper.findByFilter(map);
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("调拨单" + LocalDate.now(), storeAllotList, storeAllotColumnList);
        simpleExcelSheetList.add(simpleExcelSheet);

        List<SimpleExcelColumn> storeAllotImeColumnList = Lists.newArrayList();
        storeAllotImeColumnList.add(new SimpleExcelColumn(workbook, "storeAllot.businessId", "订单编号"));
        storeAllotImeColumnList.add(new SimpleExcelColumn(workbook, "storeAllot.outCode", "外部编号"));
        storeAllotImeColumnList.add(new SimpleExcelColumn(workbook, "storeAllot.billDate", "开单日期"));
        storeAllotImeColumnList.add(new SimpleExcelColumn(workbook, "storeAllot.fromStore.name", "发货仓库"));
        storeAllotImeColumnList.add(new SimpleExcelColumn(workbook, "storeAllot.toStore.area.name", "办事处"));
        storeAllotImeColumnList.add(new SimpleExcelColumn(workbook, "storeAllot.toStore.name", "门店"));
        storeAllotImeColumnList.add(new SimpleExcelColumn(workbook, "productIme.product.name", "产品名称"));
        storeAllotImeColumnList.add(new SimpleExcelColumn(workbook, "productIme.ime", "串码"));
        List<StoreAllotIme> storeAllotImes = storeAllotImeMapper.findByStoreAllotFilter(CollectionUtil.extractToList(storeAllotList,"id"),map);
        SimpleExcelSheet imeSheet = new SimpleExcelSheet("串码" + LocalDate.now(), storeAllotImes, storeAllotImeColumnList);
        simpleExcelSheetList.add(imeSheet);

        return simpleExcelSheetList;
    }

}
