package net.myspring.future.modules.layout.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.basic.modules.sys.dto.CompanyConfigCacheDto;
import net.myspring.cloud.modules.sys.dto.KingdeeSynReturnDto;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.enums.AdGoodsOrderStatusEnum;
import net.myspring.future.common.enums.BillTypeEnum;
import net.myspring.future.common.enums.ExpressOrderTypeEnum;
import net.myspring.future.common.enums.ShipTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.client.ActivitiClient;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.domain.*;
import net.myspring.future.modules.basic.dto.ClientDto;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.basic.manager.SalOutStockManager;
import net.myspring.future.modules.basic.repository.*;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.manager.ExpressOrderManager;
import net.myspring.future.modules.crm.repository.ExpressOrderRepository;
import net.myspring.future.modules.layout.domain.AdGoodsOrder;
import net.myspring.future.modules.layout.domain.AdGoodsOrderDetail;
import net.myspring.future.modules.layout.dto.AdGoodsOrderDetailExportDto;
import net.myspring.future.modules.layout.dto.AdGoodsOrderDetailSimpleDto;
import net.myspring.future.modules.layout.dto.AdGoodsOrderDto;
import net.myspring.future.modules.layout.repository.AdGoodsOrderDetailRepository;
import net.myspring.future.modules.layout.repository.AdGoodsOrderRepository;
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
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.IdUtils;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class AdGoodsOrderService {
    @Autowired
    private AdGoodsOrderRepository adGoodsOrderRepository;
    @Autowired
    private DepotStoreRepository depotStoreRepository;
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
    private CloudClient cloudClient;
    @Autowired
    private DepotManager depotManager;
    @Autowired
    private SalOutStockManager salOutStockManager;

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

    @Transactional
    public void save(AdGoodsOrderForm adGoodsOrderForm) {

        Depot outShop = depotRepository.findOne(adGoodsOrderForm.getOutShopId());
        ClientDto clientDto = clientRepository.findByDepotId(outShop.getId());
        if (clientDto == null || StringUtils.isBlank(clientDto.getOutId())) {
            throw new ServiceException(outShop.getName() + " 没有关联财务账号，不能申请");
        }
        AdGoodsOrder adGoodsOrder;
        if (adGoodsOrderForm.isCreate()) {
            adGoodsOrder = new AdGoodsOrder();
            //设置部分字段的初始值
            adGoodsOrder.setSplitBill(false);
            adGoodsOrder.setAmount(BigDecimal.ZERO);
            adGoodsOrder.setSmallQty(0);
            adGoodsOrder.setMediumQty(0);
            adGoodsOrder.setLargeQty(0);
            adGoodsOrder.setBillType(BillTypeEnum.柜台.name());
        } else {
            adGoodsOrder = adGoodsOrderRepository.findOne(adGoodsOrderForm.getId());
        }

        if (StringUtils.isBlank(adGoodsOrderForm.getShopId())) {
            adGoodsOrder.setShopId(outShop.getId());
        } else {
            adGoodsOrder.setShopId(adGoodsOrderForm.getShopId());
        }
        adGoodsOrder.setOutShopId(outShop.getId());
        adGoodsOrder.setInvestInCause(adGoodsOrderForm.getInvestInCause());
        adGoodsOrder.setEmployeeId(adGoodsOrderForm.getEmployeeId());
        //adGoodsOrder.setBillAddress(adGoodsOrderForm.getExpressOrderAddress());
        adGoodsOrder.setRemarks(adGoodsOrderForm.getRemarks());
        adGoodsOrderRepository.save(adGoodsOrder);

        if (adGoodsOrderForm.isCreate() || adGoodsOrder.getProcessStatus().contains("审核") || AdGoodsOrderStatusEnum.待开单.name().equals(adGoodsOrder.getProcessStatus())) {
            //在开单之前，是可以修改订货明细的
            saveAdGoodsOrderDetailInfo(adGoodsOrder, adGoodsOrderForm.getAdGoodsOrderDetailList());
        }

        saveExpressOrderInfo(adGoodsOrder, adGoodsOrderForm);

        if (adGoodsOrderForm.isCreate()) {
            startAndSaveProcessFlowInfo(adGoodsOrder);
        }
    }

private void startAndSaveProcessFlowInfo(AdGoodsOrder adGoodsOrder) {

        ActivitiStartDto activitiStartDto = activitiClient.start(new ActivitiStartForm("柜台订货", adGoodsOrder.getId(), AdGoodsOrder.class.getSimpleName(), adGoodsOrder.getOutShopId()));

        adGoodsOrder.setProcessFlowId(activitiStartDto.getProcessFlowId());
        adGoodsOrder.setProcessInstanceId(activitiStartDto.getProcessInstanceId());
        adGoodsOrder.setProcessStatus(activitiStartDto.getProcessStatus());
        adGoodsOrder.setProcessTypeId(activitiStartDto.getProcessTypeId());
        adGoodsOrder.setProcessPositionId(activitiStartDto.getPositionId());

        adGoodsOrderRepository.save(adGoodsOrder);

    }

private void saveExpressOrderInfo(AdGoodsOrder adGoodsOrder, AdGoodsOrderForm adGoodsOrderForm) {

        ExpressOrder expressOrder;

        String expressOrderId = adGoodsOrder.getExpressOrderId();
        if (StringUtils.isBlank(expressOrderId)) {
            expressOrder = new ExpressOrder();
            expressOrder.setExpressPrintQty(0);
        } else {
            expressOrder = expressOrderRepository.findOne(expressOrderId);
        }
        expressOrder.setExtendBusinessId(adGoodsOrder.getBusinessId());
        expressOrder.setToDepotId(adGoodsOrder.getShopId());
        expressOrder.setExtendType(ExpressOrderTypeEnum.物料订单.name());
        expressOrder.setExpressCompanyId(adGoodsOrderForm.getExpressOrderExpressCompanyId());
        expressOrder.setShipType(ShipTypeEnum.总部发货.name());
        expressOrder.setPrintDate(LocalDate.now());
        expressOrder.setLocked(true);
        expressOrder.setExtendId(adGoodsOrder.getId());
        expressOrder.setContator(adGoodsOrderForm.getExpressOrderContator());
        expressOrder.setMobilePhone(adGoodsOrderForm.getExpressOrderMobilePhone());
        expressOrder.setAddress(adGoodsOrderForm.getExpressOrderAddress());
        expressOrder.setExpressCodes(adGoodsOrderForm.getExpressOrderExpressCodes());
        expressOrderRepository.save(expressOrder);

        adGoodsOrder.setExpressOrderId(expressOrder.getId());
        adGoodsOrderRepository.save(adGoodsOrder);

        expressOrderManager.save(ExpressOrderTypeEnum.物料订单.name(), adGoodsOrder.getId(), adGoodsOrderForm.getExpressOrderExpressCodes(), adGoodsOrderForm.getExpressOrderExpressCompanyId());

    }

private void saveAdGoodsOrderDetailInfo(AdGoodsOrder adGoodsOrder, List<AdGoodsOrderDetailForm> detailFormList) {

        List<AdGoodsOrderDetail> toBeSaved = new ArrayList<>();
        for (AdGoodsOrderDetailForm adGoodsOrderDetailForm : detailFormList) {
            AdGoodsOrderDetail adGoodsOrderDetail;

            if (adGoodsOrderDetailForm.getQty() != null && adGoodsOrderDetailForm.getQty() < 0) {
                throw new ServiceException("每个货品订货数量不可以小于0");
            }

            if (StringUtils.isBlank(adGoodsOrderDetailForm.getId())) {
                adGoodsOrderDetail = new AdGoodsOrderDetail();
                adGoodsOrderDetail.setAdGoodsOrderId(adGoodsOrder.getId());
                adGoodsOrderDetail.setProductId(adGoodsOrderDetailForm.getProductId());
                adGoodsOrderDetail.setBillQty(0);
                adGoodsOrderDetail.setShippedQty(0);
                adGoodsOrderDetail.setShouldGet(BigDecimal.ZERO);
                adGoodsOrderDetail.setShouldPay(BigDecimal.ZERO);
            } else {
                adGoodsOrderDetail = adGoodsOrderDetailRepository.findOne(adGoodsOrderDetailForm.getId());
            }
            adGoodsOrderDetail.setQty(adGoodsOrderDetailForm.getQty() == null ? 0 : adGoodsOrderDetailForm.getQty());
            adGoodsOrderDetail.setConfirmQty(adGoodsOrderDetail.getQty());

            toBeSaved.add(adGoodsOrderDetail);
        }
        adGoodsOrderDetailRepository.save(toBeSaved);

        adGoodsOrder.setAmount(calcAmountByDetailConfirmQty(adGoodsOrder));
        adGoodsOrderRepository.save(adGoodsOrder);
    }

    private BigDecimal calcAmountByDetailConfirmQty(AdGoodsOrder adGoodsOrder) {
        List<AdGoodsOrderDetail> detailList = adGoodsOrderDetailRepository.findByAdGoodsOrderId(adGoodsOrder.getId());
        Map<String, Product> productMap = productRepository.findMap(CollectionUtil.extractToList(detailList, "productId"));
        BigDecimal amount = BigDecimal.ZERO;
        for (AdGoodsOrderDetail adGoodsOrderDetail : detailList) {
            amount = amount.add(productMap.get(adGoodsOrderDetail.getProductId()).getPrice2().multiply(new BigDecimal(adGoodsOrderDetail.getConfirmQty())));
        }
        return amount;
    }

    public AdGoodsOrderDto findDto(String id) {
        AdGoodsOrderDto adGoodsOrderDto = adGoodsOrderRepository.findDto(id);
        cacheUtils.initCacheInput(adGoodsOrderDto);
        return adGoodsOrderDto;
    }

    @Transactional
    public void logicDelete(String id) {
        adGoodsOrderRepository.logicDelete(id);
    }

    public List<AdGoodsOrderDetailSimpleDto> findDetailListForNewOrEdit(String adGoodsOrderId, boolean includeNotAllowOrderProduct) {

        List<String> outGroupIdList = IdUtils.getIdList(CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.PRODUCT_COUNTER_GROUP_IDS.name()).getValue());

        List<AdGoodsOrderDetailSimpleDto> result;
        if (StringUtils.isBlank(adGoodsOrderId)) {
            result = adGoodsOrderDetailRepository.findDetailListForNew(outGroupIdList, includeNotAllowOrderProduct);
        } else {
            result = adGoodsOrderDetailRepository.findDetailListForEdit(adGoodsOrderId, outGroupIdList, includeNotAllowOrderProduct);
        }
        cacheUtils.initCacheInput(result);
        return result;
    }

    @Transactional
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

        List<String> outGroupIdList = IdUtils.getIdList(CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.PRODUCT_COUNTER_GROUP_IDS.name()).getValue());
        List<AdGoodsOrderDetailSimpleDto> result = adGoodsOrderDetailRepository.findDetailListForBill(adGoodsOrderId, outGroupIdList);
        cacheUtils.initCacheInput(result);

        fulfillCloudQty(adGoodsOrderId, result);
        return result;
    }

    private void fulfillCloudQty(String adGoodsOrderId, List<AdGoodsOrderDetailSimpleDto> list) {
        AdGoodsOrder adGoodsOrder = adGoodsOrderRepository.findOne(adGoodsOrderId);
        String depotId = adGoodsOrder.getStoreId();
        if(StringUtils.isBlank(adGoodsOrder.getStoreId())){
            depotId = CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.AD_DEFAULT_STORE_ID.name()).getValue();
        }
        if(StringUtils.isBlank(depotId)) {
            return;
        }

        Map<String, Integer> cloudQtyMap = depotManager.getCloudQtyMap(depotId);
        for(AdGoodsOrderDetailSimpleDto adGoodsOrderDetailSimpleDto : list){
            if(cloudQtyMap.containsKey(adGoodsOrderDetailSimpleDto.getProductOutId())){
                adGoodsOrderDetailSimpleDto.setCloudQty(cloudQtyMap.get(adGoodsOrderDetailSimpleDto.getProductOutId()));
            }
        }
    }

    public List<AdGoodsOrderDetailSimpleDto> findDetailListByAdGoodsOrderId(String adGoodsOrderId) {

        List<AdGoodsOrderDetailSimpleDto> result = adGoodsOrderDetailRepository.findDtoListByAdGoodsOrderId(adGoodsOrderId);
        cacheUtils.initCacheInput(result);
        return result;
    }

    @Transactional
    public void bill(AdGoodsOrderBillForm adGoodsOrderBillForm) {

        AdGoodsOrder adGoodsOrder = adGoodsOrderRepository.findOne(adGoodsOrderBillForm.getId());

        if (!AdGoodsOrderStatusEnum.待开单.name().equals(adGoodsOrder.getProcessStatus())) {
            throw new ServiceException("该订单状态不为：" + AdGoodsOrderStatusEnum.待开单.name() + "，不能开单");
        }

        adGoodsOrder.setBusinessId(IdUtils.getNextBusinessId(adGoodsOrderRepository.findMaxBusinessId(adGoodsOrderBillForm.getBillDate()), adGoodsOrderBillForm.getBillDate()));
        adGoodsOrder.setStoreId(adGoodsOrderBillForm.getStoreId());
        adGoodsOrder.setBillDate(adGoodsOrderBillForm.getBillDate());
        adGoodsOrder.setBillAddress(adGoodsOrderBillForm.getBillAddress());
        if (StringUtils.isBlank(adGoodsOrder.getParentId())) {
            adGoodsOrder.setParentId(adGoodsOrder.getId());
        }
        adGoodsOrderRepository.save(adGoodsOrder);

        Map<String, Product> productMap = productRepository.findMap(CollectionUtil.extractToList(adGoodsOrderBillForm.getAdGoodsOrderDetailList(), "productId"));

        List<AdGoodsOrderDetail> detailList = saveDetailInfoWhenBill(adGoodsOrder, adGoodsOrderBillForm.getAdGoodsOrderDetailList(), productMap);

        ExpressOrder expressOrder = saveExpressOrderInfoWhenBill(adGoodsOrder, adGoodsOrderBillForm, detailList);

        if (Boolean.TRUE.equals(adGoodsOrderBillForm.getSplitBill())) {
            splitAdGoodsOrder(adGoodsOrder, adGoodsOrderBillForm, detailList);
        }

        if (StringUtils.isNotBlank(adGoodsOrder.getProcessInstanceId())) {
            doAndSaveProcessInfo(adGoodsOrder, true, "");
        } else {
            adGoodsOrder.setProcessStatus(AdGoodsOrderStatusEnum.待发货.name());
            adGoodsOrderRepository.save(adGoodsOrder);
        }

        if (Boolean.TRUE.equals(adGoodsOrderBillForm.getSyn())) {
            synWhenBill(adGoodsOrder,expressOrder);
        }
    }

