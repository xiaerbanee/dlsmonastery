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
import net.myspring.future.common.enums.ExpressOrderTypeEnum;
import net.myspring.future.common.enums.GoodsOrderStatusEnum;
import net.myspring.future.common.enums.NetTypeEnum;
import net.myspring.future.common.enums.ShipTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.ExpressUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.DepotStore;
import net.myspring.future.modules.basic.domain.PricesystemDetail;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.dto.DepotAccountDto;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.basic.manager.SalOutStockManager;
import net.myspring.future.modules.basic.manager.StkTransferDirectManager;
import net.myspring.future.modules.basic.repository.*;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.future.modules.crm.domain.GoodsOrderDetail;
import net.myspring.future.modules.crm.dto.GoodsOrderDetailDto;
import net.myspring.future.modules.crm.dto.GoodsOrderDto;
import net.myspring.future.modules.crm.dto.GoodsOrderImeDto;
import net.myspring.future.modules.crm.repository.ExpressOrderRepository;
import net.myspring.future.modules.crm.repository.GoodsOrderDetailRepository;
import net.myspring.future.modules.crm.repository.GoodsOrderImeRepository;
import net.myspring.future.modules.crm.repository.GoodsOrderRepository;
import net.myspring.future.modules.crm.web.form.*;
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
    private ClientRepository clientRepository;
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

    //保存及修改订单
    @Transactional
    public GoodsOrder save(GoodsOrderForm goodsOrderForm) {
        Boolean isCreate = goodsOrderForm.isCreate();
        GoodsOrder goodsOrder;
        //保存订单
        if(isCreate) {
            goodsOrder = BeanUtil.map(goodsOrderForm,GoodsOrder.class);
            goodsOrder.setStoreId(getDefaultStoreId(goodsOrder));
            goodsOrder.setStatus(GoodsOrderStatusEnum.待开单.name());
            goodsOrder.setUseTicket(false);
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
            if(goodsOrderDetailForm.getQty()<0) {
                throw new ServiceException("订货明细里的数量不能小于0");
            }
            if(goodsOrderDetailForm.isCreate()) {
                if (goodsOrderDetailForm.getQty() > 0) {
                    GoodsOrderDetail goodsOrderDetail = BeanUtil.map(goodsOrderDetailForm,GoodsOrderDetail.class);
                    goodsOrderDetail.setGoodsOrderId(goodsOrder.getId());
                    goodsOrderDetail.setBillQty(goodsOrderDetail.getQty());
                    goodsOrderDetail.setShippedQty(0);
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
        goodsOrderForm.setId(goodsOrder.getId());
        return goodsOrder;
    }

    //开单
    @Transactional
    public  void bill(GoodsOrderBillForm goodsOrderBillForm) {
        Integer totalBillQty = 0;
        Integer mobileBillQty = 0;
        GoodsOrder goodsOrder = goodsOrderRepository.findOne(goodsOrderBillForm.getId());
        Depot shop=depotRepository.findOne(goodsOrder.getShopId());
        if(goodsOrderBillForm.getSyn() && StringUtils.isBlank(shop.getClientId())){
            throw new ServiceException("门店没有对应的财务记录，不能同步财务");
        }

        ReflectionUtil.copyProperties(goodsOrderBillForm,goodsOrder);
        BigDecimal amount = BigDecimal.ZERO;
        List<GoodsOrderDetail> goodsOrderDetailList  = goodsOrderDetailRepository.findByGoodsOrderId(goodsOrder.getId());
        Map<String,GoodsOrderDetail> goodsOrderDetailMap  = CollectionUtil.extractToMap(goodsOrderDetailList,"id");
        Map<String,Product> productMap = productRepository.findMap(CollectionUtil.extractToList(goodsOrderBillForm.getGoodsOrderBillDetailFormList(),"productId"));

        for (int i = goodsOrderBillForm.getGoodsOrderBillDetailFormList().size() - 1; i >= 0; i--) {
            GoodsOrderBillDetailForm goodsOrderBillDetailForm=goodsOrderBillForm.getGoodsOrderBillDetailFormList().get(i);
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
                    goodsOrderDetail.setShippedQty(0);
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
        goodsOrder.setBusinessId(goodsOrderRepository.findNextBusinessId(RequestUtils.getCompanyId(),goodsOrderBillForm.getBillDate()));
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
        if (goodsOrderBillForm.getSyn()) {
            syn(goodsOrder, expressOrder);
        }

    }

    @Transactional
    private void syn(GoodsOrder goodsOrder, ExpressOrder expressOrder){
        Depot shop=depotRepository.findOne(goodsOrder.getShopId());

        //开单的时候，如果是选择昌东仓库，默认生成一张从大库到昌东仓库的直接调拨单
        CompanyConfigCacheDto companyConfig = CompanyConfigUtil.findByCode(redisTemplate,RequestUtils.getCompanyId(),CompanyConfigCodeEnum.MERGE_STORE_IDS.name());

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

    private ExpressOrder getExpressOrder(GoodsOrderForm goodsOrderForm) {
        Depot shop = depotRepository.findOne(goodsOrderForm.getShopId());
        ExpressOrder expressOrder = new ExpressOrder();
        if(StringUtils.isNotBlank(goodsOrderForm.getExpressOrderId())) {
            expressOrder = expressOrderRepository.findOne(goodsOrderForm.getExpressOrderId());
        }else{
            expressOrder.setExpressPrintQty(0);
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
        expressOrder.setExtendBusinessId(goodsOrder.getBusinessId());
        expressOrder.setExtendId(goodsOrder.getId());
        expressOrder.setExtendType(ExpressOrderTypeEnum.手机订单.name());
        expressOrder.setContator(goodsOrderBillForm.getContator());
        expressOrder.setAddress(goodsOrderBillForm.getAddress());
        expressOrder.setMobilePhone(goodsOrderBillForm.getMobilePhone());
        expressOrder.setToDepotId(shop.getId());
        expressOrder.setShipType(goodsOrder.getShipType());
        expressOrder.setPrintDate(goodsOrder.getBillDate());
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
            String defaultExpressCompanyId = CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.DEFAULT_EXPRESS_COMPANY_ID.name()).getValue();
            goodsOrderDto.setExpressCompanyId(StringUtils.trimToNull(defaultExpressCompanyId));
        }

        //是否自动同步，根据门店是否包含client
        goodsOrderDto.setSyn(false);
        Depot shop = depotRepository.findOne(goodsOrderDto.getShopId());
        if(StringUtils.isNotBlank(shop.getClientId())) {
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

    public void batchAdd(GoodsOrderBatchAddForm goodsOrderBatchAddForm) {
        for(GoodsOrderBatchAddDetailForm goodsOrderBatchAddDetailForm : goodsOrderBatchAddForm.getGoodsOrderBatchAddDetailFormList()){

            GoodsOrderForm goodsOrderForm = new GoodsOrderForm();
            goodsOrderForm.setShipType(goodsOrderBatchAddDetailForm.getShipType());
            goodsOrderForm.setNetType(goodsOrderBatchAddDetailForm.getNetType());
            Depot depot = depotRepository.findByEnabledIsTrueAndCompanyIdAndName(RequestUtils.getCompanyId(),goodsOrderBatchAddDetailForm.getShopName());
            if(depot == null){
                throw new ServiceException("门店："+goodsOrderBatchAddDetailForm.getShopName()+"不存在");
            }
            goodsOrderForm.setShopId(depot.getId());
            goodsOrderForm.setRemarks(goodsOrderBatchAddDetailForm.getRemarks());

            GoodsOrderDetailForm goodsOrderDetailForm = new GoodsOrderDetailForm();
            Product product = productRepository.findByEnabledIsTrueAndCompanyIdAndName(RequestUtils.getCompanyId(),goodsOrderBatchAddDetailForm.getProductName());
            if(product == null){
                throw new ServiceException("型号："+goodsOrderBatchAddDetailForm.getProductName()+"不存在");
            }
            goodsOrderDetailForm.setProductId(product.getId());
            goodsOrderDetailForm.setPrice(goodsOrderBatchAddDetailForm.getPrice());
            goodsOrderDetailForm.setQty(goodsOrderBatchAddDetailForm.getQty());
            goodsOrderForm.setGoodsOrderDetailFormList(Collections.singletonList(goodsOrderDetailForm));

            GoodsOrder goodsOrder = save(goodsOrderForm);

            ExpressOrder expressOrder = expressOrderRepository.findOne(goodsOrder.getExpressOrderId());
            expressOrder.setContator(goodsOrderBatchAddDetailForm.getContator());
            expressOrder.setAddress(goodsOrderBatchAddDetailForm.getAddress());
            expressOrder.setMobilePhone(goodsOrderBatchAddDetailForm.getMobilePhone());
            expressOrderRepository.save(expressOrder);

        }
    }
}
