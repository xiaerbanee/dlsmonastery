package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import com.mongodb.gridfs.GridFSFile;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.enums.ExpressOrderTypeEnum;
import net.myspring.future.common.enums.ShipTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.basic.mapper.ExpressCompanyMapper;
import net.myspring.future.modules.basic.service.ProductService;
import net.myspring.future.modules.crm.domain.Express;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.future.modules.crm.domain.StoreAllot;
import net.myspring.future.modules.crm.dto.ExpressOrderDto;
import net.myspring.future.modules.crm.mapper.ExpressMapper;
import net.myspring.future.modules.crm.mapper.ExpressOrderMapper;
import net.myspring.future.modules.crm.web.form.ExpressOrderForm;
import net.myspring.future.modules.crm.web.form.StoreAllotDetailForm;
import net.myspring.future.modules.crm.web.form.StoreAllotForm;
import net.myspring.future.modules.crm.web.query.ExpressOrderQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
@Transactional
public class ExpressOrderService {

    @Autowired
    private ExpressOrderMapper expressOrderMapper;
    @Autowired
    private ExpressCompanyMapper expressCompanyMapper;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private ExpressMapper expressMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private GridFsTemplate tempGridFsTemplate;
    @Autowired
    private CacheUtils cacheUtils;

    public ExpressOrder findOne(String id){
        ExpressOrder expressOrder = expressOrderMapper.findOne(id);
        return expressOrder;
    }

