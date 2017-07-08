package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.myspring.cloud.common.enums.ExtendTypeEnum;
import net.myspring.cloud.modules.report.dto.CustomerReceiveDto;
import net.myspring.cloud.modules.report.web.query.CustomerReceiveQuery;
import net.myspring.cloud.modules.sys.dto.KingdeeSynReturnDto;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.exception.ServiceException;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestErrorField;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.ExpressOrderTypeEnum;
import net.myspring.future.common.enums.GoodsOrderStatusEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.domain.Client;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.DepotStore;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.manager.SalReturnStockManager;
import net.myspring.future.modules.basic.manager.StkTransferDirectManager;
import net.myspring.future.modules.basic.repository.ClientRepository;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.DepotStoreRepository;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.crm.domain.*;
import net.myspring.future.modules.crm.dto.GoodsOrderDetailDto;
import net.myspring.future.modules.crm.dto.GoodsOrderDto;
import net.myspring.future.modules.crm.dto.GoodsOrderPrintDto;
import net.myspring.future.modules.crm.dto.ProductPrintDto;
import net.myspring.future.modules.crm.manager.ExpressOrderManager;
import net.myspring.future.modules.crm.repository.*;
import net.myspring.future.modules.crm.web.form.GoodsOrderDetailForm;
import net.myspring.future.modules.crm.web.form.GoodsOrderForm;
import net.myspring.future.modules.crm.web.form.GoodsOrderShipForm;
import net.myspring.future.modules.crm.web.query.GoodsOrderQuery;
import net.myspring.future.modules.crm.web.query.ProductImeShipQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.IdUtils;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class GoodsOrderShipService {
    @Autowired
    private GoodsOrderRepository goodsOrderRepository;
    @Autowired
    private GoodsOrderDetailRepository goodsOrderDetailRepository;
    @Autowired
    private GoodsOrderImeRepository goodsOrderImeRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductImeRepository productImeRepository;
    @Autowired
    private ExpressOrderRepository expressOrderRepository;
    @Autowired
    private ExpressOrderManager expressOrderManager;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private DepotStoreRepository depotStoreRepository;
    @Autowired
    private ExpressRepository expressRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private CloudClient cloudClient;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private StkTransferDirectManager stkTransferDirectManager;
    @Autowired
    private SalReturnStockManager salReturnStockManager;

    public Page<GoodsOrderDto> findAll(Pageable pageable, GoodsOrderQuery goodsOrderQuery) {
        goodsOrderQuery.setStatusList(Arrays.asList("待发货", "待签收", "已完成"));
        if (goodsOrderQuery.getExpressCodes() != null) {
            goodsOrderQuery.setExpresscodeList(Arrays.asList(goodsOrderQuery.getExpressCodes().split("[\n,]")));
        }
        if (goodsOrderQuery.getBusinessIds() != null) {
            goodsOrderQuery.setBusinessIdList(Arrays.asList(goodsOrderQuery.getBusinessIds().split("[\n,]")));
        }
        Page<GoodsOrderDto> page = goodsOrderRepository.findAll(pageable,  goodsOrderQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public Map<String,Object> shipCheck(GoodsOrderShipForm goodsOrderShipForm) {
       RestResponse restResponse =  new RestResponse("valid",ResponseCodeEnum.valid.name());
        Integer totalShouldShipQty = 0;
        Integer totalShippedQty = 0;
        GoodsOrder goodsOrder = goodsOrderRepository.findOne(goodsOrderShipForm.getId());
        List<GoodsOrderDetail> goodsOrderDetailList  = goodsOrderDetailRepository.findByGoodsOrderId(goodsOrder.getId());
        List<GoodsOrderDetailDto> goodsOrderDetailDtoList = BeanUtil.map(goodsOrderDetailList,GoodsOrderDetailDto.class);
        Map<String,GoodsOrderDetailDto> goodsOrderDetailMap  = CollectionUtil.extractToMap(goodsOrderDetailDtoList,"productId");
        Map<String,Product> productMap = productRepository.findMap(CollectionUtil.extractToList(goodsOrderDetailList,"productId"));
        for (GoodsOrderDetailDto goodsOrderDetailDto : goodsOrderDetailDtoList) {
            if (goodsOrderDetailDto.getBillQty() > 0 && productMap.get(goodsOrderDetailDto.getProductId()).getHasIme()) {
                goodsOrderDetailDto.setShipQty(0);
            }
        }
        ProductImeShipQuery productImeShipQuery = new ProductImeShipQuery();
        productImeShipQuery.setDepotId(goodsOrder.getStoreId());
        productImeShipQuery.setImeList(goodsOrderShipForm.getImeList());
        productImeShipQuery.setBoxImeList(goodsOrderShipForm.getBoxImeList());
        List<ProductIme> productImeList = productImeRepository.findShipList(productImeShipQuery);
        Set<String> boxImeSet = Sets.newHashSet();
        Set<String> imeSet = Sets.newHashSet();
        for (ProductIme productIme : productImeList) {
            boxImeSet.add(productIme.getBoxIme());
            imeSet.add(productIme.getIme());
            if (StringUtils.isNotBlank(productIme.getMeid())) {
                imeSet.add(productIme.getMeid());
            }
            if (StringUtils.isNotBlank(productIme.getIme2())) {
                imeSet.add(productIme.getIme2());
            }
            Product product = productMap.get(productIme.getProductId());
            if(product==null || !goodsOrderDetailMap.containsKey(product.getId())){
                restResponse.getErrors().add(new RestErrorField("箱号：" + productIme.getBoxIme() +"，串码：" + productIme.getIme() + "的货品不在订货范围内","ime_error","imeStr"));
            } else {
                goodsOrderDetailMap.get(product.getId()).setShipQty(goodsOrderDetailMap.get(product.getId()).getShipQty() + 1);
            }
        }
        for (String boxIme : goodsOrderShipForm.getBoxImeList()) {
            if (!boxImeSet.contains(boxIme)) {
                restResponse.getErrors().add(new RestErrorField("箱号：" + boxIme  + "，在仓库不存在","box_ime_error","boxIme"));
            }
        }
        for (String ime : goodsOrderShipForm.getImeList()) {
            if (!imeSet.contains(ime)) {
                restResponse.getErrors().add(new RestErrorField("串码：" + ime + "，在仓库不存在","ime_error","ime"));
            }
        }
        for (GoodsOrderDetailDto goodsOrderDetailDto: goodsOrderDetailDtoList) {
            //仅比对包含串码的产品的发货数量，不包含串码的产品，默认全部发货
            if(productMap.get(goodsOrderDetailDto.getProductId()).getHasIme()){
                totalShouldShipQty = totalShouldShipQty +goodsOrderDetailDto.getRealBillQty();
                totalShippedQty = totalShippedQty + goodsOrderDetailDto.getShippedQty() + goodsOrderDetailDto.getShipQty();
            }

            Integer qty = goodsOrderDetailDto.getShippedQty() + goodsOrderDetailDto.getShipQty();
            Integer realBillQty=goodsOrderDetailDto.getRealBillQty();
            if (qty > realBillQty) {
                StringBuilder errorMessage = new StringBuilder("货品:"+ productMap.get(goodsOrderDetailDto.getProductId()).getName() +"总发货数："+ qty+ "大于实际开单数："  + realBillQty + "串码：");
                //显示发货不对的串码
                for (ProductIme productIme : productImeList) {
                    if (productIme.getProductId().equals(goodsOrderDetailDto.getProductId())) {
                        errorMessage.append(productIme.getIme()).append(CharConstant.TAB);
                    }
                }
                restResponse.getErrors().add(new RestErrorField(errorMessage.toString(),"qty_error","ime"));
            }
        }
        Map<String, Object> result = Maps.newHashMap();
        if (!totalShouldShipQty.equals(totalShippedQty)) {
            result.put("warnMsg", "货品总开单数"+totalShouldShipQty+ ",和实际发货数"+totalShippedQty + "不一致");
        }
        Map<String, Integer> shipQtyMap = Maps.newHashMap();
        for (GoodsOrderDetailDto goodsOrderDetailDto : goodsOrderDetailMap.values()) {
            shipQtyMap.put(goodsOrderDetailDto.getProductId(), goodsOrderDetailDto.getShipQty());
        }
        result.put("restResponse", restResponse);
        result.put("shipQtyMap", shipQtyMap);
        result.put("totalQty", productImeList.size());
        return result;
    }

    @Transactional
    public GoodsOrderPrintDto print(String goodsOrderId) {
        GoodsOrder goodsOrder = goodsOrderRepository.findOne(goodsOrderId);
        ExpressOrder expressOrder = expressOrderRepository.findOne(goodsOrder.getExpressOrderId());
        if (expressOrder != null) {
            if (expressOrder.getOutPrintDate() == null) {
                expressOrder.setOutPrintDate(LocalDateTime.now());
            }
            expressOrderRepository.save(expressOrder);
        }
        if(StringUtils.isEmpty(goodsOrder.getOutCode())){
            String outCode = stkTransferDirectManager.getOutCode(goodsOrder.getId(), ExtendTypeEnum.货品订货.name());
            goodsOrder.setOutCode(outCode);
            goodsOrderRepository.save(goodsOrder);
        }
        List<String> depotIdList=Lists.newArrayList(goodsOrder.getShopId(),goodsOrder.getStoreId());
        Map<String,Depot> depotMap=depotRepository.findMap(depotIdList);
        GoodsOrderPrintDto goodsOrderPrintDto=BeanUtil.map(goodsOrder,GoodsOrderPrintDto.class);
        Depot shop=depotMap.get(goodsOrder.getShopId());
        goodsOrderPrintDto.setStoreName(depotMap.get(goodsOrder.getStoreId()).getName());
        goodsOrderPrintDto.setShopName(shop.getName());
        goodsOrderPrintDto.setContator(shop.getContator());
        goodsOrderPrintDto.setAddress(shop.getAddress());
        goodsOrderPrintDto.setMobilePhone(shop.getMobilePhone());
        if(StringUtils.isNotBlank(shop.getClientId())){
            Client shopClient = clientRepository.findOne(shop.getClientId());
            goodsOrderPrintDto.setShopClientName(shopClient.getName());
        }

        goodsOrderPrintDto.setExpressOrderCode(expressOrder.getOutCode());
        goodsOrderPrintDto.setRemarks(goodsOrder.getRemarks());
        goodsOrderPrintDto.setLxMallOrder(goodsOrder.getLxMallOrder());
        List<GoodsOrderDetail> goodsOrderDetailList=goodsOrderDetailRepository.findByGoodsOrderId(goodsOrderId);
        List<String> productIdList=CollectionUtil.extractToList(goodsOrderDetailList,"productId");
        Map<String,Product> productMap=productRepository.findMap(productIdList);
        List<ProductPrintDto> productPrintDtoList=Lists.newArrayList();
        Integer totalQty=0;
        BigDecimal total=BigDecimal.ZERO;
        for(GoodsOrderDetail goodsOrderDetail:goodsOrderDetailList){
            if(goodsOrderDetail.getRealBillQty()>0){
                ProductPrintDto productPrintDto=new ProductPrintDto();
                productPrintDto.setQty(goodsOrderDetail.getRealBillQty());
                productPrintDto.setUnit("套");
                totalQty+=productPrintDto.getQty();
                Product product=productMap.get(goodsOrderDetail.getProductId());
                productPrintDto.setProductName(product.getName());
                if(shop.getPrintPrice()!=null&&shop.getPrintPrice()){
                    productPrintDto.setPrice(goodsOrderDetail.getPrice());
                    productPrintDto.setTotal(goodsOrderDetail.getPrice().multiply(new BigDecimal(productPrintDto.getQty())));
                    total=total.add(productPrintDto.getTotal());
                }
                productPrintDtoList.add(productPrintDto);
            }
        }
        ProductPrintDto productPrintDto=new ProductPrintDto();
        productPrintDto.setProductName("合计");
        productPrintDto.setQty(totalQty);
        productPrintDto.setTotal(total.compareTo(BigDecimal.ZERO)<=0?null:total);
        productPrintDtoList.add(productPrintDto);
        goodsOrderPrintDto.setProductList(productPrintDtoList);
        return goodsOrderPrintDto;
    }

    @Transactional
    public GoodsOrderPrintDto shipPrint(String goodsOrderId) {
        GoodsOrder goodsOrder = goodsOrderRepository.findOne(goodsOrderId);
        ExpressOrder expressOrder = expressOrderRepository.findOne(goodsOrder.getExpressOrderId());
        if (expressOrder != null) {
            if (expressOrder.getExpressPrintDate() == null) {
                expressOrder.setExpressPrintDate(LocalDateTime.now());
            }
            expressOrderRepository.save(expressOrder);
        }
        GoodsOrderPrintDto goodsOrderPrintDto=new GoodsOrderPrintDto();
        Depot depot=depotRepository.findOne(goodsOrder.getShopId());
        goodsOrderPrintDto.setContator(depot.getContator());
        goodsOrderPrintDto.setAddress(depot.getAddress());
        goodsOrderPrintDto.setShopName(depot.getName());
        goodsOrderPrintDto.setMobilePhone(depot.getMobilePhone());
        return goodsOrderPrintDto;
    }


    @Transactional
    public void ship(GoodsOrderShipForm goodsOrderShipForm) {
        if(StringUtils.isBlank(goodsOrderShipForm.getId())){
            throw new ServiceException("发货的订单id不能为空");
        }
        GoodsOrder goodsOrder = goodsOrderRepository.findOne(goodsOrderShipForm.getId());
        if(!GoodsOrderStatusEnum.待发货.name().equals(goodsOrder.getStatus())){
            throw new ServiceException("该订单的状态不为待发货，不能发货");
        }
        Map<String, GoodsOrderDetail> goodsOrderDetailMap = Maps.newHashMap();
        List<GoodsOrderDetail> goodsOrderDetailList = goodsOrderDetailRepository.findByGoodsOrderId(goodsOrderShipForm.getId());
        Map<String,Product> productMap = productRepository.findMap(CollectionUtil.extractToList(goodsOrderDetailList,"productId"));
        for (GoodsOrderDetail goodsOrderDetail : goodsOrderDetailList) {
            if (goodsOrderDetail.getRealBillQty() > 0 && !goodsOrderDetail.getRealBillQty().equals( goodsOrderDetail.getShippedQty())) {
                goodsOrderDetailMap.put(goodsOrderDetail.getProductId(), goodsOrderDetail);
            }
            //对没有串码的货品全部发货
            Product product = productMap.get(goodsOrderDetail.getProductId());
            if (!product.getHasIme() && goodsOrderDetail.getShippedQty() == 0) {
                goodsOrderDetail.setShippedQty(goodsOrderDetail.getRealBillQty());
                goodsOrderDetailRepository.save(goodsOrderDetail);
            }
        }
        //搜索串码
        if (StringUtils.isNotBlank(goodsOrderShipForm.getBoxImeStr()) || StringUtils.isNotBlank(goodsOrderShipForm.getImeStr())) {
            ProductImeShipQuery productImeShipQuery = new ProductImeShipQuery();
            productImeShipQuery.setBoxImeList(goodsOrderShipForm.getBoxImeList());
            productImeShipQuery.setImeList(goodsOrderShipForm.getImeList());
            productImeShipQuery.setDepotId(goodsOrder.getStoreId());

            List<ProductIme> productImeList = productImeRepository.findShipList(productImeShipQuery);
            if (CollectionUtil.isNotEmpty(productImeList)) {
                for (ProductIme productIme : productImeList) {
                    String productId = productIme.getProductId();
                    GoodsOrderDetail goodsOrderDetail = goodsOrderDetailMap.get(productId);
                    if (goodsOrderDetail != null) {
                        goodsOrderDetail.setShippedQty(goodsOrderDetail.getShippedQty() == null ? 0 : goodsOrderDetail.getShippedQty() + 1);
                        GoodsOrderIme goodsOrderIme = new GoodsOrderIme();
                        goodsOrderIme.setGoodsOrderId(goodsOrder.getId());
                        goodsOrderIme.setProductImeId(productIme.getId());
                        goodsOrderIme.setProductId(productIme.getProductId());
                        goodsOrderIme.setRemarks(goodsOrderShipForm.getShipRemarks());
                        goodsOrderImeRepository.save(goodsOrderIme);
                        //串码调拨
                        productIme.setDepotId(goodsOrder.getShopId());
                        productIme.setRetailShopId(goodsOrder.getShopId());
                        productImeRepository.save(productIme);
                    }
                }
            }
        }
        goodsOrderDetailRepository.save(goodsOrderDetailMap.values());

        //如果所有发货完成，修改订单状态
        boolean isAllShipped = true;
        for (GoodsOrderDetail goodsOrderDetail : goodsOrderDetailMap.values()) {
            Integer realBillQty = goodsOrderDetail.getRealBillQty();
            if (!goodsOrderDetail.getShippedQty().equals(realBillQty)) {
                isAllShipped = false;
                break;
            }
        }
        if (isAllShipped) {
            goodsOrder.setStatus(GoodsOrderStatusEnum.待签收.name());
        } else {
            goodsOrder.setStatus(GoodsOrderStatusEnum.待发货.name());
        }
        goodsOrder.setShipDate(LocalDateTime.now());
        goodsOrderRepository.save(goodsOrder);
        //设置快递单
        ExpressOrder expressOrder = expressOrderRepository.findOne(goodsOrder.getExpressOrderId());
        expressOrderManager.save(ExpressOrderTypeEnum.手机订单.name(),goodsOrder.getId(),goodsOrderShipForm.getExpressCodes(),expressOrder.getExpressCompanyId());
    }

    @Transactional
    public void sreturn(GoodsOrderForm goodsOrderForm) {
        GoodsOrder goodsOrder = goodsOrderRepository.findOne(goodsOrderForm.getId());
        if(StringUtils.isEmpty(goodsOrder.getOutCode())){
            String outCode = stkTransferDirectManager.getOutCode(goodsOrder.getId(), ExtendTypeEnum.货品订货.name());
            goodsOrder.setOutCode(outCode);
        }
        Map<String,GoodsOrderDetail> goodsOrderDetailMap = goodsOrderDetailRepository.findMap(CollectionUtil.extractToList(goodsOrderForm.getGoodsOrderDetailFormList(),"id"));
        BigDecimal amount = BigDecimal.ZERO;
        for (GoodsOrderDetailForm goodsOrderDetailForm:goodsOrderForm.getGoodsOrderDetailFormList()) {
            GoodsOrderDetail goodsOrderDetail = goodsOrderDetailMap.get(goodsOrderDetailForm.getId());

            if (goodsOrderDetailForm.getReturnQty() != null && goodsOrderDetailForm.getReturnQty() < 0) {
                throw new ServiceException("退货数不能小于0");
            }

            if (goodsOrderDetailForm.getReturnQty() != null && goodsOrderDetailForm.getReturnQty() >=0) {
                goodsOrderDetail.setReturnQty(goodsOrderDetailForm.getReturnQty());
                goodsOrderDetailRepository.save(goodsOrderDetail);
            }
            amount  = amount.add(new BigDecimal( goodsOrderDetail.getRealBillQty()).multiply(goodsOrderDetail.getPrice()));
        }
        goodsOrder.setAmount(amount);
        goodsOrderRepository.save(goodsOrder);
        if(goodsOrderForm.getSyn()){
            Depot depot=depotRepository.findOne(goodsOrder.getShopId());

            if (StringUtils.isNotBlank(depot.getDelegateDepotId())) {
                DepotStore allotFromStock = depotStoreRepository.findByEnabledIsTrueAndDepotId(depot.getDelegateDepotId());
                DepotStore allotToStock = depotStoreRepository.findByEnabledIsTrueAndDepotId(goodsOrder.getStoreId());

                KingdeeSynReturnDto kingdeeSynReturnDto = stkTransferDirectManager.synForGoodsOrderReturn(goodsOrder,allotFromStock.getOutCode(),allotToStock.getOutCode());
                goodsOrder.setOutCode(StringUtils.appendString(goodsOrder.getOutCode(),kingdeeSynReturnDto.getBillNo(),CharConstant.COMMA));
                goodsOrderRepository.save(goodsOrder);
            } else {
                List<KingdeeSynReturnDto> kingdeeSynReturnDtos = salReturnStockManager.synForGoodsOrderShip(goodsOrder);
                if(CollectionUtil.isNotEmpty(kingdeeSynReturnDtos)){
                    goodsOrder.setOutCode(StringUtils.appendString(goodsOrder.getOutCode(),kingdeeSynReturnDtos.get(0).getBillNo(),CharConstant.COMMA));
                    goodsOrderRepository.save(goodsOrder);
                }
            }

        }

    }

    @Transactional
    public void sign(String goodsOrderId) {
        GoodsOrder goodsOrder = goodsOrderRepository.findOne(goodsOrderId);
        if(goodsOrder!=null&&GoodsOrderStatusEnum.待签收.name().equals(goodsOrder.getStatus())){
            goodsOrder.setStatus(GoodsOrderStatusEnum.已完成.name());
            goodsOrderRepository.save(goodsOrder);
        }
    }

    @Transactional
    public void shipBack(String goodsOrderId) {
        GoodsOrder goodsOrder = goodsOrderRepository.findOne(goodsOrderId);
        List<GoodsOrderIme> goodsOrderImeList = goodsOrderImeRepository.findByEnabledIsTrueAndGoodsOrderId(goodsOrderId);
        List<GoodsOrderDetail> goodsOrderDetailList  = goodsOrderDetailRepository.findByGoodsOrderId(goodsOrderId);
        Map<String,ProductIme> productImeMap = productImeRepository.findMap(CollectionUtil.extractToList(goodsOrderImeList,"productImeId"));
        //串码调拨
        if (CollectionUtil.isNotEmpty(goodsOrderImeList)) {

            for (GoodsOrderIme goodsOrderIme : goodsOrderImeList) {
                ProductIme productIme = productImeMap.get(goodsOrderIme.getProductImeId());
                productIme.setDepotId(goodsOrder.getStoreId());
                productIme.setRetailShopId(goodsOrder.getStoreId());
                productImeRepository.save(productIme);
            }
        }
        goodsOrderImeRepository.delete(goodsOrderImeList);
        for (GoodsOrderDetail goodsOrderDetail : goodsOrderDetailList) {
            goodsOrderDetail.setShippedQty(0);
        }
        goodsOrder.setStatus(GoodsOrderStatusEnum.待发货.name());
        goodsOrder.setShipDate(null);
        //删除快递单
        expressRepository.deleteByExpressOrderId(goodsOrder.getExpressOrderId());
        goodsOrderRepository.save(goodsOrder);
    }

    public GoodsOrderDto getShip(String id) {
        GoodsOrder goodsOrder = null;
        GoodsOrderDto goodsOrderDto = null;
        if(StringUtils.isNotBlank(id)) {
            id = IdUtils.getId(id);
            if(StringUtils.isNotBlank(id)) {
                goodsOrder = goodsOrderRepository.findOne(id);
            }
        }
        if(goodsOrder != null) {
            goodsOrderDto = BeanUtil.map(goodsOrder,GoodsOrderDto.class);
            Depot shop=depotRepository.findOne(goodsOrder.getShopId());
            goodsOrderDto.setShopAreaId(shop.getAreaId());
            if(StringUtils.isNotBlank(goodsOrder.getExpressOrderId())){
                ExpressOrder expressOrder = expressOrderRepository.findOne(goodsOrder.getExpressOrderId());
                goodsOrderDto.setExpressOrderExpressCodes(expressOrder.getExpressCodes());
            }
            cacheUtils.initCacheInput(goodsOrderDto);
            List<GoodsOrderDetail> goodsOrderDetailList = goodsOrderDetailRepository.findByGoodsOrderId(id);
            List<GoodsOrderDetailDto> goodsOrderDetailDtoList = BeanUtil.map(goodsOrderDetailList,GoodsOrderDetailDto.class);
            cacheUtils.initCacheInput(goodsOrderDetailDtoList);

            Map<String,Product> productMap = productRepository.findMap(CollectionUtil.extractToList(goodsOrderDetailDtoList,"productId"));
            for(GoodsOrderDetailDto goodsOrderDetailDto:goodsOrderDetailDtoList) {
                Product product = productMap.get(goodsOrderDetailDto.getProductId());
                goodsOrderDetailDto.setHasIme(product.getHasIme());
            }
            goodsOrderDto.setGoodsOrderDetailDtoList(goodsOrderDetailDtoList);
        }
        return goodsOrderDto;
    }

    public GoodsOrderDto getSreturn(String id){
        GoodsOrderDto goodsOrderDto=getShip(id);
        Depot depot=depotRepository.findOne(goodsOrderDto.getShopId());
        goodsOrderDto.setShopCredit(depot.getCredit());
        if(StringUtils.isNotBlank(depot.getClientId())){
            Client client=clientRepository.findOne(depot.getClientId());
            CustomerReceiveQuery customerReceiveQuery=new CustomerReceiveQuery(LocalDate.now(),LocalDate.now(),client.getOutId());
            List<CustomerReceiveDto> customerReceiveList = cloudClient.getCustomerReceiveList(customerReceiveQuery);
            goodsOrderDto.setShopShouldGet(customerReceiveList.get(0).getEndShouldGet());
        }
        return goodsOrderDto;
    }

    public GoodsOrderDto getShipByBusinessId(String businessId) {

        List<GoodsOrder> goodsOrderList =  goodsOrderRepository.findByBusinessIdIn(Collections.singletonList(businessId));
        if(CollectionUtil.isEmpty(goodsOrderList)){
            return new GoodsOrderDto();
        }
        return getShip(goodsOrderList.get(0).getId());
    }
}
