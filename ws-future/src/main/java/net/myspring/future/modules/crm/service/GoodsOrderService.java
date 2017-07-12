package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.basic.common.util.OfficeUtil;
import net.myspring.basic.modules.sys.dto.CompanyConfigCacheDto;
import net.myspring.basic.modules.sys.dto.OfficeDto;
import net.myspring.cloud.modules.report.dto.CustomerReceiveDto;
import net.myspring.cloud.modules.report.web.query.CustomerReceiveQuery;
import net.myspring.cloud.modules.sys.dto.KingdeeSynReturnDto;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.common.enums.JointLevelEnum;
import net.myspring.common.exception.ServiceException;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestErrorField;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.*;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.ExpressUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.api.domain.CarrierOrder;
import net.myspring.future.modules.api.repository.CarrierOrderRepository;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.DepotStore;
import net.myspring.future.modules.basic.domain.PricesystemDetail;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.dto.DepotAccountDto;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.basic.manager.SalOutStockManager;
import net.myspring.future.modules.basic.manager.StkTransferDirectManager;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.DepotStoreRepository;
import net.myspring.future.modules.basic.repository.PricesystemDetailRepository;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.future.modules.crm.domain.GoodsOrderDetail;
import net.myspring.future.modules.crm.dto.GoodsOrderDetailDto;
import net.myspring.future.modules.crm.dto.GoodsOrderDto;
import net.myspring.future.modules.crm.dto.GoodsOrderImeDto;
import net.myspring.future.modules.crm.manager.ExpressOrderManager;
import net.myspring.future.modules.crm.repository.ExpressOrderRepository;
import net.myspring.future.modules.crm.repository.GoodsOrderDetailRepository;
import net.myspring.future.modules.crm.repository.GoodsOrderImeRepository;
import net.myspring.future.modules.crm.repository.GoodsOrderRepository;
import net.myspring.future.modules.crm.web.form.*;
import net.myspring.future.modules.crm.web.query.GoodsOrderQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.*;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
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
import java.time.LocalTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class GoodsOrderService {
    @Autowired
    private GoodsOrderRepository goodsOrderRepository;
    @Autowired
    private GoodsOrderDetailRepository goodsOrderDetailRepository;
    @Autowired
    private GoodsOrderImeRepository goodsOrderImeRepository;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private DepotStoreRepository depotStoreRepository;
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
    @Autowired
    private SalOutStockManager salOutStockManager;
    @Autowired
    private StkTransferDirectManager stkTransferDirectManager;
    @Autowired
    private DepotManager depotManager;
    @Autowired
    private CloudClient cloudClient;
    @Autowired
    private CarrierOrderRepository carrierOrderRepository;
    @Autowired
    private ExpressOrderManager expressOrderManager;

    public GoodsOrder findByBusinessId(String businessId){
        return goodsOrderRepository.findByBusinessId(businessId);
    }

    public Page<GoodsOrderDto> findAll(Pageable pageable, GoodsOrderQuery goodsOrderQuery) {
        if (goodsOrderQuery.getExpressCodes() != null) {
            goodsOrderQuery.setExpresscodeList(Arrays.asList(goodsOrderQuery.getExpressCodes().split("[\n,]")));
        }
        if (goodsOrderQuery.getBusinessIds() != null) {
            goodsOrderQuery.setBusinessIdList(Arrays.asList(goodsOrderQuery.getBusinessIds().split("[\n,]")));
        }
        goodsOrderQuery.setDepotIdList(depotManager.filterDepotIds(RequestUtils.getAccountId()));
        Page<GoodsOrderDto> page = goodsOrderRepository.findAll(pageable, goodsOrderQuery);
        cacheUtils.initCacheInput(page.getContent());

        if (CollectionUtil.isNotEmpty(page.getContent())) {
            CustomerReceiveQuery customerReceiveQuery = new CustomerReceiveQuery();
            customerReceiveQuery.setDateStart(LocalDate.now());
            customerReceiveQuery.setDateEnd(customerReceiveQuery.getDateStart());
            List<String> clientOutIdList = CollectionUtil.extractToList(page.getContent(), "clientOutId");
            customerReceiveQuery.setCustomerIdList(clientOutIdList);
            List<CustomerReceiveDto> customerReceiveDtoList = cloudClient.getCustomerReceiveList(customerReceiveQuery);
            Map<String, CustomerReceiveDto> customerReceiveDtoMap = new HashMap<>();
            if(CollectionUtil.isNotEmpty(customerReceiveDtoList)){
                customerReceiveDtoMap = CollectionUtil.extractToMap(customerReceiveDtoList, "customerId");
            }

            for (GoodsOrderDto goodsOrderDto : page.getContent()) {
                if (GoodsOrderStatusEnum.待开单.name().equals(goodsOrderDto.getStatus())) {
                    if(StringUtils.isNotBlank(goodsOrderDto.getClientOutId())){
                        BigDecimal shouldGet = customerReceiveDtoMap.containsKey(goodsOrderDto.getClientOutId()) ?  customerReceiveDtoMap.get(goodsOrderDto.getClientOutId()).getEndShouldGet() : BigDecimal.ZERO ;
                        goodsOrderDto.setShopShouldGet(shouldGet);
                    }
                }
            }
        }
        return page;
    }

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

    @Transactional
    public GoodsOrder save(GoodsOrderForm goodsOrderForm) {

        Depot shop = depotRepository.findOne(goodsOrderForm.getShopId());
        GoodsOrder goodsOrder;
        if(StringUtils.isNotBlank(goodsOrderForm.getId())) {
            goodsOrder = goodsOrderRepository.findOne(goodsOrderForm.getId());
        } else {
            goodsOrder = new GoodsOrder();
            goodsOrder.setShopId(goodsOrderForm.getShopId());
            goodsOrder.setNetType(goodsOrderForm.getNetType());
            goodsOrder.setShipType(goodsOrderForm.getShipType());
            goodsOrder.setStatus(GoodsOrderStatusEnum.待开单.name());
            goodsOrder.setUseTicket(false);
            goodsOrder.setStoreId(getDefaultStoreId(goodsOrderForm.getNetType(), shop));
        }
        goodsOrder.setLxMallOrder(NetTypeEnum.联信.name().equals(goodsOrderForm.getNetType()) ? goodsOrderForm.getLxMallOrder() : null );
        goodsOrder.setRemarks(goodsOrderForm.getRemarks());
        goodsOrderRepository.save(goodsOrder);

        saveDetailInfo(goodsOrder, goodsOrderForm, shop);
        saveExpressOrderInfo(goodsOrder, shop);
        return goodsOrder;
    }

    private void saveDetailInfo(GoodsOrder goodsOrder, GoodsOrderForm goodsOrderForm, Depot shop) {
        List<GoodsOrderDetail> goodsOrderDetailList  = goodsOrderDetailRepository.findByGoodsOrderId(goodsOrder.getId());
        Map<String,GoodsOrderDetail> goodsOrderDetailMap  = CollectionUtil.extractToMap(goodsOrderDetailList,"id");

        BigDecimal amount = BigDecimal.ZERO;
        List<PricesystemDetail> pricesystemDetailList = pricesystemDetailRepository.findByPricesystemId(shop.getPricesystemId());
        Map<String,PricesystemDetail> pricesystemDetailMap = CollectionUtil.extractToMap(pricesystemDetailList,"productId");
        List<GoodsOrderDetail> detailsToBeSaved = new ArrayList<>();
        for (GoodsOrderDetailForm goodsOrderDetailForm : goodsOrderForm.getGoodsOrderDetailFormList()) {
            if(goodsOrderDetailForm.getQty() != null && goodsOrderDetailForm.getQty()<0) {
                throw new ServiceException("订货明细里的数量不能小于0");
            }
            if(!pricesystemDetailMap.containsKey(goodsOrderDetailForm.getProductId())){
                throw new ServiceException("该产品（"+goodsOrderDetailForm.getProductId()+"）不包含在该门店价格体系内，不允许订货");
            }

            GoodsOrderDetail goodsOrderDetail;
            if(StringUtils.isNotBlank(goodsOrderDetailForm.getId())) {
                goodsOrderDetail = goodsOrderDetailMap.get(goodsOrderDetailForm.getId());
            } else {
                goodsOrderDetail = new GoodsOrderDetail();
                goodsOrderDetail.setGoodsOrderId(goodsOrder.getId());
                goodsOrderDetail.setProductId(goodsOrderDetailForm.getProductId());
                goodsOrderDetail.setReturnQty(0);
                goodsOrderDetail.setShippedQty(0);
                goodsOrderDetail.setPrice(pricesystemDetailMap.get(goodsOrderDetail.getProductId()).getPrice());
            }
            goodsOrderDetail.setQty(goodsOrderDetailForm.getQty() == null ? 0 : goodsOrderDetailForm.getQty());
            goodsOrderDetail.setBillQty(goodsOrderDetail.getQty());

            detailsToBeSaved.add(goodsOrderDetail);
            amount = amount.add(new BigDecimal(goodsOrderDetail.getBillQty()).multiply(goodsOrderDetail.getPrice()));
        }
        goodsOrderDetailRepository.save(detailsToBeSaved);

        goodsOrder.setAmount(amount);
        goodsOrderRepository.save(goodsOrder);
    }

    private void saveExpressOrderInfo(GoodsOrder goodsOrder, Depot shop) {
        if(StringUtils.isNotBlank(goodsOrder.getExpressOrderId())){
            //如果已经存在，快递单不需要进行任何修改
            return;
        }

        ExpressOrder  expressOrder = new ExpressOrder();
        expressOrder.setExpressPrintQty(0);
        expressOrder.setExtendId(goodsOrder.getId());
        expressOrder.setExtendType(ExpressOrderTypeEnum.手机订单.name());
        expressOrder.setContator(shop.getContator());
        expressOrder.setAddress(shop.getAddress());
        expressOrder.setMobilePhone(shop.getMobilePhone());
        expressOrder.setToDepotId(shop.getId());
        expressOrder.setShipType(goodsOrder.getShipType());
        expressOrderRepository.save(expressOrder);

        goodsOrder.setExpressOrderId(expressOrder.getId());
        goodsOrderRepository.save(goodsOrder);
    }

    @Transactional
    public  void bill(GoodsOrderBillForm goodsOrderBillForm) {

        GoodsOrder goodsOrder = goodsOrderRepository.findOne(goodsOrderBillForm.getId());
        Depot shop=depotRepository.findOne(goodsOrder.getShopId());
        if(goodsOrderBillForm.getSyn() && StringUtils.isBlank(shop.getClientId())){
            throw new ServiceException("门店没有对应的财务记录，不能同步财务");
        }
        goodsOrder.setStoreId(goodsOrderBillForm.getStoreId());
        goodsOrder.setBillDate(goodsOrderBillForm.getBillDate());
        goodsOrder.setRemarks(goodsOrderBillForm.getRemarks());
        goodsOrder.setStatus(GoodsOrderStatusEnum.待发货.name());
        goodsOrder.setBusinessId(goodsOrderRepository.findNextBusinessId(goodsOrderBillForm.getBillDate()));
        goodsOrderRepository.save(goodsOrder);

        Map<String,Product> productMap = productRepository.findMap(CollectionUtil.extractToList(goodsOrderBillForm.getGoodsOrderBillDetailFormList(),"productId"));

        List<GoodsOrderDetail> detailList = saveDetailInfoWhenBill(goodsOrder, goodsOrderBillForm);

        ExpressOrder expressOrder = saveExpressOrderInfoWhenBill(goodsOrder, goodsOrderBillForm, detailList, productMap);
        if (Boolean.TRUE.equals(goodsOrderBillForm.getSyn())) {
            syn(goodsOrder, expressOrder);
        }
    }

    private List<GoodsOrderDetail> saveDetailInfoWhenBill(GoodsOrder goodsOrder, GoodsOrderBillForm goodsOrderBillForm) {
        BigDecimal amount = BigDecimal.ZERO;
        List<GoodsOrderDetail> goodsOrderDetailList  = goodsOrderDetailRepository.findByGoodsOrderId(goodsOrder.getId());
        Map<String,GoodsOrderDetail> goodsOrderDetailMap  = CollectionUtil.extractToMap(goodsOrderDetailList,"id");

        List<GoodsOrderDetail> detailsToBeSaved = new ArrayList<>();
        for (GoodsOrderBillDetailForm goodsOrderBillDetailForm : goodsOrderBillForm.getGoodsOrderBillDetailFormList()) {
            GoodsOrderDetail goodsOrderDetail;
            if(StringUtils.isNotBlank(goodsOrderBillDetailForm.getId())){
                goodsOrderDetail = goodsOrderDetailMap.get(goodsOrderBillDetailForm.getId());
            }else{
                goodsOrderDetail = new GoodsOrderDetail();
                goodsOrderDetail.setGoodsOrderId(goodsOrder.getId());
                goodsOrderDetail.setProductId(goodsOrderBillDetailForm.getProductId());
                goodsOrderDetail.setQty(0);
                goodsOrderDetail.setShippedQty(0);
                goodsOrderDetail.setReturnQty(0);
            }
            goodsOrderDetail.setBillQty(goodsOrderBillDetailForm.getBillQty() == null ? 0 : goodsOrderBillDetailForm.getBillQty());
            goodsOrderDetail.setPrice(goodsOrderBillDetailForm.getPrice());
            detailsToBeSaved.add(goodsOrderDetail);

            amount = amount.add(new BigDecimal(goodsOrderBillDetailForm.getBillQty() == null ? 0 : goodsOrderBillDetailForm.getBillQty()).multiply(goodsOrderBillDetailForm.getPrice()));
        }
        goodsOrderDetailRepository.save(detailsToBeSaved);

        goodsOrder.setAmount(amount);
        goodsOrderRepository.save(goodsOrder);

        return detailsToBeSaved;
    }

    private ExpressOrder saveExpressOrderInfoWhenBill(GoodsOrder goodsOrder, GoodsOrderBillForm goodsOrderBillForm, List<GoodsOrderDetail> detailList, Map<String,Product> productMap) {
        ExpressOrder expressOrder = expressOrderRepository.findOne(goodsOrder.getExpressOrderId());
        expressOrder.setExtendBusinessId(goodsOrder.getBusinessId());
        expressOrder.setContator(goodsOrderBillForm.getContator());
        expressOrder.setAddress(goodsOrderBillForm.getAddress());
        expressOrder.setMobilePhone(goodsOrderBillForm.getMobilePhone());
        expressOrder.setFromDepotId(goodsOrderBillForm.getStoreId());
        expressOrder.setPrintDate(goodsOrder.getBillDate());

        int totalBillQty = 0;
        int mobileBillQty = 0;
        for(GoodsOrderDetail goodsOrderDetail : detailList){
            totalBillQty += goodsOrderDetail.getBillQty();
            if(productMap.get(goodsOrderDetail.getProductId()).getHasIme()){
                mobileBillQty += goodsOrderDetail.getBillQty();
            }
        }
        expressOrder.setTotalQty(totalBillQty);
        expressOrder.setMobileQty(mobileBillQty);
        expressOrder.setExpressPrintQty(ShipTypeEnum.总部发货.name().equals(goodsOrder.getShipType()) ? expressUtils.getExpressPrintQty(totalBillQty) : 0 );
        expressOrderRepository.save(expressOrder);
        return expressOrder;
    }

    private void syn(GoodsOrder goodsOrder, ExpressOrder expressOrder){
        Depot shop=depotRepository.findOne(goodsOrder.getShopId());

        //开单的时候，如果是选择昌东仓库，默认生成一张从大库到昌东仓库的直接调拨单
        CompanyConfigCacheDto companyConfig = CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.MERGE_STORE_IDS.name());

        if (companyConfig!=null&&StringUtils.isNotBlank(companyConfig.getValue())) {
            String mergeStoreIds =companyConfig.getValue();
            List<String> storeIds = StringUtils.getSplitList(mergeStoreIds, CharConstant.COMMA);
            String fromStockId = storeIds.get(0);
            String toStockId = storeIds.get(1);
            if (goodsOrder.getStoreId().equals(toStockId)) {
                DepotStore allotFromStock = depotStoreRepository.findByEnabledIsTrueAndDepotId(fromStockId);
                DepotStore allotToStock = depotStoreRepository.findByEnabledIsTrueAndDepotId(toStockId);
                KingdeeSynReturnDto kingdeeSynReturnDto = stkTransferDirectManager.synForGoodsOrder(goodsOrder, allotFromStock.getOutCode(), allotToStock.getOutCode());
                goodsOrder.setOutCode(StringUtils.appendString(goodsOrder.getOutCode(),kingdeeSynReturnDto.getBillNo(),CharConstant.COMMA));
            }
        }
        if (StringUtils.isNotBlank(shop.getDelegateDepotId())) {
            DepotStore allotFromStock = depotStoreRepository.findByEnabledIsTrueAndDepotId(goodsOrder.getStoreId());
            DepotStore allotToStock = depotStoreRepository.findByEnabledIsTrueAndDepotId(shop.getDelegateDepotId());
            KingdeeSynReturnDto kingdeeSynReturnDto = stkTransferDirectManager.synForGoodsOrder(goodsOrder,allotFromStock.getOutCode(),allotToStock.getOutCode());
            goodsOrder.setOutCode(StringUtils.appendString(goodsOrder.getOutCode(),kingdeeSynReturnDto.getBillNo(),CharConstant.COMMA));

        } else {
            KingdeeSynReturnDto kingdeeSynReturnDto = salOutStockManager.synForGoodsOrder(goodsOrder);
            String outCode = StringUtils.appendString(goodsOrder.getOutCode(),kingdeeSynReturnDto.getBillNo(),CharConstant.COMMA);
            if("AR_receivable".equals(kingdeeSynReturnDto.getNextFormId()) && StringUtils.isNotBlank(kingdeeSynReturnDto.getNextBillNo())){
                outCode = StringUtils.appendString(outCode,"应收单:"+kingdeeSynReturnDto.getNextBillNo(),CharConstant.COMMA);
            }
            goodsOrder.setOutCode(outCode);
        }
        goodsOrderRepository.save(goodsOrder);

        expressOrder.setOutCode(goodsOrder.getOutCode()==null ? null : goodsOrder.getOutCode().replaceAll(CharConstant.COMMA, "<br/>"));
        expressOrderRepository.save(expressOrder);
    }

    private String getDefaultStoreId(String netType, Depot shop) {
        String defaultStoreId;
        if(NetTypeEnum.联信.name().equals(netType)){
            defaultStoreId = CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.LX_DEFAULT_STORE_ID.name()).getValue();
        }else {
            defaultStoreId = CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.DEFAULT_STORE_ID.name()).getValue();
        }
        String carrierLockOfficeIds = CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.CARRIER_LOCK_OFFICE.name()).getValue();
        if(StringUtils.isNotBlank(carrierLockOfficeIds)){
            List<String> officeIdList = StringUtils.getSplitList(carrierLockOfficeIds, CharConstant.COMMA);
            if(officeIdList.contains(shop.getOfficeId())) {
                defaultStoreId = CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.DEFALULT_CARRIAR_STORE_ID.name()).getValue();
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

    @Transactional
    public void updatePullStatus(String id, String pullStatus,String expressOrderExpressCodes) {
        GoodsOrder goodsOrder = goodsOrderRepository.findOne(id);
        goodsOrder.setPullStatus(pullStatus);
        goodsOrderRepository.save(goodsOrder);

        if (GoodsOrderPullStatusEnum.已推送.name().equals(pullStatus) && GoodsOrderStatusEnum.待签收.name().equals(goodsOrder.getStatus())) {
            List<CarrierOrder> carrierOrders = carrierOrderRepository.findByGoodsOrderId(id);
            for (CarrierOrder carrierOrder : carrierOrders) {
                carrierOrder.setStatus(CarrierOrderStatusEnum.已导入.name());
            }
            carrierOrderRepository.save(carrierOrders);
        }

        if(StringUtils.isNotBlank(goodsOrder.getExpressOrderId())){
            ExpressOrder expressOrder=expressOrderRepository.findOne(goodsOrder.getExpressOrderId());
            expressOrderManager.save(ExpressOrderTypeEnum.手机订单.name(), goodsOrder.getId(), expressOrderExpressCodes,expressOrder.getExpressCompanyId());
        }

    }

    public List<GoodsOrderDetailDto> findDetailList(String id,String shopId,String netType,String shipType) {
        List<GoodsOrderDetailDto> goodsOrderDetailDtoList   = Lists.newArrayList();
        Map<String,GoodsOrderDetail> goodsOrderDetailMap = Maps.newHashMap();
        Map<String,Product> productMap = Maps.newHashMap();
        if(StringUtils.isNotBlank(id)) {
            GoodsOrder goodsOrder = goodsOrderRepository.findOne(id);
            shopId = goodsOrder.getShopId();
            netType = goodsOrder.getNetType();
            shipType = goodsOrder.getShipType();

            List<GoodsOrderDetail> goodsOrderDetailList  = goodsOrderDetailRepository.findByGoodsOrderId(id);
            if(CollectionUtil.isNotEmpty(goodsOrderDetailList)) {
                goodsOrderDetailMap = CollectionUtil.extractToMap(goodsOrderDetailList,"productId");
                List<GoodsOrderDetailDto> list = BeanUtil.map(goodsOrderDetailList,GoodsOrderDetailDto.class);
                goodsOrderDetailDtoList.addAll(list);
                productMap = productRepository.findMap(CollectionUtil.extractToList(goodsOrderDetailList,"productId"));
            }
        }
        Depot shop = depotRepository.findOne(shopId);
        List<PricesystemDetail> pricesystemDetailList = pricesystemDetailRepository.findByPricesystemId(shop.getPricesystemId());
        productMap.putAll(productRepository.findMap(CollectionUtil.extractToList(pricesystemDetailList,"productId")));
        for(PricesystemDetail pricesystemDetail:pricesystemDetailList) {
            Product product = productMap.get(pricesystemDetail.getProductId());
            if(!goodsOrderDetailMap.containsKey(pricesystemDetail.getProductId()) && product != null && netTypeMatch(netType, product.getNetType())) {
                if(allowOrder(product, shipType)) {
                    GoodsOrderDetailDto goodsOrderDetailDto= new GoodsOrderDetailDto();
                    goodsOrderDetailDto.setProductId(pricesystemDetail.getProductId());
                    goodsOrderDetailDto.setPrice(pricesystemDetail.getPrice());
                    goodsOrderDetailDtoList.add(goodsOrderDetailDto);
                }
            }
        }
        //办事处已订货数
        List<GoodsOrderDetail> areaDetailList = goodsOrderDetailRepository.findByAreaIdAndCreatedDate(shop.getAreaId(), LocalDateTime.of(LocalDate.now(), LocalTime.MIN),LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIN));
        Map<String,Integer>  areaDetailMap = Maps.newHashMap();
        for(GoodsOrderDetail goodsOrderDetail:areaDetailList) {
            if(!goodsOrderDetail.getGoodsOrderId().equals(id)) {
                if(!areaDetailMap.containsKey(goodsOrderDetail.getProductId())) {
                    areaDetailMap.put(goodsOrderDetail.getProductId(),0);
                }
                areaDetailMap.put(goodsOrderDetail.getProductId(),areaDetailMap.get(goodsOrderDetail.getProductId())+ goodsOrderDetail.getQty());
            }
        }
        //设置其他数据
        for(GoodsOrderDetailDto goodsOrderDetailDto:goodsOrderDetailDtoList) {
            Product product= productMap.get(goodsOrderDetailDto.getProductId());
            goodsOrderDetailDto.setProductName(product.getName());
            goodsOrderDetailDto.setAllowOrder(product.getAllowOrder());
            goodsOrderDetailDto.setHasIme(product.getHasIme());
            goodsOrderDetailDto.setAreaQty(areaDetailMap.get(product.getId()));
        }
        return goodsOrderDetailDtoList;
    }

    private boolean allowOrder(Product product, String shipType) {
        if(!Boolean.TRUE.equals(product.getVisible())){
            return false;
        }
        //TODO 判断逻辑是否完整
        //如果是总部发货，且下单人员是地区人员，则根据货品是否开放下单
        if(ShipTypeEnum.总部发货.name().equals(shipType) || ShipTypeEnum.总部自提.name().equals(shipType)) {
            OfficeDto officeDto = OfficeUtil.findOne(redisTemplate,RequestUtils.getOfficeId());
            if(JointLevelEnum.二级.name().equals(officeDto.getJointLevel())) {
                return product.getAllowOrder();
            }
        }
        return true;
    }

    public GoodsOrderDto getBill(String id) {
        GoodsOrderDto goodsOrderDto = findOne(id);
        ExpressOrder expressOrder = expressOrderRepository.findOne(goodsOrderDto.getExpressOrderId());

        goodsOrderDto.setBillDate(LocalDate.now());
        if(StringUtils.isNotBlank(expressOrder.getExpressCompanyId())){
            goodsOrderDto.setExpressCompanyId(expressOrder.getExpressCompanyId());
        }else{
            String defaultExpressCompanyId = CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.DEFAULT_EXPRESS_COMPANY_ID.name()).getValue();
            goodsOrderDto.setExpressCompanyId(StringUtils.trimToNull(defaultExpressCompanyId));
        }

        //是否自动同步，根据门店是否包含client
        goodsOrderDto.setSyn(false);
        Depot shop = depotRepository.findOne(goodsOrderDto.getShopId());
        if(StringUtils.isNotBlank(shop.getClientId()) && !ShipTypeEnum.代理发货.name().equals(goodsOrderDto.getShipType()) && !ShipTypeEnum.代理自提.name().equals(goodsOrderDto.getShipType()) ) {
            goodsOrderDto.setSyn(true);
        }
        goodsOrderDto.setShipType(expressOrder.getShipType());
        goodsOrderDto.setAddress(expressOrder.getAddress());
        goodsOrderDto.setContator(expressOrder.getContator());
        goodsOrderDto.setMobilePhone(expressOrder.getMobilePhone());
        //开单明细
        List<GoodsOrderDetail> goodsOrderDetailList = goodsOrderDetailRepository.findByGoodsOrderId(id);
        List<GoodsOrderDetailDto> goodsOrderDetailDtoList = BeanUtil.map(goodsOrderDetailList,GoodsOrderDetailDto.class);
        Map<String,GoodsOrderDetailDto> goodsOrderDetailDtoMap = CollectionUtil.extractToMap(goodsOrderDetailDtoList,"productId");
        List<PricesystemDetail> pricesystemDetailList = pricesystemDetailRepository.findByPricesystemId(shop.getPricesystemId());
        Map<String,PricesystemDetail> pricesystemDetailMap =  CollectionUtil.extractToMap(pricesystemDetailList,"productId");
        Map<String,Product> productMap = productRepository.findMap(CollectionUtil.extractToList(goodsOrderDetailList,"productId"));
        for(GoodsOrderDetailDto goodsOrderDetailDto:goodsOrderDetailDtoList) {
            Product product = productMap.get(goodsOrderDetailDto.getProductId());
            goodsOrderDetailDto.setPrice(pricesystemDetailMap.get(product.getId()).getPrice());
            goodsOrderDetailDto.setProductName(product.getName());
            goodsOrderDetailDto.setAllowOrder(product.getAllowOrder());
            goodsOrderDetailDto.setHasIme(product.getHasIme());
            goodsOrderDetailDto.setProductOutId(product.getOutId());
        }
        //价格体系
        productMap.putAll(productRepository.findMap(CollectionUtil.extractToList(pricesystemDetailList,"productId")));
        for(PricesystemDetail pricesystemDetail:pricesystemDetailList) {
            Product product = productMap.get(pricesystemDetail.getProductId());
            if(product != null && Boolean.TRUE.equals(product.getVisible()) && !goodsOrderDetailDtoMap.containsKey(pricesystemDetail.getProductId()) && netTypeMatch(goodsOrderDto.getNetType(), product.getNetType())) {
                GoodsOrderDetailDto goodsOrderDetailDto = new GoodsOrderDetailDto();
                goodsOrderDetailDto.setProductId(product.getId());
                goodsOrderDetailDto.setProductOutId(product.getOutId());
                goodsOrderDetailDto.setPrice(pricesystemDetailMap.get(product.getId()).getPrice());

                goodsOrderDetailDto.setProductName(product.getName());
                goodsOrderDetailDto.setAllowOrder(product.getAllowOrder());
                goodsOrderDetailDto.setHasIme(product.getHasIme());
                goodsOrderDetailDtoList.add(goodsOrderDetailDto);
            }
        }

        // 设置areaQty
        Map<String, Integer> areaBillQtyMap = getAreaBillQtyMap( shop.getAreaId(), id);
        for(GoodsOrderDetailDto goodsOrderDetailDto:goodsOrderDetailDtoList) {
            goodsOrderDetailDto.setAreaQty(areaBillQtyMap.get(goodsOrderDetailDto.getProductId()));
        }
        goodsOrderDto.setGoodsOrderDetailDtoList(goodsOrderDetailDtoList);

        return goodsOrderDto;
    }

    private boolean netTypeMatch(String netType1, String netType2) {
        return netType1.equals(netType2) || NetTypeEnum.全网通.name().equals(netType1) || NetTypeEnum.全网通.name().equals(netType2);
    }

    private Map<String,Integer> getAreaBillQtyMap(String areaId, String exceptGoodsOrderId) {

        List<GoodsOrderDetail> areaDetailList = goodsOrderDetailRepository.findByAreaIdAndCreatedDate(areaId, LocalDateTime.of(LocalDate.now(), LocalTime.MIN),LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIN));
        Map<String,Integer>  areaBillQtyMap = Maps.newHashMap();
        for(GoodsOrderDetail goodsOrderDetail:areaDetailList) {
            if(!goodsOrderDetail.getGoodsOrderId().equals(exceptGoodsOrderId)) {
                if(!areaBillQtyMap.containsKey(goodsOrderDetail.getProductId())) {
                    areaBillQtyMap.put(goodsOrderDetail.getProductId(),0);
                }
                areaBillQtyMap.put(goodsOrderDetail.getProductId(),areaBillQtyMap.get(goodsOrderDetail.getProductId())+ goodsOrderDetail.getBillQty());
            }
        }

        return areaBillQtyMap;
    }

    public GoodsOrderDto findDetail(String id) {
        GoodsOrderDto goodsOrderDto = findOne(id);
        List<GoodsOrderDetail> goodsOrderDetailList = goodsOrderDetailRepository.findByGoodsOrderId(id);
        List<GoodsOrderDetailDto> goodsOrderDetailDtoList = BeanUtil.map(goodsOrderDetailList,GoodsOrderDetailDto.class);
        cacheUtils.initCacheInput(goodsOrderDetailDtoList);
        List<GoodsOrderImeDto> goodsOrderImeDtoList  = goodsOrderImeRepository.findDtoListByGoodsOrderId(id);
        cacheUtils.initCacheInput(goodsOrderImeDtoList);
        goodsOrderDto.setGoodsOrderDetailDtoList(goodsOrderDetailDtoList);
        goodsOrderDto.setGoodsOrderImeDtoList(goodsOrderImeDtoList);
        return goodsOrderDto;
    }

    @Transactional
    public void delete(String id) {
        goodsOrderRepository.logicDelete(id);
    }

    public DepotAccountDto findShopAccountByGoodsOrderId(String goodsOrderId) {
        GoodsOrder goodsOrder = goodsOrderRepository.findOne(goodsOrderId);
        if(StringUtils.isBlank(goodsOrder.getShopId())){
            return new DepotAccountDto();
        }

        DepotAccountDto result = depotRepository.findDepotAccount(goodsOrder.getShopId());
        cacheUtils.initCacheInput(result);

        CustomerReceiveQuery customerReceiveQuery = new CustomerReceiveQuery();
        customerReceiveQuery.setDateStart(LocalDate.now());
        customerReceiveQuery.setDateEnd(customerReceiveQuery.getDateStart());
        customerReceiveQuery.setCustomerIdList(Lists.newArrayList(result.getClientOutId()));
        List<CustomerReceiveDto> customerReceiveDtoList = cloudClient.getCustomerReceiveList(customerReceiveQuery);
        if(CollectionUtil.isNotEmpty(customerReceiveDtoList)){
            result.setQcys(customerReceiveDtoList.get(0).getBeginShouldGet());
            result.setQmys(customerReceiveDtoList.get(0).getEndShouldGet());
        }else {
            result.setQcys(BigDecimal.ZERO);
            result.setQmys(BigDecimal.ZERO);
        }
        return result;
    }

    @Transactional
    public void batchAdd(GoodsOrderBatchAddForm goodsOrderBatchAddForm) {

        String defaultStoreId = CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.DEFAULT_STORE_ID.name()).getValue();

        Map<String, List<GoodsOrderBatchAddDetailForm>> orderMap = CollectionUtil.extractToMapList(goodsOrderBatchAddForm.getGoodsOrderBatchAddDetailFormList(), "carrierOrderId");
        for(List<GoodsOrderBatchAddDetailForm> each : orderMap.values()){
            GoodsOrderBatchAddDetailForm firstDetailForm = each.get(0);
            Depot shop = depotRepository.findByEnabledIsTrueAndName(firstDetailForm.getShopName());
            if(shop == null){
                throw new ServiceException("门店："+firstDetailForm.getShopName()+"不存在");
            }else if(StringUtils.isBlank(shop.getPricesystemId())){
                throw new ServiceException("门店："+firstDetailForm.getShopName()+"没有绑定价格体系");
            }else if(StringUtils.isBlank(shop.getDepotShopId())){
                throw new ServiceException("门店："+firstDetailForm.getShopName()+"没有关联的DepotShop记录");
            }

            GoodsOrder goodsOrder = new GoodsOrder();
            goodsOrder.setUseTicket(false);
            goodsOrder.setStoreId(defaultStoreId);
            goodsOrder.setStatus(GoodsOrderStatusEnum.待开单.name());
            goodsOrder.setShopId(shop.getId());
            goodsOrder.setNetType(firstDetailForm.getNetType());
            goodsOrder.setShipType(firstDetailForm.getShipType());
            goodsOrder.setRemarks(firstDetailForm.getRemarks());
            goodsOrderRepository.save(goodsOrder);

            CarrierOrder carrierOrder=new CarrierOrder();
            carrierOrder.setGoodsOrderId(goodsOrder.getId());
            carrierOrder.setCode(firstDetailForm.getCarrierOrderId());
            carrierOrderRepository.save(carrierOrder);

            saveExpressOrderInfoWhenBatchAdd(goodsOrder, firstDetailForm, shop);

            saveGoodsOrderDetailInfoWhenBatchAdd(goodsOrder, each);

        }
    }

    private void saveExpressOrderInfoWhenBatchAdd(GoodsOrder goodsOrder, GoodsOrderBatchAddDetailForm firstDetailForm, Depot toDepot) {
        ExpressOrder expressOrder=new ExpressOrder();

        expressOrder.setExpressPrintQty(0);
        expressOrder.setExtendType(ExpressOrderTypeEnum.手机订单.name());
        expressOrder.setExtendId(goodsOrder.getId());
        expressOrder.setContator(firstDetailForm.getContator());
        expressOrder.setAddress(firstDetailForm.getAddress());
        expressOrder.setMobilePhone(firstDetailForm.getMobilePhone());
        expressOrder.setToDepotId(toDepot.getId());
        expressOrder.setShipType(firstDetailForm.getShipType());
        expressOrderRepository.save(expressOrder);

        goodsOrder.setExpressOrderId(expressOrder.getId());
        goodsOrderRepository.save(goodsOrder);
    }

    private void saveGoodsOrderDetailInfoWhenBatchAdd(GoodsOrder goodsOrder, List<GoodsOrderBatchAddDetailForm> goodsOrderBatchAddDetailFormList) {

        Map<String, List<GoodsOrderBatchAddDetailForm>> detailMap = CollectionUtil.extractToMapList(goodsOrderBatchAddDetailFormList, "productName");
        BigDecimal amount = BigDecimal.ZERO;
        List<GoodsOrderDetail> toBeSaved = new ArrayList<>();
        for(List<GoodsOrderBatchAddDetailForm> goodsOrderDetailGroup : detailMap.values()){
            GoodsOrderBatchAddDetailForm  firstDetailForm = goodsOrderDetailGroup.get(0);
            Product product = productRepository.findByEnabledIsTrueAndName(firstDetailForm.getProductName());
            if(product == null){
                throw new ServiceException("型号："+firstDetailForm.getProductName()+"不存在");
            }else if(!netTypeMatch(product.getNetType(), firstDetailForm.getNetType())){
                throw new ServiceException("型号："+firstDetailForm.getProductName()+"的网络制式和订货的网络制式不匹配");
            }

            GoodsOrderDetail goodsOrderDetail = new GoodsOrderDetail();
            goodsOrderDetail.setShippedQty(0);
            goodsOrderDetail.setProductId(product.getId());
            goodsOrderDetail.setPrice(firstDetailForm.getPrice());
            goodsOrderDetail.setGoodsOrderId(goodsOrder.getId());
            int goodsOrderDetailQty = goodsOrderDetailGroup.stream().mapToInt(GoodsOrderBatchAddDetailForm::getQty).sum();
            goodsOrderDetail.setQty(goodsOrderDetailQty);
            goodsOrderDetail.setBillQty(goodsOrderDetailQty);
            toBeSaved.add(goodsOrderDetail);

            amount=amount.add(goodsOrderDetail.getPrice().multiply(new BigDecimal(goodsOrderDetail.getQty())));
        }
        goodsOrderDetailRepository.save(toBeSaved);

        goodsOrder.setAmount(amount);
        goodsOrderRepository.save(goodsOrder);
    }

    public SimpleExcelBook export(GoodsOrderQuery goodsOrderQuery) {

        Workbook workbook = new SXSSFWorkbook(10000);
        Map<String, CellStyle> cellStyleMap=ExcelUtils.getCellStyleMap(workbook);

        if (goodsOrderQuery.getExpressCodes() != null) {
            goodsOrderQuery.setExpresscodeList(Arrays.asList(goodsOrderQuery.getExpressCodes().split("[\n,]")));
        }
        if (goodsOrderQuery.getBusinessIds() != null) {
            goodsOrderQuery.setBusinessIdList(Arrays.asList(goodsOrderQuery.getBusinessIds().split("[\n,]")));
        }
        goodsOrderQuery.setDepotIdList(depotManager.filterDepotIds(RequestUtils.getAccountId()));

        List<GoodsOrderDto> goodsOrderDtoList=goodsOrderRepository.findAll(new PageRequest(0,10000), goodsOrderQuery).getContent();
        cacheUtils.initCacheInput(goodsOrderDtoList);

        List<String> goodsOrderIdList = CollectionUtil.extractToList(goodsOrderDtoList, "id");

        List<GoodsOrderDetailDto> goodsOrderDetailDtos = goodsOrderDetailRepository.findDtoListByGoodsOrderIdList(goodsOrderIdList);
        Map<String, List<GoodsOrderDetailDto>> goodsOrderDetailMap = CollectionUtil.extractToMapList(goodsOrderDetailDtos, "goodsOrderId");

        List<GoodsOrderImeDto> goodsOrderImeDtos = goodsOrderImeRepository.findDtoListByGoodsOrderIdList(goodsOrderIdList);
        Map<String, List<GoodsOrderImeDto>> goodsOrderImeMap = CollectionUtil.extractToMapList(goodsOrderImeDtos, "goodsOrderId");

        Map<String, String> carrierOrderMap = getCarrierOrderCodeMap(goodsOrderIdList);

        List<SimpleExcelSheet> sheetList = new ArrayList<>();
        sheetList.add(getGoodsOrderListSheet( goodsOrderDtoList, goodsOrderDetailMap, carrierOrderMap,cellStyleMap));
        sheetList.add(getGoodsOrderDetailSheet(goodsOrderDtoList, goodsOrderDetailMap, carrierOrderMap,cellStyleMap));
        sheetList.add(getGoodsOrderImeSheet(goodsOrderDtoList, goodsOrderImeMap, carrierOrderMap,cellStyleMap));
        for(SimpleExcelSheet simpleExcelSheet : sheetList){
            ExcelUtils.doWrite(workbook, simpleExcelSheet);
        }

        return  new SimpleExcelBook(workbook,"货品订货"+ LocalDate.now()+".xlsx", sheetList);
    }

    private SimpleExcelSheet getGoodsOrderImeSheet(List<GoodsOrderDto> goodsOrderDtoList, Map<String, List<GoodsOrderImeDto>> goodsOrderImeMap, Map<String, String> carrierOrderMap,Map<String, CellStyle> cellStyleMap) {
        CellStyle headCellStyle = cellStyleMap.get(ExcelCellStyle.HEADER.name());
        CellStyle dataCellStyle = cellStyleMap.get(ExcelCellStyle.DATA.name());
        List<List<SimpleExcelColumn>> excelColumnList= Lists.newArrayList();
        List<SimpleExcelColumn> headColumnList=Lists.newArrayList();
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"订单编号"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"外部编号"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"商城单号"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"开单日期"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"发货仓库"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"办事处"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"考核区域"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"地区属性"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"门店"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"产品名称"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"串码"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"串码2"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"MEID"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"创建人"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"备注"));
        excelColumnList.add(headColumnList);

        for(GoodsOrderDto goodsOrderDto : goodsOrderDtoList){
            List<GoodsOrderImeDto> goodsOrderImeDtoList = goodsOrderImeMap.get(goodsOrderDto.getId());
            if(goodsOrderImeDtoList == null){
                continue;
            }
            for(GoodsOrderImeDto goodsOrderImeDto : goodsOrderImeDtoList){
                List<SimpleExcelColumn> simpleExcelColumnList=Lists.newArrayList();
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getFormatId()));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getOutCode()));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,carrierOrderMap.get(goodsOrderDto.getId()) != null ? carrierOrderMap.get(goodsOrderDto.getId()).trim() : ""));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getBillDate()));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getStoreName()));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getShopAreaName()));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getShopOfficeName()));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getShopDepotShopAreaType()));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getShopName()));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderImeDto.getProductName()));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderImeDto.getProductImeIme()));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderImeDto.getProductImeIme2()));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderImeDto.getProductImeMeid()));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getCreatedByName()));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getRemarks()));
                excelColumnList.add(simpleExcelColumnList);
            }

        }
        return new SimpleExcelSheet("订货串码",excelColumnList);
    }

    private SimpleExcelSheet getGoodsOrderDetailSheet(List<GoodsOrderDto> goodsOrderDtoList, Map<String, List<GoodsOrderDetailDto>> goodsOrderDetailMap, Map<String, String> carrierOrderMap, Map<String, CellStyle> cellStyleMap) {
        CellStyle headCellStyle = cellStyleMap.get(ExcelCellStyle.HEADER.name());
        CellStyle dataCellStyle = cellStyleMap.get(ExcelCellStyle.DATA.name());
        List<List<SimpleExcelColumn>> excelColumnList= Lists.newArrayList();

        List<SimpleExcelColumn> headColumnList=Lists.newArrayList();
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"订单编号"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"财务编号"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"开单日期"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"状态"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"仓库"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"办事处"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"考核区域"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"门店"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"区域属性"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"产品名称"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"单价"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"开单数"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"金额"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"商城状态"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"商城单号"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"发货时间"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"创建人"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"备注"));
        excelColumnList.add(headColumnList);

        for(GoodsOrderDto goodsOrderDto : goodsOrderDtoList){
            List<GoodsOrderDetailDto> goodsOrderDetailDtoList = goodsOrderDetailMap.get(goodsOrderDto.getId());
            if(goodsOrderDetailDtoList == null){
                continue;
            }
            for(GoodsOrderDetailDto goodsOrderDetailDto : goodsOrderDetailDtoList){
                List<SimpleExcelColumn> simpleExcelColumnList=Lists.newArrayList();
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getFormatId()));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getOutCode()));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getBillDate()));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getStatus()));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getStoreName()));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getShopAreaName()));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getShopOfficeName()));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getShopName()));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getShopDepotShopAreaType()));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDetailDto.getProductName()));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDetailDto.getPrice()));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDetailDto.getRealBillQty()));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,new BigDecimal(goodsOrderDetailDto.getRealBillQty()).multiply(goodsOrderDetailDto.getPrice())));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getPullStatus()));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,carrierOrderMap.get(goodsOrderDto.getId()) != null ? carrierOrderMap.get(goodsOrderDto.getId()).trim() : ""));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getShipDate()));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getCreatedByName()));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getRemarks()));
                excelColumnList.add(simpleExcelColumnList);
            }
        }
        return new SimpleExcelSheet("订货单明细",excelColumnList);
    }

    private SimpleExcelSheet getGoodsOrderListSheet( List<GoodsOrderDto> goodsOrderDtoList, Map<String, List<GoodsOrderDetailDto>> goodsOrderDetailMap, Map<String, String> carrierOrderMap,Map<String, CellStyle> cellStyleMap) {

        CellStyle headCellStyle = cellStyleMap.get(ExcelCellStyle.HEADER.name());
        CellStyle dataCellStyle = cellStyleMap.get(ExcelCellStyle.DATA.name());

        List<List<SimpleExcelColumn>> excelColumnList1= Lists.newArrayList();

        List<SimpleExcelColumn> headColumnList1=Lists.newArrayList();
        headColumnList1.add(new SimpleExcelColumn(headCellStyle,"单号"));
        headColumnList1.add(new SimpleExcelColumn(headCellStyle,"开单日期"));
        headColumnList1.add(new SimpleExcelColumn(headCellStyle,"发货日期"));
        headColumnList1.add(new SimpleExcelColumn(headCellStyle,"状态"));
        headColumnList1.add(new SimpleExcelColumn(headCellStyle,"商城单号"));
        headColumnList1.add(new SimpleExcelColumn(headCellStyle,"货品信息"));
        headColumnList1.add(new SimpleExcelColumn(headCellStyle,"仓库"));
        headColumnList1.add(new SimpleExcelColumn(headCellStyle,"门店"));
        headColumnList1.add(new SimpleExcelColumn(headCellStyle,"门店类型"));
        headColumnList1.add(new SimpleExcelColumn(headCellStyle,"区域属性"));
        headColumnList1.add(new SimpleExcelColumn(headCellStyle,"外部单号"));
        headColumnList1.add(new SimpleExcelColumn(headCellStyle,"是否天翼购订货"));
        headColumnList1.add(new SimpleExcelColumn(headCellStyle,"总金额"));
        headColumnList1.add(new SimpleExcelColumn(headCellStyle,"创建人"));
        headColumnList1.add(new SimpleExcelColumn(headCellStyle,"快递公司"));
        headColumnList1.add(new SimpleExcelColumn(headCellStyle,"快递单号"));
        headColumnList1.add(new SimpleExcelColumn(headCellStyle,"发货备注"));
        headColumnList1.add(new SimpleExcelColumn(headCellStyle,"订单备注"));
        excelColumnList1.add(headColumnList1);

        for(GoodsOrderDto goodsOrderDto : goodsOrderDtoList){
            List<SimpleExcelColumn> tmpColumnList=Lists.newArrayList();
            tmpColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getFormatId()));
            tmpColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getBillDate()));
            tmpColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getShipDate()));
            tmpColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getStatus()));
            tmpColumnList.add(new SimpleExcelColumn(dataCellStyle,carrierOrderMap.get(goodsOrderDto.getId()) != null ? carrierOrderMap.get(goodsOrderDto.getId()).trim() : ""));
            tmpColumnList.add(new SimpleExcelColumn(dataCellStyle,summarizeProductInfo(goodsOrderDetailMap.get(goodsOrderDto.getId()))));
            tmpColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getStoreName()));
            tmpColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getShopName()));
            tmpColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getShopType()));
            tmpColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getShopDepotShopAreaType()));
            tmpColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getOutCode()));
            tmpColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getLxMallOrder()!=null&&goodsOrderDto.getLxMallOrder()?"是":"否"));
            tmpColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getAmount()));
            tmpColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getCreatedByName()));
            tmpColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getExpressCompanyName()));
            tmpColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getExpressOrderExpressCodes()));
            tmpColumnList.add(new SimpleExcelColumn(dataCellStyle,""));
            tmpColumnList.add(new SimpleExcelColumn(dataCellStyle,goodsOrderDto.getRemarks()));
            excelColumnList1.add(tmpColumnList);
        }

        return new SimpleExcelSheet("订货单",excelColumnList1);
    }

    private String summarizeProductInfo(List<GoodsOrderDetailDto> goodsOrderDetailDtos) {
        if(CollectionUtil.isEmpty(goodsOrderDetailDtos)){
            return "";
        }
        StringBuilder summary = new StringBuilder();
        for (GoodsOrderDetailDto goodsOrderDetailDto : goodsOrderDetailDtos) {
            summary.append(goodsOrderDetailDto.getProductName()).append(":数量").append(goodsOrderDetailDto.getRealBillQty()).append(CharConstant.ENTER);

        }
        return summary.toString();
    }

    private Map<String, String> getCarrierOrderCodeMap(List<String> goodsOrderIdList) {
        List<CarrierOrder> carrierOrders = carrierOrderRepository.findByEnabledIsTrueAndGoodsOrderIdIn(goodsOrderIdList);
        Map<String, String> carrierOrderMap = Maps.newHashMap();
        for (CarrierOrder carrierOrder : carrierOrders) {
            if (!carrierOrderMap.containsKey(carrierOrder.getGoodsOrderId())) {
                carrierOrderMap.put(carrierOrder.getGoodsOrderId(), "");
            }
            carrierOrderMap.put(carrierOrder.getGoodsOrderId(),carrierOrderMap.get(carrierOrder.getGoodsOrderId()) + carrierOrder.getCode() + CharConstant.ENTER);
        }
        return carrierOrderMap;
    }

}