private List<AdGoodsOrderDetail> saveDetailInfoWhenBill(AdGoodsOrder adGoodsOrder, List<AdGoodsOrderBillDetailForm> detailFormList, Map<String, Product> productMap) {

        Map<String, AdPricesystemDetail> priceMap = Maps.newHashMap();
        Depot depot = depotRepository.findOne(adGoodsOrder.getShopId());
        List<AdPricesystemDetail> adPricesystemDetailList = adPricesystemDetailRepository.findByEnabledIsTrueAndAdPricesystemId(depot.getAdPricesystemId());
        for (AdPricesystemDetail adPricesystemDetail : adPricesystemDetailList) {
            priceMap.put(adPricesystemDetail.getProductId(), adPricesystemDetail);
        }

        List<AdGoodsOrderDetail> toBeSaved = new ArrayList<>();
        for (AdGoodsOrderBillDetailForm adGoodsOrderBillDetailForm : detailFormList) {
            if (adGoodsOrderBillDetailForm.getBillQty() != null && adGoodsOrderBillDetailForm.getBillQty() < 0) {
                throw new ServiceException("开单数量不可以小于0");
            }

            AdGoodsOrderDetail adGoodsOrderDetail;
            if (StringUtils.isBlank(adGoodsOrderBillDetailForm.getId())) {
                adGoodsOrderDetail = new AdGoodsOrderDetail();
                adGoodsOrderDetail.setAdGoodsOrderId(adGoodsOrder.getId());
                adGoodsOrderDetail.setProductId(adGoodsOrderBillDetailForm.getProductId());
                adGoodsOrderDetail.setQty(0);
                adGoodsOrderDetail.setConfirmQty(0);
                adGoodsOrderDetail.setShippedQty(0);
            } else {
                adGoodsOrderDetail = adGoodsOrderDetailRepository.findOne(adGoodsOrderBillDetailForm.getId());
            }
            adGoodsOrderDetail.setBillQty(adGoodsOrderBillDetailForm.getBillQty() == null ? 0 : adGoodsOrderBillDetailForm.getBillQty());
            if (priceMap.containsKey(adGoodsOrderDetail.getProductId())) {
                adGoodsOrderDetail.setShouldPay(priceMap.get(adGoodsOrderDetail.getProductId()).getPrice());
                adGoodsOrderDetail.setShouldGet(productMap.get(adGoodsOrderDetail.getProductId()).getShouldGet());
            } else {
                adGoodsOrderDetail.setShouldPay(BigDecimal.ZERO);
                adGoodsOrderDetail.setShouldGet(BigDecimal.ZERO);
            }

            toBeSaved.add(adGoodsOrderDetail);
        }
        adGoodsOrderDetailRepository.save(toBeSaved);

        BigDecimal amount = BigDecimal.ZERO;
        for (AdGoodsOrderDetail adGoodsOrderDetail : toBeSaved) {
            if(productMap.get(adGoodsOrderDetail.getProductId()).getPrice2()!=null){
                amount = amount.add(productMap.get(adGoodsOrderDetail.getProductId()).getPrice2().multiply(new BigDecimal(adGoodsOrderDetail.getBillQty())));
            }

        }
        adGoodsOrder.setAmount(amount);
        adGoodsOrderRepository.save(adGoodsOrder);

        return toBeSaved;
    }

