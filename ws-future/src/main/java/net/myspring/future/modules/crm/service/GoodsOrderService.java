package net.myspring.future.modules.crm.service;

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

        for (GoodsOrderDto each : page.getContent()) {
            if (GoodsOrderStatusEnum.待开单.name().equals(each.getStatus())) {
//               TODO 设置应收 each.setShopShouldGet();

            }
        }

        cacheUtils.initCacheInput(page.getContent());
        return page;
    }


    public GoodsOrderBillForm getBillForm(GoodsOrderBillForm goodsOrderBillForm) {

        GoodsOrderBillForm result = new GoodsOrderBillForm();


        result.setId(goodsOrderBillForm.getId());




//        Company company = AccountUtils.getCompany();
//        //检查是否存在已经缓存的日期
//        Date date = new Date();
//        Object billDateObj = AccountUtils.getCacheMap().get("goodsOrder_billDate");
//        if(billDateObj !=null) {
//            date = (Date)billDateObj;
//        }
//        AccountUtils.getCacheMap().put("goodsOrder_billDate",date);
//        boolean sameDate = false;
//        if(DateUtils.formatDate(new Date()).equals(DateUtils.formatDate(date))) {
//            sameDate = true;
//        }
//        model.addAttribute("sameDate",sameDate);
//        TODO 需要修改開單日期和sameDate
//        result.setSameDate(Boolean.TRUE);
//        result.setBillDate(LocalDate.now());
//
//        result.setGoodsOrderDetailFormList(findDetailFormList(god.getId()));
//
//        ExpressOrderDto eod = expressOrderMapper.findDto(god.getExpressOrderId());
//        if(eod!=null){
//            result.setExpressMobilePhone(eod.getMobilePhone());
//            result.setExpressAddress(eod.getAddress());
//            result.setExpressContator(eod.getContator());
//            if(eod.getExpressCompanyId() == null){
//                result.setExpressCompanyId(expressCompanyService.getDefaultExpressCompanyId());
//            }else{
//                result.setExpressCompanyId(eod.getExpressCompanyId());
//            }
//        }else{
//            result.setExpressMobilePhone(shopDto.getMobilePhone());
//            result.setExpressAddress(shopDto.getAddress());
//            result.setExpressContator(shopDto.getContator());
//            result.setExpressCompanyId(expressCompanyService.getDefaultExpressCompanyId());
//        }
//
//        result.setRemarks(god.getRemarks());


//      TODO  账上余额及信誉
//        if (goodsOrder.getSyn()) {
//            shop.setShouldGet(k3cloudService.findShouldGet(company.getOutDbname(), shop.getRealOutId()));
//        }
        // TODO 财务库存
//        Map<String, Integer> storeDetailMap = Maps.newHashMap();
//        if (store != null) {
//            if (goodsOrder.getSyn()) {
//                List<BdInventory> Inventorys = k3cloudService.findBdInventorys(company.getOutDbname(), store.getOutId());
//                Map<String, BdInventory> stockMap = Collections3.extractToMap(Inventorys, "FMATERIALID");
//                for (PricesystemDetail pricesystemDetail : shop.getPricesystem().getPricesystemDetails()) {
//                    BdInventory bdInventory = stockMap.get(pricesystemDetail.getProduct().getOutId());
//                    if (bdInventory != null) {
//                        storeDetailMap.put(pricesystemDetail.getProduct().getOutId(), bdInventory.getFBASEQTY());
//                    }
//                }
//                // 移动仓库，省公司仓库，必须合并库存，根据配置文件中的需要合并仓库来判断
//                String mergeStoreIds = Global.getConfig("crm.goodsOrder.mergeStoreIds");
//                if (StringUtils.isNotBlank(mergeStoreIds)) {
//                    List<String> storeIds = StringUtils.getSplitList(mergeStoreIds, Const.CHAR_COMMA);
//                    if (storeIds.contains(store.getIdStr())) {
//                        for (String storeId : storeIds) {
//                            Long id = Long.valueOf(storeId);
//                            if (!id.equals(store.getId())) {
//                                Depot mergeStore = depotService.findOne(id);
//                                Inventorys = k3cloudService.findBdInventorys(company.getOutDbname(),mergeStore.getOutId());
//                                stockMap = Collections3.extractToMap(Inventorys, "FMATERIALID");
//                                for (PricesystemDetail pricesystemDetail : shop.getPricesystem().getPricesystemDetails()) {
//                                    String outId = pricesystemDetail.getProduct().getOutId();
//                                    BdInventory bdInventory = stockMap.get(outId);
//                                    if (bdInventory != null) {
//                                        if (!storeDetailMap.containsKey(outId)) {
//                                            storeDetailMap.put(outId, 0);
//                                        }
//                                        storeDetailMap.put(outId, storeDetailMap.get(outId) + bdInventory.getFBASEQTY());
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            } else {
//                Map<String, Integer> stockMap = productImeService.findProductQty(store.getId());
//                for (PricesystemDetail pricesystemDetail : shop.getPricesystem().getPricesystemDetails()) {
//                    if (stockMap.containsKey(pricesystemDetail.getProduct().getOutId())) {
//                        storeDetailMap.put(pricesystemDetail.getProduct().getOutId(), stockMap.get(pricesystemDetail.getProduct().getId()));
//                    }
//                }
//            }
//        }

//    TODO 設置是否計算運費
//            if(StringUtils.isBlank(Global.getCompanyConfig(AccountUtils.getCompany().getId(), CompanyConfigCode.EXPRESS_PRODUCT_ID.getCode()))) {
//                return false;
//            }
//            if(StringUtils.isBlank(Global.getCompanyConfig(AccountUtils.getCompany().getId(), CompanyConfigCode.EXPRESS_SHOULD_GET_RULE.getCode()))) {
//                return false;
//            }
//            return true;
//        result.setTakeExpress(Boolean.TRUE);


//        if (result.getTakeExpress()) {
        // TODO 設置運費計算規則
//            result.expressProductId
//            model.addAttribute("expressProductId", Global.getCompanyConfig(AccountUtils.getCompany().getId(),CompanyConfigCode.EXPRESS_PRODUCT_ID.getCode()));
//            model.addAttribute("expressRule", Global.getCompanyConfig(AccountUtils.getCompany().getId(),CompanyConfigCode.EXPRESS_SHOULD_GET_RULE.getCode()));
//        } else {
//            model.addAttribute("expressProductId", "");
//            model.addAttribute("expressRule", "{}");
//        }

        // 办事处已开单数
        //TODO 設置rebateRULe
//        CompanyConfig goodsOrderRebateRule = companyConfigService.findByCode(AccountUtils.getCompany().getId(),CompanyConfigCode.GOODS_ORDER_REBATE_RULE.name());
//        if (goodsOrderRebateRule != null) {
//            result.set
//            model.addAttribute("goodsOrderRebateRule", goodsOrderRebateRule);
//        }

        return result;
    }


    public List<GoodsOrderDetailDto> findDetailListForNew(String shopId, String netType) {

        GoodsOrderDetailQuery goodsOrderDetailQuery = new GoodsOrderDetailQuery();
        goodsOrderDetailQuery.setCompanyId(RequestUtils.getCompanyId());
        goodsOrderDetailQuery.setNetType(netType);
        Depot depot = depotMapper.findOne(shopId);
        goodsOrderDetailQuery.setPricesystemId(depot.getPricesystemId());
        goodsOrderDetailQuery.setShowAll(Boolean.FALSE);
        //已下条件用于统计办事处已订货数
        LocalDateTime createDateStart = LocalDate.now().atStartOfDay();
        goodsOrderDetailQuery.setCreateDateStart(createDateStart);
        goodsOrderDetailQuery.setCreateDateEnd(createDateStart.plusDays(1));
        goodsOrderDetailQuery.setOfficeIdList(officeClient.getSameAreaByOfficeId(depot.getOfficeId()));

        List<GoodsOrderDetailDto> result = goodsOrderDetailMapper.findListForNewOrEditOrBillWithAreaQty(goodsOrderDetailQuery);

        return result;

    }

    public List<GoodsOrderDetailDto> findDetailListForEdit(String goodsOrderId) {

        GoodsOrder goodsOrder = goodsOrderMapper.findOne(goodsOrderId);
        GoodsOrderDetailQuery goodsOrderDetailQuery = new GoodsOrderDetailQuery();
        goodsOrderDetailQuery.setGoodsOrderId(goodsOrderId);
        goodsOrderDetailQuery.setCompanyId(RequestUtils.getCompanyId());
        goodsOrderDetailQuery.setNetType(goodsOrder.getNetType());
        Depot depot = depotMapper.findOne(goodsOrder.getShopId());
        goodsOrderDetailQuery.setPricesystemId(depot.getPricesystemId());
        goodsOrderDetailQuery.setShowAll(Boolean.FALSE);

        //已下条件用于统计办事处已订货数
        LocalDateTime createDateStart = LocalDate.now().atStartOfDay();
        goodsOrderDetailQuery.setCreateDateStart(createDateStart);
        goodsOrderDetailQuery.setCreateDateEnd(createDateStart.plusDays(1));
        goodsOrderDetailQuery.setOfficeIdList(officeClient.getSameAreaByOfficeId(depot.getOfficeId()));

        List<GoodsOrderDetailDto> result = goodsOrderDetailMapper.findListForNewOrEditOrBillWithAreaQty(goodsOrderDetailQuery);

        return result;
    }


    public List<GoodsOrderDetailDto> findDetailListForBill(String goodsOrderId, String storeId) {

        GoodsOrder goodsOrder = goodsOrderMapper.findOne(goodsOrderId);
        GoodsOrderDetailQuery goodsOrderDetailQuery = new GoodsOrderDetailQuery();
        goodsOrderDetailQuery.setGoodsOrderId(goodsOrderId);
        goodsOrderDetailQuery.setCompanyId(RequestUtils.getCompanyId());
        goodsOrderDetailQuery.setNetType(goodsOrder.getNetType());
        Depot depot = depotMapper.findOne(goodsOrder.getShopId());
        goodsOrderDetailQuery.setPricesystemId(depot.getPricesystemId());
        goodsOrderDetailQuery.setShowAll(Boolean.FALSE);

        //已下条件用于统计办事处已开单数
        LocalDateTime billDateStart = LocalDate.now().atStartOfDay();
        goodsOrderDetailQuery.setBillDateStart(billDateStart);
        goodsOrderDetailQuery.setBillDateEnd(billDateStart.plusDays(1));
        goodsOrderDetailQuery.setOfficeIdList(officeClient.getSameAreaByOfficeId(depot.getOfficeId()));

        List<String> shipTypes= Lists.newArrayList();
        shipTypes.add(ShipTypeEnum.总部发货.name());
        shipTypes.add(ShipTypeEnum.总部自提.name());
        shipTypes.add(ShipTypeEnum.地区发货.name());
        shipTypes.add(ShipTypeEnum.地区自提.name());
        goodsOrderDetailQuery.setShipTypeList(shipTypes);

        List<GoodsOrderDetailDto> result = goodsOrderDetailMapper.findListForNewOrEditOrBillWithAreaQty(goodsOrderDetailQuery);

        return result;

    }

    public GoodsOrderForm getForm(GoodsOrderForm goodsOrderForm) {
        if(goodsOrderForm.isCreate()){
            return findFormForCreate();
        }

        GoodsOrderForm result = new GoodsOrderForm();
        result.setShipTypeList(ShipTypeEnum.getList());
        //TODO 需要修改
        result.setNetTypeList(Arrays.asList(NetTypeEnum.移动.name(), NetTypeEnum.联信.name()));
//TODO 判斷公司類比額
//        if(CompanyNameEnum.JXOPPO.name().equals(RequestUtils.getCompanyId().getCompany().getName()) || Company.CompanyName.JXvivo.name().equals(AccountUtils.getCompany().getName()) ){
//            result.setNetTypeList(Arrays.asList(NetTypeEnum.移动.name(), NetTypeEnum.联信.name()));
//        }else{
//            result.setNetTypeList(Arrays.asList(NetTypeEnum.全网通.name()));
//        }



        //TODO  還不可以修改
        GoodsOrderDto god= goodsOrderMapper.findDto(goodsOrderForm.getId());
        cacheUtils.initCacheInput(god);
        result =  BeanUtil.map(god, GoodsOrderForm.class);


        GoodsOrderDetailQuery godq = new GoodsOrderDetailQuery();
        godq.setCompanyId(RequestUtils.getCompanyId());
        godq.setNetType(result.getNetType());
        Depot d = depotMapper.findOne(result.getShopId());
        godq.setOfficeIdList(officeClient.getSameAreaByOfficeId(d.getOfficeId()));
//        godq.setAreaId(d.getAreaId());
        godq.setGoodsOrderId(result.getId());
        godq.setPricesystemId(d.getPricesystemId());
        //TODO 判斷showall
        Boolean showAll = Boolean.FALSE;
        godq.setShowAll(showAll);
        LocalDateTime dateStart = LocalDate.now().atStartOfDay();
        LocalDateTime dateEnd = dateStart.plusDays(1);
        godq.setCreateDateStart(dateStart);
        godq.setCreateDateEnd(dateEnd);

//        result.setGoodsOrderDetailFormList(goodsOrderDetailService.getListForNewOrUpdateWithAreaQty(godq));

        return result;
    }

    private GoodsOrderForm findFormForCreate() {
        GoodsOrderForm result = new GoodsOrderForm();
        result.setShipTypeList(ShipTypeEnum.getList());
        //TODO 需要修改判斷公司類別
        result.setNetTypeList(Arrays.asList(NetTypeEnum.移动.name(), NetTypeEnum.联信.name()));
//TODO 判斷公司類別
//        if(CompanyNameEnum.JXOPPO.name().equals(RequestUtils.getCompanyId().getCompany().getName()) || Company.CompanyName.JXvivo.name().equals(AccountUtils.getCompany().getName()) ){
//            result.setNetTypeList(Arrays.asList(NetTypeEnum.移动.name(), NetTypeEnum.联信.name()));
//        }else{
//            result.setNetTypeList(Arrays.asList(NetTypeEnum.全网通.name()));
//        }

        return result;

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
    public List<GoodsOrderDetailDto> findDetailDtoList(String goodsOrderId) {
        GoodsOrder goodsOrder = goodsOrderMapper.findOne(goodsOrderId);
        List<GoodsOrderDetail> goodsOrderDetailList  =goodsOrderDetailMapper.findByGoodsOrderId(goodsOrderId);
        Map<String,GoodsOrderDetail> goodsOrderDetailMap = CollectionUtil.extractToMap(goodsOrderDetailList,"productId");
        List<GoodsOrderDetailDto> goodsOrderDetailDtoList = Lists.newArrayList();
        if(CollectionUtil.isNotEmpty(goodsOrderDetailList)) {
            for(GoodsOrderDetail goodsOrderDetail:goodsOrderDetailList) {
                GoodsOrderDetailDto goodsOrderDetailDto  = new GoodsOrderDetailDto();
                goodsOrderDetailDtoList.add(goodsOrderDetailDto);
            }
        }
        List<GoodsOrderDetailDto> list = findDetailDtoList(goodsOrder.getShopId(),goodsOrder.getNetType());
        for(GoodsOrderDetailDto goodsOrderDetailDto:list) {
            if(!goodsOrderDetailMap.containsKey(goodsOrderDetailDto.getProductId())) {
                goodsOrderDetailDtoList.add(goodsOrderDetailDto);
            }
        }
        return goodsOrderDetailDtoList;
    }


    //根据门店和netType查明细单
    public List<GoodsOrderDetailDto> findDetailDtoList(String shopId,String netType) {
        Depot shop = depotMapper.findOne(shopId);
        List<PricesystemDetail> pricesystemDetailList  = pricesystemDetailMapper.findByPricesystemId(shop.getPricesystemId());
        List<GoodsOrderDetailDto> goodsOrderDetailDtoList = Lists.newArrayList();
        for(PricesystemDetail pricesystemDetail:pricesystemDetailList) {
            GoodsOrderDetailDto goodsOrderDetailDto = new GoodsOrderDetailDto();
            goodsOrderDetailDto.setProductId(pricesystemDetail.getProductId());
            goodsOrderDetailDto.setPrice(pricesystemDetail.getPrice());

            goodsOrderDetailDtoList.add(goodsOrderDetailDto);
        }
        return goodsOrderDetailDtoList;
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
        ExpressOrder expressOrder = getExpressOrder(goodsOrderForm, goodsOrder.getExpressOrderId());
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
        ExpressOrder expressOrder = getExpressOrder(goodsOrderBillForm, goodsOrder);
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


    private ExpressOrder getExpressOrder(GoodsOrderForm goodsOrderForm, String expressOrderId) {
        Depot shop = depotMapper.findOne(goodsOrderForm.getShopId());
        ExpressOrder expressOrder = new ExpressOrder();
        if(StringUtils.isNotBlank(expressOrderId)) {
            expressOrder = expressOrderMapper.findOne(expressOrderId);
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

    private ExpressOrder getExpressOrder(GoodsOrderBillForm goodsOrderBillForm, GoodsOrder goodsOrder) {
        Depot shop = depotMapper.findOne(goodsOrder.getShopId());
        ExpressOrder expressOrder = new ExpressOrder();
        if(StringUtils.isNotBlank(goodsOrder.getExpressOrderId())) {
            expressOrder = expressOrderMapper.findOne(goodsOrder.getExpressOrderId());
        }
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

    public GoodsOrderDto findOne(String goodsOrderId) {
        GoodsOrderDto goodsOrderDto = goodsOrderMapper.findDto(goodsOrderId);
        if(goodsOrderDto == null){
            return null;
        }
        CompanyConfigCacheDto goodsOrderRebateRule = CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(),CompanyConfigCodeEnum.GOODS_ORDER_REBATE_RULE.name());
        if (goodsOrderRebateRule != null) {
            goodsOrderDto.setGoodsOrderRebateRuleRemarks( goodsOrderRebateRule.getValue());
        }

//        TODO 金蝶 outCode 和purchaseinfo
//        goodsOrderDto.setPurchaseInfo(sb.toString());
//
//        if(StringUtils.isEmpty(goodsOrder.getOutCode())){
//            String outCode = k3cloudSynService.getOutCode(goodsOrder.getId(), K3CloudSynEntity.ExtendType.货品订货.name());
//            goodsOrderDto.setOutCode(outCode);
//        }

        cacheUtils.initCacheInput(goodsOrderDto);
        return goodsOrderDto;
    }

    public List<GoodsOrderDetailDto> findDtoListByGoodsOrderIdForView(String goodsOrderId) {

        List<GoodsOrderDetailDto> result =  goodsOrderDetailMapper.findDtoListByGoodsOrderId(goodsOrderId);
        cacheUtils.initCacheInput(result);
        return result;
    }


}
