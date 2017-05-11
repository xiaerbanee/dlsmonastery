package net.myspring.future.modules.layout.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.future.modules.basic.domain.AdPricesystem;
import net.myspring.future.modules.basic.domain.AdPricesystemDetail;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.mapper.AdPricesystemDetailMapper;
import net.myspring.future.modules.basic.mapper.AdPricesystemMapper;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.basic.mapper.ProductMapper;
import net.myspring.future.modules.crm.domain.*;
import net.myspring.future.modules.crm.mapper.*;
import net.myspring.future.modules.layout.domain.AdGoodsOrder;
import net.myspring.future.modules.layout.domain.AdGoodsOrderDetail;
import net.myspring.future.modules.layout.dto.AdGoodsOrderDto;
import net.myspring.future.modules.layout.mapper.AdGoodsOrderDetailMapper;
import net.myspring.future.modules.layout.mapper.AdGoodsOrderMapper;
import net.myspring.future.modules.layout.mapper.ShopDepositMapper;
import net.myspring.future.modules.layout.web.query.AdGoodsOrderQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = false)
public class AdGoodsOrderService {

    @Autowired
    private AdGoodsOrderMapper adGoodsOrderMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private AdGoodsOrderDetailMapper adGoodsOrderDetailMapper;
    @Autowired
    private ExpressOrderMapper expressOrderMapper;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private ShopDepositMapper shopDepositMapper;
    @Autowired
    private AdPricesystemDetailMapper adPricesystemDetailMapper;
    @Autowired
    private AdPricesystemMapper adPricesystemMapper;

    public Page<AdGoodsOrderDto> findPage(Pageable pageable, AdGoodsOrderQuery adGoodsOrderQuery) {
        Page<AdGoodsOrderDto> page = adGoodsOrderMapper.findPage(pageable, adGoodsOrderQuery);
        return page;
    }

    public AdGoodsOrder findOne(String id) {
        AdGoodsOrder adGoodsOrder = adGoodsOrderMapper.findOne(id);
        return adGoodsOrder;
    }

    public Map<String,Object> getAmountMap(AdGoodsOrder adGoodsOrder){
        Map<String,Object> map=Maps.newHashMap();
        // 统计应付运费,以门店物料运费为准
        Map<String,AdPricesystemDetail> priceMap = Maps.newHashMap();
        Depot shop=depotMapper.findOne(adGoodsOrder.getShopId());
        if(StringUtils.isNotBlank(shop.getAdPricesystemId())){
            List<AdPricesystemDetail> adPricesystemDetailList = adPricesystemDetailMapper.findByAdPricesystemId(shop.getAdPricesystemId());
            for (AdPricesystemDetail adPricesystemDetail : adPricesystemDetailList) {
                priceMap.put(adPricesystemDetail.getProductId(), adPricesystemDetail);
            }
        }
        
        
        BigDecimal yfyfAmount = BigDecimal.ZERO;
        for (AdGoodsOrderDetail adGoodsOrderDetail : adGoodsOrder.getAdGoodsOrderDetailList()) {
            if (priceMap.get(adGoodsOrderDetail.getProductId()) != null) {
                BigDecimal price = priceMap.get(adGoodsOrderDetail.getProductId()).getPrice();
                if (price != null) {
                    yfyfAmount = yfyfAmount.add(new BigDecimal(adGoodsOrderDetail.getBillQty()).multiply(price));
                }
            }
        }
        // 统计应收运费 ，全部以A类物料运费为准
        Map<String, AdPricesystemDetail> ysyfMap = Maps.newHashMap();
        AdPricesystem defaultAdPricesystem = adPricesystemMapper.findByName("");
        if (defaultAdPricesystem != null) {
            List<AdPricesystemDetail> adPricesystemDetailList=adPricesystemDetailMapper.findByAdPricesystemId(defaultAdPricesystem.getId());
            for (AdPricesystemDetail adDetail : adPricesystemDetailList) {
                ysyfMap.put(adDetail.getProductId(), adDetail);
            }
        }
        BigDecimal ysyfAmount = BigDecimal.ZERO;
        for (AdGoodsOrderDetail adGoodsOrderDetail : adGoodsOrder.getAdGoodsOrderDetailList()) {
            if (ysyfMap.get(adGoodsOrderDetail.getProductId()) != null) {
                BigDecimal price = ysyfMap.get(adGoodsOrderDetail.getProductId()).getPrice();
                if (price != null) {
                    ysyfAmount = ysyfAmount.add(new BigDecimal(adGoodsOrderDetail.getBillQty()).multiply(price));
                }
            }
        }
        map.put("ysyfAmount", ysyfAmount);
        map.put("yfyfAmount", yfyfAmount);
        map.put("priceMap", priceMap);
        return map;
    }

    public AdGoodsOrder save(AdGoodsOrder adGoodsOrder) {
        Depot outShop=depotMapper.findOne(adGoodsOrder.getOutShopId());
        if(adGoodsOrder.getShopId()==null){
            adGoodsOrder.setShopId(outShop.getId());
        }
        return adGoodsOrder;
    }

    public AdGoodsOrder getAdGoodsOrderDetail(AdGoodsOrder adGoodsOrder) {
        return adGoodsOrder;
    }

    public void audit(AdGoodsOrder adGoodsOrder, boolean pass, String comment) {
    }
    
