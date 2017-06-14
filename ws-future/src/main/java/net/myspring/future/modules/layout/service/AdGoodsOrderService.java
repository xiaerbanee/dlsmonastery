package net.myspring.future.modules.layout.service;

import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.enums.BillTypeEnum;
import net.myspring.future.common.enums.ExpressOrderTypeEnum;
import net.myspring.future.common.enums.ShipTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.ActivitiClient;
import net.myspring.future.modules.basic.domain.Client;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.repository.*;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.repository.ExpressOrderRepository;
import net.myspring.future.modules.layout.domain.AdGoodsOrder;
import net.myspring.future.modules.layout.domain.AdGoodsOrderDetail;
import net.myspring.future.modules.layout.dto.AdGoodsOrderDetailDto;
import net.myspring.future.modules.layout.dto.AdGoodsOrderDto;
import net.myspring.future.modules.layout.repository.AdGoodsOrderDetailRepository;
import net.myspring.future.modules.layout.repository.AdGoodsOrderRepository;
import net.myspring.future.modules.layout.repository.ShopDepositRepository;
import net.myspring.future.modules.layout.web.form.AdGoodsOrderForm;
import net.myspring.future.modules.layout.web.query.AdGoodsOrderQuery;
import net.myspring.general.modules.sys.dto.ActivitiStartDto;
import net.myspring.general.modules.sys.form.ActivitiStartForm;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.IdUtils;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AdGoodsOrderService {
    @Autowired
    private AdGoodsOrderRepository adGoodsOrderRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AdGoodsOrderDetailRepository adGoodsOrderDetailRepository;
    @Autowired
    private ExpressOrderRepository expressOrderRepository;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private ShopDepositRepository shopDepositRepository;
    @Autowired
    private AdPricesystemDetailRepository adPricesystemDetailRepository;
    @Autowired
    private AdpricesystemRepository adpricesystemRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private ActivitiClient activitiClient;
    @Autowired
    private RedisTemplate redisTemplate;

    public Page<AdGoodsOrderDto> findPage(Pageable pageable, AdGoodsOrderQuery adGoodsOrderQuery) {
        Page<AdGoodsOrderDto> page = adGoodsOrderRepository.findPage(pageable, adGoodsOrderQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }


//    public Map<String,Object> getAmountMap(AdGoodsOrder adGoodsOrder){
//        Map<String,Object> map=Maps.newHashMap();
//        // 统计应付运费,以门店物料运费为准
//        Map<String,AdPricesystemDetail> priceMap = Maps.newHashMap();
//        Depot shop=depotRepository.findOne(adGoodsOrder.getShopId());
//
//
//        BigDecimal yfyfAmount = BigDecimal.ZERO;
//        for (AdGoodsOrderDetail adGoodsOrderDetail : adGoodsOrder.getAdGoodsOrderDetailList()) {
//            if (priceMap.get(adGoodsOrderDetail.getProductId()) != null) {
//                BigDecimal price = priceMap.get(adGoodsOrderDetail.getProductId()).getPrice();
//                if (price != null) {
//                    yfyfAmount = yfyfAmount.add(new BigDecimal(adGoodsOrderDetail.getBillQty()).multiply(price));
//                }
//            }
//        }
//        // 统计应收运费 ，全部以A类物料运费为准
//        Map<String, AdPricesystemDetail> ysyfMap = Maps.newHashMap();
//        AdPricesystem defaultAdPricesystem = adpricesystemRepository.findByName("");
//        if (defaultAdPricesystem != null) {
//            List<AdPricesystemDetail> adPricesystemDetailList=adPricesystemDetailRepository.findByAdPricesystemId(defaultAdPricesystem.getId());
//            for (AdPricesystemDetail adDetail : adPricesystemDetailList) {
//                ysyfMap.put(adDetail.getProductId(), adDetail);
//            }
//        }
//        BigDecimal ysyfAmount = BigDecimal.ZERO;
//        for (AdGoodsOrderDetail adGoodsOrderDetail : adGoodsOrder.getAdGoodsOrderDetailList()) {
//            if (ysyfMap.get(adGoodsOrderDetail.getProductId()) != null) {
//                BigDecimal price = ysyfMap.get(adGoodsOrderDetail.getProductId()).getPrice();
//                if (price != null) {
//                    ysyfAmount = ysyfAmount.add(new BigDecimal(adGoodsOrderDetail.getBillQty()).multiply(price));
//                }
//            }
//        }
//        map.put("ysyfAmount", ysyfAmount);
//        map.put("yfyfAmount", yfyfAmount);
//        map.put("priceMap", priceMap);
//        return map;
//    }

    public void save(AdGoodsOrderForm adGoodsOrderForm) {



            Depot outShop=depotRepository.findOne(adGoodsOrderForm.getOutShopId());
            Client client = clientRepository.findByDepotId(outShop.getId());
            if(StringUtils.isBlank(client.getOutId())){
                throw new ServiceException(outShop.getName()+" 没有关联财务账号，不能申请");
            }

            AdGoodsOrder adGoodsOrder = new AdGoodsOrder();
            if(StringUtils.isBlank(adGoodsOrderForm.getShopId())){
                adGoodsOrder.setShopId(outShop.getId());
            }else{
                adGoodsOrder.setShopId(adGoodsOrderForm.getShopId());
            }
            adGoodsOrder.setOutShopId(outShop.getId());
            adGoodsOrder.setBillType(BillTypeEnum.柜台.name());
            adGoodsOrder.setEmployeeId(adGoodsOrderForm.getEmployeeId());
            adGoodsOrderRepository.save(adGoodsOrder);

            saveAdGoodsOrderDetailList(adGoodsOrder.getId(), adGoodsOrderForm.getAdGoodsOrderDetailList());

            List<AdGoodsOrderDetailDto> detailList = adGoodsOrderDetailRepository.findDtoListByAdGoodsOrderId(adGoodsOrder.getId());
            BigDecimal amount = BigDecimal.ZERO;
            for (AdGoodsOrderDetailDto adGoodsOrderDetailDto : detailList) {
                amount = amount.add(adGoodsOrderDetailDto.getProductPrice2().multiply(new BigDecimal(adGoodsOrderDetailDto.getConfirmQty())));
            }
            adGoodsOrder.setAmount(amount);

            ExpressOrder savedExpressOrder = saveExpressOrder(adGoodsOrder, adGoodsOrderForm);
            adGoodsOrder.setExpressOrderId(savedExpressOrder.getId());
            adGoodsOrderRepository.save(adGoodsOrder);


            if (adGoodsOrderForm.isCreate()) {
                startAndSaveProcessFlow(adGoodsOrder);

            }



    }

    private void startAndSaveProcessFlow(AdGoodsOrder adGoodsOrder) {

        ActivitiStartDto activitiStartDto = activitiClient.start(new ActivitiStartForm("柜台订货",adGoodsOrder.getId(),AdGoodsOrder.class.getSimpleName(),adGoodsOrder.getOutShopId()));

        adGoodsOrder.setProcessFlowId(activitiStartDto.getProcessFlowId());
        adGoodsOrder.setProcessInstanceId(activitiStartDto.getProcessInstanceId());
        adGoodsOrder.setProcessStatus(activitiStartDto.getProcessStatus());
        adGoodsOrder.setProcessTypeId(activitiStartDto.getProcessTypeId());
        adGoodsOrder.setProcessPositionId(activitiStartDto.getPositionId());

        adGoodsOrderRepository.save(adGoodsOrder);

    }

    private ExpressOrder saveExpressOrder(AdGoodsOrder adGoodsOrder, AdGoodsOrderForm adGoodsOrderForm) {

        ExpressOrder expressOrder;

        String expressOrderId = adGoodsOrder.getExpressOrderId();
        if(StringUtils.isBlank(expressOrderId)){
            expressOrder = new ExpressOrder();
        }else{
            expressOrder = expressOrderRepository.findOne(expressOrderId);
        }

        expressOrder.setExtendBusinessId(adGoodsOrder.getBusinessId());
        expressOrder.setToDepotId(adGoodsOrder.getShopId());
        expressOrder.setExtendType(ExpressOrderTypeEnum.物料订单.name());
        expressOrder.setExpressCompanyId(adGoodsOrderForm.getExpressCompanyId());
        expressOrder.setShipType(ShipTypeEnum.总部发货.name());
        expressOrder.setPrintDate(LocalDate.now());
        expressOrder.setLocked(true);
        expressOrder.setExtendId(adGoodsOrder.getId());

        expressOrderRepository.save(expressOrder);

        return expressOrder;
    }

    private void saveAdGoodsOrderDetailList(String adGoodsOrderId, List<AdGoodsOrderDetailDto> adGoodsOrderDetailList) {

        List<AdGoodsOrderDetail> toBeSaved = new ArrayList<>();
        for(AdGoodsOrderDetailDto adGoodsOrderDetailDto : adGoodsOrderDetailList){
            AdGoodsOrderDetail adGoodsOrderDetail;

            if(adGoodsOrderDetailDto.getQty() !=null && adGoodsOrderDetailDto.getQty() < 0){
                throw new ServiceException("订货数量不可以小于0");
            }

            if(StringUtils.isBlank(adGoodsOrderDetailDto.getId())){
                adGoodsOrderDetail = new AdGoodsOrderDetail();
                adGoodsOrderDetail.setAdGoodsOrderId(adGoodsOrderId);
                adGoodsOrderDetail.setProductId(adGoodsOrderDetailDto.getProductId());
                adGoodsOrderDetail.setQty(adGoodsOrderDetailDto.getQty() == null ? 0 : adGoodsOrderDetailDto.getQty());
                adGoodsOrderDetail.setConfirmQty(adGoodsOrderDetail.getQty());
                adGoodsOrderDetail.setBillQty(0);
                adGoodsOrderDetail.setShippedQty(0);
                adGoodsOrderDetail.setShouldGet(BigDecimal.ZERO);
                adGoodsOrderDetail.setShouldPay(BigDecimal.ZERO);
            }else{
                adGoodsOrderDetail = adGoodsOrderDetailRepository.findOne(adGoodsOrderDetailDto.getId());
                adGoodsOrderDetail.setQty(adGoodsOrderDetailDto.getQty() == null ? 0 : adGoodsOrderDetailDto.getQty());
                adGoodsOrderDetail.setConfirmQty(adGoodsOrderDetail.getQty());
            }
            toBeSaved.add(adGoodsOrderDetail);
        }
        adGoodsOrderDetailRepository.save(toBeSaved);
    }

    public AdGoodsOrderDto findOne(String id) {
        AdGoodsOrderDto adGoodsOrderDto = new AdGoodsOrderDto();
        if(id!=null){
            AdGoodsOrder adGoodsOrder = adGoodsOrderRepository.findOne(id);
            adGoodsOrderDto = BeanUtil.map(adGoodsOrder,AdGoodsOrderDto.class);
//            if(adGoodsOrderDto.getExpressOrderId()!=null){
//                adGoodsOrderDto.setExpressOrderDto(expressOrderRepository.findDto(adGoodsOrderDto.getExpressOrderId()));
//            }
            cacheUtils.initCacheInput(adGoodsOrderDto);
        }
        return adGoodsOrderDto;
    }

    public AdGoodsOrderForm getForm(AdGoodsOrderForm adGoodsOrderForm){


//        List<String> outGroupIds = IdUtils.getIdList(CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(),CompanyConfigCodeEnum.PRODUCT_COUNTER_GROUP_IDS.name()).getValue());
//
//        if(!adGoodsOrderForm.isCreate()){
//            List<AdGoodsOrderDetailDto> adGoodsOrderDetails = adGoodsOrderDetailRepository.findByAdGoodsOrderForEdit(outGroupIds,adGoodsOrderForm.getId());
//            adGoodsOrderForm.setAdGoodsOrderDetails(adGoodsOrderDetails);
//        }else{
//           List<AdGoodsOrderDetailDto> adGoodsOrderDetails = adGoodsOrderDetailRepository.findByAdGoodsOrderForNew(outGroupIds);
//            adGoodsOrderForm.setAdGoodsOrderDetails(adGoodsOrderDetails);
//
//        }
//        cacheUtils.initCacheInput(adGoodsOrderForm);
        return adGoodsOrderForm;
    }

    public void audit(AdGoodsOrderForm adGoodsOrderForm) {
//        if(!adGoodsOrderForm.isCreate()) {
//            ActivitiCompleteForm activitiCompleteForm = new ActivitiCompleteForm();
//            activitiCompleteForm.setPass(adGoodsOrderForm.getPass());
//            activitiCompleteForm.setComment(adGoodsOrderForm.getPassRemarks());
//
//            activitiCompleteForm.setProcessTypeId(adGoodsOrderRepository.findOne(adGoodsOrderForm.getId()).getProcessTypeId());
//            activitiCompleteForm.setProcessInstanceId(adGoodsOrderRepository.findOne(adGoodsOrderForm.getId()).getProcessInstanceId());
//
//            ActivitiCompleteDto activitiCompleteDto = activitiClient.complete(activitiCompleteForm);
//
//            if(activitiCompleteDto!=null){
//                AdGoodsOrder adGoodsOrder = adGoodsOrderRepository.findOne(adGoodsOrderForm.getId());
//                adGoodsOrder.setProcessPositionId(activitiCompleteDto.getPositionId());
//                adGoodsOrder.setProcessStatus(activitiCompleteDto.getProcessStatus());
//                adGoodsOrder.setProcessFlowId(activitiCompleteDto.getProcessFlowId());
//                adGoodsOrderRepository.save(adGoodsOrder);
//            }
//
//        }
    }
    
//    public AdGoodsOrder bill(AdGoodsOrder adGoodsOrder) {
//        Map<String,AdPricesystemDetail> priceMap = Maps.newHashMap();
//        Depot shop=depotRepository.findOne(adGoodsOrder.getShopId());
//        List<AdGoodsOrderDetail> adGoodsOrderDetailList=Lists.newArrayList();
//        for (int i = adGoodsOrder.getAdGoodsOrderDetailList().size() - 1; i >= 0; i--) {
//            AdGoodsOrderDetail agod = adGoodsOrder.getAdGoodsOrderDetailList().get(i);
//            agod.setQty(agod.getQty()==null?0:agod.getQty());
//            agod.setConfirmQty(agod.getConfirmQty()==null?0:agod.getConfirmQty());
//            agod.setBillQty(agod.getBillQty()==null?0:agod.getBillQty());
//            if(agod.getBillQty()!=null&&agod.getBillQty() > 0&&(agod.getQty() == null || agod.getQty() == 0)) {
//                agod.setQty(0);
//                agod.setShippedQty(0);
//                agod.setAdGoodsOrderId(adGoodsOrder.getId());
//                adGoodsOrderDetailList.add(agod);
//            }else if(agod.getBillQty()!=null&&agod.getBillQty() > 0){
//                adGoodsOrderDetailRepository.save(agod);
//            }
//        }
//        //应付运费
//        BigDecimal shouldPay = BigDecimal.ZERO;
//        //应收运费
//        BigDecimal shouldGet = BigDecimal.ZERO;
//        BigDecimal amount = BigDecimal.ZERO;
//        for(AdGoodsOrderDetail adGoodsOrderDetail:adGoodsOrder.getAdGoodsOrderDetailList()) {
//            if(adGoodsOrderDetail.getBillQty()>0) {
//                BigDecimal price2 = adGoodsOrderDetail.getProduct().getPrice2();
//                amount = amount.add(new BigDecimal(adGoodsOrderDetail.getBillQty()).multiply(price2));
//                if (priceMap.containsKey(adGoodsOrderDetail.getProductId())) {
//                    adGoodsOrderDetail.setShouldPay(priceMap.get(adGoodsOrderDetail.getProductId()).getPrice());
//                    adGoodsOrderDetail.setShouldGet(adGoodsOrderDetail.getProduct().getShouldGet());
//                    shouldPay = shouldPay.add(adGoodsOrderDetail.getShouldPay().multiply(new BigDecimal(adGoodsOrderDetail.getBillQty())));
//                    shouldGet = shouldGet.add(adGoodsOrderDetail.getShouldGet().multiply(new BigDecimal(adGoodsOrderDetail.getBillQty())));
//                }
//            }
//        }
//        if(adGoodsOrder.getParentId()==null){
//            adGoodsOrder.setParentId(adGoodsOrder.getId());
//        }
//
//        //分批开单
//        if(adGoodsOrder.getSplitBill()){
//            List<AdGoodsOrderDetail> newAdGoodsOrderDetails= Lists.newArrayList();
//            for(AdGoodsOrderDetail adGoodsOrderDetail:adGoodsOrder.getAdGoodsOrderDetailList()){
//                if(adGoodsOrderDetail.getConfirmQty()>adGoodsOrderDetail.getBillQty()){
//                    AdGoodsOrderDetail newAdGoodsOrderDetail=new AdGoodsOrderDetail();
//                    Integer billEnabledQty=adGoodsOrderDetail.getConfirmQty()-adGoodsOrderDetail.getBillQty();
//                    newAdGoodsOrderDetail.setProduct(adGoodsOrderDetail.getProduct());
//                    newAdGoodsOrderDetail.setQty(billEnabledQty);
//                    newAdGoodsOrderDetail.setConfirmQty(billEnabledQty);
//                    newAdGoodsOrderDetail.setBillQty(0);
//                    newAdGoodsOrderDetail.setShippedQty(0);
//                    newAdGoodsOrderDetails.add(newAdGoodsOrderDetail);
//                }
//            }
//            if(CollectionUtil.isNotEmpty(newAdGoodsOrderDetails)){
//            }
//        }
//        return adGoodsOrder;
//    }
//
//    public Boolean ship(AdGoodsOrder adGoodsOrder) {
//        for (AdGoodsOrderDetail adGoodsOrderDetail : adGoodsOrder.getAdGoodsOrderDetailList()) {
//            if (adGoodsOrderDetail.getShipQty() != null && adGoodsOrderDetail.getShipQty() > 0) {
//                Integer shippedQty = adGoodsOrderDetail.getShippedQty();
//                Integer shipQty = adGoodsOrderDetail.getShipQty();
//                adGoodsOrderDetail.setShippedQty(shippedQty + shipQty);
//                adGoodsOrderDetailRepository.save(adGoodsOrderDetail);
//            }
//        }
//        boolean isAllShipped = true;
//        for (AdGoodsOrderDetail adGoodsOrderDetail : adGoodsOrder.getAdGoodsOrderDetailList()) {
//            if (adGoodsOrderDetail.getBillQty()==adGoodsOrderDetail.getShippedQty()) {
//                isAllShipped = false;
//                break;
//            }
//        }
//        expressOrderRepository.save(adGoodsOrder.getExpressOrder());
//        adGoodsOrderRepository.save(adGoodsOrder);
//        return true;
//    }

    public void sign(AdGoodsOrder adGoodsOrder) {
    }

    public void logicDelete(String id) {
        adGoodsOrderRepository.logicDelete(id);
    }

    public List<AdGoodsOrderDetailDto> findDetailListForNewOrEdit(String adGoodsOrderId, boolean includeNotAllowOrderProduct) {

        List<String> outGroupIds = IdUtils.getIdList(CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.PRODUCT_COUNTER_GROUP_IDS.name()).getValue());

        List<AdGoodsOrderDetailDto> result;
        if(StringUtils.isBlank(adGoodsOrderId)){
            result = adGoodsOrderDetailRepository.findDetailListForNew(RequestUtils.getCompanyId(), outGroupIds, includeNotAllowOrderProduct);
        }else{
            result = adGoodsOrderDetailRepository.findDetailListForEdit(adGoodsOrderId, RequestUtils.getCompanyId(), outGroupIds, includeNotAllowOrderProduct);
        }
        cacheUtils.initCacheInput(result);
        return result;
    }


//    public void print(AdGoodsOrder adGoodsOrder) {
//        ExpressOrder expressOrder = adGoodsOrder.getExpressOrder();
//        if(expressOrder != null){
//            if(expressOrder.getOutPrintDate() == null){
//                expressOrder.setOutPrintDate(LocalDateTime.now());
//            }
//            expressOrderRepository.save(expressOrder);
//        }
//    }

}
