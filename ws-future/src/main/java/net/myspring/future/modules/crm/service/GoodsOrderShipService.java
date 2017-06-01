package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestErrorField;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.ExpressOrderTypeEnum;
import net.myspring.future.common.enums.GoodsOrderStatusEnum;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.repository.*;
import net.myspring.future.modules.crm.domain.*;
import net.myspring.future.modules.crm.manager.ExpressOrderManager;
import net.myspring.future.modules.crm.repository.*;
import net.myspring.future.modules.crm.web.form.GoodsOrderDetailForm;
import net.myspring.future.modules.crm.web.form.GoodsOrderForm;
import net.myspring.future.modules.crm.web.form.GoodsOrderShipForm;
import net.myspring.future.modules.crm.web.query.ProductImeShipQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class GoodsOrderShipService {
    @Autowired
    private GoodsOrderRepository goodsOrderRepository;
    @Autowired
    private GoodsOrderDetailRepository goodsOrderDetailRepository;
    @Autowired
    private GoodsOrderImeRepository goodsOrderImeRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductImeRepository productImeRepository;
    @Autowired
    private ExpressOrderRepository expressOrderRepository;
    @Autowired
    private ExpressOrderManager expressOrderManager;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private ExpressRepository expressRepository;


    public Map<String,Object> shipCheck(GoodsOrderShipForm goodsOrderShipForm) {
       RestResponse restResponse =  new RestResponse("valid",ResponseCodeEnum.valid.name());
        Integer totalShouldShipQty = 0;
        Integer totalShippedQty = 0;
        GoodsOrder goodsOrder = goodsOrderRepository.findOne(goodsOrderShipForm.getGoodsOrderId());
        List<GoodsOrderDetail> goodsOrderDetailList  = goodsOrderDetailRepository.findByGoodsOrderId(goodsOrder.getId());
        Map<String,GoodsOrderDetail> goodsOrderDetailMap  = CollectionUtil.extractToMap(goodsOrderDetailList,"id");
        Map<String,Product> productMap = productRepository.findMap(CollectionUtil.extractToList(goodsOrderDetailList,"productId"));
        for (GoodsOrderDetail goodsOrderDetail : goodsOrderDetailList) {
            if (goodsOrderDetail.getBillQty() > 0 && productMap.get(goodsOrderDetail.getProductId()).getHasIme()) {
                goodsOrderDetail.setShipQty(0);
            }
        }
        ProductImeShipQuery productImeShipQuery = new ProductImeShipQuery();
        productImeShipQuery.setDepotId(goodsOrder.getStoreId());
        productImeShipQuery.setImeList(goodsOrderShipForm.getImeList());
        productImeShipQuery.setBoxImeList(goodsOrderShipForm.getBoxImeList());
        List<ProductIme> productImeList = productImeRepository.findShipList(productImeShipQuery);
        Set<String> boxImeSet = Sets.newHashSet();
        Set<String> imeSet = Sets.newHashSet();
        for (ProductIme productIme : productImeList) {
            boxImeSet.add(productIme.getBoxIme());
            imeSet.add(productIme.getIme());
            if (StringUtils.isNotBlank(productIme.getMeid())) {
                imeSet.add(productIme.getMeid());
            }
            if (StringUtils.isNotBlank(productIme.getIme2())) {
                imeSet.add(productIme.getIme2());
            }
            Product product = productMap.get(productIme.getProductId());
            if (!goodsOrderDetailMap.containsKey(product.getId())) {
                restResponse.getErrors().add(new RestErrorField("箱号：" + productIme.getBoxIme() +"，串码：" + productIme.getIme() + "，货品为：" + product.getName() + "，不在订货范围内","ime_error","imeStr"));
            } else {
                goodsOrderDetailMap.get(product.getId()).setShipQty(goodsOrderDetailMap.get(product.getId()).getShipQty() + 1);
            }
        }
        for (String boxIme : goodsOrderShipForm.getBoxImeList()) {
            if (!boxImeSet.contains(boxIme)) {
                restResponse.getErrors().add(new RestErrorField("箱号：" + boxIme  + "，在仓库不存在","box_ime_error","boxIme"));
            }
        }
        for (String ime : goodsOrderShipForm.getImeList()) {
            if (!imeSet.contains(ime)) {
                restResponse.getErrors().add(new RestErrorField("串码：" + ime + "，在仓库不存在","ime_error","ime"));
            }
        }
        for (GoodsOrderDetail goodsOrderDetail : goodsOrderDetailList) {
            totalShouldShipQty = totalShouldShipQty +goodsOrderDetail.getRealBillQty();
            totalShippedQty = totalShippedQty + goodsOrderDetail.getShippedQty() + goodsOrderDetail.getShipQty();
            Integer qty = goodsOrderDetail.getShippedQty() + goodsOrderDetail.getShipQty();
            Integer realBillQty=goodsOrderDetail.getRealBillQty();
            if (qty > realBillQty) {
                StringBuilder errorMessage = new StringBuilder("货品:"+ productMap.get(goodsOrderDetail.getProductId()).getName() +"总发货数："+ qty+ "大于实际开单数："  + realBillQty + "串码：");
                //显示发货不对的串码
                for (ProductIme productIme : productImeList) {
                    if (productIme.getProductId().equals(goodsOrderDetail.getProductId())) {
                        errorMessage.append(productIme.getIme()).append(CharConstant.TAB);
                    }
                }
                restResponse.getErrors().add(new RestErrorField(errorMessage.toString(),"qty_error","ime"));
            }
        }
        Map<String, Object> result = Maps.newHashMap();
        if (!totalShouldShipQty.equals(totalShippedQty)) {
            result.put("warnMsg", "货品总开单数和实际发货数不一致");
        }
        Map<String, Integer> shipQtyMap = Maps.newHashMap();
        for (GoodsOrderDetail goodsOrderDetail : goodsOrderDetailMap.values()) {
            shipQtyMap.put(goodsOrderDetail.getProductId(), goodsOrderDetail.getShipQty());
        }
        result.put("restResponse", restResponse);
        result.put("shipQtyMap", shipQtyMap);
        result.put("totalQty", productImeList.size());
        return result;
    }

    public void print(String goodsOrderId) {
        GoodsOrder goodsOrder = goodsOrderRepository.findOne(goodsOrderId);
        ExpressOrder expressOrder = expressOrderRepository.findOne(goodsOrder.getExpressOrderId());
        if (expressOrder != null) {
            if (expressOrder.getOutPrintDate() == null) {
                expressOrder.setOutPrintDate(LocalDateTime.now());
            }
            expressOrderRepository.save(expressOrder);
        }
    }

    public void shipPrint(String goodsOrderId) {
        GoodsOrder goodsOrder = goodsOrderRepository.findOne(goodsOrderId);
        ExpressOrder expressOrder = expressOrderRepository.findOne(goodsOrder.getExpressOrderId());
        if (expressOrder != null) {
            if (expressOrder.getExpressPrintDate() == null) {
                expressOrder.setExpressPrintDate(LocalDateTime.now());
            }
            expressOrderRepository.save(expressOrder);
        }
    }



    public void ship(GoodsOrderShipForm goodsOrderShipForm) {
        GoodsOrder goodsOrder = goodsOrderRepository.findOne(goodsOrderShipForm.getGoodsOrderId());
        Map<String, GoodsOrderDetail> goodsOrderDetailMap = Maps.newHashMap();
        List<GoodsOrderDetail> goodsOrderDetailList = goodsOrderDetailRepository.findByGoodsOrderId(goodsOrderShipForm.getGoodsOrderId());
        Map<String,Product> productMap = productRepository.findMap(CollectionUtil.extractToList(goodsOrderDetailList,"productId"));
        for (GoodsOrderDetail goodsOrderDetail : goodsOrderDetailList) {
            if (goodsOrderDetail.getRealBillQty() > 0 && goodsOrderDetail.getRealBillQty() != goodsOrderDetail.getShippedQty()) {
                goodsOrderDetailMap.put(goodsOrderDetail.getProductId(), goodsOrderDetail);
            }
            //对没有串码的货品全部发货
            Product product = productMap.get(goodsOrderDetail.getProductId());
            if (!product.getHasIme() && goodsOrderDetail.getShippedQty() == 0) {
                goodsOrderDetail.setShippedQty(goodsOrderDetail.getRealBillQty());
                goodsOrderDetailRepository.save(goodsOrderDetail);
            }
        }
        //搜索串码
        if (StringUtils.isNotBlank(goodsOrderShipForm.getBoxImeStr()) || StringUtils.isNotBlank(goodsOrderShipForm.getImeStr())) {
            ProductImeShipQuery productImeShipQuery = new ProductImeShipQuery();
            productImeShipQuery.setBoxImeList(goodsOrderShipForm.getBoxImeList());
            productImeShipQuery.setImeList(goodsOrderShipForm.getImeList());
            productImeShipQuery.setDepotId(goodsOrder.getStoreId());

            List<ProductIme> productImeList = productImeRepository.findShipList(productImeShipQuery);
            if (CollectionUtil.isNotEmpty(productImeList)) {
                for (ProductIme productIme : productImeList) {
                    String productId = productIme.getProductId();
                    GoodsOrderDetail goodsOrderDetail = goodsOrderDetailMap.get(productId);
                    if (goodsOrderDetail != null) {
                        goodsOrderDetail.setShippedQty(goodsOrderDetail.getShippedQty() == null ? 0 : goodsOrderDetail.getShippedQty() + 1);
                        GoodsOrderIme goodsOrderIme = new GoodsOrderIme();
                        goodsOrderIme.setGoodsOrderId(goodsOrder.getId());
                        goodsOrderIme.setProductImeId(productIme.getId());
                        goodsOrderIme.setProductId(productIme.getProductId());
                        goodsOrderIme.setRemarks(goodsOrderShipForm.getRemarks());
                        goodsOrderImeRepository.save(goodsOrderIme);
                        //串码调拨
                        productIme.setDepotId(goodsOrder.getShopId());
                        productIme.setRetailShopId(goodsOrder.getShopId());
                        productImeRepository.save(productIme);
                    }
                }
            }
        }
        for (GoodsOrderDetail goodsOrderDetail : goodsOrderDetailMap.values()) {
            goodsOrderDetailRepository.save(goodsOrderDetail);
        }
        //如果所有发货完成，修改订单状态
        boolean isAllShipped = true;
        for (GoodsOrderDetail goodsOrderDetail : goodsOrderDetailMap.values()) {
            Integer realBillQty = goodsOrderDetail.getRealBillQty();
            if (!goodsOrderDetail.getShippedQty().equals(realBillQty)) {
                isAllShipped = false;
                break;
            }
        }
        if (isAllShipped) {
            goodsOrder.setStatus(GoodsOrderStatusEnum.待签收.name());
        } else {
            goodsOrder.setStatus(GoodsOrderStatusEnum.待发货.name());
        }
        goodsOrder.setShipDate(LocalDateTime.now());
        goodsOrderRepository.save(goodsOrder);
        //设置快递单
        ExpressOrder expressOrder = expressOrderRepository.findOne(goodsOrder.getExpressOrderId());
        expressOrderManager.save(ExpressOrderTypeEnum.手机订单.name(),goodsOrder.getId(),goodsOrderShipForm.getExpressStr(),expressOrder.getExpressCompanyId());
    }


    public GoodsOrder sreturn(GoodsOrderForm goodsOrderForm) {
        GoodsOrder goodsOrder = goodsOrderRepository.findOne(goodsOrderForm.getId());
        Map<String,GoodsOrderDetail> goodsOrderDetailMap = goodsOrderDetailRepository.findMap(CollectionUtil.extractToList(goodsOrderForm.getGoodsOrderDetailFormList(),"goodsOrderDetailId"));
        BigDecimal amount = BigDecimal.ZERO;
        for (GoodsOrderDetailForm goodsOrderDetailForm:goodsOrderForm.getGoodsOrderDetailFormList()) {
            GoodsOrderDetail goodsOrderDetail = goodsOrderDetailMap.get(goodsOrderDetailForm.getGoodsOrderDetailId());
            if (goodsOrderDetailForm.getReturnQty() != null && goodsOrderDetailForm.getReturnQty() > 0) {
                goodsOrderDetail.setReturnQty(goodsOrderDetail.getReturnQty());
                goodsOrderDetailRepository.save(goodsOrderDetail);
            }
            amount  = amount.add(new BigDecimal( goodsOrderDetail.getRealBillQty()).multiply(goodsOrderDetail.getPrice()));
        }
        goodsOrder.setAmount(amount);
        goodsOrderRepository.save(goodsOrder);
        goodsOrderRepository.save(goodsOrder);
        return goodsOrder;
    }

    public void sign(String goodsOrderId) {
        GoodsOrder goodsOrder = goodsOrderRepository.findOne(goodsOrderId);
        goodsOrder.setStatus(GoodsOrderStatusEnum.已完成.name());
        goodsOrderRepository.save(goodsOrder);
    }


    public void shipBack(String goodsOrderId) {
        GoodsOrder goodsOrder = goodsOrderRepository.findOne(goodsOrderId);
        Depot store = depotRepository.findOne(goodsOrder.getStoreId());
        List<GoodsOrderIme> goodsOrderImeList = goodsOrderImeRepository.findByGoodsOrderId(goodsOrderId);
        List<GoodsOrderDetail> goodsOrderDetailList  = goodsOrderDetailRepository.findByGoodsOrderId(goodsOrderId);
        Map<String,ProductIme> productImeMap = productImeRepository.findMap(CollectionUtil.extractToList(goodsOrderImeList,"productImeId"));
        //串码调拨
        if (CollectionUtil.isNotEmpty(goodsOrderImeList)) {
            List<ProductIme> productImes = Lists.newArrayList();
            for (GoodsOrderIme goodsOrderIme : goodsOrderImeList) {
                ProductIme productIme = productImeMap.get(goodsOrderIme.getProductImeId());
                productIme.setDepotId(goodsOrder.getStoreId());
                productIme.setRetailShopId(goodsOrder.getStoreId());
                productImeRepository.save(productIme);
                productImes.add(productIme);
            }
        }
        goodsOrderImeRepository.delete(goodsOrderImeList);
        for (GoodsOrderDetail goodsOrderDetail : goodsOrderDetailList) {
            goodsOrderDetail.setShippedQty(0);
        }
        goodsOrder.setStatus(GoodsOrderStatusEnum.待发货.name());
        goodsOrder.setShipDate(null);
        //删除快递单
        expressRepository.deleteByExpressOrderId(goodsOrder.getExpressOrderId());
        goodsOrderRepository.save(goodsOrder);
    }

}