    public AdGoodsOrder bill(AdGoodsOrder adGoodsOrder) {
        Map<String,AdPricesystemDetail> priceMap = Maps.newHashMap();
        Depot shop=depotMapper.findOne(adGoodsOrder.getShopId());
        if(StringUtils.isNotBlank(shop.getAdPricesystemId())){
            List<AdPricesystemDetail> adPricesystemDetailList = adPricesystemDetailMapper.findByAdPricesystemId(shop.getAdPricesystemId());
            for(AdPricesystemDetail adPricesystemDetail:adPricesystemDetailList) {
                priceMap.put(adPricesystemDetail.getProductId(), adPricesystemDetail);
            }
        }
        List<AdGoodsOrderDetail> adGoodsOrderDetailList=Lists.newArrayList();
        for (int i = adGoodsOrder.getAdGoodsOrderDetailList().size() - 1; i >= 0; i--) {
            AdGoodsOrderDetail agod = adGoodsOrder.getAdGoodsOrderDetailList().get(i);
            agod.setQty(agod.getQty()==null?0:agod.getQty());
            agod.setConfirmQty(agod.getConfirmQty()==null?0:agod.getConfirmQty());
            agod.setBillQty(agod.getBillQty()==null?0:agod.getBillQty());
            if(agod.getBillQty()!=null&&agod.getBillQty() > 0&&(agod.getQty() == null || agod.getQty() == 0)) {
                agod.setQty(0);
                agod.setShippedQty(0);
                agod.setAdGoodsOrderId(adGoodsOrder.getId());
                adGoodsOrderDetailList.add(agod);
            }else if(agod.getBillQty()!=null&&agod.getBillQty() > 0){
                adGoodsOrderDetailMapper.update(agod);
            }
        }
        //应付运费
        BigDecimal shouldPay = BigDecimal.ZERO;
        //应收运费
        BigDecimal shouldGet = BigDecimal.ZERO;
        BigDecimal amount = BigDecimal.ZERO;
        for(AdGoodsOrderDetail adGoodsOrderDetail:adGoodsOrder.getAdGoodsOrderDetailList()) {
            if(adGoodsOrderDetail.getBillQty()>0) {
                BigDecimal price2 = adGoodsOrderDetail.getProduct().getPrice2();
                amount = amount.add(new BigDecimal(adGoodsOrderDetail.getBillQty()).multiply(price2));
                if (priceMap.containsKey(adGoodsOrderDetail.getProductId())) {
                    adGoodsOrderDetail.setShouldPay(priceMap.get(adGoodsOrderDetail.getProductId()).getPrice());
                    adGoodsOrderDetail.setShouldGet(adGoodsOrderDetail.getProduct().getShouldGet());
                    shouldPay = shouldPay.add(adGoodsOrderDetail.getShouldPay().multiply(new BigDecimal(adGoodsOrderDetail.getBillQty())));
                    shouldGet = shouldGet.add(adGoodsOrderDetail.getShouldGet().multiply(new BigDecimal(adGoodsOrderDetail.getBillQty())));
                }
            }
        }
        if(adGoodsOrder.getParentId()==null){
            adGoodsOrder.setParentId(adGoodsOrder.getId());
        }

        //分批开单
        if(adGoodsOrder.getSplitBill()){
            List<AdGoodsOrderDetail> newAdGoodsOrderDetails= Lists.newArrayList();
            for(AdGoodsOrderDetail adGoodsOrderDetail:adGoodsOrder.getAdGoodsOrderDetailList()){
                if(adGoodsOrderDetail.getConfirmQty()>adGoodsOrderDetail.getBillQty()){
                    AdGoodsOrderDetail newAdGoodsOrderDetail=new AdGoodsOrderDetail();
                    Integer billEnabledQty=adGoodsOrderDetail.getConfirmQty()-adGoodsOrderDetail.getBillQty();
                    newAdGoodsOrderDetail.setProduct(adGoodsOrderDetail.getProduct());
                    newAdGoodsOrderDetail.setQty(billEnabledQty);
                    newAdGoodsOrderDetail.setConfirmQty(billEnabledQty);
                    newAdGoodsOrderDetail.setBillQty(0);
                    newAdGoodsOrderDetail.setShippedQty(0);
                    newAdGoodsOrderDetails.add(newAdGoodsOrderDetail);
                }
            }
            if(CollectionUtil.isNotEmpty(newAdGoodsOrderDetails)){
            }
        }
        return adGoodsOrder;
    }

    public Boolean ship(AdGoodsOrder adGoodsOrder) {
        for (AdGoodsOrderDetail adGoodsOrderDetail : adGoodsOrder.getAdGoodsOrderDetailList()) {
            if (adGoodsOrderDetail.getShipQty() != null && adGoodsOrderDetail.getShipQty() > 0) {
                Integer shippedQty = adGoodsOrderDetail.getShippedQty();
                Integer shipQty = adGoodsOrderDetail.getShipQty();
                adGoodsOrderDetail.setShippedQty(shippedQty + shipQty);
                adGoodsOrderDetailMapper.update(adGoodsOrderDetail);
            }
        }
        boolean isAllShipped = true;
        for (AdGoodsOrderDetail adGoodsOrderDetail : adGoodsOrder.getAdGoodsOrderDetailList()) {
            if (adGoodsOrderDetail.getBillQty()==adGoodsOrderDetail.getShippedQty()) {
                isAllShipped = false;
                break;
            }
        }
        expressOrderMapper.update(adGoodsOrder.getExpressOrder());
        adGoodsOrderMapper.update(adGoodsOrder);
        return true;
    }

    public void sign(AdGoodsOrder adGoodsOrder) {
    }

    public void logicDelete(String id) {
        adGoodsOrderMapper.logicDeleteOne(id);
    }


    public void print(AdGoodsOrder adGoodsOrder) {
        ExpressOrder expressOrder = adGoodsOrder.getExpressOrder();
        if(expressOrder != null){
            if(expressOrder.getOutPrintDate() == null){
                expressOrder.setOutPrintDate(LocalDateTime.now());
            }
            expressOrderMapper.update(expressOrder);
        }
    }

}