    public Page<ExpressOrderDto> findPage(Pageable pageable, ExpressOrderQuery expressOrderQuery) {
        Page<ExpressOrderDto> page = expressOrderMapper.findPage(pageable, expressOrderQuery);
        page.getContent().stream().filter(each-> (each.getWeight()!=null && each.getTotalQty()!=null&&each.getTotalQty()>0)).forEach(each -> each.setAverageWeight(each.getWeight().divide(new BigDecimal(each.getTotalQty()),2, BigDecimal.ROUND_HALF_UP)));

        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public void resetPrintStatus(ExpressOrder expressOrder){
        expressOrder.setExpressPrintDate(null);
        expressOrder.setOutPrintDate(null);
        expressOrderMapper.update(expressOrder);
    }

    public void update(ExpressOrder expressOrder){
        expressOrderMapper.update(expressOrder);
    }

    public void save(ExpressOrder expressOrder){
        expressOrderMapper.save(expressOrder);
    }

    public void save(String extendType,String extendId,String expressCodes,String expressCompanyId) {
        ExpressOrder expressOrder = expressOrderMapper.findByExtendIdAndType(extendId, extendType);
        expressOrder.setExpressCompanyId(expressCompanyId);
        expressOrder.setExpressCodes(expressCodes);

        List<String> expressCodeList = StringUtils.getSplitList(expressCodes, "");
        List<Express> expresses = expressMapper.findByExpressOrderId(expressOrder.getId());
        if(CollectionUtil.isNotEmpty(expresses)) {
            for(int i = expresses.size()-1;i>=0;i--) {
                Express express = expresses.get(i);
                if(!expressCodeList.contains(express.getCode())) {
                    express.setEnabled(false);
                    expressMapper.update(express);
                    expresses.remove(i);
                }
            }
        }
        Map<String,Express> expressMap = CollectionUtil.extractToMap(expresses, "code");
        for(String code:expressCodeList) {
            if(!expressMap.containsKey(code)){
                Express express = new Express();
                express.setExpressOrderId(expressOrder.getId());
                express.setCode(code);
                expressMapper.save(express);
            }
        }
        expressOrderMapper.update(expressOrder);
    }

    public ExpressOrder saveExpressOrder(StoreAllot storeAllot, StoreAllotForm storeAllotForm) {
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
                    Product product = productService.findOne(storeAllotDetailForm.getProductId());
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

    public ExpressOrder saveOrUpdate(ExpressOrder expressOrder) {
        if(expressOrder == null){
            return null;
        }
        if(StringUtils.isNotBlank(expressOrder.getId())){
            expressOrderMapper.update(expressOrder);
        }else{
            expressOrderMapper.save(expressOrder);
        }
        return expressOrder;
    }


    public ExpressOrder saveOrUpdateExpressOrder(GoodsOrder goodsOrder) {
        ExpressOrder expressOrder=null;
        if(goodsOrder.getExpressOrderId()!=null){
            expressOrder = expressOrderMapper.findOne(goodsOrder.getExpressOrderId());
        }else{
            expressOrder = new ExpressOrder();
        }
        expressOrder.setExtendType(ExpressOrderTypeEnum.手机订单.name());
        Depot shop=depotMapper.findOne(goodsOrder.getShopId());
        expressOrder.setContator(shop.getContator());
        expressOrder.setAddress(shop.getAddress());
        expressOrder.setMobilePhone(shop.getMobilePhone());
        expressOrder.setToDepotId(shop.getId());
        expressOrder.setShipType(goodsOrder.getShipType());
        expressOrder.setExtendId(goodsOrder.getId());
        expressOrder = saveOrUpdate(expressOrder);
        return expressOrder;
    }

    public ExpressOrder reCalcAndUpdateExpressCodes(String expressOrderId) {

        ExpressOrder eo = expressOrderMapper.findOne(expressOrderId);
        eo.setExpressCodes(CollectionUtil.extractAndJoin( expressMapper.findByExpressOrderId(expressOrderId), "code"));
        expressOrderMapper.update(eo);
        return eo;
    }

    public ExpressOrderForm findForm(ExpressOrderForm expressOrderForm) {

        if(expressOrderForm.isCreate()){
            throw new ServiceException("expressOrderCantNew");
        }

        ExpressOrderDto expressOrderDto = expressOrderMapper.findDto(expressOrderForm.getId());
        cacheUtils.initCacheInput(expressOrderDto);
        ExpressOrderForm result = BeanUtil.map(expressOrderDto, ExpressOrderForm.class);

        return result;

    }

    public void save(ExpressOrderForm expressOrderForm) {
        if(expressOrderForm.isCreate()){
            throw new ServiceException("expressOrderCantNew");
        }
        ExpressOrder eo = expressOrderMapper.findOne(expressOrderForm.getId());
        ReflectionUtil.copyProperties(expressOrderForm, eo);
        expressOrderMapper.update(eo);
    }

    public void resetPrintStatus(String id) {
        ExpressOrder eo = expressOrderMapper.findOne(id);

        eo.setExpressPrintDate(null);
        eo.setOutPrintDate(null);
        expressOrderMapper.update(eo);
    }


    public String genEMSDataFileForExport(ExpressOrderQuery expressOrderQuery) {
        return null;

    }

    public String genDataFileForExport(ExpressOrderQuery expressOrderQuery) {
        Workbook workbook = new SXSSFWorkbook(10000);
        Page<ExpressOrderDto> page=findPage(new PageRequest(0,10000), expressOrderQuery);

        List<SimpleExcelColumn> simpleExcelColumnList=Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"extendBusinessId","订单号"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"extendType","订单类型"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"expressCompanyName","快递公司"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"fromDepotName","发货方"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"toDepotName","收货方"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"shouldGet","应收运费"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"shouldPay","应付运费"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"realPay","实付运费"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"contator","联系人"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"mobilePhone","联系电话"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"address","地址"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"expressPrintDate","快递打印时间"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"outPrintDate","出库单打印时间"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"createdDate","创建时间"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"averageWeight","单机重量"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"remarks","备注"));

        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("快递打印列表",page.getContent(), simpleExcelColumnList);
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"快递打印列表"+ UUID.randomUUID()+".xlsx",simpleExcelSheet);
        ByteArrayInputStream byteArrayInputStream=ExcelUtils.doWrite(simpleExcelBook.getWorkbook(),simpleExcelBook.getSimpleExcelSheets());
        GridFSFile gridFSFile = tempGridFsTemplate.store(byteArrayInputStream,simpleExcelBook.getName(),"application/octet-stream; charset=utf-8", RequestUtils.getDbObject());
        return StringUtils.toString(gridFSFile.getId());

    }
}
