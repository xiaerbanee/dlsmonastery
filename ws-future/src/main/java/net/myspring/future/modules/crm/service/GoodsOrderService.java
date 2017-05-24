package net.myspring.future.modules.crm.service;

import com.ctc.wstx.util.StringUtil;
import com.google.common.collect.Lists;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.basic.modules.sys.dto.CompanyConfigCacheDto;
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
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.basic.mapper.PricesystemDetailMapper;
import net.myspring.future.modules.basic.mapper.ProductMapper;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.future.modules.crm.domain.GoodsOrderDetail;
import net.myspring.future.modules.crm.dto.GoodsOrderDetailDto;
import net.myspring.future.modules.crm.dto.GoodsOrderDto;
import net.myspring.future.modules.crm.mapper.ExpressOrderMapper;
import net.myspring.future.modules.crm.mapper.GoodsOrderDetailMapper;
import net.myspring.future.modules.crm.mapper.GoodsOrderMapper;
import net.myspring.future.modules.crm.web.form.GoodsOrderBillDetailForm;
import net.myspring.future.modules.crm.web.form.GoodsOrderBillForm;
import net.myspring.future.modules.crm.web.form.GoodsOrderDetailForm;
import net.myspring.future.modules.crm.web.form.GoodsOrderForm;
import net.myspring.future.modules.crm.web.query.GoodsOrderDetailQuery;
import net.myspring.future.modules.crm.web.query.GoodsOrderQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class GoodsOrderService {
    @Autowired
    private GoodsOrderMapper goodsOrderMapper;
    @Autowired
    private GoodsOrderDetailMapper goodsOrderDetailMapper;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private ExpressOrderMapper expressOrderMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ExpressUtils expressUtils;
    @Autowired
    private PricesystemDetailMapper pricesystemDetailMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private CacheUtils cacheUtils;


    public Page<GoodsOrderDto> findPage(Pageable pageable, GoodsOrderQuery goodsOrderQuery) {
        Page<GoodsOrderDto> page = goodsOrderMapper.findPage(pageable, goodsOrderQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }


    //检测门店
    @Transactional(readOnly = true)
    public RestResponse validateShop(String goodsOrderId,String shopId) {
        Depot shop = depotMapper.findOne(shopId);
        RestResponse restResponse = new RestResponse("有效门店", ResponseCodeEnum.valid.name(),true);
        if(StringUtils.isBlank(shop.getPricesystemId())) {
            restResponse.getErrors().add(new RestErrorField("没有价格体系","no_pricesystem","shopId"));
            restResponse.setSuccess(false);
        }
        //检查当前客户是否有未处理订单
        GoodsOrderQuery goodsOrderQuery = new GoodsOrderQuery();
        goodsOrderQuery.setShopId(shop.getId());
        goodsOrderQuery.setStatus(GoodsOrderStatusEnum.待开单.name());
        List<GoodsOrder> goodsOrderList = goodsOrderMapper.findList(goodsOrderQuery);
        if (CollectionUtil.isNotEmpty(goodsOrderList)) {
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
            goodsOrderMapper.save(goodsOrder);
        } else {
            goodsOrder = goodsOrderMapper.findOne(goodsOrderForm.getId());
            ReflectionUtil.copyProperties(goodsOrderForm,goodsOrder);
            goodsOrderMapper.update(goodsOrder);
        }

        List<GoodsOrderDetail> goodsOrderDetailList  = goodsOrderDetailMapper.findByGoodsOrderId(goodsOrder.getId());
        Map<String,GoodsOrderDetail> goodsOrderDetailMap  = CollectionUtil.extractToMap(goodsOrderDetailList,"id");
        //保存订单明细
        BigDecimal amount = BigDecimal.ZERO;
        Depot shop = depotMapper.findOne(goodsOrder.getShopId());
        for (int i = goodsOrderForm.getGoodsOrderDetailList().size() - 1; i >= 0; i--) {
            GoodsOrderDetailForm goodsOrderDetailForm = goodsOrderForm.getGoodsOrderDetailList().get(i);
            if(goodsOrderDetailForm.getQty()==null) {
                goodsOrderDetailForm.setQty(0);
            }

            if(goodsOrderDetailForm.isCreate()) {
                if (goodsOrderDetailForm.getQty() > 0) {
                    GoodsOrderDetail goodsOrderDetail = BeanUtil.map(goodsOrderDetailForm,GoodsOrderDetail.class);

                    goodsOrderDetail.setGoodsOrderId(goodsOrder.getId());
                    goodsOrderDetail.setBillQty(goodsOrderDetail.getQty());
                    PricesystemDetail pricesystemDetail = pricesystemDetailMapper.findByPricesystemIdAndProductId(shop.getPricesystemId(), goodsOrderDetailForm.getProductId());
                    goodsOrderDetail.setPrice(pricesystemDetail.getPrice());
                    goodsOrderDetailMapper.save(goodsOrderDetail);
                    amount = amount.add(new BigDecimal(goodsOrderDetail.getBillQty()).multiply(goodsOrderDetail.getPrice()));
                }
            } else {
                //防止前台篡改
                if(goodsOrderDetailMap.containsKey(goodsOrderDetailForm.getId())) {

                    if (goodsOrderDetailForm.getQty() <= 0) {
                        goodsOrderDetailMapper.deleteOne(goodsOrderDetailForm.getId());
                    }else{
                        GoodsOrderDetail goodsOrderDetail = goodsOrderDetailMapper.findOne(goodsOrderDetailForm.getId());
                        goodsOrderDetail.setQty(goodsOrderDetailForm.getQty());
                        goodsOrderDetail.setBillQty(goodsOrderDetailForm.getQty());
                        goodsOrderDetailMapper.update(goodsOrderDetail);
                        amount = amount.add(new BigDecimal(goodsOrderDetail.getBillQty()).multiply(goodsOrderDetail.getPrice()));
                    }
                }
            }
        }
        //更新总价
        goodsOrder.setAmount(amount);
        //更新快递单信息
        ExpressOrder expressOrder = getExpressOrder(goodsOrderForm);
        if(expressOrder.isCreate()) {
            expressOrderMapper.save(expressOrder);
        } else {
            expressOrderMapper.update(expressOrder);
        }
        goodsOrder.setExpressOrderId(expressOrder.getId());
        goodsOrderMapper.update(goodsOrder);
        return goodsOrder;
    }

    //开单
    public  GoodsOrder bill(GoodsOrderBillForm goodsOrderBillForm) {
        Integer totalBillQty = 0;
        Integer mobileBillQty = 0;
        GoodsOrder goodsOrder = goodsOrderMapper.findOne(goodsOrderBillForm.getId());
        BigDecimal amount = BigDecimal.ZERO;
        List<GoodsOrderDetail> goodsOrderDetailList  = goodsOrderDetailMapper.findByGoodsOrderId(goodsOrder.getId());
        Map<String,GoodsOrderDetail> goodsOrderDetailMap  = CollectionUtil.extractToMap(goodsOrderDetailList,"id");
        for (int i = goodsOrderBillForm.getGoodsOrderDetailList().size() - 1; i >= 0; i--) {
            GoodsOrderBillDetailForm goodsOrderBillDetailForm=goodsOrderBillForm.getGoodsOrderDetailList().get(i);

            if (goodsOrderBillDetailForm.getBillQty() == null) {
                goodsOrderBillDetailForm.setBillQty(0);
            }
            totalBillQty = totalBillQty + goodsOrderBillDetailForm.getBillQty();
            Product product = productMapper.findOne(goodsOrderBillDetailForm.getProductId());
            if(product.getHasIme()) {
                mobileBillQty = mobileBillQty + goodsOrderBillDetailForm.getBillQty();
            }
            if(goodsOrderBillDetailForm.isCreate()) {
                if (goodsOrderBillDetailForm.getBillQty() > 0) {
                    GoodsOrderDetail goodsOrderDetail = BeanUtil.map(goodsOrderBillDetailForm,GoodsOrderDetail.class);

                    goodsOrderDetail.setQty(0);
                    goodsOrderDetail.setGoodsOrderId(goodsOrder.getId());

                    goodsOrderDetailMapper.save(goodsOrderDetail);
                    amount = amount.add(new BigDecimal(goodsOrderDetail.getBillQty()).multiply(goodsOrderDetail.getPrice()));
                }
            } else {
                if(goodsOrderDetailMap.containsKey(goodsOrderBillDetailForm.getId())) {
                    GoodsOrderDetail goodsOrderDetail = goodsOrderDetailMapper.findOne(goodsOrderBillDetailForm.getId());
                    goodsOrderDetail.setBillQty(goodsOrderBillDetailForm.getBillQty());
                    goodsOrderDetail.setPrice(goodsOrderBillDetailForm.getPrice());
                    goodsOrderDetailMapper.update(goodsOrderDetail);
                    amount = amount.add(new BigDecimal(goodsOrderBillDetailForm.getBillQty()).multiply(goodsOrderBillDetailForm.getPrice()));
                }
            }
        }
        goodsOrder.setAmount(amount);
        goodsOrder.setStatus(GoodsOrderStatusEnum.待发货.name());
        goodsOrderMapper.update(goodsOrder);
        ExpressOrder expressOrder = getExpressOrder(goodsOrderBillForm);
        //设置需要打印的快递单个数
        Integer expressPrintQty = 0;
        if (ShipTypeEnum.总部发货.name().equals(goodsOrder.getShipType())) {
            expressPrintQty = expressUtils.getExpressPrintQty(totalBillQty);
        }
        expressOrder.setExpressPrintQty(expressPrintQty);
        expressOrder.setTotalQty(totalBillQty);
        expressOrder.setMobileQty(mobileBillQty);
        expressOrderMapper.update(expressOrder);
        return goodsOrder;
    }


    private ExpressOrder getExpressOrder(GoodsOrderForm goodsOrderForm) {
        Depot shop = depotMapper.findOne(goodsOrderForm.getShopId());
        ExpressOrder expressOrder = new ExpressOrder();
        if(StringUtils.isNotBlank(goodsOrderForm.getExpressOrderId())) {
            expressOrder = expressOrderMapper.findOne(goodsOrderForm.getExpressOrderId());
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
        GoodsOrder goodsOrder = goodsOrderMapper.findOne(goodsOrderBillForm.getId());
        Depot shop = depotMapper.findOne(goodsOrder.getShopId());
        ExpressOrder expressOrder = expressOrderMapper.findOne(goodsOrder.getExpressOrderId());
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
            Depot shop = depotMapper.findOne(goodsOrder.getShopId());
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
            GoodsOrder goodsOrder = goodsOrderMapper.findOne(id);
            goodsOrderDto = BeanUtil.map(goodsOrder,GoodsOrderDto.class);
            cacheUtils.initCacheInput(goodsOrderDto);
        }
        return goodsOrderDto;
    }

}
