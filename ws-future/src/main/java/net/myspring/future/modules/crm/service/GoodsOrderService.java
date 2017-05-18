package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
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
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.basic.mapper.PricesystemDetailMapper;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.future.modules.crm.domain.GoodsOrderDetail;
import net.myspring.future.modules.crm.mapper.ExpressOrderMapper;
import net.myspring.future.modules.crm.mapper.GoodsOrderDetailMapper;
import net.myspring.future.modules.crm.mapper.GoodsOrderMapper;
import net.myspring.future.modules.crm.web.form.GoodsOrderDetailForm;
import net.myspring.future.modules.crm.web.form.GoodsOrderForm;
import net.myspring.future.modules.crm.web.query.GoodsOrderQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
        //检查门店价格体系是否和订单价格体系一致
        if(StringUtils.isNotBlank(goodsOrderId)) {
            GoodsOrder goodsOrder = goodsOrderMapper.findOne(goodsOrderId);
            Depot currentShop = depotMapper.findOne(goodsOrder.getShopId());
            if(!currentShop.getPricesystemId().equals(shop.getPricesystemId())) {
                restResponse.getErrors().add(new RestErrorField("订单门店与修改后门店价格体系不一致","not_same_pricesystem","shopId"));
                restResponse.setSuccess(false);
            }
        }
        return restResponse;
    }

    //根据订单号查明细单
    public List<GoodsOrderDetailForm> findDetailFormList(String goodsOrderId) {
        GoodsOrder goodsOrder = goodsOrderMapper.findOne(goodsOrderId);
        List<GoodsOrderDetail> goodsOrderDetailList  =goodsOrderDetailMapper.findByGoodsOrderId(goodsOrderId);
        Map<String,GoodsOrderDetail> goodsOrderDetailMap = CollectionUtil.extractToMap(goodsOrderDetailList,"productId");
        List<GoodsOrderDetailForm> goodsOrderDetailFormList = Lists.newArrayList();
        if(CollectionUtil.isNotEmpty(goodsOrderDetailList)) {
            for(GoodsOrderDetail goodsOrderDetail:goodsOrderDetailList) {
                GoodsOrderDetailForm goodsOrderDetailForm  = new GoodsOrderDetailForm();
                goodsOrderDetailFormList.add(goodsOrderDetailForm);
            }
        }
        List<GoodsOrderDetailForm> list = findDetailFormList(goodsOrder.getShopId(),goodsOrder.getNetType());
        for(GoodsOrderDetailForm goodsOrderDetailForm:list) {
            if(!goodsOrderDetailMap.containsKey(goodsOrderDetailForm.getProdcutId())) {
                goodsOrderDetailFormList.add(goodsOrderDetailForm);
            }
        }
        return goodsOrderDetailFormList;
    }


    //根据门店和netType查明细单
    public List<GoodsOrderDetailForm> findDetailFormList(String shopId,String netType) {
        Depot shop = depotMapper.findOne(shopId);
        List<PricesystemDetail> pricesystemDetailList  = pricesystemDetailMapper.findByPricesystemId(shop.getPricesystemId());
        List<GoodsOrderDetailForm> goodsOrderDetailFormList = Lists.newArrayList();
        for(PricesystemDetail pricesystemDetail:pricesystemDetailList) {
            GoodsOrderDetailForm goodsOrderDetailForm = new GoodsOrderDetailForm();
            goodsOrderDetailFormList.add(goodsOrderDetailForm);
        }
        return goodsOrderDetailFormList;
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
        Depot shop = depotMapper.findOne(goodsOrder.getShopId());
        //初始化form值
        goodsOrderForm.setExpressOrderId(goodsOrder.getExpressOrderId());
        goodsOrderForm.setContator(shop.getContator());
        goodsOrderForm.setAddress(shop.getAddress());
        goodsOrderForm.setMobilePhone(shop.getMobilePhone());

        List<GoodsOrderDetail> goodsOrderDetailList  = goodsOrderDetailMapper.findByGoodsOrderId(goodsOrder.getId());
        Map<String,GoodsOrderDetail> goodsOrderDetailMap  = CollectionUtil.extractToMap(goodsOrderDetailList,"id");
        //保存订单明细
        BigDecimal amount = BigDecimal.ZERO;
        for (int i = goodsOrderForm.getGoodsOrderDetailFormList().size() - 1; i >= 0; i--) {
            GoodsOrderDetailForm goodsOrderDetailForm = goodsOrderForm.getGoodsOrderDetailFormList().get(i);
            if(goodsOrderDetailForm.getQty()==null) {
                goodsOrderDetailForm.setQty(0);
            }
            goodsOrderDetailForm.setBillQty(goodsOrderDetailForm.getQty());
            goodsOrderDetailForm.setGoodsOrderId(goodsOrder.getId());
            GoodsOrderDetail  goodsOrderDetail;
            if(goodsOrderDetailForm.isCreate()) {
                if (goodsOrderDetailForm.getQty() > 0) {
                    goodsOrderDetail = BeanUtil.map(goodsOrderDetailForm,GoodsOrderDetail.class);
                    goodsOrderDetailMapper.save(goodsOrderDetail);
                    amount = amount.add(new BigDecimal(goodsOrderDetail.getBillQty()).multiply(goodsOrderDetail.getPrice()));
                }
            } else {
                //防止前台篡改
                if(goodsOrderDetailMap.containsKey(goodsOrderDetailForm.getId())) {
                    goodsOrderDetailMapper.updateForm(goodsOrderDetailForm);
                    amount = amount.add(new BigDecimal(goodsOrderDetailForm.getBillQty()).multiply(goodsOrderDetailForm.getPrice()));
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
    public  GoodsOrder bill(GoodsOrderForm goodsOrderForm) {
        Integer totalBillQty = 0;
        Integer mobileBillQty = 0;
        GoodsOrder goodsOrder = goodsOrderMapper.findOne(goodsOrderForm.getId());
        BigDecimal amount = BigDecimal.ZERO;
        List<GoodsOrderDetail> goodsOrderDetailList  = goodsOrderDetailMapper.findByGoodsOrderId(goodsOrder.getId());
        Map<String,GoodsOrderDetail> goodsOrderDetailMap  = CollectionUtil.extractToMap(goodsOrderDetailList,"id");
        for (int i = goodsOrderForm.getGoodsOrderDetailFormList().size() - 1; i >= 0; i--) {
            GoodsOrderDetailForm goodsOrderDetailForm=goodsOrderForm.getGoodsOrderDetailFormList().get(i);
            if (goodsOrderDetailForm.getQty() == null) {
                goodsOrderDetailForm.setQty(0);
            }
            if (goodsOrderDetailForm.getBillQty() == null) {
                goodsOrderDetailForm.setBillQty(0);
            }
            totalBillQty = totalBillQty + goodsOrderDetailForm.getBillQty();
            if(goodsOrderDetailForm.getHasIme()) {
                mobileBillQty = mobileBillQty + goodsOrderDetailForm.getBillQty();
            }
            GoodsOrderDetail goodsOrderDetail;
            if(goodsOrderDetailForm.isCreate()) {
                if (goodsOrderDetailForm.getBillQty() > 0) {
                    goodsOrderDetail = BeanUtil.map(goodsOrderDetailForm,GoodsOrderDetail.class);
                    goodsOrderDetailMapper.save(goodsOrderDetail);
                    amount = amount.add(new BigDecimal(goodsOrderDetail.getBillQty()).multiply(goodsOrderDetail.getPrice()));
                }
            } else {
                if(goodsOrderDetailMap.containsKey(goodsOrderDetailForm.getId())) {
                    goodsOrderDetailMapper.updateForm(goodsOrderDetailForm);
                    amount = amount.add(new BigDecimal(goodsOrderDetailForm.getBillQty()).multiply(goodsOrderDetailForm.getPrice()));
                }
            }
        }
        goodsOrder.setAmount(amount);
        goodsOrder.setStatus(GoodsOrderStatusEnum.待发货.name());
        goodsOrderMapper.update(goodsOrder);
        ExpressOrder expressOrder = getExpressOrder(goodsOrderForm);
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
        expressOrder.setExtendType(ExpressOrderExtendTypeEnum.手机订单.name());
        expressOrder.setContator(goodsOrderForm.getContator());
        expressOrder.setAddress(goodsOrderForm.getAddress());
        expressOrder.setMobilePhone(goodsOrderForm.getMobilePhone());
        expressOrder.setToDepotId(shop.getId());
        expressOrder.setShipType(goodsOrderForm.getShipType());
        return expressOrder;
    }

    private String getDefaultStoreId(GoodsOrder goodsOrder) {
        String defaultStoreId;
        if(NetTypeEnum.联信.name().equals(goodsOrder.getNetType())){
            defaultStoreId = CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.LX_DEFAULT_STORE_ID.name()).getValue();
        }else {
            defaultStoreId = CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.DEFAULT_STORE_ID.name()).getValue();
        }
        String carrierLockOfficeIds = CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.CARRIER_LOCK_OFFICE.name()).getValue();
        if(StringUtils.isNotBlank(carrierLockOfficeIds)){
            List<String> officeIdList = StringUtils.getSplitList(carrierLockOfficeIds, CharConstant.COMMA);
            Depot shop = depotMapper.findOne(goodsOrder.getShopId());
            if(officeIdList.contains(shop.getOfficeId())) {
                defaultStoreId = CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.DEFALULT_CARRIAR_STORE_ID.name()).getValue();
            }
        }
        return defaultStoreId;
    }

}
