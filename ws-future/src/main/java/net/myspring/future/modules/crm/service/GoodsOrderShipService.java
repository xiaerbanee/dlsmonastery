package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestErrorField;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.ExpressOrderExtendTypeEnum;
import net.myspring.future.common.enums.GoodsOrderStatusEnum;
import net.myspring.future.common.enums.NetTypeEnum;
import net.myspring.future.common.enums.ShipTypeEnum;
import net.myspring.future.common.utils.ExpressUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.PricesystemDetail;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.basic.mapper.PricesystemDetailMapper;
import net.myspring.future.modules.basic.mapper.ProductMapper;
import net.myspring.future.modules.crm.domain.*;
import net.myspring.future.modules.crm.mapper.*;
import net.myspring.future.modules.crm.web.form.GoodsOrderDetailForm;
import net.myspring.future.modules.crm.web.form.GoodsOrderForm;
import net.myspring.future.modules.crm.web.form.GoodsOrderShipForm;
import net.myspring.future.modules.crm.web.query.GoodsOrderQuery;
import net.myspring.future.modules.crm.web.query.ProductImeShipQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class GoodsOrderShipService {
    @Autowired
    private GoodsOrderMapper goodsOrderMapper;
    @Autowired
    private GoodsOrderDetailMapper goodsOrderDetailMapper;
    @Autowired
    private GoodsOrderImeMapper goodsOrderImeMapper;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductImeMapper productImeMapper;
    @Autowired
    private ExpressOrderMapper expressOrderMapper;
    @Autowired
    private ExpressMapper expressMapper;
    @Autowired
    private RedisTemplate redisTemplate;


    public RestResponse shipCheck(GoodsOrderShipForm goodsOrderShipForm) {
        RestResponse restResponse = new RestResponse("发货串码正确", ResponseCodeEnum.valid.name(),true);
        Integer totalShouldShipQty = 0;
        Integer totalShippedQty = 0;
        GoodsOrder goodsOrder = goodsOrderMapper.findOne(goodsOrderShipForm.getId());
        return null;
    }



    public void print(String goodsOrderId) {
        GoodsOrder goodsOrder = goodsOrderMapper.findOne(goodsOrderId);
        ExpressOrder expressOrder = expressOrderMapper.findOne(goodsOrder.getExpressOrderId());
        if (expressOrder != null) {
            if (expressOrder.getOutPrintDate() == null) {
                expressOrder.setOutPrintDate(LocalDateTime.now());
            }
            expressOrderMapper.update(expressOrder);
        }
    }

    public void shipPrint(String goodsOrderId) {
        GoodsOrder goodsOrder = goodsOrderMapper.findOne(goodsOrderId);
        ExpressOrder expressOrder = expressOrderMapper.findOne(goodsOrder.getExpressOrderId());
        if (expressOrder != null) {
            if (expressOrder.getExpressPrintDate() == null) {
                expressOrder.setExpressPrintDate(LocalDateTime.now());
            }
            expressOrderMapper.update(expressOrder);
        }
    }



    public void ship(GoodsOrderShipForm goodsOrderShipForm) {
        GoodsOrder goodsOrder = goodsOrderMapper.findOne(goodsOrderShipForm.getGoodsOrderId());
        Map<String, GoodsOrderDetail> goodsOrderDetailMap = Maps.newHashMap();
        List<GoodsOrderDetail> goodsOrderDetailList = goodsOrderDetailMapper.findByGoodsOrderId(goodsOrderShipForm.getGoodsOrderId());
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
                goodsOrderDetailMapper.update(goodsOrderDetail);
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

            List<ProductIme> productImeList = productImeMapper.findShipList(productImeShipQuery);
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
                        goodsOrderImeMapper.save(goodsOrderIme);
                        //串码调拨
                        productIme.setDepotId(goodsOrder.getShopId());
                        productIme.setRetailShopId(goodsOrder.getShopId());
                        productImeMapper.update(productIme);
                    }
                }
            }
        }
        for (GoodsOrderDetail goodsOrderDetail : goodsOrderDetailMap.values()) {
            goodsOrderDetailMapper.update(goodsOrderDetail);
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
        goodsOrderMapper.update(goodsOrder);
        //设置快递单
        if(CollectionUtil.isNotEmpty(goodsOrderShipForm.getExpressList())) {

        }
    }


    @Transactional
    public void shipBack(String goodsOrderId) {
        GoodsOrder goodsOrder = goodsOrderMapper.findOne(goodsOrderId);
        //串码调拨
        List<GoodsOrderIme> goodsOrderImeList = goodsOrderImeMapper.findByGoodsOrderId(goodsOrderId);
        List<ProductIme> productImeList = productImeMapper.findByIds(CollectionUtil.extractToList(goodsOrderImeList,"productImeId"));
        Map<String,ProductIme> productImeMap = CollectionUtil.extractToMap(productImeList,"id");
        if (CollectionUtil.isNotEmpty(goodsOrderImeList)) {
            for (GoodsOrderIme goodsOrderIme : goodsOrderImeList) {
                ProductIme productIme = productImeMap.get(goodsOrderIme.getProductImeId());
                productIme.setDepotId(goodsOrder.getStoreId());
                productIme.setRetailShopId(goodsOrder.getStoreId());
                productImeMapper.update(productIme);
            }
            goodsOrderImeMapper.logicDeleteByIds(CollectionUtil.extractToList(goodsOrderImeList, "id"));
        }
        for (GoodsOrderDetail goodsOrderDetail : goodsOrderDetailMapper.findByGoodsOrderId(goodsOrderId)) {
            goodsOrderDetail.setShippedQty(0);
            goodsOrderDetailMapper.update(goodsOrderDetail);
        }
        goodsOrder.setStatus(GoodsOrderStatusEnum.待发货.name());
        goodsOrder.setShipDate(null);
        //删除快递单
        List<Express> expressList = expressMapper.findByExpressOrderId(goodsOrder.getExpressOrderId());
        expressMapper.deleteByIds(CollectionUtil.extractToList(expressList, "id"));
        goodsOrderMapper.save(goodsOrder);
    }

}
