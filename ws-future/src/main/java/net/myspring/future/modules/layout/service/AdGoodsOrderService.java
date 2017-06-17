package net.myspring.future.modules.layout.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.gridfs.GridFSFile;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.enums.AdGoodsOrderStatusEnum;
import net.myspring.future.common.enums.BillTypeEnum;
import net.myspring.future.common.enums.ExpressOrderTypeEnum;
import net.myspring.future.common.enums.ShipTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.ActivitiClient;
import net.myspring.future.modules.basic.domain.*;
import net.myspring.future.modules.basic.repository.*;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.manager.ExpressOrderManager;
import net.myspring.future.modules.crm.repository.ExpressOrderRepository;
import net.myspring.future.modules.crm.repository.ExpressRepository;
import net.myspring.future.modules.layout.domain.AdGoodsOrder;
import net.myspring.future.modules.layout.domain.AdGoodsOrderDetail;
import net.myspring.future.modules.layout.dto.AdGoodsOrderDetailExportDto;
import net.myspring.future.modules.layout.dto.AdGoodsOrderDetailSimpleDto;
import net.myspring.future.modules.layout.dto.AdGoodsOrderDto;
import net.myspring.future.modules.layout.repository.AdGoodsOrderDetailRepository;
import net.myspring.future.modules.layout.repository.AdGoodsOrderRepository;
import net.myspring.future.modules.layout.repository.ShopDepositRepository;
import net.myspring.future.modules.layout.web.form.*;
import net.myspring.future.modules.layout.web.query.AdGoodsOrderQuery;
import net.myspring.general.modules.sys.dto.ActivitiCompleteDto;
import net.myspring.general.modules.sys.dto.ActivitiStartDto;
import net.myspring.general.modules.sys.form.ActivitiCompleteForm;
import net.myspring.general.modules.sys.form.ActivitiStartForm;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.text.IdUtils;
import net.myspring.util.text.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private ExpressRepository expressRepository;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private ShopDepositRepository shopDepositRepository;
    @Autowired
    private AdPricesystemDetailRepository adPricesystemDetailRepository;
    @Autowired
    private AdpricesystemRepository adpricesystemRepository;
    @Autowired
    private ExpressOrderManager expressOrderManager;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private ActivitiClient activitiClient;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private GridFsTemplate tempGridFsTemplate;

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
            if(client == null || StringUtils.isBlank(client.getOutId())){
                throw new ServiceException(outShop.getName()+" 没有关联财务账号，不能申请");
            }

            AdGoodsOrder adGoodsOrder;
            if(adGoodsOrderForm.isCreate()){
                adGoodsOrder = new AdGoodsOrder();
            }else{
                adGoodsOrder = adGoodsOrderRepository.findOne(adGoodsOrderForm.getId());
            }

            if(StringUtils.isBlank(adGoodsOrderForm.getShopId())){
                adGoodsOrder.setShopId(outShop.getId());
            }else{
                adGoodsOrder.setShopId(adGoodsOrderForm.getShopId());
            }
            adGoodsOrder.setOutShopId(outShop.getId());
            adGoodsOrder.setInvestInCause(adGoodsOrderForm.getInvestInCause());
            adGoodsOrder.setBillType(BillTypeEnum.柜台.name());
            adGoodsOrder.setEmployeeId(adGoodsOrderForm.getEmployeeId());
        //TODO 确认下面的billAddress赋值语句是否正确
            adGoodsOrder.setBillAddress(adGoodsOrderForm.getExpressOrderAddress());
            adGoodsOrder.setSplitBill(false);
            adGoodsOrder.setAmount(BigDecimal.ZERO);
            adGoodsOrder.setSmallQty(0);
            adGoodsOrder.setMediumQty(0);
            adGoodsOrder.setLargeQty(0);
            adGoodsOrder.setRemarks(adGoodsOrderForm.getRemarks());
            adGoodsOrderRepository.save(adGoodsOrder);

            saveAdGoodsOrderDetailInfo(adGoodsOrder, adGoodsOrderForm.getAdGoodsOrderDetailList());

            saveExpressOrderInfo(adGoodsOrder, adGoodsOrderForm);

            if (adGoodsOrderForm.isCreate()) {
                startAndSaveProcessFlowInfo(adGoodsOrder);
            }
    }

    private void startAndSaveProcessFlowInfo(AdGoodsOrder adGoodsOrder) {

        ActivitiStartDto activitiStartDto = activitiClient.start(new ActivitiStartForm("柜台订货",adGoodsOrder.getId(),AdGoodsOrder.class.getSimpleName(),adGoodsOrder.getOutShopId()));

        adGoodsOrder.setProcessFlowId(activitiStartDto.getProcessFlowId());
        adGoodsOrder.setProcessInstanceId(activitiStartDto.getProcessInstanceId());
        adGoodsOrder.setProcessStatus(activitiStartDto.getProcessStatus());
        adGoodsOrder.setProcessTypeId(activitiStartDto.getProcessTypeId());
        adGoodsOrder.setProcessPositionId(activitiStartDto.getPositionId());

        adGoodsOrderRepository.save(adGoodsOrder);

    }

    private ExpressOrder saveExpressOrderInfo(AdGoodsOrder adGoodsOrder, AdGoodsOrderForm adGoodsOrderForm) {

        ExpressOrder expressOrder;

        String expressOrderId = adGoodsOrder.getExpressOrderId();
        if(StringUtils.isBlank(expressOrderId)){
            expressOrder = new ExpressOrder();
        }else{
            expressOrder = expressOrderRepository.findOne(expressOrderId);
        }

        expressOrder.setContator(adGoodsOrderForm.getExpressOrderContator());
        expressOrder.setMobilePhone(adGoodsOrderForm.getExpressOrderMobilePhone());
        expressOrder.setAddress(adGoodsOrderForm.getExpressOrderAddress());
        expressOrder.setExtendBusinessId(adGoodsOrder.getBusinessId());
        expressOrder.setToDepotId(adGoodsOrder.getShopId());
        expressOrder.setExtendType(ExpressOrderTypeEnum.物料订单.name());
        expressOrder.setExpressCompanyId(adGoodsOrderForm.getExpressOrderExpressCompanyId());
        expressOrder.setShipType(ShipTypeEnum.总部发货.name());
        expressOrder.setPrintDate(LocalDate.now());
        expressOrder.setLocked(true);
        expressOrder.setExtendId(adGoodsOrder.getId());

        expressOrderRepository.save(expressOrder);

        adGoodsOrder.setExpressOrderId(expressOrder.getId());
        adGoodsOrderRepository.save(adGoodsOrder);

        return expressOrder;
    }

    private void saveAdGoodsOrderDetailInfo(AdGoodsOrder adGoodsOrder, List<AdGoodsOrderDetailForm> adGoodsOrderDetailList) {

        List<AdGoodsOrderDetail> toBeSaved = new ArrayList<>();
        for(AdGoodsOrderDetailForm adGoodsOrderDetailForm : adGoodsOrderDetailList){
            AdGoodsOrderDetail adGoodsOrderDetail;

            if(adGoodsOrderDetailForm.getQty() !=null && adGoodsOrderDetailForm.getQty() < 0){
                throw new ServiceException("订货数量不可以小于0");
            }

            if(StringUtils.isBlank(adGoodsOrderDetailForm.getId())){
                adGoodsOrderDetail = new AdGoodsOrderDetail();
                adGoodsOrderDetail.setAdGoodsOrderId(adGoodsOrder.getId());
                adGoodsOrderDetail.setProductId(adGoodsOrderDetailForm.getProductId());
                adGoodsOrderDetail.setQty(adGoodsOrderDetailForm.getQty() == null ? 0 : adGoodsOrderDetailForm.getQty());
                adGoodsOrderDetail.setConfirmQty(adGoodsOrderDetail.getQty());
                adGoodsOrderDetail.setBillQty(0);
                adGoodsOrderDetail.setShippedQty(0);
                adGoodsOrderDetail.setShouldGet(BigDecimal.ZERO);
                adGoodsOrderDetail.setShouldPay(BigDecimal.ZERO);
            }else{
                adGoodsOrderDetail = adGoodsOrderDetailRepository.findOne(adGoodsOrderDetailForm.getId());
                adGoodsOrderDetail.setQty(adGoodsOrderDetailForm.getQty() == null ? 0 : adGoodsOrderDetailForm.getQty());
                adGoodsOrderDetail.setConfirmQty(adGoodsOrderDetail.getQty());
            }
            toBeSaved.add(adGoodsOrderDetail);
        }
        adGoodsOrderDetailRepository.save(toBeSaved);

        Map<String, Product> productMap = productRepository.findMap(CollectionUtil.extractToList(toBeSaved, "productId"));
        BigDecimal amount = BigDecimal.ZERO;
        for (AdGoodsOrderDetail adGoodsOrderDetail : toBeSaved) {
            amount = amount.add(productMap.get(adGoodsOrderDetail.getProductId()).getPrice2().multiply(new BigDecimal(adGoodsOrderDetail.getConfirmQty())));
        }
        adGoodsOrder.setAmount(amount);
        adGoodsOrderRepository.save(adGoodsOrder);
    }

    public AdGoodsOrderDto findDto(String id) {

        AdGoodsOrderDto adGoodsOrderDto = adGoodsOrderRepository.findDto(id);
        cacheUtils.initCacheInput(adGoodsOrderDto);
        return adGoodsOrderDto;
    }

    public void logicDelete(String id) {
        adGoodsOrderRepository.logicDelete(id);
    }

    public List<AdGoodsOrderDetailSimpleDto> findDetailListForNewOrEdit(String adGoodsOrderId, boolean includeNotAllowOrderProduct) {

        List<String> outGroupIdList = IdUtils.getIdList(CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.PRODUCT_COUNTER_GROUP_IDS.name()).getValue());

        List<AdGoodsOrderDetailSimpleDto> result;
        if(StringUtils.isBlank(adGoodsOrderId)){
            result = adGoodsOrderDetailRepository.findDetailListForNew(RequestUtils.getCompanyId(), outGroupIdList, includeNotAllowOrderProduct);
        }else{
            result = adGoodsOrderDetailRepository.findDetailListForEdit(adGoodsOrderId, RequestUtils.getCompanyId(), outGroupIdList, includeNotAllowOrderProduct);
        }
        cacheUtils.initCacheInput(result);
        return result;
    }

    public void audit(AdGoodsOrderAuditForm adGoodsOrderAuditForm) {

        ActivitiCompleteForm activitiCompleteForm = new ActivitiCompleteForm();
        activitiCompleteForm.setPass(adGoodsOrderAuditForm.getPass());
        activitiCompleteForm.setComment(adGoodsOrderAuditForm.getRemarks());
        AdGoodsOrder adGoodsOrder = adGoodsOrderRepository.findOne(adGoodsOrderAuditForm.getId());
        activitiCompleteForm.setProcessTypeId(adGoodsOrder.getProcessTypeId());
        activitiCompleteForm.setProcessInstanceId(adGoodsOrder.getProcessInstanceId());
        ActivitiCompleteDto activitiCompleteDto = activitiClient.complete(activitiCompleteForm);

        adGoodsOrder.setProcessPositionId(activitiCompleteDto.getPositionId());
        adGoodsOrder.setProcessStatus(activitiCompleteDto.getProcessStatus());
        adGoodsOrder.setProcessFlowId(activitiCompleteDto.getProcessFlowId());
        adGoodsOrder.setLocked(true);
        adGoodsOrderRepository.save(adGoodsOrder);

    }

    public List<AdGoodsOrderDetailSimpleDto> findDetailListForBill(String adGoodsOrderId) {

        List<String> outGroupIdList = IdUtils.getIdList(CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.PRODUCT_COUNTER_GROUP_IDS.name()).getValue());

        List<AdGoodsOrderDetailSimpleDto>  result = adGoodsOrderDetailRepository.findDetailListForBill(adGoodsOrderId, RequestUtils.getCompanyId(), outGroupIdList);

        cacheUtils.initCacheInput(result);
        return result;
    }

    public List<AdGoodsOrderDetailSimpleDto> findDetailListByAdGoodsOrderId(String adGoodsOrderId) {

        List<AdGoodsOrderDetailSimpleDto>  result = adGoodsOrderDetailRepository.findDtoListByAdGoodsOrderId(adGoodsOrderId);
        cacheUtils.initCacheInput(result);
        return result;
    }

    public void bill(AdGoodsOrderBillForm adGoodsOrderBillForm) {

        AdGoodsOrder adGoodsOrder = adGoodsOrderRepository.findOne(adGoodsOrderBillForm.getId());
        adGoodsOrder.setBusinessId(IdUtils.getNextBusinessId(adGoodsOrderRepository.findMaxBusinessId(adGoodsOrderBillForm.getBillDate())));
        adGoodsOrder.setStoreId(adGoodsOrderBillForm.getStoreId());
        adGoodsOrder.setBillDate(adGoodsOrderBillForm.getBillDate());
        adGoodsOrder.setBillAddress(StringUtils.isBlank(adGoodsOrderBillForm.getBillAddress()) ? adGoodsOrderBillForm.getExpressOrderAddress() : adGoodsOrderBillForm.getBillAddress());
        if(StringUtils.isBlank(adGoodsOrder.getParentId())){
            adGoodsOrder.setParentId(adGoodsOrder.getId());
        }
        adGoodsOrderRepository.save(adGoodsOrder);

        saveDetailInfoWhenBill(adGoodsOrder, adGoodsOrderBillForm.getAdGoodsOrderDetailList());

        if(Boolean.TRUE.equals(adGoodsOrderBillForm.getSplitBill())){
            splitAdGoodsOrder(adGoodsOrder, adGoodsOrderBillForm);
        }

        saveExpressOrderInfoWhenBill(adGoodsOrder);

        //如果有工作流，审批通过
        if(StringUtils.isNotBlank(adGoodsOrder.getProcessInstanceId())) {
            doAndSaveProcessInfo(adGoodsOrder, true, "");
        } else {
            adGoodsOrder.setProcessStatus(AdGoodsOrderStatusEnum.待发货.name());
            adGoodsOrderRepository.save(adGoodsOrder);
        }

        if(Boolean.TRUE.equals(adGoodsOrderBillForm.getSyn())) {
            synWhenBill(adGoodsOrder);
        }
    }

    private void saveDetailInfoWhenBill(AdGoodsOrder adGoodsOrder, List<AdGoodsOrderBillDetailForm> adGoodsOrderDetailList) {

            Map<String, AdPricesystemDetail> priceMap = Maps.newHashMap();
            Depot depot = depotRepository.findOne(adGoodsOrder.getShopId());
            List<AdPricesystemDetail> adPricesystemDetailList = adPricesystemDetailRepository.findByAdPricesystemId(depot.getAdPricesystemId());
            for(AdPricesystemDetail adPricesystemDetail : adPricesystemDetailList) {
                    priceMap.put(adPricesystemDetail.getProductId(), adPricesystemDetail);
            }
            Map<String, Product> productMap = productRepository.findMap(CollectionUtil.extractToList(adGoodsOrderDetailList, "productId"));

            List<AdGoodsOrderDetail> toBeSaved = new ArrayList<>();
            for(AdGoodsOrderBillDetailForm adGoodsOrderBillDetailForm : adGoodsOrderDetailList){
                AdGoodsOrderDetail adGoodsOrderDetail;

                if(adGoodsOrderBillDetailForm.getBillQty() !=null && adGoodsOrderBillDetailForm.getBillQty() < 0){
                    throw new ServiceException("开单数量不可以小于0");
                }

                if(StringUtils.isBlank(adGoodsOrderBillDetailForm.getId())){
                    adGoodsOrderDetail = new AdGoodsOrderDetail();
                    adGoodsOrderDetail.setAdGoodsOrderId(adGoodsOrder.getId());
                    adGoodsOrderDetail.setProductId(adGoodsOrderBillDetailForm.getProductId());
                    adGoodsOrderDetail.setQty(0);
                    adGoodsOrderDetail.setConfirmQty(0);
                    adGoodsOrderDetail.setBillQty(adGoodsOrderBillDetailForm.getBillQty() == null ? 0 : adGoodsOrderBillDetailForm.getBillQty());
                    adGoodsOrderDetail.setShippedQty(0);

                }else{
                    adGoodsOrderDetail = adGoodsOrderDetailRepository.findOne(adGoodsOrderBillDetailForm.getId());
                    adGoodsOrderDetail.setBillQty(adGoodsOrderBillDetailForm.getBillQty() == null ? 0 : adGoodsOrderBillDetailForm.getBillQty());
                }

                if (priceMap.containsKey(adGoodsOrderDetail.getProductId())) {
                    adGoodsOrderDetail.setShouldPay(priceMap.get(adGoodsOrderDetail.getProductId()).getPrice());
                    adGoodsOrderDetail.setShouldGet(productMap.get(adGoodsOrderDetail.getProductId()).getShouldGet());
                }
                if (adGoodsOrderDetail.getShouldPay() == null) {
                    adGoodsOrderDetail.setShouldPay(BigDecimal.ZERO);
                }
                if (adGoodsOrderDetail.getShouldGet() == null) {
                    adGoodsOrderDetail.setShouldGet(BigDecimal.ZERO);
                }

                toBeSaved.add(adGoodsOrderDetail);
            }
            adGoodsOrderDetailRepository.save(toBeSaved);

            BigDecimal amount = BigDecimal.ZERO;
            for (AdGoodsOrderDetail adGoodsOrderDetail : toBeSaved) {
                amount = amount.add(productMap.get(adGoodsOrderDetail.getProductId()).getPrice2().multiply(new BigDecimal(adGoodsOrderDetail.getBillQty())));
            }
            adGoodsOrder.setAmount(amount);
            adGoodsOrderRepository.save(adGoodsOrder);
    }

    private void splitAdGoodsOrder(AdGoodsOrder adGoodsOrder, AdGoodsOrderBillForm adGoodsOrderBillForm) {

        //开始保存拆分后的newAdGoodsOrder的基本信息
        AdGoodsOrder newAdGoodsOrder=new AdGoodsOrder();
        newAdGoodsOrder.setStoreId(adGoodsOrder.getStoreId());
        newAdGoodsOrder.setOutShopId(adGoodsOrder.getOutShopId());
        newAdGoodsOrder.setShopId(adGoodsOrder.getShopId());
        newAdGoodsOrder.setBillType(BillTypeEnum.柜台.name());
        newAdGoodsOrder.setCreatedBy(adGoodsOrder.getCreatedBy());
        newAdGoodsOrder.setCreatedDate(adGoodsOrder.getCreatedDate());
        newAdGoodsOrder.setRemarks(adGoodsOrder.getRemarks());
        newAdGoodsOrder.setParentId(adGoodsOrder.getParentId());
        newAdGoodsOrder.setSplitBill(false);
        newAdGoodsOrder.setSmallQty(0);
        newAdGoodsOrder.setMediumQty(0);
        newAdGoodsOrder.setLargeQty(0);
        newAdGoodsOrder.setProcessStatus(AdGoodsOrderStatusEnum.待开单.name());
        adGoodsOrderRepository.save(newAdGoodsOrder);

        //开始保存拆分后的detail信息
        List<AdGoodsOrderDetail> newAdGoodsOrderDetails= Lists.newArrayList();
        List<AdGoodsOrderDetail> adGoodsOrderDetailList = adGoodsOrderDetailRepository.findByAdGoodsOrderId(adGoodsOrder.getId());
        for(AdGoodsOrderDetail adGoodsOrderDetail : adGoodsOrderDetailList){
            if(adGoodsOrderDetail.getConfirmQty()>adGoodsOrderDetail.getBillQty()){
                AdGoodsOrderDetail newAdGoodsOrderDetail=new AdGoodsOrderDetail();
                Integer billEnabledQty=adGoodsOrderDetail.getConfirmQty()-adGoodsOrderDetail.getBillQty();
                newAdGoodsOrderDetail.setProductId(adGoodsOrderDetail.getProductId());
                newAdGoodsOrderDetail.setQty(billEnabledQty);
                newAdGoodsOrderDetail.setConfirmQty(billEnabledQty);
                newAdGoodsOrderDetail.setBillQty(0);
                newAdGoodsOrderDetail.setShippedQty(0);
                newAdGoodsOrderDetail.setAdGoodsOrderId(newAdGoodsOrder.getId());
                newAdGoodsOrderDetails.add(newAdGoodsOrderDetail);
            }
        }
        adGoodsOrderDetailRepository.save(newAdGoodsOrderDetails);

        Map<String, Product> productMap = productRepository.findMap(CollectionUtil.extractToList(newAdGoodsOrderDetails, "productId"));
        BigDecimal childAmount = BigDecimal.ZERO;
        for (AdGoodsOrderDetail adGoodsOrderDetail : newAdGoodsOrderDetails) {
            BigDecimal price2 = productMap.get(adGoodsOrderDetail.getProductId()).getPrice2();
            childAmount = childAmount.add(price2.multiply(new BigDecimal(adGoodsOrderDetail.getConfirmQty())));
        }
        newAdGoodsOrder.setAmount(childAmount);
        adGoodsOrderRepository.save(newAdGoodsOrder);

        //开始保存拆分后的expressOrder信息
        ExpressOrder expressOrder=new ExpressOrder();
        expressOrder.setAddress(adGoodsOrderBillForm.getExpressOrderAddress());
        expressOrder.setContator(adGoodsOrderBillForm.getExpressOrderContactor());
        expressOrder.setMobilePhone(adGoodsOrderBillForm.getExpressOrderMobilePhone());
        expressOrder.setToDepotId(newAdGoodsOrder.getShopId());
        expressOrder.setLocked(true);
        expressOrder.setExtendType(ExpressOrderTypeEnum.物料订单.name());
        expressOrder.setShipType(ShipTypeEnum.总部发货.name());
        expressOrder.setPrintDate(LocalDate.now());
        expressOrder.setExtendId(newAdGoodsOrder.getId());
        expressOrderRepository.save(expressOrder);
        newAdGoodsOrder.setExpressOrderId(expressOrder.getId());
        adGoodsOrderRepository.save(newAdGoodsOrder);
    }

    private void synWhenBill(AdGoodsOrder adGoodsOrder) {
          //TODO 同步金蝶，同時更新自己的adGoodsOrder和expressOrder等
//        K3CloudSynEntity k3CloudSynEntity = new K3CloudSynEntity(K3CloudSave.K3CloudFormId.SAL_OUTSTOCK.name(),adGoodsOrder.getSaleOutstock(),adGoodsOrder.getId(),adGoodsOrder.getFormatId(), K3CloudSynEntity.ExtendType.柜台订货.name());
//        k3cloudSynDao.save(k3CloudSynEntity);
//        K3cloudUtils.save(k3CloudSynEntity);
//        k3CloudSynEntity.setLocked(true);
//        k3cloudSynDao.save(k3CloudSynEntity);
//        adGoodsOrder.setK3CloudSynEntity(k3CloudSynEntity);
//        adGoodsOrderDao.save(adGoodsOrder);
//        if(adGoodsOrder.getExpressOrder()!=null){
//            ExpressOrder exp=adGoodsOrder.getExpressOrder();
//            exp.setOutCode(adGoodsOrder.getOutCode());
//            expressOrderDao.save(exp);
//        }
    }

    private void saveExpressOrderInfoWhenBill(AdGoodsOrder adGoodsOrder) {

        ExpressOrder expressOrder = expressOrderRepository.findOne(adGoodsOrder.getExpressOrderId());
        expressOrder.setFromDepotId(adGoodsOrder.getStoreId());
        expressOrder.setExtendBusinessId(adGoodsOrder.getBusinessId());
        expressOrder.setExtendId(adGoodsOrder.getId());
        expressOrder.setToDepotId(adGoodsOrder.getShopId());
        expressOrder.setLocked(false);
        expressOrder.setExtendType(ExpressOrderTypeEnum.物料订单.name());
        expressOrder.setShipType(ShipTypeEnum.总部发货.name());
        expressOrder.setPrintDate(adGoodsOrder.getBillDate());

        List<AdGoodsOrderDetail> adGoodsOrderDetailList = adGoodsOrderDetailRepository.findByAdGoodsOrderId(adGoodsOrder.getId());

        BigDecimal shouldPay = BigDecimal.ZERO;
        BigDecimal shouldGet = BigDecimal.ZERO;
        for(AdGoodsOrderDetail adGoodsOrderDetail : adGoodsOrderDetailList) {
            if(adGoodsOrderDetail.getBillQty() != null && adGoodsOrderDetail.getBillQty()>0) {
                shouldPay = shouldPay.add(adGoodsOrderDetail.getShouldPay().multiply(new BigDecimal(adGoodsOrderDetail.getBillQty())));
                shouldGet = shouldGet.add(adGoodsOrderDetail.getShouldGet().multiply(new BigDecimal(adGoodsOrderDetail.getBillQty())));
            }
        }
        expressOrder.setShouldGet(shouldGet);
        expressOrder.setShouldPay(shouldPay);

        expressOrderRepository.save(expressOrder);

    }

    public void ship(AdGoodsOrderShipForm adGoodsOrderShipForm) {

        AdGoodsOrder adGoodsOrder = adGoodsOrderRepository.findOne(adGoodsOrderShipForm.getId());
        adGoodsOrder.setSmallQty(adGoodsOrderShipForm.getSmallQty());
        adGoodsOrder.setMediumQty(adGoodsOrderShipForm.getMediumQty());
        adGoodsOrder.setLargeQty(adGoodsOrderShipForm.getLargeQty());

        adGoodsOrderRepository.save(adGoodsOrder);

        Map<String, AdGoodsOrderShipDetailForm> adGoodsOrderShipFormMap = CollectionUtil.extractToMap(adGoodsOrderShipForm.getAdGoodsOrderDetailList(), "id");
        List<AdGoodsOrderDetail> adGoodsOrderDetailList = adGoodsOrderDetailRepository.findByAdGoodsOrderId(adGoodsOrderShipForm.getId());

        boolean isAllShipped = true;
        for (AdGoodsOrderDetail adGoodsOrderDetail : adGoodsOrderDetailList) {
            AdGoodsOrderShipDetailForm adGoodsOrderShipDetailForm = adGoodsOrderShipFormMap.get(adGoodsOrderDetail.getId());
            if (adGoodsOrderShipDetailForm !=null && adGoodsOrderShipDetailForm.getShipQty() != null && adGoodsOrderShipDetailForm.getShipQty() > 0) {
                adGoodsOrderDetail.setShippedQty(adGoodsOrderDetail.getShippedQty() + adGoodsOrderShipDetailForm.getShipQty());
            }
            if(adGoodsOrderDetail.getBillQty() - adGoodsOrderDetail.getShippedQty() > 0){
                isAllShipped = false;
            }
        }

        if (isAllShipped) {
            //如果有工作流，审批通过
            if(StringUtils.isNotBlank(adGoodsOrder.getProcessInstanceId())) {
                doAndSaveProcessInfo(adGoodsOrder, true, "");
            } else {
                adGoodsOrder.setProcessStatus(AdGoodsOrderStatusEnum.待签收.name());
                adGoodsOrderRepository.save(adGoodsOrder);
            }
        }
        adGoodsOrderRepository.save(adGoodsOrder);


            ExpressOrder expressOrder = expressOrderRepository.findOne(adGoodsOrder.getExpressOrderId());
            expressOrder.setExpressCompanyId(adGoodsOrderShipForm.getExpressOrderExpressComapnyId());
            expressOrder.setExpressCodes(adGoodsOrderShipForm.getExpressOrderExpressCodes());
            expressOrder.setShouldGet(adGoodsOrderShipForm.getExpressOrderShouldGet());
            expressOrder.setShouldPay(adGoodsOrderShipForm.getExpressOrderShouldPay());
            expressOrder.setRealPay(adGoodsOrderShipForm.getExpressOrderRealPay());
            expressOrderRepository.save(expressOrder);

            expressOrderManager.save(expressOrder.getExtendType(), expressOrder.getExtendId(), expressOrder.getExpressCodes(), expressOrder.getExpressCompanyId());

    }

    private void doAndSaveProcessInfo(AdGoodsOrder adGoodsOrder, boolean pass, String comment) {
        ActivitiCompleteForm activitiCompleteForm = new ActivitiCompleteForm();
        activitiCompleteForm.setPass(pass);
        activitiCompleteForm.setComment(comment);
        activitiCompleteForm.setProcessTypeId(adGoodsOrder.getProcessTypeId());
        activitiCompleteForm.setProcessInstanceId(adGoodsOrder.getProcessInstanceId());
        ActivitiCompleteDto activitiCompleteDto = activitiClient.complete(activitiCompleteForm);

        adGoodsOrder.setProcessPositionId(activitiCompleteDto.getPositionId());
        adGoodsOrder.setProcessStatus(activitiCompleteDto.getProcessStatus());
        adGoodsOrder.setProcessFlowId(activitiCompleteDto.getProcessFlowId());
        adGoodsOrder.setLocked(true);
        adGoodsOrderRepository.save(adGoodsOrder);
    }

    public void sign(String id) {
        AdGoodsOrder adGoodsOrder = adGoodsOrderRepository.findOne(id);

        if(StringUtils.isNotBlank(adGoodsOrder.getProcessInstanceId())) {
            doAndSaveProcessInfo(adGoodsOrder, true, "");
        } else {
            adGoodsOrder.setProcessStatus(AdGoodsOrderStatusEnum.已完成.name());
            adGoodsOrderRepository.save(adGoodsOrder);
        }
    }

    public AdGoodsOrderDto print(String id) {
        AdGoodsOrder adGoodsOrder = adGoodsOrderRepository.findOne(id);
        ExpressOrder expressOrder = expressOrderRepository.findOne(adGoodsOrder.getExpressOrderId());
        if(expressOrder != null && expressOrder.getOutPrintDate() == null ){
            expressOrder.setOutPrintDate(LocalDateTime.now());
            expressOrderRepository.saveAndFlush(expressOrder);
        }

        return findDto(id);

    }

    public String export(AdGoodsOrderQuery adGoodsOrderQuery) {


        Workbook workbook = new SXSSFWorkbook(10000);

        List<SimpleExcelSheet> simpleExcelSheetList = Lists.newArrayList();
        List<SimpleExcelColumn> adGoodsOrderColumnList = Lists.newArrayList();
        adGoodsOrderColumnList.add(new SimpleExcelColumn(workbook, "id", "订单号"));
        adGoodsOrderColumnList.add(new SimpleExcelColumn(workbook, "outCode", "财务编号"));
        adGoodsOrderColumnList.add(new SimpleExcelColumn(workbook, "outShopName", "财务门店"));
        adGoodsOrderColumnList.add(new SimpleExcelColumn(workbook, "shopAreaName", "办事处"));
        adGoodsOrderColumnList.add(new SimpleExcelColumn(workbook, "shopName", "门店"));
        adGoodsOrderColumnList.add(new SimpleExcelColumn(workbook, "depotShopAreaType", "区域类型"));
        adGoodsOrderColumnList.add(new SimpleExcelColumn(workbook, "processStatus", "当前状态"));
        adGoodsOrderColumnList.add(new SimpleExcelColumn(workbook, "expressOrderExpressCompanyName", "快递公司"));
        adGoodsOrderColumnList.add(new SimpleExcelColumn(workbook, "expressOrderExpressCodes", "快递单号"));
        adGoodsOrderColumnList.add(new SimpleExcelColumn(workbook, "shopAddress", "门店地址"));
        adGoodsOrderColumnList.add(new SimpleExcelColumn(workbook, "billAddress", "目的地"));
        adGoodsOrderColumnList.add(new SimpleExcelColumn(workbook, "expressOrderContator", "联系人"));
        adGoodsOrderColumnList.add(new SimpleExcelColumn(workbook, "expressOrderMobilePhone", "手机"));
        adGoodsOrderColumnList.add(new SimpleExcelColumn(workbook, "createdByName", "创建人"));
        adGoodsOrderColumnList.add(new SimpleExcelColumn(workbook, "createdDate", "创建时间"));
        adGoodsOrderColumnList.add(new SimpleExcelColumn(workbook, "billDate", "开单时间"));
        adGoodsOrderColumnList.add(new SimpleExcelColumn(workbook, "billRemarks", "开单备注"));
        adGoodsOrderColumnList.add(new SimpleExcelColumn(workbook, "remarks", "备注"));
        adGoodsOrderColumnList.add(new SimpleExcelColumn(workbook, "expressOrderShouldPay", "应付运费"));
        adGoodsOrderColumnList.add(new SimpleExcelColumn(workbook, "expressOrderRealPay", "实付运费"));
        adGoodsOrderColumnList.add(new SimpleExcelColumn(workbook, "expressOrderShouldGet", "应收运费"));

        List<AdGoodsOrderDto> adGoodsOrderDtoList = findPage(new PageRequest(0,10000), adGoodsOrderQuery).getContent();
        simpleExcelSheetList.add(new SimpleExcelSheet("订单数据", adGoodsOrderDtoList, adGoodsOrderColumnList));


        List<SimpleExcelColumn> adGoodsOrderDetailColumnList = Lists.newArrayList();
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "adGoodsOrderFormatId", "订单号"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "adGoodsOrderOutCode", "财务编号"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "adGoodsOrderOutShopName", "财务门店"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "adGoodsOrderShopName", "门店"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "adGoodsOrderDepotShopAreaType", "区域类型"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "adGoodsOrderShopAddress", "门店地址"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "adGoodsOrderBillAddress", "目的地"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "expressOrderContator", "联系人"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "expressOrderMobilePhone", "手机"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "adGoodsOrderEmployeeName", "业务"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "adGoodsOrderEmployeeMobilePhone", "业务电话"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "adGoodsOrderBillRemarks", "开单备注"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "adGoodsOrderProcessStatus", "当前状态"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "expressOrderExpressCompanyName", "货运部"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "productCode", "货品编码"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "productName", "货品"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "confirmQty", "订货数"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "billQty", "开单数"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "shippedQty", "发货数"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "productPrice2", "价格"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "amount", "总金额"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "productVolume", "体积"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "expressOrderShouldPay", "应付运费"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "expressOrderRealPay", "实付运费"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "expressOrderShouldGet", "应收运费"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "adGoodsOrderCreatedByName", "创建人"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "adGoodsOrderCreatedDate", "创建时间"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "adGoodsOrderBillDate", "开单时间"));
        List<AdGoodsOrderDetailExportDto> adGoodsOrderDetailExportDtoList = adGoodsOrderDetailRepository.findDtoListForExport(CollectionUtil.extractToList(adGoodsOrderDtoList, "id"), 10000);
        cacheUtils.initCacheInput(adGoodsOrderDetailExportDtoList);
        simpleExcelSheetList.add(new SimpleExcelSheet("订单明细", adGoodsOrderDetailExportDtoList, adGoodsOrderDetailColumnList));


        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"物料订货数据"+LocalDate.now()+".xlsx", simpleExcelSheetList);
        ByteArrayInputStream byteArrayInputStream= ExcelUtils.doWrite(simpleExcelBook.getWorkbook(),simpleExcelBook.getSimpleExcelSheets());
        GridFSFile gridFSFile = tempGridFsTemplate.store(byteArrayInputStream,simpleExcelBook.getName(),"application/octet-stream; charset=utf-8", RequestUtils.getDbObject());
        return StringUtils.toString(gridFSFile.getId());

    }

}
