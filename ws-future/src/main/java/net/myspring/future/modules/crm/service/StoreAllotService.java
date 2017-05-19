package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.enums.ExpressOrderTypeEnum;
import net.myspring.future.common.enums.ShipTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.basic.mapper.ProductMapper;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.domain.StoreAllot;
import net.myspring.future.modules.crm.domain.StoreAllotDetail;
import net.myspring.future.modules.crm.domain.StoreAllotIme;
import net.myspring.future.modules.crm.dto.StoreAllotDto;
import net.myspring.future.modules.crm.mapper.ExpressOrderMapper;
import net.myspring.future.modules.crm.mapper.StoreAllotDetailMapper;
import net.myspring.future.modules.crm.mapper.StoreAllotImeMapper;
import net.myspring.future.modules.crm.mapper.StoreAllotMapper;
import net.myspring.future.modules.crm.web.form.StoreAllotDetailForm;
import net.myspring.future.modules.crm.web.form.StoreAllotForm;
import net.myspring.future.modules.crm.web.query.StoreAllotQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.IdUtils;
import net.myspring.util.text.StringUtils;
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
    @Autowired
    private StoreAllotDetailMapper storeAllotDetailMapper;

    @Autowired
    private DepotMapper depotMapper;

    private ProductMapper productMapper;
    @Autowired
    private ExpressOrderMapper expressOrderMapper;

    @Autowired
    private CacheUtils cacheUtils;


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

    public void delete(StoreAllot storeAllot) {
    }

    public Page<StoreAllotDto> findPage(Pageable pageable, StoreAllotQuery storeAllotQuery) {
        Page<StoreAllotDto> page = storeAllotMapper.findPage(pageable, storeAllotQuery);
        cacheUtils.initCacheInput(page.getContent());
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



    public StoreAllotForm findFormWithoutStoreAllotDetails(StoreAllotForm storeAllotForm) {
       if(storeAllotForm.isCreate()){
           return storeAllotForm;
       }
       StoreAllot storeAllot=storeAllotMapper.findOne(storeAllotForm.getId());
       StoreAllotForm result= BeanUtil.map(storeAllot, StoreAllotForm.class);
       cacheUtils.initCacheInput(result);
       return result;
    }

    public StoreAllot saveStoreAllot(StoreAllotForm storeAllotForm, String cloudSynId) {
        StoreAllot storeAllot = new StoreAllot();
        ReflectionUtil.copyProperties(storeAllotForm, storeAllot);
        String maxBusinessId = storeAllotMapper.findMaxBusinessId(LocalDate.now());
        storeAllot.setBusinessId(IdUtils.getNextBusinessId(maxBusinessId));
        storeAllot.setBillDate(LocalDate.now());
        storeAllot.setCloudSynId(cloudSynId);
        storeAllotMapper.save(storeAllot);
        return storeAllot;
    }

    public StoreAllot saveForm(StoreAllotForm storeAllotForm) {
        //大库调拨单只允许新增和删除，不能修改
        if(!storeAllotForm.isCreate()){
            throw new ServiceException("error.storeAllot.cantEdit");
        }

        String cloudSynId = null;

        if(storeAllotForm.getSyn()){
            //TODO 金蝶接口调用，调用成功之后设置cloudSynId
            // cloudSynId=
        }

        StoreAllot storeAllot = saveStoreAllot(storeAllotForm, cloudSynId);

        saveStoreAllotDetails(storeAllot.getId(), storeAllotForm.getStoreAllotDetailFormList());

        ExpressOrder expressOrder = saveExpressOrder(storeAllot, storeAllotForm);

        storeAllot.setExpressOrderId(expressOrder.getId());
        storeAllotMapper.update(storeAllot);

        return storeAllot;
    }

    private void saveStoreAllotDetails(String storeAllotId, List<StoreAllotDetailForm> storeAllotDetailFormList) {
//        storeAllotDetailMapper.deleteByStoreAllotId(storeAllotId);
        if(storeAllotDetailFormList == null){
            return;
        }

        List<StoreAllotDetail> toBeSaved = Lists.newArrayList();
        for(StoreAllotDetailForm each : storeAllotDetailFormList){
            if(each == null || each.getBillQty() == null || each.getBillQty() <=0){
                continue;
            }
            StoreAllotDetail storeAllotDetail = BeanUtil.map(each, StoreAllotDetail.class);
            storeAllotDetail.setStoreAllotId(storeAllotId);
            toBeSaved.add(storeAllotDetail);
        }
        if(!toBeSaved.isEmpty()){
            storeAllotDetailMapper.batchSave(toBeSaved);
        }

    }

    private ExpressOrder saveExpressOrder(StoreAllot storeAllot, StoreAllotForm storeAllotForm) {
        //增加快递单信息
        ExpressOrder expressOrder = new ExpressOrder();
        expressOrder.setExtendBusinessId(storeAllot.getBusinessId());
        expressOrder.setExtendId(storeAllot.getId());
        expressOrder.setExtendType(ExpressOrderTypeEnum.大库调拨.name());
        expressOrder.setExpressCompanyId(storeAllotForm.getExpressCompanyId());
        expressOrder.setFromDepotId(storeAllot.getFromStoreId());
        expressOrder.setToDepotId(storeAllot.getToStoreId());
        expressOrder.setPrintDate(storeAllot.getBillDate());
        expressOrder.setOutCode(storeAllot.getOutCode());

        Depot toStore = depotMapper.findOne(storeAllot.getToStoreId());
        expressOrder.setAddress(toStore.getAddress());
        expressOrder.setMobilePhone(toStore.getMobilePhone());
        expressOrder.setContator(toStore.getContator());

        Integer totalBillQty = 0;
        Integer mobileQty = 0;
        for(int i=storeAllotForm.getStoreAllotDetailFormList().size()-1;i>=0;i--){
            StoreAllotDetailForm storeAllotDetailForm = storeAllotForm.getStoreAllotDetailFormList().get(i);
            if(storeAllotDetailForm.getBillQty()!=null && storeAllotDetailForm.getBillQty()>0) {
                totalBillQty = totalBillQty + storeAllotDetailForm.getBillQty();
                Product product = productMapper.findOne(storeAllotDetailForm.getProductId());
                if(product!=null && product.getHasIme()) {
                    mobileQty = mobileQty + storeAllotDetailForm.getBillQty();
                }
            }
        }
        expressOrder.setTotalQty(totalBillQty);
        expressOrder.setMobileQty(mobileQty);

        //设置需要打印的快递单个数
        Integer expressPrintQty = 0;
        if (ShipTypeEnum.总部发货.name().equals(storeAllot.getShipType())) {
            expressPrintQty = getExpressPrintQty(totalBillQty);
        }
        expressOrder.setExpressPrintQty(expressPrintQty);
        expressOrderMapper.save(expressOrder);
        return expressOrder;

    }


    public static Integer getExpressPrintQty(Integer totalBillQty) {
        //TODO 需要完善该方法，
        //String companyName= RequestUtils.getCompanyName();
        Integer expressPrintQty = 1;
//        if(CompanyNameEnum.JXOPPO.name().equals(companyName)){
//            expressPrintQty=Const.OPPO_ORDER_EXPRESS_PRODUCT_QTY;
//        }else if(CompanyNameEnum.JXVIVO.name().equals(companyName)) {
//            expressPrintQty = Const.VIVO_ORDER_EXPRESS_PRODUCT_QTY;
//        }else if(CompanyNameEnum.JXIMOO.name().equals(companyName)){
//            expressPrintQty = Const.IMOO_ORDER_EXPRESS_PRODUCT_QTY;
//        }else{
//            expressPrintQty=Const.LX_ORDER_EXPRESS_PRODUCT_QTY;
//        }
        if(0 == totalBillQty % expressPrintQty){
            expressPrintQty = totalBillQty / expressPrintQty;
        } else{
            expressPrintQty = totalBillQty / expressPrintQty + 1;
        }
        return expressPrintQty;
    }


    public StoreAllotDto findStoreAllotDtoById(String id) {
        StoreAllotDto result = storeAllotMapper.findStoreAllotDtoById(id);
        cacheUtils.initCacheInput(result);
        return result;

    }
}