private void splitAdGoodsOrder(AdGoodsOrder adGoodsOrder, AdGoodsOrderBillForm adGoodsOrderBillForm, List<AdGoodsOrderDetail> detailList) {

        //开始保存拆分后的newAdGoodsOrder的基本信息
        AdGoodsOrder newAdGoodsOrder = new AdGoodsOrder();
        newAdGoodsOrder.setStoreId(adGoodsOrder.getStoreId());
        newAdGoodsOrder.setOutShopId(adGoodsOrder.getOutShopId());
        newAdGoodsOrder.setShopId(adGoodsOrder.getShopId());
        newAdGoodsOrder.setBillType(BillTypeEnum.柜台.name());
        newAdGoodsOrder.setCreatedBy(adGoodsOrder.getCreatedBy());
        newAdGoodsOrder.setCreatedDate(adGoodsOrder.getCreatedDate());
        newAdGoodsOrder.setEmployeeId(adGoodsOrder.getEmployeeId());
        newAdGoodsOrder.setInvestInCause(adGoodsOrder.getInvestInCause());
        newAdGoodsOrder.setRemarks(adGoodsOrder.getRemarks());
        newAdGoodsOrder.setParentId(adGoodsOrder.getParentId());
        newAdGoodsOrder.setSplitBill(false);
        newAdGoodsOrder.setSmallQty(0);
        newAdGoodsOrder.setMediumQty(0);
        newAdGoodsOrder.setLargeQty(0);
        newAdGoodsOrder.setProcessStatus(AdGoodsOrderStatusEnum.待开单.name());
        adGoodsOrderRepository.save(newAdGoodsOrder);

        //开始保存拆分后的detail信息
        List<AdGoodsOrderDetail> newAdGoodsOrderDetailList = Lists.newArrayList();
        for (AdGoodsOrderDetail adGoodsOrderDetail : detailList) {
            if (adGoodsOrderDetail.getConfirmQty() > adGoodsOrderDetail.getBillQty()) {
                AdGoodsOrderDetail newAdGoodsOrderDetail = new AdGoodsOrderDetail();
                Integer billEnabledQty = adGoodsOrderDetail.getConfirmQty() - adGoodsOrderDetail.getBillQty();
                newAdGoodsOrderDetail.setProductId(adGoodsOrderDetail.getProductId());
                newAdGoodsOrderDetail.setQty(billEnabledQty);
                newAdGoodsOrderDetail.setConfirmQty(billEnabledQty);
                newAdGoodsOrderDetail.setBillQty(0);
                newAdGoodsOrderDetail.setShippedQty(0);
                newAdGoodsOrderDetail.setShouldGet(BigDecimal.ZERO);
                newAdGoodsOrderDetail.setShouldPay(BigDecimal.ZERO);
                newAdGoodsOrderDetail.setAdGoodsOrderId(newAdGoodsOrder.getId());
                newAdGoodsOrderDetailList.add(newAdGoodsOrderDetail);
            }
        }
        if(newAdGoodsOrderDetailList.isEmpty()){
            throw new ServiceException("所有确认订货数均已开单，不需要拆单");
        }
        adGoodsOrderDetailRepository.save(newAdGoodsOrderDetailList);
        newAdGoodsOrder.setAmount(calcAmountByDetailConfirmQty(newAdGoodsOrder));
        adGoodsOrderRepository.save(newAdGoodsOrder);

        //开始保存拆分后的expressOrder信息
        ExpressOrder expressOrder = new ExpressOrder();
        expressOrder.setExpressCompanyId(adGoodsOrderBillForm.getExpressOrderExpressCompanyId());
        expressOrder.setAddress(adGoodsOrderBillForm.getExpressOrderAddress());
        expressOrder.setContator(adGoodsOrderBillForm.getExpressOrderContator());
        expressOrder.setMobilePhone(adGoodsOrderBillForm.getExpressOrderMobilePhone());
        expressOrder.setToDepotId(newAdGoodsOrder.getShopId());
        expressOrder.setLocked(true);
        expressOrder.setExtendType(ExpressOrderTypeEnum.物料订单.name());
        expressOrder.setShipType(ShipTypeEnum.总部发货.name());
        expressOrder.setPrintDate(LocalDate.now());
        expressOrder.setExtendId(newAdGoodsOrder.getId());
        expressOrder.setExpressPrintQty(0);
        expressOrderRepository.save(expressOrder);
        newAdGoodsOrder.setExpressOrderId(expressOrder.getId());
        adGoodsOrderRepository.save(newAdGoodsOrder);
    }

