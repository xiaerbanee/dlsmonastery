package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestErrorField;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.ExpressOrderTypeEnum;
import net.myspring.future.common.enums.GoodsOrderStatusEnum;
import net.myspring.future.common.enums.NetTypeEnum;
import net.myspring.future.common.enums.ShipTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.ExpressUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.PricesystemDetail;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.PricesystemDetailRepository;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.future.modules.crm.domain.GoodsOrderDetail;
import net.myspring.future.modules.crm.dto.GoodsOrderDto;
import net.myspring.future.modules.crm.repository.ExpressOrderRepository;
import net.myspring.future.modules.crm.repository.GoodsOrderDetailRepository;
import net.myspring.future.modules.crm.repository.GoodsOrderRepository;
import net.myspring.future.modules.crm.web.form.GoodsOrderBillDetailForm;
import net.myspring.future.modules.crm.web.form.GoodsOrderBillForm;
import net.myspring.future.modules.crm.web.form.GoodsOrderDetailForm;
import net.myspring.future.modules.crm.web.form.GoodsOrderForm;
import net.myspring.future.modules.crm.web.query.GoodsOrderQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class GoodsOrderService {

    @Autowired
    private GoodsOrderRepository goodsOrderRepository;
    @Autowired
    private GoodsOrderDetailRepository goodsOrderDetailRepository;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private ExpressOrderRepository expressOrderRepository;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ExpressUtils expressUtils;
    @Autowired
    private PricesystemDetailRepository pricesystemDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<GoodsOrderDto> findAll(Pageable pageable, GoodsOrderQuery goodsOrderQuery) {
        Page<GoodsOrderDto> page = goodsOrderRepository.findAll(pageable, goodsOrderQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    //检测门店
    @Transactional(readOnly = true)
    public RestResponse validateShop(String shopId) {
        Depot shop = depotRepository.findOne(shopId);
        RestResponse restResponse = new RestResponse("有效门店", ResponseCodeEnum.valid.name(),true);
        if(StringUtils.isBlank(shop.getPricesystemId())) {
            restResponse.getErrors().add(new RestErrorField("没有价格体系","no_pricesystem","shopId"));
        }
        //检查当前客户是否有未处理订单
        GoodsOrderQuery goodsOrderQuery = new GoodsOrderQuery();
        goodsOrderQuery.setShopId(shop.getId());
        goodsOrderQuery.setStatus(GoodsOrderStatusEnum.待开单.name());
        List<GoodsOrderDto> goodsOrderDtoList = goodsOrderRepository.findAll(new PageRequest(0, 1), goodsOrderQuery).getContent();
        if (CollectionUtil.isNotEmpty(goodsOrderDtoList)) {
            restResponse.getErrors().add(new RestErrorField("门店有未处理的单据","exist_order_for_bill","shopId"));
        }
        return restResponse;
    }

    //保存及修改订单
    public GoodsOrder save(GoodsOrderForm goodsOrderForm) {
        Boolean isCreate = goodsOrderForm.isCreate();
        GoodsOrder goodsOrder;
        //保存订单
        if(isCreate) {
            goodsOrder = BeanUtil.map(goodsOrderForm,GoodsOrder.class);
            goodsOrder.setStoreId(getDefaultStoreId(goodsOrder));
            goodsOrder.setStatus(GoodsOrderStatusEnum.待开单.name());
            goodsOrderRepository.save(goodsOrder);
        } else {
            goodsOrder = goodsOrderRepository.findOne(goodsOrderForm.getId());
            ReflectionUtil.copyProperties(goodsOrderForm,goodsOrder);
            goodsOrderRepository.save(goodsOrder);
        }

        List<GoodsOrderDetail> goodsOrderDetailList  = goodsOrderDetailRepository.findByGoodsOrderId(goodsOrder.getId());
        Map<String,GoodsOrderDetail> goodsOrderDetailMap  = CollectionUtil.extractToMap(goodsOrderDetailList,"id");
        //保存订单明细
        BigDecimal amount = BigDecimal.ZERO;
        Depot shop = depotRepository.findOne(goodsOrder.getShopId());
        //价格体系
        List<PricesystemDetail> pricesystemDetailList = pricesystemDetailRepository.findByPricesystemId(shop.getPricesystemId());
        Map<String,PricesystemDetail> pricesystemDetailMap = CollectionUtil.extractToMap(pricesystemDetailList,"productId");

        for (int i = goodsOrderForm.getGoodsOrderDetailFormList().size() - 1; i >= 0; i--) {
            GoodsOrderDetailForm goodsOrderDetailForm = goodsOrderForm.getGoodsOrderDetailFormList().get(i);
            if(goodsOrderDetailForm.getQty()==null) {
                goodsOrderDetailForm.setQty(0);
            }
            if(goodsOrderDetailForm.isCreate()) {
                if (goodsOrderDetailForm.getQty() > 0) {
                    GoodsOrderDetail goodsOrderDetail = BeanUtil.map(goodsOrderDetailForm,GoodsOrderDetail.class);
                    goodsOrderDetail.setGoodsOrderId(goodsOrder.getId());
                    goodsOrderDetail.setBillQty(goodsOrderDetail.getQty());
                    PricesystemDetail pricesystemDetail = pricesystemDetailMap.get(goodsOrderDetail.getProductId());
                    goodsOrderDetail.setPrice(pricesystemDetail.getPrice());
                    goodsOrderDetailRepository.save(goodsOrderDetail);
                    amount = amount.add(new BigDecimal(goodsOrderDetail.getBillQty()).multiply(goodsOrderDetail.getPrice()));
                }
            } else {
                if(goodsOrderDetailMap.containsKey(goodsOrderDetailForm.getId())) {
                    GoodsOrderDetail goodsOrderDetail = goodsOrderDetailMap.get(goodsOrderDetailForm.getId());
                    goodsOrderDetail.setQty(goodsOrderDetailForm.getQty());
                    goodsOrderDetail.setBillQty(goodsOrderDetailForm.getQty());
                    goodsOrderDetailRepository.save(goodsOrderDetail);
                    amount = amount.add(new BigDecimal(goodsOrderDetail.getBillQty()).multiply(goodsOrderDetail.getPrice()));
                }
            }
        }
        //更新总价
        goodsOrder.setAmount(amount);
        //更新快递单信息
        ExpressOrder expressOrder = getExpressOrder(goodsOrderForm);
        expressOrderRepository.save(expressOrder);
        goodsOrder.setExpressOrderId(expressOrder.getId());
        goodsOrderRepository.save(goodsOrder);
        return goodsOrder;
    }

    //开单
    public  GoodsOrder bill(GoodsOrderBillForm goodsOrderBillForm) {
        Integer totalBillQty = 0;
        Integer mobileBillQty = 0;
        GoodsOrder goodsOrder = goodsOrderRepository.findOne(goodsOrderBillForm.getId());
        BigDecimal amount = BigDecimal.ZERO;
        List<GoodsOrderDetail> goodsOrderDetailList  = goodsOrderDetailRepository.findByGoodsOrderId(goodsOrder.getId());
        Map<String,GoodsOrderDetail> goodsOrderDetailMap  = CollectionUtil.extractToMap(goodsOrderDetailList,"id");
        Map<String,Product> productMap = productRepository.findMap(CollectionUtil.extractToList(goodsOrderDetailList,"productId"));

        for (int i = goodsOrderBillForm.getGoodsOrderDetailList().size() - 1; i >= 0; i--) {
            GoodsOrderBillDetailForm goodsOrderBillDetailForm=goodsOrderBillForm.getGoodsOrderDetailList().get(i);
            if (goodsOrderBillDetailForm.getBillQty() == null) {
                goodsOrderBillDetailForm.setBillQty(0);
            }
            totalBillQty = totalBillQty + goodsOrderBillDetailForm.getBillQty();
            Product product = productMap.get(goodsOrderBillDetailForm.getProductId());
            if(product.getHasIme()) {
                mobileBillQty = mobileBillQty + goodsOrderBillDetailForm.getBillQty();
            }
            if(goodsOrderBillDetailForm.isCreate()) {
                if (goodsOrderBillDetailForm.getBillQty() > 0) {
                    GoodsOrderDetail goodsOrderDetail = BeanUtil.map(goodsOrderBillDetailForm,GoodsOrderDetail.class);
                    goodsOrderDetail.setQty(0);
                    goodsOrderDetail.setGoodsOrderId(goodsOrder.getId());
                    goodsOrderDetailRepository.save(goodsOrderDetail);
                    amount = amount.add(new BigDecimal(goodsOrderDetail.getBillQty()).multiply(goodsOrderDetail.getPrice()));
                }
            } else {
                if(goodsOrderDetailMap.containsKey(goodsOrderBillDetailForm.getId())) {
                    GoodsOrderDetail goodsOrderDetail = goodsOrderDetailMap.get(goodsOrderBillDetailForm.getId());
                    goodsOrderDetail.setBillQty(goodsOrderBillDetailForm.getBillQty());
                    goodsOrderDetail.setPrice(goodsOrderBillDetailForm.getPrice());
                    goodsOrderDetailRepository.save(goodsOrderDetail);
                    amount = amount.add(new BigDecimal(goodsOrderBillDetailForm.getBillQty()).multiply(goodsOrderBillDetailForm.getPrice()));
                }
            }
        }
        goodsOrder.setAmount(amount);
        goodsOrder.setStatus(GoodsOrderStatusEnum.待发货.name());
        goodsOrderRepository.save(goodsOrder);
        ExpressOrder expressOrder = getExpressOrder(goodsOrderBillForm);
        //设置需要打印的快递单个数
        Integer expressPrintQty = 0;
        if (ShipTypeEnum.总部发货.name().equals(goodsOrder.getShipType())) {
            expressPrintQty = expressUtils.getExpressPrintQty(totalBillQty);
        }
        expressOrder.setExpressPrintQty(expressPrintQty);
        expressOrder.setTotalQty(totalBillQty);
        expressOrder.setMobileQty(mobileBillQty);
        expressOrderRepository.save(expressOrder);
        return goodsOrder;
    }


    private ExpressOrder getExpressOrder(GoodsOrderForm goodsOrderForm) {
        Depot shop = depotRepository.findOne(goodsOrderForm.getShopId());
        ExpressOrder expressOrder = new ExpressOrder();
        if(StringUtils.isNotBlank(goodsOrderForm.getExpressOrderId())) {
            expressOrder = expressOrderRepository.findOne(goodsOrderForm.getExpressOrderId());
        }
        expressOrder.setExtendId(goodsOrderForm.getId());
        expressOrder.setExtendType(ExpressOrderTypeEnum.手机订单.name());
        expressOrder.setContator(shop.getContator());
        expressOrder.setAddress(shop.getAddress());
        expressOrder.setMobilePhone(shop.getMobilePhone());
        expressOrder.setToDepotId(shop.getId());
        expressOrder.setShipType(goodsOrderForm.getShipType());
        return expressOrder;
    }


    private ExpressOrder getExpressOrder(GoodsOrderBillForm goodsOrderBillForm) {
        GoodsOrder goodsOrder = goodsOrderRepository.findOne(goodsOrderBillForm.getId());
        Depot shop = depotRepository.findOne(goodsOrder.getShopId());
        ExpressOrder expressOrder = expressOrderRepository.findOne(goodsOrder.getExpressOrderId());
        expressOrder.setExtendId(goodsOrder.getId());
        expressOrder.setExtendType(ExpressOrderTypeEnum.手机订单.name());
        expressOrder.setContator(goodsOrderBillForm.getExpressContator());
        expressOrder.setAddress(goodsOrderBillForm.getExpressAddress());
        expressOrder.setMobilePhone(goodsOrderBillForm.getExpressMobilePhone());
        expressOrder.setToDepotId(shop.getId());
        expressOrder.setShipType(goodsOrder.getShipType());
        return expressOrder;
    }


    private String getDefaultStoreId(GoodsOrder goodsOrder) {
        String defaultStoreId;
        if(NetTypeEnum.联信.name().equals(goodsOrder.getNetType())){
            defaultStoreId = CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.LX_DEFAULT_STORE_ID.name()).getValue();
        }else {
            defaultStoreId = CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.DEFAULT_STORE_ID.name()).getValue();
        }
        String carrierLockOfficeIds = CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.CARRIER_LOCK_OFFICE.name()).getValue();
        if(StringUtils.isNotBlank(carrierLockOfficeIds)){
            List<String> officeIdList = StringUtils.getSplitList(carrierLockOfficeIds, CharConstant.COMMA);
            Depot shop = depotRepository.findOne(goodsOrder.getShopId());
            if(officeIdList.contains(shop.getOfficeId())) {
                defaultStoreId = CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(),CompanyConfigCodeEnum.DEFALULT_CARRIAR_STORE_ID.name()).getValue();
            }
        }
        return defaultStoreId;
    }

    public GoodsOrderDto findOne(String id) {
        GoodsOrderDto goodsOrderDto;
        if(StringUtils.isBlank(id)) {
            goodsOrderDto = new GoodsOrderDto();
        } else {
            GoodsOrder goodsOrder = goodsOrderRepository.findOne(id);
            goodsOrderDto = BeanUtil.map(goodsOrder,GoodsOrderDto.class);
            cacheUtils.initCacheInput(goodsOrderDto);
        }
        return goodsOrderDto;
    }

    public List<GoodsOrderDetailForm> findGoodsOrderDetailFormList(String id,String shopId,String netType) {
        //修改订单的时候
        List<GoodsOrderDetailForm> goodsOrderDetailFormList  = Lists.newArrayList();
        if(StringUtils.isNotBlank(id)) {
            GoodsOrder goodsOrder = goodsOrderRepository.findOne(id);
            shopId = goodsOrder.getShopId();
            netType = goodsOrder.getNetType();

            List<GoodsOrderDetail> goodsOrderDetailList  = goodsOrderDetailRepository.findByGoodsOrderId(id);
            for(GoodsOrderDetail goodsOrderDetail:goodsOrderDetailList) {

            }

        }
        return null;
    }
}
