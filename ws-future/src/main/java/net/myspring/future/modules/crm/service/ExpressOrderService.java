package net.myspring.future.modules.crm.service;

import net.myspring.future.common.enums.ExpressOrderTypeEnum;
import net.myspring.future.common.enums.ShipTypeEnum;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.basic.mapper.ExpressCompanyMapper;
import net.myspring.future.modules.basic.service.ProductService;
import net.myspring.future.modules.crm.domain.Express;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.domain.StoreAllot;
import net.myspring.future.modules.crm.mapper.ExpressMapper;
import net.myspring.future.modules.crm.mapper.ExpressOrderMapper;
import net.myspring.future.modules.crm.web.form.StoreAllotDetailForm;
import net.myspring.future.modules.crm.web.form.StoreAllotForm;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional(readOnly=false)
@Service
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

    public ExpressOrder findOne(String id){
        ExpressOrder expressOrder = expressOrderMapper.findOne(id);
        return expressOrder;
    }

    public Page<ExpressOrder> findPage(Pageable pageable, Map<String, Object> map) {
        Page<ExpressOrder> page = expressOrderMapper.findPage(pageable, map);
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
        String companyName= SecurityUtils.getCompanyName();
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
}