private void synWhenBill(AdGoodsOrder adGoodsOrder, ExpressOrder expressOrder) {
        KingdeeSynReturnDto kingdeeSynReturnDto = salOutStockManager.synForAdGoodsOrder(adGoodsOrder);

        adGoodsOrder.setCloudSynId(kingdeeSynReturnDto.getId());
        adGoodsOrder.setOutCode(kingdeeSynReturnDto.getBillNo());
        adGoodsOrderRepository.save(adGoodsOrder);

        expressOrder.setOutCode(kingdeeSynReturnDto.getBillNo());
        expressOrderRepository.save(expressOrder);

    }

private ExpressOrder saveExpressOrderInfoWhenBill(AdGoodsOrder adGoodsOrder, AdGoodsOrderBillForm adGoodsOrderBillForm, List<AdGoodsOrderDetail> detailList) {

        ExpressOrder expressOrder = expressOrderRepository.findOne(adGoodsOrder.getExpressOrderId());
        expressOrder.setFromDepotId(adGoodsOrder.getStoreId());
        expressOrder.setExtendBusinessId(adGoodsOrder.getBusinessId());
        expressOrder.setExtendId(adGoodsOrder.getId());
        expressOrder.setToDepotId(adGoodsOrder.getShopId());
        expressOrder.setLocked(false);
        expressOrder.setExtendType(ExpressOrderTypeEnum.物料订单.name());
        expressOrder.setShipType(ShipTypeEnum.总部发货.name());
        expressOrder.setPrintDate(adGoodsOrder.getBillDate());
        expressOrder.setExpressCompanyId(adGoodsOrderBillForm.getExpressOrderExpressCompanyId());
        expressOrder.setAddress(adGoodsOrderBillForm.getExpressOrderAddress());
        expressOrder.setContator(adGoodsOrderBillForm.getExpressOrderContator());
        expressOrder.setMobilePhone(adGoodsOrderBillForm.getExpressOrderMobilePhone());

        BigDecimal shouldPay = BigDecimal.ZERO;
        BigDecimal shouldGet = BigDecimal.ZERO;
        for (AdGoodsOrderDetail adGoodsOrderDetail : detailList) {
            if (adGoodsOrderDetail.getBillQty() != null && adGoodsOrderDetail.getBillQty() > 0) {
                shouldPay = shouldPay.add(adGoodsOrderDetail.getShouldPay().multiply(new BigDecimal(adGoodsOrderDetail.getBillQty())));
                shouldGet = shouldGet.add(adGoodsOrderDetail.getShouldGet().multiply(new BigDecimal(adGoodsOrderDetail.getBillQty())));
            }
        }
        expressOrder.setShouldGet(shouldGet);
        expressOrder.setShouldPay(shouldPay);

        expressOrderRepository.save(expressOrder);

        return expressOrder;

    }

    @Transactional
    public void ship(AdGoodsOrderShipForm adGoodsOrderShipForm) {

        AdGoodsOrder adGoodsOrder = adGoodsOrderRepository.findOne(adGoodsOrderShipForm.getId());
        adGoodsOrder.setSmallQty(adGoodsOrderShipForm.getSmallQty());
        adGoodsOrder.setMediumQty(adGoodsOrderShipForm.getMediumQty());
        adGoodsOrder.setLargeQty(adGoodsOrderShipForm.getLargeQty());
        adGoodsOrderRepository.save(adGoodsOrder);

        boolean isAllShipped = saveDetailInfoWhenShip(adGoodsOrderShipForm);

        saveExpressOrderInfoWhenShip(adGoodsOrder, adGoodsOrderShipForm);

        if (isAllShipped) {
            //如果有工作流，审批通过
            if (StringUtils.isNotBlank(adGoodsOrder.getProcessInstanceId())) {
                doAndSaveProcessInfo(adGoodsOrder, true, "");
            } else {
                adGoodsOrder.setProcessStatus(AdGoodsOrderStatusEnum.待签收.name());
                adGoodsOrderRepository.save(adGoodsOrder);
            }
        }

    }

