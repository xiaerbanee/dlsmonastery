package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.future.common.enums.ExpressOrderTypeEnum;
import net.myspring.future.common.enums.ShipTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.basic.web.form.PrintConfigForm;
import net.myspring.future.modules.basic.web.query.PrintConfigQuery;
import net.myspring.future.modules.crm.domain.*;
import net.myspring.future.modules.crm.dto.ExpressOrderPrintDetailDto;
import net.myspring.future.modules.crm.dto.ExpressOrderPrintDto;
import net.myspring.future.modules.crm.repository.*;
import net.myspring.future.modules.layout.domain.AdGoodsOrder;
import net.myspring.future.modules.layout.domain.AdGoodsOrderDetail;
import net.myspring.future.modules.layout.repository.AdGoodsOrderDetailRepository;
import net.myspring.future.modules.layout.repository.AdGoodsOrderRepository;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.IdUtils;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class PrintService {
    @Value("${app.print.version}")
    private String version;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private ExpressOrderRepository expressOrderRepository;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private GoodsOrderDetailRepository goodsOrderDetailRepository;
    @Autowired
    private GoodsOrderRepository goodsOrderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AdGoodsOrderRepository adGoodsOrderRepository;
    @Autowired
    private AdGoodsOrderDetailRepository adGoodsOrderDetailRepository;
    @Autowired
    private StoreAllotDetailRepository storeAllotDetailRepository;
    @Autowired
    private StoreAllotRepository storeAllotRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public Map<String,String> checkConfig(PrintConfigForm printConfigForm) {
        Map<String,String> map = Maps.newHashMap();
        //检查配置
        StringBuilder sb = new StringBuilder();
        if(StringUtils.isBlank(printConfigForm.getOrderType())) {
            sb.append("请填写单据类型\n");
        } else {
            if(!ExpressOrderTypeEnum.getList().contains(printConfigForm.getOrderType())) {
                sb.append("单据类型：");
                sb.append(printConfigForm.getOrderType());
                sb.append(" 在系统中不存在\n");
            }
        }
        if(StringUtils.isBlank(printConfigForm.getStoreNames())) {
            sb.append("请填写仓库名称\n");
        } else {
            List<String> storeNameList = StringUtils.getSplitList(printConfigForm.getStoreNames(), CharConstant.COMMA);
            for(String storeName:storeNameList) {
                if(depotRepository.findByName(storeName)==null) {
                    sb.append("仓库：").append(storeName).append(" 在系统中不存在\n");
                }
            }
        }
        if(!version.equals(printConfigForm.getVersion())) {
            sb.append("最新打印软件版本为：").append(version).append("，当前使用版本为").append(printConfigForm.getVersion());
        }
        String message = sb.toString();
        if(StringUtils.isBlank(message)) {
            map.put("success","true");
        } else {
            map.put("success","false");
            map.put("message",message);
        }
        return map;
    }

    public void print(String expressOrderId) {
        ExpressOrder expressOrder = expressOrderRepository.findOne(expressOrderId);
        expressOrder.setOutPrintDate(LocalDateTime.now());
        expressOrder.setLastModifiedBy(RequestUtils.getAccountId());
        expressOrderRepository.save(expressOrder);
    }


    public List<ExpressOrderPrintDto> findOrderList(PrintConfigQuery printConfigQuery) {
        List<ExpressOrder> list = Lists.newArrayList();
        List<String> storeNameList = StringUtils.getSplitList(printConfigQuery.getStoreNames(),CharConstant.COMMA);
        List<Depot> depots = depotRepository.findByNameList(storeNameList);
        List<String> depotIds = CollectionUtil.extractToList(depots, "id");
        if(StringUtils.isNotBlank(printConfigQuery.getOrderIds())) {
            List<String> orderIds = StringUtils.getSplitList(printConfigQuery.getOrderIds(),CharConstant.COMMA);
            List<String> businessIds = Lists.newArrayList();
            for(String orderId:orderIds) {
                if(StringUtils.isNotBlank(orderId)) {
                    businessIds.add(IdUtils.getId(orderId));
                }
            }
            if(ExpressOrderTypeEnum.物料订单.name().equals(printConfigQuery.getOrderType())) {
                list = expressOrderRepository.findByTypeAndExtendIds(printConfigQuery.getOrderType(), businessIds);
            }else{
                list = expressOrderRepository.findByTypeAndBusinessIds(printConfigQuery.getOrderType(), businessIds);
            }
        } else {
           String printDate=printConfigQuery.getPrintDate()!=null? HtmlUtils.htmlUnescape(printConfigQuery.getPrintDate()):LocalDateUtils.format(LocalDate.now());
           printConfigQuery.setDepotIdList(depotIds);
           printConfigQuery.setPrintDate(printDate);
            list=expressOrderRepository.findPrint(printConfigQuery);
            if(printConfigQuery.getQty()>0&&CollectionUtil.isNotEmpty(list)) {
                if(list.size()>=printConfigQuery.getQty()){
                    list=list.subList(0,printConfigQuery.getQty());
                }
            }
        }
        return setPrintOutDetail(printConfigQuery,list);
    }

    private List<ExpressOrderPrintDto> setPrintOutDetail(PrintConfigQuery printConfigQuery,List<ExpressOrder> expressOrders) {
        List<ExpressOrderPrintDto> expressOrderPrintList=Lists.newArrayList();
        if(CollectionUtil.isEmpty(expressOrders) || !"出库单".equals(printConfigQuery.getConfigType())) {
            return expressOrderPrintList;
        }
        String expressProductId = CompanyConfigUtil.findByCode(redisTemplate,RequestUtils.getCompanyId(), CompanyConfigCodeEnum.EXPRESS_PRODUCT_ID.name()).getValue();
        List<String> businessIds = CollectionUtil.extractToList(expressOrders, "extendBusinessId");
        List<String> depotIdList=Lists.newArrayList();
        depotIdList.addAll(CollectionUtil.extractToList(expressOrders,"toDepotId"));
        depotIdList.addAll(CollectionUtil.extractToList(expressOrders,"fromDepotId"));
        Map<String,Depot> depotMap=depotRepository.findMap(depotIdList);
        if(ExpressOrderTypeEnum.手机订单.name().equals(printConfigQuery.getOrderType())) {
            List<GoodsOrder> goodsOrderList=goodsOrderRepository.findByBusinessIdIn(businessIds);
            List<GoodsOrderDetail> list = goodsOrderDetailRepository.findByGoodsOrderIdIn(CollectionUtil.extractToList(goodsOrderList,"id"));
            Map<String,GoodsOrder> goodsOrderMap=CollectionUtil.extractToMap(goodsOrderList,"id");
            Map<String,Depot> toDepotMap=depotRepository.findMap(CollectionUtil.extractToList(expressOrders,"toDepotId"));
            Map<String,List<GoodsOrderDetail>> map= Maps.newHashMap();
            for(GoodsOrderDetail goodsOrderDetail:list) {
                GoodsOrder goodsOrder=goodsOrderMap.get(goodsOrderDetail.getGoodsOrderId());
                String businessId = goodsOrder.getBusinessId();
                if(!map.containsKey(businessId)) {
                    map.put(businessId,new ArrayList<GoodsOrderDetail>());
                }
                map.get(businessId).add(goodsOrderDetail);
            }
            Map<String,Product> productMap=productRepository.findMap(CollectionUtil.extractToList(list,"productId"));
            for(ExpressOrder expressOrder:expressOrders) {
                if(map.containsKey(expressOrder.getExtendBusinessId())) {
                    ExpressOrderPrintDto expressOrderPrintDto=new ExpressOrderPrintDto();
                    Integer totalQty = 0;
                    for(GoodsOrderDetail goodsOrderDetail:map.get(expressOrder.getExtendBusinessId())) {
                        GoodsOrder goodsOrder=goodsOrderMap.get(goodsOrderDetail.getGoodsOrderId());
                        if(!expressProductId.equals(goodsOrderDetail.getProductId())) {
                            if(goodsOrderDetail.getRealBillQty()>0 ) {
                                ExpressOrderPrintDetailDto printOutDetail = new ExpressOrderPrintDetailDto();
                                Product product=productMap.get(goodsOrderDetail.getProductId());
                                printOutDetail.setProductName(product.getName());
                                printOutDetail.setQty(goodsOrderDetail.getRealBillQty());
                                printOutDetail.setCode(product.getCode());
                                Depot toDepot=toDepotMap.get(expressOrder.getToDepotId());
                                if(toDepot.getPrintPrice()) {
                                    printOutDetail.setPrice(goodsOrderDetail.getPrice());
                                }
                                expressOrderPrintDto.getExpressOrderPrintDetailDtoList().add(printOutDetail);
                                totalQty =totalQty + printOutDetail.getQty();
                            }
                        } else {
                            expressOrderPrintDto.setShouldGet(goodsOrderDetail.getPrice().multiply(new BigDecimal(goodsOrderDetail.getBillQty())));
                        }
                        expressOrderPrintDto.setFromDepotName(depotMap.get(expressOrder.getFromDepotId()).getName());
                        expressOrderPrintDto.setToDepotName(depotMap.get(expressOrder.getToDepotId()).getName());
                        expressOrderPrintDto.setBillDate(goodsOrder.getBillDate());
                        expressOrderPrintDto.setFormatId(goodsOrder.getBusinessId());
                        expressOrderPrintDto.setPrintId(goodsOrder.getId());
                        expressOrderPrintDto.setContator(expressOrder.getContator());
                        expressOrderPrintDto.setMobilePhone(expressOrder.getMobilePhone());
                        expressOrderPrintDto.setAddress(expressOrder.getAddress());
                        expressOrderPrintDto.setRemarks(expressOrder.getRemarks());
                        expressOrderPrintDto.setBillRemarks(goodsOrder.getRemarks()+(goodsOrder.getLxMallOrder()!=null&&goodsOrder.getLxMallOrder()?"，天翼购订货":""));
                    }
                    ExpressOrderPrintDetailDto printOutDetail = new ExpressOrderPrintDetailDto();
                    printOutDetail.setProductName("合计");
                    printOutDetail.setQty(totalQty);
                    expressOrderPrintDto.getExpressOrderPrintDetailDtoList().add(printOutDetail);
                    expressOrderPrintList.add(expressOrderPrintDto);
                }
            }
        }else if(ExpressOrderTypeEnum.物料订单.name().equals(printConfigQuery.getOrderType())) {
            List<AdGoodsOrder> list = adGoodsOrderRepository.findByBusinessIdIn(businessIds);
            List<AdGoodsOrderDetail> adGoodsOrderDetailList=adGoodsOrderDetailRepository.findByAdGoodsOrderIdIn(CollectionUtil.extractToList(list,"id"));
            Map<String,List<AdGoodsOrderDetail>> adGoodsOrderDetailMap=CollectionUtil.extractToMapList(adGoodsOrderDetailList,"adGoodsOrderId");
            Map<String,AdGoodsOrder> adGoodsOrderMap=CollectionUtil.extractToMap(list,"id");
            Map<String,List<AdGoodsOrderDetail>> map= Maps.newHashMap();
            for(AdGoodsOrder adGoodsOrder:list) {
                String businessId = adGoodsOrder.getBusinessId();
                if(!map.containsKey(businessId)) {
                    map.put(businessId,adGoodsOrderDetailMap.get(adGoodsOrder.getId()));
                }
            }
            Map<String,Product> productMap=productRepository.findMap(CollectionUtil.extractToList(adGoodsOrderDetailList,"productId"));
            Map<String,Depot> goodsOrderDepotMap=depotRepository.findMap(CollectionUtil.extractToList(list,"shopId"));
            for(ExpressOrder expressOrder:expressOrders) {
                if(map.containsKey(expressOrder.getExtendBusinessId())) {
                    ExpressOrderPrintDto expressOrderPrintDto=new ExpressOrderPrintDto();
                    Integer totalQty = 0;
                    BigDecimal amount=BigDecimal.ZERO;
                    for(AdGoodsOrderDetail adGoodsOrderDetail:map.get(expressOrder.getExtendBusinessId())) {
                        Product product=productMap.get(adGoodsOrderDetail.getProductId());
                        AdGoodsOrder adGoodsOrder=adGoodsOrderMap.get(adGoodsOrderDetail.getAdGoodsOrderId());
                        if(!expressProductId.equals(adGoodsOrderDetail.getProductId())) {
                            if(adGoodsOrderDetail.getBillQty()>0 ) {
                                ExpressOrderPrintDetailDto printOutDetail = new ExpressOrderPrintDetailDto();
                                printOutDetail.setProductName(product.getName());
                                printOutDetail.setQty(adGoodsOrderDetail.getBillQty());
                                printOutDetail.setPrice(product.getPrice2());
                                printOutDetail.setCode(product.getCode());
                                expressOrderPrintDto.getExpressOrderPrintDetailDtoList().add(printOutDetail);
                                totalQty =totalQty + printOutDetail.getQty();
                                amount=amount.add(new BigDecimal(printOutDetail.getQty()).multiply(printOutDetail.getPrice()));
                            }
                        } else {
                            expressOrderPrintDto.setShouldGet(product.getPrice2().multiply(new BigDecimal(adGoodsOrderDetail.getBillQty())));
                        }
                        expressOrderPrintDto.setShipStatus(adGoodsOrder.getProcessStatus());
                        expressOrderPrintDto.setEmployeeId(adGoodsOrder.getEmployeeId());
                        expressOrderPrintDto.setFromDepotName(depotMap.get(expressOrder.getFromDepotId()).getName());
                        expressOrderPrintDto.setToDepotName(depotMap.get(expressOrder.getToDepotId()).getName());
                        expressOrderPrintDto.setBillDate(adGoodsOrder.getBillDate());
                        expressOrderPrintDto.setFormatId(adGoodsOrder.getBusinessId());
                        expressOrderPrintDto.setPrintId(adGoodsOrder.getId());
                        expressOrderPrintDto.setContator(expressOrder.getContator());
                        expressOrderPrintDto.setMobilePhone(expressOrder.getMobilePhone());
                        expressOrderPrintDto.setAddress(expressOrder.getAddress());
                        expressOrderPrintDto.setRemarks(expressOrder.getRemarks());
                        Depot depot=goodsOrderDepotMap.get(adGoodsOrder.getShopId());
                        expressOrderPrintDto.setBillRemarks(adGoodsOrder.getBillRemarks()+"   门店地址："+depot.getAddress());
                    }
                    ExpressOrderPrintDetailDto printOutDetail = new ExpressOrderPrintDetailDto();
                    printOutDetail.setProductName("合计");
                    printOutDetail.setQty(totalQty);
                    printOutDetail.setPrice(amount);
                    expressOrderPrintDto.getExpressOrderPrintDetailDtoList().add(printOutDetail);
                    expressOrderPrintList.add(expressOrderPrintDto);
                }
            }
        } else if (ExpressOrderTypeEnum.大库调拨.name().equals(printConfigQuery.getOrderType())) {
            List<StoreAllot> storeAllotList=storeAllotRepository.findByBusinessIdIn(businessIds);
            List<StoreAllotDetail> list = storeAllotDetailRepository.findByStoreAllotIdIn(CollectionUtil.extractToList(storeAllotList,"id"));
            Map<String,StoreAllot> storeAllotMap=CollectionUtil.extractToMap(storeAllotList,"id");
            Map<String,List<StoreAllotDetail>> map= Maps.newHashMap();
            for(StoreAllotDetail storeAllotDetail:list) {
                String businessId = storeAllotMap.get(storeAllotDetail.getStoreAllotId()).getBusinessId();
                if(!map.containsKey(businessId)) {
                    map.put(businessId,new ArrayList<StoreAllotDetail>());
                }
                map.get(businessId).add(storeAllotDetail);
            }
            Map<String,Product> productMap=productRepository.findMap(CollectionUtil.extractToList(list,"productId"));
            for(ExpressOrder expressOrder:expressOrders) {
                if(map.containsKey(expressOrder.getExtendBusinessId())) {
                    ExpressOrderPrintDto expressOrderPrintDto=new ExpressOrderPrintDto();
                    Integer totalQty = 0;
                    for(StoreAllotDetail storeAllotDetail:map.get(expressOrder.getExtendBusinessId())) {
                        ExpressOrderPrintDetailDto printOutDetail = new ExpressOrderPrintDetailDto();
                        StoreAllot storeAllot=storeAllotMap.get(storeAllotDetail.getStoreAllotId());
                        Product product=productMap.get(storeAllotDetail.getProductId());
                        printOutDetail.setProductName(product.getName());
                        printOutDetail.setQty(storeAllotDetail.getBillQty());
                        printOutDetail.setCode(product.getCode());
                        totalQty=totalQty+printOutDetail.getQty();
                        expressOrderPrintDto.getExpressOrderPrintDetailDtoList().add(printOutDetail);
                        expressOrderPrintDto.setFromDepotName(depotMap.get(expressOrder.getFromDepotId()).getName());
                        expressOrderPrintDto.setToDepotName(depotMap.get(expressOrder.getToDepotId()).getName());
                        expressOrderPrintDto.setFormatId(storeAllot.getBusinessId());
                        expressOrderPrintDto.setContator(expressOrder.getContator());
                        expressOrderPrintDto.setMobilePhone(expressOrder.getMobilePhone());
                        expressOrderPrintDto.setAddress(expressOrder.getAddress());
                        expressOrderPrintDto.setRemarks(expressOrder.getRemarks());
                        expressOrderPrintDto.setBillDate(storeAllot.getBillDate());
                        expressOrderPrintDto.setPrintId(storeAllot.getId());
                    }
                    ExpressOrderPrintDetailDto printOutDetail = new ExpressOrderPrintDetailDto();
                    printOutDetail.setProductName("合计");
                    printOutDetail.setQty(totalQty);
                    expressOrderPrintDto.getExpressOrderPrintDetailDtoList().add(printOutDetail);
                    expressOrderPrintList.add(expressOrderPrintDto);
                }
            }
        }
        cacheUtils.initCacheInput(expressOrderPrintList);
        return expressOrderPrintList;
    }
}
