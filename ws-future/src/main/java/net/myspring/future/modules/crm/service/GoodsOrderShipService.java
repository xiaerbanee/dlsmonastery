package net.myspring.future.modules.crm.service;

import com.google.common.collect.Maps;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.GoodsOrderStatusEnum;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.mapper.ProductMapper;
import net.myspring.future.modules.basic.repository.*;
import net.myspring.future.modules.crm.domain.*;
import net.myspring.future.modules.crm.mapper.ExpressOrderMapper;
import net.myspring.future.modules.crm.mapper.ProductImeMapper;
import net.myspring.future.modules.crm.web.form.GoodsOrderShipForm;
import net.myspring.future.modules.crm.web.query.ProductImeShipQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    private ProductMapper productMapper;

    @Autowired
    private ProductImeRepository productImeRepository;


    @Autowired
    private ExpressOrderRepository expressOrderRepository;

    @Autowired
    private ExpressRepository expressRepository;
    @Autowired
    private RedisTemplate redisTemplate;


    public RestResponse shipCheck(GoodsOrderShipForm goodsOrderShipForm) {
        RestResponse restResponse = new RestResponse("发货串码正确", ResponseCodeEnum.valid.name(),true);
        Integer totalShouldShipQty = 0;
        Integer totalShippedQty = 0;
        GoodsOrder goodsOrder = goodsOrderRepository.findOne(goodsOrderShipForm.getId());
        return null;
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
        List<Product> productList  = productMapper.findByIds(CollectionUtil.extractToList(goodsOrderDetailList,"productId"));
        Map<String,Product> productMap = CollectionUtil.extractToMap(productList,"id");
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
            //如果串码是以86开头，有可能扫描的结果带有字母，此时需要将串码中的字符串去除，只取出数字部分与数据库匹配
            List<String> imeList = goodsOrderShipForm.getImeList();
            List<String> boxImeList = goodsOrderShipForm.getBoxImeList();
            for (int i = 0; i < imeList.size(); i++) {
                String ime = imeList.get(i);
                if (ime.startsWith("86")) {
                    imeList.set(i, StringUtils.getNumberStr(ime));
                }
            }
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
        if(CollectionUtil.isNotEmpty(goodsOrderShipForm.getExpressList())) {

        }
    }

    public void shipBack(String goodsOrderId) {
        GoodsOrder goodsOrder = goodsOrderRepository.findOne(goodsOrderId);
        //串码调拨
        List<GoodsOrderIme> goodsOrderImeList = goodsOrderImeRepository.findByGoodsOrderId(goodsOrderId);
        List<ProductIme> productImeList = productImeRepository.findAll(CollectionUtil.extractToList(goodsOrderImeList,"productImeId"));
        Map<String,ProductIme> productImeMap = CollectionUtil.extractToMap(productImeList,"id");
        if (CollectionUtil.isNotEmpty(goodsOrderImeList)) {
            for (GoodsOrderIme goodsOrderIme : goodsOrderImeList) {
                ProductIme productIme = productImeMap.get(goodsOrderIme.getProductImeId());
                productIme.setDepotId(goodsOrder.getStoreId());
                productIme.setRetailShopId(goodsOrder.getStoreId());
                productImeRepository.save(productIme);
            }
            goodsOrderImeRepository.logicDeleteByIds(CollectionUtil.extractToList(goodsOrderImeList, "id"));
        }
        for (GoodsOrderDetail goodsOrderDetail : goodsOrderDetailRepository.findByGoodsOrderId(goodsOrderId)) {
            goodsOrderDetail.setShippedQty(0);
            goodsOrderDetailRepository.save(goodsOrderDetail);
        }
        goodsOrder.setStatus(GoodsOrderStatusEnum.待发货.name());
        goodsOrder.setShipDate(null);
        //删除快递单
        List<Express> expressList = expressRepository.findByExpressOrderId(goodsOrder.getExpressOrderId());
        expressRepository.logicDeleteByIds(CollectionUtil.extractToList(expressList, "id"));
        goodsOrderRepository.save(goodsOrder);
    }

}