private void saveExpressOrderInfoWhenShip(AdGoodsOrder adGoodsOrder, AdGoodsOrderShipForm adGoodsOrderShipForm) {
        ExpressOrder expressOrder = expressOrderRepository.findOne(adGoodsOrder.getExpressOrderId());
        expressOrder.setExpressCompanyId(adGoodsOrderShipForm.getExpressOrderExpressCompanyId());
        expressOrder.setExpressCodes(adGoodsOrderShipForm.getExpressOrderExpressCodes());
        expressOrder.setShouldGet(adGoodsOrderShipForm.getExpressOrderShouldGet());
        expressOrder.setShouldPay(adGoodsOrderShipForm.getExpressOrderShouldPay());
        expressOrder.setRealPay(adGoodsOrderShipForm.getExpressOrderRealPay());
        expressOrderRepository.save(expressOrder);

        expressOrderManager.save(expressOrder.getExtendType(), expressOrder.getExtendId(), expressOrder.getExpressCodes(), expressOrder.getExpressCompanyId());
    }

private boolean saveDetailInfoWhenShip(AdGoodsOrderShipForm adGoodsOrderShipForm) {
        Map<String, AdGoodsOrderShipDetailForm> adGoodsOrderShipFormMap = CollectionUtil.extractToMap(adGoodsOrderShipForm.getAdGoodsOrderDetailList(), "id");
        List<AdGoodsOrderDetail> adGoodsOrderDetailList = adGoodsOrderDetailRepository.findByAdGoodsOrderId(adGoodsOrderShipForm.getId());
        boolean isAllShipped = true;
        for (AdGoodsOrderDetail adGoodsOrderDetail : adGoodsOrderDetailList) {
            AdGoodsOrderShipDetailForm adGoodsOrderShipDetailForm = adGoodsOrderShipFormMap.get(adGoodsOrderDetail.getId());
            if (adGoodsOrderShipDetailForm != null && adGoodsOrderShipDetailForm.getShipQty() != null && adGoodsOrderShipDetailForm.getShipQty() > 0) {
                adGoodsOrderDetail.setShippedQty(adGoodsOrderDetail.getShippedQty() + adGoodsOrderShipDetailForm.getShipQty());
            }

            if (adGoodsOrderDetail.getBillQty() < adGoodsOrderDetail.getShippedQty()) {
                throw new ServiceException("每个产品的累计发货数量不能大于相应开单数量");
            }
            if (adGoodsOrderDetail.getBillQty() > adGoodsOrderDetail.getShippedQty()) {
                isAllShipped = false;
            }
        }
        adGoodsOrderDetailRepository.save(adGoodsOrderDetailList);
        return isAllShipped;
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

    @Transactional
    public void sign(String id) {
        AdGoodsOrder adGoodsOrder = adGoodsOrderRepository.findOne(id);

        if (StringUtils.isNotBlank(adGoodsOrder.getProcessInstanceId())) {
            doAndSaveProcessInfo(adGoodsOrder, true, "");
        } else {
            adGoodsOrder.setProcessStatus(AdGoodsOrderStatusEnum.已完成.name());
            adGoodsOrderRepository.save(adGoodsOrder);
        }
    }

    @Transactional
    public AdGoodsOrderDto print(String id) {
        AdGoodsOrder adGoodsOrder = adGoodsOrderRepository.findOne(id);
        ExpressOrder expressOrder = expressOrderRepository.findOne(adGoodsOrder.getExpressOrderId());
        if (expressOrder != null && expressOrder.getOutPrintDate() == null) {
            expressOrder.setOutPrintDate(LocalDateTime.now());
            expressOrderRepository.saveAndFlush(expressOrder);
        }

        return findDto(id);

    }

    public SimpleExcelBook export(AdGoodsOrderQuery adGoodsOrderQuery) {

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

        List<AdGoodsOrderDto> adGoodsOrderDtoList = findPage(new PageRequest(0, 10000), adGoodsOrderQuery).getContent();
        simpleExcelSheetList.add(new SimpleExcelSheet("订单数据", adGoodsOrderDtoList, adGoodsOrderColumnList));


        List<SimpleExcelColumn> adGoodsOrderDetailColumnList = Lists.newArrayList();
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "adGoodsOrderFormatId", "订单号"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "adGoodsOrderOutCode", "财务编号"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "adGoodsOrderOutShopName", "财务门店"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "adGoodsOrderShopName", "门店"));
        adGoodsOrderDetailColumnList.add(new SimpleExcelColumn(workbook, "adGoodsOrderShopAreaName", "办事处"));
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
        List<AdGoodsOrderDetailExportDto> adGoodsOrderDetailExportDtoList = Lists.newArrayList();
        if(CollectionUtil.isNotEmpty(adGoodsOrderDtoList)){
            adGoodsOrderDetailExportDtoList = adGoodsOrderDetailRepository.findDtoListForExport(CollectionUtil.extractToList(adGoodsOrderDtoList, "id"), 10000);
            cacheUtils.initCacheInput(adGoodsOrderDetailExportDtoList);
        }
        simpleExcelSheetList.add(new SimpleExcelSheet("订单明细", adGoodsOrderDetailExportDtoList, adGoodsOrderDetailColumnList));

        ExcelUtils.doWrite(workbook, simpleExcelSheetList);
        return new SimpleExcelBook(workbook,"物料订货数据"+ LocalDateUtils.format(LocalDate.now())+".xlsx",simpleExcelSheetList);
    }

    public Map<String, Object> getYsyfMap(String adGoodsOrderId) {

        // 统计应付运费,以门店物料运费为准
        AdGoodsOrder adGoodsOrder = adGoodsOrderRepository.findOne(adGoodsOrderId);
        Depot depot = depotRepository.findOne(adGoodsOrder.getShopId());
        List<AdPricesystemDetail> adPricesystemDetailList = adPricesystemDetailRepository.findByEnabledIsTrueAndAdPricesystemId(depot.getAdPricesystemId());
        Map<String, BigDecimal> priceMap = Maps.newHashMap();
        for (AdPricesystemDetail adPricesystemDetail : adPricesystemDetailList) {
            if(adPricesystemDetail.getPrice() != null){
                priceMap.put(adPricesystemDetail.getProductId(), adPricesystemDetail.getPrice());
            }
        }

        List<AdGoodsOrderDetail> adGoodsOrderDetailList = adGoodsOrderDetailRepository.findByAdGoodsOrderId(adGoodsOrderId);
        BigDecimal yfAmount = BigDecimal.ZERO;
        for (AdGoodsOrderDetail adGoodsOrderDetail : adGoodsOrderDetailList) {
            if (priceMap.get(adGoodsOrderDetail.getProductId()) != null) {
                yfAmount = yfAmount.add(new BigDecimal(adGoodsOrderDetail.getBillQty()).multiply(priceMap.get(adGoodsOrderDetail.getProductId())));
            }
        }

        BigDecimal ysAmount = getYsAmount(adGoodsOrderDetailList);

        BigDecimal smallPrice = BigDecimal.ZERO;
        BigDecimal mediumPrice = BigDecimal.ZERO;
        BigDecimal largePrice = BigDecimal.ZERO;
        Map<String, Object> adShipPriceRuleMap = getAdShipPriceRuleMap();
        if (adShipPriceRuleMap != null) {
            smallPrice = new BigDecimal(String.valueOf(adShipPriceRuleMap.get("small")));
            mediumPrice = new BigDecimal(String.valueOf(adShipPriceRuleMap.get("medium")));
            largePrice = new BigDecimal(String.valueOf(adShipPriceRuleMap.get("big")));
        }

        int smallQty = (adGoodsOrder.getSmallQty() == null ? 0 : adGoodsOrder.getSmallQty());
        int mediumQty = (adGoodsOrder.getMediumQty() == null ? 0 : adGoodsOrder.getMediumQty());
        int largeQty = (adGoodsOrder.getLargeQty() == null ? 0 : adGoodsOrder.getLargeQty());

        BigDecimal shipPrice = smallPrice.multiply(new BigDecimal(smallQty)).add(mediumPrice.multiply(new BigDecimal(mediumQty))).add(largePrice.multiply(new BigDecimal(largeQty)));

        BigDecimal realPay = shipPrice; //初始实付运费，通过billQty计算，因为页面默认是所有开单货品都发货
        for(AdGoodsOrderDetail adGoodsOrderDetail : adGoodsOrderDetailList){
            if(adGoodsOrderDetail.getBillQty() != null){
                BigDecimal price = (priceMap.get(adGoodsOrderDetail.getProductId()) ==null ? BigDecimal.ZERO : priceMap.get(adGoodsOrderDetail.getProductId()));
                realPay = realPay.add(price.multiply(new BigDecimal(adGoodsOrderDetail.getBillQty())));
            }
        }

        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("smallPrice", smallPrice);
        resultMap.put("mediumPrice", mediumPrice);
        resultMap.put("largePrice", largePrice);
        resultMap.put("yfAmount", yfAmount);
        resultMap.put("ysAmount", ysAmount);
        resultMap.put("priceMap", priceMap);
        resultMap.put("initShouldGet", ysAmount.add(shipPrice));
        resultMap.put("initShouldPay", yfAmount.add(shipPrice));
        resultMap.put("initRealPay", realPay);

        return resultMap;

    }

    private BigDecimal getYsAmount(List<AdGoodsOrderDetail> adGoodsOrderDetailList) {
        //统计应收运费 全部以A类物料运费为准
        Map<String, BigDecimal> ysMap = Maps.newHashMap();
        AdPricesystem defaultAdPricesystem = adpricesystemRepository.findByEnabledIsTrueAndName( "A类物料运费");//TODO 默认的ADPriceSystem，应该写成constant或者在companyConfig中配置
        if (defaultAdPricesystem != null) {
            for (AdPricesystemDetail adPricesystemDetail : adPricesystemDetailRepository.findByEnabledIsTrueAndAdPricesystemId(defaultAdPricesystem.getId())) {
                ysMap.put(adPricesystemDetail.getProductId(), adPricesystemDetail.getPrice());
            }
        }
        BigDecimal ysAmount = BigDecimal.ZERO;
        for (AdGoodsOrderDetail adGoodsOrderDetail : adGoodsOrderDetailList) {
            if (ysMap.get(adGoodsOrderDetail.getProductId()) != null) {
                ysAmount = ysAmount.add(new BigDecimal(adGoodsOrderDetail.getBillQty()).multiply(ysMap.get(adGoodsOrderDetail.getProductId())));
            }
        }
        return ysAmount;
    }

    private Map<String, Object> getAdShipPriceRuleMap() {
        CompanyConfigCacheDto companyConfigCacheDto = CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.AD_SHIP_PRICE_RULE.name());
        if (companyConfigCacheDto == null || StringUtils.isBlank(companyConfigCacheDto.getValue())) {
            return null;
        }

        List<Map<String, Object>> list = ObjectMapperUtils.readValue(companyConfigCacheDto.getValue(), List.class);
        if (list == null) {
            return null;
        }
        return list.get(0);

    }
}
