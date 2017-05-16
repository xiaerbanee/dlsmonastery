package net.myspring.future.modules.crm.service;

import com.google.common.collect.Maps;
import net.myspring.future.common.enums.ExpressCompanyTypeEnum;
import net.myspring.future.common.enums.GoodsOrderStatusEnum;
import net.myspring.future.common.enums.NetTypeEnum;
import net.myspring.future.common.enums.ShipTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.mapper.*;
import net.myspring.future.modules.basic.service.DepotService;
import net.myspring.future.modules.basic.service.ExpressCompanyService;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.future.modules.crm.domain.GoodsOrderDetail;
import net.myspring.future.modules.crm.dto.ExpressOrderDto;
import net.myspring.future.modules.crm.dto.GoodsOrderDto;
import net.myspring.future.modules.crm.mapper.*;
import net.myspring.future.modules.crm.web.form.GoodsOrderBillForm;
import net.myspring.future.modules.crm.web.form.GoodsOrderDetailForm;
import net.myspring.future.modules.crm.web.form.GoodsOrderForm;
import net.myspring.future.modules.crm.web.query.GoodsOrderDetailQuery;
import net.myspring.future.modules.crm.web.query.GoodsOrderQuery;
import net.myspring.future.modules.layout.mapper.ShopGoodsDepositMapper;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private ProductMapper productMapper;
    @Autowired
    private ProductImeMapper productImeMapper;
    @Autowired
    private GoodsOrderImeMapper goodsOrderImeMapper;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private PricesystemDetailMapper pricesystemDetailMapper;
    @Autowired
    private ExpressOrderMapper expressOrderMapper;
    @Autowired
    private ExpressCompanyMapper expressCompanyMapper;
    @Autowired
    private ExpressCompanyService expressCompanyService;
    @Autowired
    private ExpressMapper expressMapper;
    @Autowired
    private ShopGoodsDepositMapper shopGoodsDepositMapper;
    @Autowired
    private BankMapper bankMapper;
    @Autowired
    private DepotService depotService;

    @Autowired

    private CacheUtils cacheUtils;
    @Autowired
    private GoodsOrderDetailService goodsOrderDetailService;

    @Autowired
    private ExpressOrderService expressOrderService;


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

    public  List<Depot> findStores(GoodsOrder goodsOrder){
        return null;
    }

    public GoodsOrder findOne(String id) {
        GoodsOrder goodsOrder = goodsOrderMapper.findOne(id);
        return goodsOrder;
    }

    public void sign(GoodsOrder goodsOrder) {
        goodsOrderMapper.update(goodsOrder);
    }

    public GoodsOrder shipBoxAndIme(GoodsOrder goodsOrder) {
        return goodsOrder;
    }

    public void print(GoodsOrder goodsOrder) {
//        ExpressOrder expressOrder = goodsOrder.getExpressOrder();
//        if (expressOrder != null) {
//            if (expressOrder.getOutPrintDate() == null) {
//                expressOrder.setOutPrintDate(LocalDateTime.now());
//            }
//            expressOrderMapper.update(expressOrder);
//        }
    }

    public void shipPrint(GoodsOrder goodsOrder) {
//        ExpressOrder expressOrder = goodsOrder.getExpressOrder();
//        if (expressOrder != null) {
//            if (expressOrder.getExpressPrintDate() == null) {
//                expressOrder.setExpressPrintDate(LocalDateTime.now());
//            }
//            expressOrderMapper.update(expressOrder);
//        }
    }


    public void shipBack(GoodsOrder goodsOrder) {
    }

    public void delete(GoodsOrder goodsOrder) {
    }

    public GoodsOrder save(GoodsOrder goodsOrder) {
        return null;
    }


    public GoodsOrder bill(GoodsOrder goodsOrder) throws Exception{
        return null;
    }

    private GoodsOrder syn(GoodsOrder goodsOrder) throws Exception{
        return goodsOrder;
    }

    public void sreturn(GoodsOrder goodsOrder) {
        goodsOrderMapper.update(goodsOrder);
    }

    public GoodsOrder getBillChange(GoodsOrder goodsOrder) {
        return goodsOrder;
    }

    public GoodsOrder getBillGoodsOrder(GoodsOrder goodsOrder) {
        return goodsOrder;
    }

    public GoodsOrder findGoodsOrderDetail(GoodsOrder goodsOrder) {
        return goodsOrder;
    }

    public Map<String, Object> findBtnCopy(GoodsOrder goodsOrder) {
        Map<String, Object> paramMap = Maps.newHashMap();
        return paramMap;
    }

    public void ship(GoodsOrder goodsOrder) {
        goodsOrderMapper.update(goodsOrder);
        //设置快递单
    }


    public GoodsOrder findByFormatId(String formatId) {
        return null;
    }

    private Map<String, Integer> getMap(List<GoodsOrderDetail> orderList, String columnName) {
        return null;
    }


    public List<GoodsOrder> findByStoreBillData(LocalDate billDate, String storeId, String status){
        return goodsOrderMapper.findByStoreBillData(billDate, storeId, status);
    }

    public GoodsOrderForm findForm(GoodsOrderForm goodsOrderForm) {
        if(goodsOrderForm.isCreate()){
            return findFormForCreate(goodsOrderForm);
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
        godq.setAreaId(d.getAreaId());
        godq.setGoodsOrderId(result.getId());
        godq.setPricesystemId(d.getPricesystemId());
        //TODO 判斷showall
        Boolean showAll = Boolean.FALSE;
        godq.setShowAll(showAll);
        LocalDateTime dateStart = LocalDate.now().atStartOfDay();
        LocalDateTime dateEnd = dateStart.plusDays(1);
        godq.setCreateDateStart(dateStart);
        godq.setCreateDateEnd(dateEnd);

        result.setDetailFormList(goodsOrderDetailService.getListForNewOrUpdateWithAreaQty(godq));

        return result;
    }

    private GoodsOrderForm findFormForCreate(GoodsOrderForm goodsOrderForm) {
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

            result.setShopId(goodsOrderForm.getShopId());
            result.setNetType(goodsOrderForm.getNetType());
            result.setShipType(goodsOrderForm.getShipType());

//            result.setShopType(); TODO 填写shoptype
            if(goodsOrderForm.getShopId()!=null){
                Depot d = depotMapper.findOne(goodsOrderForm.getShopId());
                result.setClientId(d.getClientId());
                result.setPriceSystemId(d.getPricesystemId());
            }
            if(StringUtils.isNotBlank(goodsOrderForm.getShopId())&&StringUtils.isNotBlank(goodsOrderForm.getNetType())&&StringUtils.isNotBlank(goodsOrderForm.getShipType())){

                GoodsOrderDetailQuery godq = new GoodsOrderDetailQuery();
                godq.setCompanyId(RequestUtils.getCompanyId());
                godq.setNetType(result.getNetType());
                Depot d = depotMapper.findOne(goodsOrderForm.getShopId());
                godq.setAreaId(d.getAreaId());
                godq.setPricesystemId(d.getPricesystemId());
                //TODO 判斷showall
                Boolean showAll = Boolean.FALSE;
                godq.setShowAll(showAll);
                LocalDateTime dateStart = LocalDate.now().atStartOfDay();
                LocalDateTime dateEnd = dateStart.plusDays(1);
                godq.setCreateDateStart(dateStart);
                godq.setCreateDateEnd(dateEnd);

                result.setDetailFormList(goodsOrderDetailService.getListForNewOrUpdateWithAreaQty(godq));
            }
            return result;

    }

    public GoodsOrder save(GoodsOrderForm goodsOrderForm) {

//        Long defaultCarrierStoreId=null;
//        List<Long> carrierLockOffices= Lists.newArrayList();

        GoodsOrder goodsOrderToBeSaved = null;
        if(!goodsOrderForm.isCreate()){
            goodsOrderToBeSaved = goodsOrderMapper.findOne(goodsOrderForm.getId());
        }else{
            goodsOrderToBeSaved = new GoodsOrder();
        }
        ReflectionUtil.copyProperties(goodsOrderForm, goodsOrderToBeSaved);

        if(goodsOrderForm.isCreate()) {
            String defaultStoreId =null;
            //TODO 獲取defaultStoreId
            if(NetTypeEnum.联信.name().equals(goodsOrderForm.getNetType())){
//                defaultStoreId = Long.valueOf(Global.getCompanyConfig(AccountUtils.getCompany().getId(), CompanyConfigCode.LX_DEFAULT_STORE_ID.getCode()));
            }else {
//                defaultStoreId = Long.valueOf(Global.getCompanyConfig(AccountUtils.getCompany().getId(), CompanyConfigCode.DEFAULT_STORE_ID.getCode()));
            }
//            String defaultCarrierStoreStr=Global.getCompanyConfig(AccountUtils.getCompany().getId(), CompanyConfigCode.DEFALULT_CARRIAR_STORE_ID.getCode());
//            if(StringUtils.isNotBlank(defaultCarrierStoreStr)){
//                defaultCarrierStoreId=Long.valueOf(defaultCarrierStoreStr);
//            }
//            String carrierLockOfficeStrs=Global.getCompanyConfig(AccountUtils.getCompany().getId(), CompanyConfigCode.CARRIER_LOCK_OFFICE.getCode());
//            if(StringUtils.isNotBlank(carrierLockOfficeStrs)){
//                carrierLockOffices = IdUtils.getIdList(carrierLockOfficeStrs);
//            }
//            Depot shop=depotMapper.findOne(goodsOrderForm.getShopId());
//            goodsOrder.setShop(shop);
//            if(defaultCarrierStoreId!=null&&Collections3.isNotEmpty(carrierLockOffices)&&StringUtils.isNotBlank(shop.getAreaType())&& !Const.DEPOT_AREA_TYPE_TOWN.equals(shop.getAreaType())
//                    && (carrierLockOffices.contains(shop.getOfficeId())||carrierLockOffices.contains(shop.getOffice().getArea().getId()))&&StringUtils.isNotBlank(goodsOrder.getCarrierDetails())){
//                goodsOrder.setStore(depotDao.findOne(defaultCarrierStoreId));
//            }else{
            goodsOrderToBeSaved.setStoreId(defaultStoreId);
//            }
            goodsOrderToBeSaved.setStatus(GoodsOrderStatusEnum.待开单.name());
        }

        List<GoodsOrderDetail> detailsToBeSaved = getDetailsToBeSaved(goodsOrderForm, goodsOrderToBeSaved.getStatus());
        goodsOrderToBeSaved.setAmount(getAmount(detailsToBeSaved));
        saveOrUpdate(goodsOrderToBeSaved);
        goodsOrderDetailService.batchSave(goodsOrderToBeSaved.getId(), detailsToBeSaved);

        ExpressOrder expressOrder = expressOrderService.saveOrUpdateExpressOrder(goodsOrderToBeSaved);

        goodsOrderToBeSaved.setExpressOrderId(expressOrder.getId());
        saveOrUpdate(goodsOrderToBeSaved);


        return goodsOrderToBeSaved;
    }

    private List<GoodsOrderDetail> getDetailsToBeSaved(GoodsOrderForm goodsOrderForm, String status) {
        List<GoodsOrderDetail> detailsToBeSaved = new ArrayList<>();

        for (int i = goodsOrderForm.getDetailFormList().size() - 1; i >= 0; i--) {
            GoodsOrderDetailForm goodsOrderDetailForm = goodsOrderForm.getDetailFormList().get(i);

            if (GoodsOrderStatusEnum.待开单.name().equals(status) && (goodsOrderDetailForm.getQty() == null || goodsOrderDetailForm.getQty() <= 0)) {
                continue;
            }
            if ((goodsOrderDetailForm.getQty() == null || goodsOrderDetailForm.getQty() <= 0) && (goodsOrderDetailForm.getBillQty() == null || goodsOrderDetailForm.getBillQty() <= 0)) {
                continue;
            }
            GoodsOrderDetail detail = new GoodsOrderDetail();
            ReflectionUtil.copyProperties(goodsOrderDetailForm, detail);
            if(GoodsOrderStatusEnum.待开单.name().equals(status)){
                detail.setBillQty(detail.getQty());
            }
            detailsToBeSaved.add(detail);
        }
        return detailsToBeSaved;
    }

    private BigDecimal getAmount(List<GoodsOrderDetail> detailsToBeSaved) {
        if(detailsToBeSaved == null){
            return BigDecimal.ZERO;
        }
        return detailsToBeSaved.stream().filter(each -> (each.getBillQty() != null && each.getBillQty() > 0 && each.getPrice() != null)).map(each -> new BigDecimal(each.getBillQty()).multiply(each.getPrice())).reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    private GoodsOrder saveOrUpdate(GoodsOrder goodsOrder) {
        if(goodsOrder == null){
            return null;
        }
        if(StringUtils.isNotBlank(goodsOrder.getId())){
            goodsOrderMapper.update(goodsOrder);
        }else{
            goodsOrderMapper.save(goodsOrder);
        }
        return goodsOrder;
    }

    public GoodsOrderBillForm getBillForm(GoodsOrderBillForm goodsOrderBillForm) {
        GoodsOrderBillForm result = new GoodsOrderBillForm();
        GoodsOrderDto god = goodsOrderMapper.findDto(goodsOrderBillForm.getId());
        DepotDto shopDto = depotService.findById(god.getShopId());
        DepotDto storeDto = null;
        if(god.getStoreId()!=null){
            storeDto = depotService.findById(god.getStoreId());
        }
        result.setGoodsOrderDto(god);
        result.setShopDto(shopDto);
        result.setStoreDto(storeDto);

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
        result.setSameDate(Boolean.TRUE);
        result.setBillDate(LocalDateTime.now());

        result.setExpressCompanyList(expressCompanyService.findDtoListByCompanyIdAndExpressType(RequestUtils.getCompanyId(), ExpressCompanyTypeEnum.手机订单.name()));
        result.setStoreList(depotService.findStoreList(god.getShipType()));
        result.setDetailFormList(goodsOrderDetailService.getListForBillWithTodaysAreaBillQty(god.getId(), shopDto.getPricesystemId(), god.getNetType(), shopDto.getAreaId()));

        ExpressOrderDto eod = expressOrderMapper.findDto(god.getExpressOrderId());
        if(eod==null){
            eod = new ExpressOrderDto();
            eod.setContator(shopDto.getContator());
            eod.setMobilePhone(shopDto.getMobilePhone());
            eod.setAddress(shopDto.getAddress());
        }
        if (eod.getExpressCompanyId() == null) {
            eod.setExpressCompanyId(expressCompanyService.getDefaultExpressCompanyId());
        }
        result.setExpressOrderDto(eod);

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
result.setTakeExpress(Boolean.TRUE);


        if (result.getTakeExpress()) {
            // TODO 設置運費計算規則
//            result.expressProductId
//            model.addAttribute("expressProductId", Global.getCompanyConfig(AccountUtils.getCompany().getId(),CompanyConfigCode.EXPRESS_PRODUCT_ID.getCode()));
//            model.addAttribute("expressRule", Global.getCompanyConfig(AccountUtils.getCompany().getId(),CompanyConfigCode.EXPRESS_SHOULD_GET_RULE.getCode()));
        } else {
//            model.addAttribute("expressProductId", "");
//            model.addAttribute("expressRule", "{}");
        }

        // 办事处已开单数
       //TODO 設置rebateRULe
//        CompanyConfig goodsOrderRebateRule = companyConfigService.findByCode(AccountUtils.getCompany().getId(),CompanyConfigCode.GOODS_ORDER_REBATE_RULE.name());
//        if (goodsOrderRebateRule != null) {
//            result.set
//            model.addAttribute("goodsOrderRebateRule", goodsOrderRebateRule);
//        }

      return result;
    }
}
