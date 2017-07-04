package net.myspring.future.modules.api.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.basic.modules.sys.dto.CompanyConfigCacheDto;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.future.common.enums.CarrierOrderStatusEnum;
import net.myspring.future.common.enums.GoodsOrderStatusEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.api.domain.CarrierOrder;
import net.myspring.future.modules.api.domain.CarrierProduct;
import net.myspring.future.modules.api.repository.CarrierOrderRepository;
import net.myspring.future.modules.api.repository.CarrierProductRepository;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.crm.domain.*;
import net.myspring.future.modules.api.dto.CarrierOrderDetailDto;
import net.myspring.future.modules.api.dto.CarrierOrderDto;
import net.myspring.future.modules.api.dto.CarrierOrderExportDetailDto;
import net.myspring.future.modules.api.dto.CarrierOrderMainDto;
import net.myspring.future.modules.crm.repository.*;
import net.myspring.future.modules.api.web.form.CarrierOrderFrom;
import net.myspring.future.modules.crm.web.form.GoodsOrderShipForm;
import net.myspring.future.modules.api.web.query.CarrierOrderQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.MD5Utils;
import net.myspring.util.text.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CarrierOrderService {

    @Autowired
    private CarrierOrderRepository carrierOrderRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private GoodsOrderRepository goodsOrderRepository;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CarrierProductRepository carrierProductRepository;
    @Autowired
    private GoodsOrderDetailRepository goodsOrderDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private GoodsOrderImeRepository goodsOrderImeRepository;
    @Autowired
    private ProductImeRepository productImeRepository;
    @Autowired
    private DepotManager depotManager;

    public Page<CarrierOrderDto> findPage(Pageable pageable, CarrierOrderQuery carrierOrderQuery) {
        Page<CarrierOrderDto> page = carrierOrderRepository.findPage(pageable, carrierOrderQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public  void updateStatusAndRemarks(CarrierOrderFrom carrierOrderFrom){
        CarrierOrder carrierOrder=carrierOrderRepository.findOne(carrierOrderFrom.getId());
        carrierOrder.setStatus(carrierOrderFrom.getStatus());
        carrierOrder.setRemarks(carrierOrderFrom.getRemarks());
        carrierOrderRepository.save(carrierOrder);
    }


    public Map<String, Object> checkDetailJsons(CarrierOrderFrom carrierOrderFrom) {
        Boolean checkColor = carrierOrderFrom.getCheckColor();
        GoodsOrder goodsOrder=null;
        if(StringUtils.isNotBlank(carrierOrderFrom.getBusinessId())){
            //判断昌东仓库，昌东仓库需要校验颜色
            goodsOrder = goodsOrderRepository.findByBusinessId(carrierOrderFrom.getBusinessId());
            CompanyConfigCacheDto companyConfig = CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.MERGE_STORE_IDS.name());
            if (companyConfig != null && StringUtils.isNotBlank(companyConfig.getValue())) {
                String mergeStoreIds = companyConfig.getValue();
                if (StringUtils.isNotBlank(mergeStoreIds)) {
                    List<String> storeIds = StringUtils.getSplitList(mergeStoreIds, CharConstant.COMMA);
                    String storeId = storeIds.get(1);
                    if (goodsOrder.getStoreId().equals(storeId)) {
                        checkColor = true;
                    }
                }
            }
        }
        List<CarrierOrderMainDto> carrierOrderMains = Lists.newArrayList();
        StringBuilder sb = new StringBuilder();
        String detailJsons = carrierOrderFrom.getDetailJson();
        List<String> detailJsonList = Arrays.asList(detailJsons.replace("]}", "]}\n").split(CharConstant.ENTER));
        if (CollectionUtil.isEmpty(detailJsonList)) {
            sb.append("商城订单信息格式不正确，无法读取");
        } else {
            for (String detailJson : detailJsonList) {
                CarrierOrderMainDto carrierOrderMain = ObjectMapperUtils.readValue(detailJson, CarrierOrderMainDto.class);
                if (carrierOrderMain == null) {
                    sb.append("商城信息：" + detailJson + "格式不正确，无法读取;");
                } else {
                    String md5 = carrierOrderMain.getMd5();
                    String mallOrderId = carrierOrderMain.getMallOrderId();
                    List<CarrierOrderDetailDto> details = carrierOrderMain.getDetail();
                    Map<Object, Object> carrierMap = Maps.newLinkedHashMap();
                    if (StringUtils.isNotBlank(carrierOrderMain.getId())) {
                        carrierMap.put("id", carrierOrderMain.getId());
                    }
                    if (StringUtils.isNotBlank(mallOrderId)) {
                        carrierMap.put("mallOrderId", mallOrderId);
                    }
                    if (StringUtils.isNotBlank(md5)) {
                        carrierMap.put("md5", "");
                    }
                    carrierMap.put("detail", details);
                    String jsonStr = ObjectMapperUtils.writeValueAsString(carrierMap);
                    carrierOrderMains.add(carrierOrderMain);
//                    if (md5 == null || !md5.equals(MD5Utils.encode(jsonStr))) {
//                        sb.append("商城信息：" + detailJson + "格式不正确,加密后字符串" + MD5Utils.encode(jsonStr) + "与原字符串不匹配");
//                    } else {
//                        carrierOrderMains.add(carrierOrderMain);
//                    }
                }
            }
        }
        //货品数量;
        String carrierCodes = "";
        Map<String,CarrierOrder> carrierOrderMap=Maps.newHashMap();
        Map<String,GoodsOrder> goodsOrderMap=Maps.newHashMap();
        if (CollectionUtil.isNotEmpty(carrierOrderMains)) {
            List<CarrierOrder> carrierOrders=carrierOrderRepository.findByCodeIn(CollectionUtil.extractToList(carrierOrderMains,"id"));
            carrierOrderMap=CollectionUtil.extractToMap(carrierOrders,"code");
            goodsOrderMap=goodsOrderRepository.findMap(CollectionUtil.extractToList(carrierOrders,"goodsOrderId"));
            for (CarrierOrderMainDto carrierOrderMain : carrierOrderMains) {
                carrierCodes = carrierCodes + CharConstant.ENTER + carrierOrderMain.getId();
                CarrierOrder carrierOrder = carrierOrderMap.get(carrierOrderMain.getId());
                if (carrierOrder != null && goodsOrderMap.get(carrierOrder.getGoodsOrderId()).getEnabled()) {
                    if ((goodsOrder == null) || (goodsOrder != null && !goodsOrder.getId().equals(carrierOrder.getGoodsOrderId()))) {
                        sb.append("商城单号：" + carrierOrderMain.getId() + "在系统中已存在，订单号为：" + goodsOrderMap.get(carrierOrder.getGoodsOrderId()).getBusinessId());
                    }
                }
            }
        }
        Map<String, Integer> productQtyMap = Maps.newHashMap();
        Map<String, Integer> productNameQtyMap = Maps.newHashMap();
        Boolean isSame = true;
        if (checkColor) {
            if (CollectionUtil.isNotEmpty(carrierOrderMains)) {
                for (CarrierOrderMainDto carrierOrderMain : carrierOrderMains) {
                    for (CarrierOrderDetailDto carrierOrderDetail : carrierOrderMain.getDetail()) {
                        CarrierProduct carrierProduct = carrierProductRepository.findByName(carrierOrderDetail.getProduct());
                        if (carrierProduct == null || carrierProduct.getProductId() == null) {
                            sb.append("货品：" + carrierOrderDetail.getProduct() + "在系统中没有找到匹配项");
                        } else {
                            String productId = carrierProduct.getProductId();
                            if (!productQtyMap.containsKey(productId)) {
                                productQtyMap.put(productId, 0);
                            }
                            productQtyMap.put(productId, productQtyMap.get(productId) + carrierOrderDetail.getQty().intValue());
                        }
                    }
                }
            }
            //如果已开单，添加运费货品数量，检测货品型号和数量
            if (goodsOrder != null) {
                String expressProductId = CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.EXPRESS_PRODUCT_ID.name()).getValue();
                Map<String, Integer> productMap = Maps.newHashMap();
                List<GoodsOrderDetail> goodsOrderDetailList = goodsOrderDetailRepository.findByGoodsOrderId(goodsOrder.getId());
                for (GoodsOrderDetail goodsOrderDetail : goodsOrderDetailList) {
                    productMap.put(goodsOrderDetail.getProductId(), goodsOrderDetail.getQty());
                }
                if (!GoodsOrderStatusEnum.待开单.name().equals(goodsOrder.getStatus())) {
                    for (String productId : productMap.keySet()) {
                        if (expressProductId == null || !expressProductId.equals(productId)) {
                            Integer qty = productQtyMap.get(productId) == null ? 0 : productQtyMap.get(productId);
                            Integer productQty = productMap.get(productId) == null ? 0 : productMap.get(productId);
                            if (!qty.equals(productQty)) {
                                isSame = false;
                            }
                        }
                    }
                    for (String productId : productQtyMap.keySet()) {
                        Integer qty = productQtyMap.get(productId) == null ? 0 : productQtyMap.get(productId);
                        Integer productQty = productMap.get(productId) == null ? 0 : productMap.get(productId);
                        if (!qty.equals(productQty)) {
                            isSame = false;
                        }
                    }
                }
            } else {
                if (CollectionUtil.isNotEmpty(carrierOrderMains)) {
                    for (CarrierOrderMainDto carrierOrderMain : carrierOrderMains) {
                        for (CarrierOrderDetailDto carrierOrderDetail : carrierOrderMain.getDetail()) {
                            CarrierProduct carrierProduct = carrierProductRepository.findByName(carrierOrderDetail.getProduct());
                            if (carrierProduct == null || carrierProduct.getProductId() == null) {
                                sb.append("货品：" + carrierOrderDetail.getProduct() + "在系统中没有找到匹配项");
                            } else {
                                String key = carrierProduct.getMallProductTypeName();
                                if (!productNameQtyMap.containsKey(key)) {
                                    productNameQtyMap.put(key, 0);
                                }
                                productNameQtyMap.put(key, productNameQtyMap.get(key) + carrierOrderDetail.getQty().intValue());
                            }
                        }
                    }
                }
                if (goodsOrder != null) {
                    List<CarrierProduct> carrierProducts = carrierProductRepository.findAll();
                    Map<String, String> carrierProductMap = Maps.newHashMap();
                    for (CarrierProduct carrierProduct : carrierProducts) {
                        carrierProductMap.put(carrierProduct.getProductId(), carrierProduct.getMallProductTypeName());
                    }
                    String expressProductId = CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.EXPRESS_PRODUCT_ID.name()).getValue();
                    String expressProductName = productRepository.findOne(expressProductId).getName();
                    Map<String, Integer> productMap = Maps.newHashMap();
                    List<GoodsOrderDetail> goodsOrderDetailList = goodsOrderDetailRepository.findByGoodsOrderId(goodsOrder.getId());
                    for (GoodsOrderDetail goodsOrderDetail : goodsOrderDetailList) {
                        String mallProductType = carrierProductMap.get(goodsOrderDetail.getProductId());
                        if (carrierProductMap.get(goodsOrderDetail.getProductId()) == null) {
                            isSame = false;
                        } else {
                            if (!productMap.containsKey(mallProductType)) {
                                productMap.put(mallProductType, 0);
                            }
                            productMap.put(mallProductType, productMap.get(mallProductType) + goodsOrderDetail.getQty());
                        }
                    }
                    if (!GoodsOrderStatusEnum.待开单.name().equals(goodsOrder.getStatus())) {
                        for (String productName : productMap.keySet()) {
                            if (expressProductId == null || !expressProductName.equals(productName)) {
                                Integer qty = productNameQtyMap.get(productName) == null ? 0 : productNameQtyMap.get(productName);
                                Integer productQty = productMap.get(productName) == null ? 0 : productMap.get(productName);
                                if (!qty.equals(productQty)) {
                                    isSame = false;
                                }
                            }
                        }
                        for (String productName : productNameQtyMap.keySet()) {
                            Integer qty = productNameQtyMap.get(productName) == null ? 0 : productNameQtyMap.get(productName);
                            Integer productQty = productMap.get(productName) == null ? 0 : productMap.get(productName);
                            if (!qty.equals(productQty)) {
                                isSame = false;
                            }
                        }
                    }
                }
            }
        }
        if (!isSame) {
            sb.append("货品数量型号与原始订货型号数量不匹配");
        }
        Map<String, Object> result = Maps.newHashMap();
        result.put("message", sb.toString());
        result.put("carrierCodes", carrierCodes.trim());
        if (checkColor) {
            result.put("productQtyMap", productQtyMap);
        } else {
            result.put("productNameQtyMap", productNameQtyMap);
        }
        return result;
    }

    public void save(CarrierOrderFrom carrierOrderFrom) {
        String carrierShopId=carrierOrderFrom.getCarrierShopId();
        List<CarrierOrder> carrierOrders = carrierOrderRepository.findByGoodsOrderId(carrierOrderFrom.getGoodsOrderId());
        //解析获取商城单号
        Map<String, String> detailMap = Maps.newHashMap();
        List<String> carrierCodes = Lists.newArrayList();
        if (StringUtils.isNotBlank(carrierOrderFrom.getDetailJson())) {
            String carrierDetails = carrierOrderFrom.getDetailJson().replace("]}", "]}\n");
            List<String> carrierDetailList = Arrays.asList(carrierDetails.split(CharConstant.ENTER));
            for (String carrierDetail : carrierDetailList) {
                CarrierOrderMainDto carrierOrderMain = ObjectMapperUtils.readValue(carrierDetail, CarrierOrderMainDto.class);
                if (carrierOrderMain != null) {
                    detailMap.put(carrierOrderMain.getId(), carrierDetail);
                    carrierCodes.add(carrierOrderMain.getId());
                }
            }
        }
        for (int i = carrierOrders.size() - 1; i >= 0; i--) {
            CarrierOrder carrierOrder = carrierOrders.get(i);
            if(StringUtils.isBlank(carrierShopId)&&StringUtils.isNotBlank(carrierOrder.getCarrierShopId())){
                carrierShopId=carrierOrder.getCarrierShopId();
            }
            if (!carrierCodes.contains(carrierOrder.getCode())) {
                carrierOrderRepository.delete(carrierOrder.getId());
                carrierOrders.remove(i);
            }
        }
        Map<String, CarrierOrder> carrierOrderMap = CollectionUtil.extractToMap(carrierOrders, "code");
        for (String code : carrierCodes) {
            if (!carrierOrderMap.containsKey(code)) {
                CarrierOrder carrierOrder = new CarrierOrder();
                if(StringUtils.isNotBlank(carrierShopId)){
                    carrierOrder.setCarrierShopId(carrierShopId);
                }
                carrierOrder.setGoodsOrderId(carrierOrderFrom.getGoodsOrderId());
                carrierOrder.setCode(code);
                carrierOrder.setDetailJson(detailMap.get(code));
                carrierOrder.setStatus(CarrierOrderStatusEnum.空值.name());
                carrierOrderMap.put(code, carrierOrder);
            }
        }
        for (CarrierOrder carrierOrder : carrierOrderMap.values()) {
            if (detailMap.containsKey(carrierOrder.getCode())) {
                carrierOrder.setDetailJson(detailMap.get(carrierOrder.getCode()));
            }
            carrierOrderRepository.save(carrierOrder);
        }
    }

    public Map<String, Object> carrierShipCheck(List<List<String>> datas) {
        Map<String, Object> map = Maps.newHashMap();
        StringBuilder message = new StringBuilder();
        //根据Code汇总串码
        Map<String, List<String>> codeMap = Maps.newHashMap();
        Map<String, GoodsOrder> codeGoodsOrderMap = Maps.newHashMap();
        Map<String, GoodsOrderShipForm> goodsOrderShipMap = Maps.newHashMap();
        Map<String, List<String>> imeMap = Maps.newHashMap();
        for (List<String> row : datas) {
            String code = StringUtils.toString(row.get(0)).trim();
            String ime = StringUtils.toString(row.get(1)).trim();
            if (!codeMap.containsKey(code)) {
                codeMap.put(code, new ArrayList<String>());
            }
            codeMap.get(code).add(ime);
        }
        List<CarrierOrder> carrierOrderList = carrierOrderRepository.findByCodeIn(Lists.newArrayList(codeMap.keySet()));
        Map<String, CarrierOrder> carrierOrderMap = CollectionUtil.extractToMap(carrierOrderList, "code");
        Map<String, GoodsOrder> orderMap = goodsOrderRepository.findMap(CollectionUtil.extractToList(carrierOrderList, "goodsOrderId"));
        for (String code : codeMap.keySet()) {
            if (!carrierOrderMap.containsKey(code)) {
                message.append("商城单号" + code + "在系统中不存在");
            } else {
                codeGoodsOrderMap.put(code, orderMap.get(carrierOrderMap.get(code).getGoodsOrderId()));
            }
        }
        if (StringUtils.isNotBlank(message)) {
            map.put("message", message.toString());
        } else {
            for (String code : codeGoodsOrderMap.keySet()) {
                GoodsOrder goodsOrder = codeGoodsOrderMap.get(code);
                GoodsOrderShipForm goodsOrderShipForm = BeanUtil.map(goodsOrder, GoodsOrderShipForm.class);
                goodsOrderShipMap.put(goodsOrderShipForm.getId(), goodsOrderShipForm);
                if (!imeMap.containsKey(goodsOrderShipForm.getId())) {
                    imeMap.put(goodsOrderShipForm.getId(), new ArrayList<String>());
                }
                imeMap.get(goodsOrderShipForm.getId()).addAll(codeMap.get(code));
            }
            for (String id : goodsOrderShipMap.keySet()) {
                StringBuffer imeStr = new StringBuffer();
                for (String ime : imeMap.get(id)) {
                    imeStr.append(ime).append(CharConstant.ENTER);
                }
                goodsOrderShipMap.get(id).setImeStr(imeStr.toString());
            }
            List<GoodsOrderShipForm> goodsOrderShipForms = new ArrayList<GoodsOrderShipForm>(goodsOrderShipMap.values());
            map.put("goodsOrderShipForms", goodsOrderShipForms);
        }
        return map;
    }

    public SimpleExcelBook findSimpleExcelSheet(CarrierOrderQuery carrierOrderQuery) {
        carrierOrderQuery.setDepotIdList(depotManager.filterDepotIds(RequestUtils.getAccountId()));
        Workbook workbook = new SXSSFWorkbook(10000);
        List<SimpleExcelSheet> simpleExcelSheetList = Lists.newArrayList();

        String storeId = CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.DEFALULT_CARRIAR_STORE_ID.name()).getValue();
        carrierOrderQuery.setNotEqualStoreIdList(Lists.newArrayList(storeId));
        carrierOrderQuery.setNotEqualStatusList(Lists.newArrayList(CarrierOrderStatusEnum.已导入.name(), CarrierOrderStatusEnum.问题单号.name(), CarrierOrderStatusEnum.坏单.name()));
        List<CarrierOrderDto> carrierOrderList = carrierOrderRepository.findFilter(carrierOrderQuery);

        if (CollectionUtil.isNotEmpty(carrierOrderList)) {
            cacheUtils.initCacheInput(carrierOrderList);
            List<CarrierOrderExportDetailDto> detailList = Lists.newArrayList();
            List<SimpleExcelColumn> goodsOrderColumnList = Lists.newArrayList();
            List<String> goodsOrderIdList = CollectionUtil.extractToList(carrierOrderList, "goodsOrderId");
            List<GoodsOrderIme> goodsOrderImeList = goodsOrderImeRepository.findByGoodsOrderIdIn(goodsOrderIdList);
            Map<String, CarrierOrderDto> carrierOrderMap = CollectionUtil.extractToMap(carrierOrderList, "goodsOrderId");
            Map<String, Product> productMap = productRepository.findMap(CollectionUtil.extractToList(goodsOrderImeList, "productId"));
            Map<String, ProductIme> productImeMap = productImeRepository.findMap(CollectionUtil.extractToList(goodsOrderImeList, "productImeId"));
            for (GoodsOrderIme goodsOrderIme : goodsOrderImeList) {
                CarrierOrderDto carrierOrder = carrierOrderMap.get(goodsOrderIme.getGoodsOrderId());
                CarrierOrderExportDetailDto carrierOrderExportDetailDto = BeanUtil.map(carrierOrder, CarrierOrderExportDetailDto.class);
                carrierOrderExportDetailDto.setProductName(productMap.get(goodsOrderIme.getProductId()).getName());
                carrierOrderExportDetailDto.setIme(productImeMap.get(goodsOrderIme.getProductImeId()).getIme());
                detailList.add(carrierOrderExportDetailDto);
            }
            cacheUtils.initCacheInput(detailList);
            goodsOrderColumnList.add(new SimpleExcelColumn(workbook, "formatId", "订单编号"));
            goodsOrderColumnList.add(new SimpleExcelColumn(workbook, "areaName", "办事处"));
            goodsOrderColumnList.add(new SimpleExcelColumn(workbook, "depotName", "门店"));
            goodsOrderColumnList.add(new SimpleExcelColumn(workbook, "productName", "产品名称"));
            goodsOrderColumnList.add(new SimpleExcelColumn(workbook, "ime", "串码"));
            SimpleExcelSheet goodsOrderSheet = new SimpleExcelSheet("订货单", detailList, goodsOrderColumnList);
            simpleExcelSheetList.add(goodsOrderSheet);
            List<SimpleExcelColumn> carrierOrderColumnList = Lists.newArrayList();
            carrierOrderColumnList.add(new SimpleExcelColumn(workbook, "formatId", "订单号"));
            carrierOrderColumnList.add(new SimpleExcelColumn(workbook, "areaName", "办事处"));
            carrierOrderColumnList.add(new SimpleExcelColumn(workbook, "depotName", "门店"));
            carrierOrderColumnList.add(new SimpleExcelColumn(workbook, "shipDate", "发货时间"));
            carrierOrderColumnList.add(new SimpleExcelColumn(workbook, "code", "商城单号"));
            carrierOrderColumnList.add(new SimpleExcelColumn(workbook, "status", "商城状态"));
            carrierOrderColumnList.add(new SimpleExcelColumn(workbook, "remarks", "商城备注"));
            SimpleExcelSheet carrierOrderSheet = new SimpleExcelSheet("商城单号", carrierOrderList, carrierOrderColumnList);
            simpleExcelSheetList.add(carrierOrderSheet);
            ExcelUtils.doWrite(workbook, simpleExcelSheetList);
        }
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook, "商城订单" + UUID.randomUUID() + ".xlsx", simpleExcelSheetList);
        return simpleExcelBook;
    }

    public void updateStatus(CarrierOrderQuery carrierOrderQuery){
        List<GoodsOrder> goodsOrderList=goodsOrderRepository.findByBusinessIdIn(carrierOrderQuery.getBusinessIdList());
        if(CollectionUtil.isNotEmpty(goodsOrderList)){
            List<CarrierOrder> carrierOrderList=carrierOrderRepository.findByGoodsOrderIdIn(CollectionUtil.extractToList(goodsOrderList,"id"));
            for(CarrierOrder carrierOrder:carrierOrderList){
                carrierOrder.setStatus(carrierOrderQuery.getStatus());
                carrierOrderRepository.save(carrierOrder);
            }
        }
    }

    public void delete(String id) {
        carrierOrderRepository.logicDelete(id);
    }
}