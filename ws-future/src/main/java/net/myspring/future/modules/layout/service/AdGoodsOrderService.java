package net.myspring.future.modules.layout.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.future.common.enums.BillTypeEnum;
import net.myspring.future.common.enums.ExpressCompanyTypeEnum;
import net.myspring.future.common.enums.ShipTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.IdUtils;
import net.myspring.future.modules.basic.client.ActivitiClient;
import net.myspring.future.modules.basic.domain.AdPricesystem;
import net.myspring.future.modules.basic.domain.AdPricesystemDetail;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.mapper.AdPricesystemDetailMapper;
import net.myspring.future.modules.basic.mapper.AdPricesystemMapper;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.basic.mapper.ProductMapper;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.dto.ExpressOrderDto;
import net.myspring.future.modules.crm.mapper.ExpressOrderMapper;
import net.myspring.future.modules.layout.domain.AdGoodsOrder;
import net.myspring.future.modules.layout.domain.AdGoodsOrderDetail;
import net.myspring.future.modules.layout.dto.AdGoodsOrderDetailDto;
import net.myspring.future.modules.layout.dto.AdGoodsOrderDto;
import net.myspring.future.modules.layout.mapper.AdGoodsOrderDetailMapper;
import net.myspring.future.modules.layout.mapper.AdGoodsOrderMapper;
import net.myspring.future.modules.layout.mapper.ShopDepositMapper;
import net.myspring.future.modules.layout.web.form.AdGoodsOrderForm;
import net.myspring.future.modules.layout.web.query.AdGoodsOrderQuery;
import net.myspring.general.modules.sys.dto.ActivitiCompleteDto;
import net.myspring.general.modules.sys.dto.ActivitiStartDto;
import net.myspring.general.modules.sys.form.ActivitiCompleteForm;
import net.myspring.general.modules.sys.form.ActivitiStartForm;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
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
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private ActivitiClient activitiClient;
    @Autowired
    private RedisTemplate redisTemplate;

    public Page<AdGoodsOrderDto> findPage(Pageable pageable, AdGoodsOrderQuery adGoodsOrderQuery) {
        Page<AdGoodsOrderDto> page = adGoodsOrderMapper.findPage(pageable, adGoodsOrderQuery);
        cacheUtils.initCacheInput(page.getContent());
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

    public void save(AdGoodsOrderForm adGoodsOrderForm) {
        AdGoodsOrder adGoodsOrder;
        //若是直营门店，则财务门店=下单门店
        if(adGoodsOrderForm.getShopId()==null){
            adGoodsOrderForm.setShopId(adGoodsOrderForm.getOutShopId());
        }
        adGoodsOrderForm.setBillType(BillTypeEnum.柜台.name());
        String maxBusinessId  =adGoodsOrderMapper.findMaxBusinessId(LocalDate.now());
        adGoodsOrderForm.setBussinessId(StringUtils.getNextBusinessId(maxBusinessId));
        if(adGoodsOrderForm.isCreate()){
            adGoodsOrder = BeanUtil.map(adGoodsOrderForm,AdGoodsOrder.class);
            adGoodsOrderMapper.save(adGoodsOrder);
        }else{
            adGoodsOrder = adGoodsOrderMapper.findOne(adGoodsOrderForm.getId());
            ReflectionUtil.copyProperties(adGoodsOrderForm,adGoodsOrder);
            adGoodsOrderMapper.update(adGoodsOrder);
        }

        //快递信息
        ExpressOrder expressOrder;
        ExpressOrderDto expressOrderDto = adGoodsOrderForm.getExpressOrderDto();
        expressOrderDto.setToDepotId(adGoodsOrder.getShopId());

        if(expressOrderDto.getId()==null){
            expressOrderDto.setExtendBusinessId(adGoodsOrder.getBusinessId());
            expressOrderDto.setExtendType(ExpressCompanyTypeEnum.物料订单.name());
            expressOrderDto.setShipType(ShipTypeEnum.总部发货.name());
            expressOrderDto.setPrintDate(LocalDate.now());
            expressOrderDto.setExtendId(adGoodsOrder.getId());
            expressOrder = BeanUtil.map(expressOrderDto,ExpressOrder.class);
            expressOrderMapper.save(expressOrder);
        }else{
            expressOrder = expressOrderMapper.findOne(expressOrderDto.getId());
            ReflectionUtil.copyProperties(expressOrderDto,expressOrder);
            expressOrderMapper.update(expressOrder);
        }
        //物料订单详细信息,若是修改，先获取所有的detail>delete,再新增
        List<AdGoodsOrderDetailDto> adGoodsOrderDetailDtosBefore = adGoodsOrderDetailMapper.findByAdGoodsOrderIds(Arrays.asList(adGoodsOrder.getId()));
        if(adGoodsOrderDetailDtosBefore!=null){
            adGoodsOrderDetailMapper.deleteById(adGoodsOrder.getId());
        }
        AdGoodsOrderDetail adGoodsOrderDetail;
        List<AdGoodsOrderDetailDto> adGoodsOrderDetailDtos = adGoodsOrderForm.getAdGoodsOrderDetails();
        if(adGoodsOrderDetailDtos!=null){
            for(AdGoodsOrderDetailDto adGoodsOrderDetailDto:adGoodsOrderDetailDtos){
                    adGoodsOrderDetailDto.setAdGoodsOrderId(adGoodsOrder.getId());
                    adGoodsOrderDetailDto.setPrice(adGoodsOrderDetailDto.getPrice2());
                    adGoodsOrderDetail = BeanUtil.map(adGoodsOrderDetailDto,AdGoodsOrderDetail.class);
                    adGoodsOrderDetailMapper.save(adGoodsOrderDetail);
            }
        }
        adGoodsOrder.setExpressOrderId(expressOrder.getId());

        //启动流程
        if(adGoodsOrder.getId()!=null&&adGoodsOrder.getProcessInstanceId()==null){
            ActivitiStartForm activitiStartForm = new ActivitiStartForm();
            activitiStartForm.setBusinessKey(adGoodsOrder.getId());
            //待修改
            activitiStartForm.setName("AdGoodsOrder");
            activitiStartForm.setProcessTypeName("AdGoodsOrder");
            activitiStartForm.setMessage("AdGoodsOrder");
            ActivitiStartDto activitiStartDto = activitiClient.start(activitiStartForm);
            if(activitiStartDto!=null){
                adGoodsOrder.setProcessFlowId(activitiStartDto.getProcessFlowId());
                adGoodsOrder.setProcessInstanceId(activitiStartDto.getProcessInstanceId());
                adGoodsOrder.setProcessStatus(activitiStartDto.getProcessStatus());
                adGoodsOrder.setProcessTypeId(activitiStartDto.getProcessTypeId());
                adGoodsOrder.setProcessPositionId(activitiStartDto.getPositionId());
            }
        }


        adGoodsOrderMapper.update(adGoodsOrder);

    }

    public AdGoodsOrderDto getAdGoodsOrderDetail(AdGoodsOrderDto adGoodsOrderDto) {
        if(adGoodsOrderDto.getId()!=null){
            AdGoodsOrder adGoodsOrder = adGoodsOrderMapper.findOne(adGoodsOrderDto.getId());
            adGoodsOrderDto = BeanUtil.map(adGoodsOrder,AdGoodsOrderDto.class);
            if(adGoodsOrderDto.getExpressOrderId()!=null){
                adGoodsOrderDto.setExpressOrderDto(expressOrderMapper.findDto(adGoodsOrderDto.getExpressOrderId()));
            }
            cacheUtils.initCacheInput(adGoodsOrderDto);
        }
        return adGoodsOrderDto;
    }

    public AdGoodsOrderForm getFormProperty(AdGoodsOrderForm adGoodsOrderForm){

        List<String> outGroupIds = IdUtils.getIdList(CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.PRODUCT_COUNTER_GROUP_IDS.name()).getValue());

        if(!adGoodsOrderForm.isCreate()){
            AdGoodsOrder adGoodsOrder = adGoodsOrderMapper.findOne(adGoodsOrderForm.getId());
            adGoodsOrderForm = BeanUtil.map(adGoodsOrder,AdGoodsOrderForm.class);
            ExpressOrder expressOrder = expressOrderMapper.findOne(adGoodsOrderForm.getExpressOrderId());
            adGoodsOrderForm.setExpressOrderDto(BeanUtil.map(expressOrder, ExpressOrderDto.class));
            List<AdGoodsOrderDetailDto> adGoodsOrderDetails = adGoodsOrderDetailMapper.findByAdGoodsOrderForEdit(outGroupIds,adGoodsOrderForm.getId());
            adGoodsOrderForm.setAdGoodsOrderDetails(adGoodsOrderDetails);
            cacheUtils.initCacheInput(adGoodsOrderForm);
        }else{
           List<AdGoodsOrderDetailDto> adGoodsOrderDetails = adGoodsOrderDetailMapper.findByAdGoodsOrderForNew(outGroupIds);
            adGoodsOrderForm.setAdGoodsOrderDetails(adGoodsOrderDetails);
            cacheUtils.initCacheInput(adGoodsOrderForm);
        }
        return adGoodsOrderForm;
    }

    public void audit(AdGoodsOrderForm adGoodsOrderForm) {
        if(!adGoodsOrderForm.isCreate()) {
            ActivitiCompleteForm activitiCompleteForm = new ActivitiCompleteForm();
            activitiCompleteForm.setPass(adGoodsOrderForm.getPass() == "1" ? true : false);
            activitiCompleteForm.setComment(adGoodsOrderForm.getPassRemarks());

            activitiCompleteForm.setProcessTypeId(adGoodsOrderMapper.findOne(adGoodsOrderForm.getId()).getProcessTypeId());
            activitiCompleteForm.setProcessInstanceId(adGoodsOrderMapper.findOne(adGoodsOrderForm.getId()).getProcessInstanceId());

            ActivitiCompleteDto activitiCompleteDto = activitiClient.complete(activitiCompleteForm);

            if(activitiCompleteDto!=null){
                AdGoodsOrder adGoodsOrder = adGoodsOrderMapper.findOne(adGoodsOrderForm.getId());
                adGoodsOrder.setProcessPositionId(activitiCompleteDto.getPositionId());
                adGoodsOrder.setProcessStatus(activitiCompleteDto.getProcessStatus());
                adGoodsOrder.setProcessFlowId(activitiCompleteDto.getProcessFlowId());
                adGoodsOrderMapper.update(adGoodsOrder);
            }

        }
    }
    
    public AdGoodsOrder bill(AdGoodsOrder adGoodsOrder) {
        Map<String,AdPricesystemDetail> priceMap = Maps.newHashMap();
        Depot shop=depotMapper.findOne(adGoodsOrder.getShopId());
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
