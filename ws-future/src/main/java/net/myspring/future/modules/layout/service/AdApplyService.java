package net.myspring.future.modules.layout.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.cloud.modules.sys.dto.KingdeeSynReturnDto;
import net.myspring.cloud.modules.kingdee.domain.StkInventory;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.enums.BillTypeEnum;
import net.myspring.future.common.enums.ExpressOrderTypeEnum;
import net.myspring.future.common.enums.GoodsOrderStatusEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.DepotStore;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.basic.manager.SalOutStockManager;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.DepotStoreRepository;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.basic.web.form.DepotAdApplyForm;
import net.myspring.future.modules.basic.web.form.ProductAdForm;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.repository.ExpressOrderRepository;
import net.myspring.future.modules.layout.domain.AdApply;
import net.myspring.future.modules.layout.domain.AdGoodsOrder;
import net.myspring.future.modules.layout.domain.AdGoodsOrderDetail;
import net.myspring.future.modules.layout.dto.AdApplyDto;
import net.myspring.future.modules.layout.repository.AdApplyRepository;
import net.myspring.future.modules.layout.repository.AdGoodsOrderDetailRepository;
import net.myspring.future.modules.layout.repository.AdGoodsOrderRepository;
import net.myspring.future.modules.layout.web.form.*;
import net.myspring.future.modules.layout.web.query.AdApplyQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.*;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.IdUtils;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class AdApplyService {
    @Autowired
    private AdApplyRepository adApplyRepository;
    @Autowired
    private AdGoodsOrderRepository adGoodsOrderRepository;
    @Autowired
    private AdGoodsOrderDetailRepository adGoodsOrderDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private DepotStoreRepository depotStoreRepository;
    @Autowired
    private ExpressOrderRepository expressOrderRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DepotManager depotManager;
    @Autowired
    private CloudClient cloudClient;
    @Autowired
    private SalOutStockManager salOutStockManager;

    public Page<AdApplyDto> findPage(Pageable pageable, AdApplyQuery adApplyQuery) {
        adApplyQuery.setDepotIdList(depotManager.filterDepotIds(RequestUtils.getAccountId()));
        Page<AdApplyDto> page = adApplyRepository.findPage(pageable, adApplyQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public AdApplyDto findOne(String id){
        AdApplyDto adApplyDto = new AdApplyDto();
        if(StringUtils.isNotBlank(id)){
            AdApply adApply= adApplyRepository.findOne(id);
            adApplyDto = BeanUtil.map(adApply,AdApplyDto.class);
            cacheUtils.initCacheInput(adApplyDto);
        }
        return adApplyDto;
    }

    public AdApplyForm getForm(AdApplyForm adApplyForm){
        List<String> billTypes = Lists.newArrayList();
        billTypes.add(BillTypeEnum.POP.name());
        billTypes.add(BillTypeEnum.配件赠品.name());
        adApplyForm.getExtra().put("billTypes",billTypes);
        return adApplyForm;
    }

    public AdApplyBillForm getBillForm(AdApplyBillForm adApplyBillForm){
        List<String> billTypes = Lists.newArrayList();
        billTypes.add(BillTypeEnum.POP.name());
        billTypes.add(BillTypeEnum.配件赠品.name());
        adApplyBillForm.getExtra().put("billTypes",billTypes);
        adApplyBillForm.setExpressCompanyId(CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.DEFAULT_AD_EXPRESS_COMPANY_ID.name()).getValue());
        return adApplyBillForm;
    }

    public AdApplyBillTypeChangeForm findAdApplyList(String billType){
        AdApplyBillTypeChangeForm adApplyBillTypeChangeForm = new AdApplyBillTypeChangeForm();
        List<String> outGroupIds = Lists.newArrayList();
        if(BillTypeEnum.POP.name().equalsIgnoreCase(billType)){
            outGroupIds = IdUtils.getIdList(CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.PRODUCT_POP_GROUP_IDS.name()).getValue());
            adApplyBillTypeChangeForm.setStoreId(CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.AD_DEFAULT_STORE_ID.name()).getValue());
        }
        if(BillTypeEnum.配件赠品.name().equalsIgnoreCase(billType)){
            outGroupIds = IdUtils.getIdList(CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.PRODUCT_GOODS_POP_GROUP_IDS.name()).getValue());
            adApplyBillTypeChangeForm.setStoreId(CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.DEFAULT_STORE_ID.name()).getValue());
        }
        LocalDate dateStart = LocalDate.now().plusYears(-1);
        List<AdApplyDto> adApplyDtos = adApplyRepository.findByOutGroupIdAndDate(dateStart,outGroupIds);
        cacheUtils.initCacheInput(adApplyDtos);
        //同步财务库存
        if(adApplyDtos.size()>0){
            List<String> storeId = Lists.newArrayList();
            DepotStore depotStore = depotStoreRepository.findOne(adApplyBillTypeChangeForm.getStoreId());
            storeId.add(depotStore.getOutId());
            List<StkInventory> stkInventories = cloudClient.findInventoriesByDepotStoreOutIds(storeId);
            Map<String,StkInventory> stringStkInventoryMap = CollectionUtil.extractToMap(stkInventories,"FMaterialId");
            for(AdApplyDto adApplyDto:adApplyDtos){
                if(stringStkInventoryMap.get(adApplyDto.getProductOutId())!=null){
                    adApplyDto.setStoreQty(stringStkInventoryMap.get(adApplyDto.getProductOutId()).getFBaseQty());
                }else{
                    adApplyDto.setStoreQty(0);
                }
            }
        }
        adApplyBillTypeChangeForm.setAdApplyDtoList(adApplyDtos);
        return adApplyBillTypeChangeForm;
    }

    public AdApplyGoodsForm getAdApplyGoodsList(AdApplyGoodsForm adApplyGoodsForm){
        List<DepotAdApplyForm> depotAdApplyForms = BeanUtil.map(depotRepository.findByEnabledIsTrueAndAdShopIsTrueAndIsHiddenIsFalse(),DepotAdApplyForm.class);
        adApplyGoodsForm.setDepotAdApplyForms(depotAdApplyForms);
        List<String> outGroupIds = IdUtils.getIdList(CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.PRODUCT_POP_GROUP_IDS.name()).getValue());
        adApplyGoodsForm.getExtra().put("popProductList",productRepository.findByOutGroupIdIn(outGroupIds));
        return adApplyGoodsForm;
    }

    @Transactional
    public void save(AdApplyForm adApplyForm){
        if(CollectionUtil.isEmpty(adApplyForm.getProductAdForms())){
            return;
        }
        List<AdApply> adApplyList = Lists.newArrayList();
        for(ProductAdForm productAdForm:adApplyForm.getProductAdForms()){
            Integer applyQty = productAdForm.getApplyQty();
            if(applyQty!=null && applyQty < 0){
                throw new ServiceException("每个货品的开单数量不可小于0");
            }
            AdApply adApply = new AdApply();
            adApply.setApplyQty(applyQty);
            adApply.setConfirmQty(applyQty);
            adApply.setBilledQty(0);
            adApply.setLeftQty(applyQty);
            adApply.setShopId(adApplyForm.getShopId());
            adApply.setProductId(productAdForm.getId());
            adApply.setRemarks(adApplyForm.getRemarks());
            adApply.setExpiryDateRemarks(productAdForm.getExpiryDateRemarks());
            adApplyList.add(adApply);
        }
        adApplyRepository.save(adApplyList);
    }

    @Transactional
    public void saveConfirmQty(AdApplyEditForm adApplyEditForm){
        if(!adApplyEditForm.isCreate()){
            AdApply adApply = adApplyRepository.findOne(adApplyEditForm.getId());
            adApply.setConfirmQty(adApplyEditForm.getConfirmQty());
            adApply.setLeftQty(adApplyEditForm.getConfirmQty()-adApply.getBilledQty());
            adApply.setRemarks(adApplyEditForm.getRemarks());
            adApplyRepository.save(adApply);
        }
    }

    @Transactional
    public void goodsSave(AdApplyGoodsForm adApplyGoodsForm){
        if(adApplyGoodsForm.getDepotAdApplyForms() == null){
            return;
        }
        Product product = productRepository.findOne(adApplyGoodsForm.getProductId());
        if(product == null){
            throw new ServiceException("该货品未找到");
        }
        List<AdApply> adApplyList = Lists.newArrayList();
        for (DepotAdApplyForm depotAdApplyForm:adApplyGoodsForm.getDepotAdApplyForms()){
            Integer applyQty = depotAdApplyForm.getApplyQty();
            if(applyQty!=null && applyQty < 0){
                throw new ServiceException("每个货品的开单数量不可小于0");
            }
            AdApply adApply = new AdApply();
            adApply.setProductId(adApplyGoodsForm.getProductId());
            adApply.setShopId(depotAdApplyForm.getId());
            adApply.setApplyQty(depotAdApplyForm.getApplyQty());
            adApply.setConfirmQty(depotAdApplyForm.getApplyQty());
            adApply.setBilledQty(0);
            adApply.setLeftQty(depotAdApplyForm.getApplyQty());
            adApply.setExpiryDateRemarks(product.getExpiryDateRemarks());
            adApply.setRemarks(adApplyGoodsForm.getRemarks());
            adApplyList.add(adApply);
        }
        adApplyRepository.save(adApplyList);
    }

    @Transactional
    public void billSave(AdApplyBillForm adApplyBillForm){
        List<String> adApplyId  =CollectionUtil.extractToList(adApplyBillForm.getAdApplyDetailForms(),"id");
        Map<String,AdApplyDetailForm> adApplyDetailFormMap = CollectionUtil.extractToMap(adApplyBillForm.getAdApplyDetailForms(),"id");
        Map<String,Product> productMap = CollectionUtil.extractToMap(productRepository.findAll(),"id");
        List<AdApply> adApplyList = adApplyRepository.findAll(adApplyId);
        Map<String,AdGoodsOrder> adGoodsOrderMap = Maps.newHashMap();
        List<AdGoodsOrderDetail> adGoodsOrderDetails = Lists.newArrayList();
        List<AdGoodsOrder> adGoodsOrders = Lists.newArrayList();
        List<ExpressOrder> expressOrders = Lists.newArrayList();
        if(adApplyList == null){
            return;
        }
        //生成AdGoodsOrder
        for(AdApply adApply:adApplyList){
            Integer nowBilledQty = adApplyDetailFormMap.get(adApply.getId()).getNowBilledQty();
            Integer storeQty = adApplyDetailFormMap.get(adApply.getId()).getStoreQty();
            if(nowBilledQty != null && nowBilledQty < 0){
                throw new ServiceException("每个货品订货数量不可以小于0");
            }
            /*if(nowBilledQty > storeQty){
                throw new ServiceException("每个货品订货数量不可以大于库存数量");
            }*/
            if(!adGoodsOrderMap.containsKey(adApply.getShopId())){
                AdGoodsOrder adGoodsOrder = new AdGoodsOrder();
                adGoodsOrder.setBillType(adApplyBillForm.getBillType());
                adGoodsOrder.setStoreId(adApplyBillForm.getStoreId());
                adGoodsOrder.setOutShopId(adApply.getShopId());
                adGoodsOrder.setShopId(adApply.getShopId());
                adGoodsOrder.setBillDate(adApplyBillForm.getBillDate());
                adGoodsOrder.setSmallQty(0);
                adGoodsOrder.setMediumQty(0);
                adGoodsOrder.setLargeQty(0);
                adGoodsOrder.setSplitBill(false);
                adGoodsOrder.setLocked(true);
                adGoodsOrder.setBillRemarks(adApplyBillForm.getRemarks());
                adGoodsOrderMap.put(adApply.getShopId(), adGoodsOrder);
                adGoodsOrderRepository.save(adGoodsOrder);
                adGoodsOrders.add(adGoodsOrder);
            }
            AdGoodsOrder adGoodsOrder = adGoodsOrderMap.get(adApply.getShopId());
            Product product = productMap.get(adApply.getProductId());
            AdGoodsOrderDetail adGoodsOrderDetail = new AdGoodsOrderDetail();
            adGoodsOrderDetail.setAdGoodsOrderId(adGoodsOrder.getId());
            adGoodsOrderDetail.setProductId(adApply.getProductId());
            adGoodsOrderDetail.setPrice(product.getPrice2());
            adGoodsOrderDetail.setShouldGet(product.getShouldGet());
            adGoodsOrderDetail.setShouldPay(BigDecimal.ZERO);
            adGoodsOrderDetail.setShippedQty(0);
            adGoodsOrderDetail.setQty(adApply.getApplyQty());
            adGoodsOrderDetail.setConfirmQty(adApply.getConfirmQty());
            adGoodsOrderDetail.setBillQty(nowBilledQty);
            adGoodsOrderDetails.add(adGoodsOrderDetail);

        }
        adGoodsOrderDetailRepository.save(adGoodsOrderDetails);

        //自动开单
        String maxBusinessId = adGoodsOrderRepository.findMaxBusinessId(adApplyBillForm.getBillDate());

        for(AdGoodsOrder adGoodsOrder:adGoodsOrders){

            adGoodsOrder.setBusinessId(IdUtils.getNextBusinessId(maxBusinessId, adApplyBillForm.getBillDate()));
            BigDecimal amount = BigDecimal.ZERO;
            List<AdGoodsOrderDetail> adGoodsOrderDetailLists = adGoodsOrderDetailRepository.findByAdGoodsOrderId(adGoodsOrder.getId());
            for(AdGoodsOrderDetail adGoodsOrderDetail:adGoodsOrderDetailLists){
                amount = amount.add(adGoodsOrderDetail.getPrice().multiply(new BigDecimal(adGoodsOrderDetail.getBillQty())));
            }
            adGoodsOrder.setAmount(amount);
            adGoodsOrder.setProcessStatus(GoodsOrderStatusEnum.待发货.name());
            Depot depot = depotRepository.findOne(adGoodsOrder.getShopId());
            ExpressOrder expressOrder = new ExpressOrder();
            expressOrder.setExtendBusinessId(adGoodsOrder.getBusinessId());
            expressOrder.setExtendType(ExpressOrderTypeEnum.物料订单.name());
            expressOrder.setExtendId(adGoodsOrder.getId());
            expressOrder.setFromDepotId(adGoodsOrder.getStoreId());
            expressOrder.setExpressCompanyId(adApplyBillForm.getExpressCompanyId());
            expressOrder.setToDepotId(adGoodsOrder.getShopId());
            expressOrder.setContator(depot.getContator());
            expressOrder.setAddress(depot.getAddress());
            expressOrder.setMobilePhone(depot.getMobilePhone());
            expressOrder.setPrintDate(adApplyBillForm.getBillDate());
            if(BillTypeEnum.POP.name().equalsIgnoreCase(adApplyBillForm.getBillType())){
                expressOrder.setExpressPrintQty(0);
            }else{
                expressOrder.setExpressPrintQty(1);
            }
            expressOrderRepository.save(expressOrder);
            expressOrders.add(expressOrder);
            adGoodsOrder.setExpressOrderId(expressOrder.getId());
            adGoodsOrderRepository.save(adGoodsOrder);
        }

        //保存adApply
        List<AdApply> newAdApplys = Lists.newArrayList();
        for(AdApply adApply:adApplyList){
            AdGoodsOrder adGoodsOrder = adGoodsOrderMap.get(adApply.getShopId());
            AdApplyDetailForm adApplyDetailForm = adApplyDetailFormMap.get(adApply.getId());
            adApply.setBilledQty(adApply.getBilledQty()+adApplyDetailForm.getNowBilledQty());
            if(StringUtils.isBlank(adApply.getOrderId())){
                adApply.setOrderId(adGoodsOrder.getId());
            }else{
                adApply.setOrderId(adApply.getOrderId()+ CharConstant.COMMA+adGoodsOrder.getId());
            }
            adApply.setLeftQty(adApply.getConfirmQty()-adApply.getBilledQty());
            newAdApplys.add(adApply);
        }
        adApplyRepository.save(newAdApplys);

       List<KingdeeSynReturnDto> kingdeeSynReturnDtos = salOutStockManager.synForAdApply(adGoodsOrders);
        if(kingdeeSynReturnDtos.size()!=adGoodsOrders.size()){
            throw new ServiceException("同步金蝶开单数据有误");
        }
       for(int i = 0;i<adGoodsOrders.size();i++){
           adGoodsOrders.get(i).setCloudSynId(kingdeeSynReturnDtos.get(i).getId());
           adGoodsOrders.get(i).setOutCode(kingdeeSynReturnDtos.get(i).getBillNo());
           expressOrders.get(i).setOutCode(kingdeeSynReturnDtos.get(i).getBillNo());
       }
       adGoodsOrderRepository.save(adGoodsOrders);
       expressOrderRepository.save(expressOrders);
    }

    public SimpleExcelBook export(AdApplyQuery adApplyQuery){
        Workbook workbook = new SXSSFWorkbook(10000);
        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();
        List<SimpleExcelSheet> simpleExcelSheetList = Lists.newArrayList();

        List<AdApplyDto> adApplyDtos = findPage(new PageRequest(0,10000),adApplyQuery).getContent();
        cacheUtils.initCacheInput(adApplyDtos);

        List<String> depotNameList = Lists.newArrayList();
        Map<String,Map<String,Integer>> productShopLeftQtyMap = Maps.newLinkedHashMap();
        //循环遍历要导出数据，拼接成由办事处和货品的Map
        for(AdApplyDto adApplyDto:adApplyDtos){
            if(adApplyDto.getLeftQty()>0){
                if(!depotNameList.contains(adApplyDto.getShopName())){
                    depotNameList.add(adApplyDto.getShopName());
                }
            }
            if(!productShopLeftQtyMap.containsKey(adApplyDto.getProductId())){
                productShopLeftQtyMap.put(adApplyDto.getProductId(),new LinkedHashMap<>());
            }
            if(!productShopLeftQtyMap.get(adApplyDto.getProductId()).containsKey(adApplyDto.getShopName())){
                productShopLeftQtyMap.get(adApplyDto.getProductId()).put(adApplyDto.getShopName(),0);
            }
            Integer leftQty = productShopLeftQtyMap.get(adApplyDto.getProductId()).get(adApplyDto.getShopName());
            productShopLeftQtyMap.get(adApplyDto.getProductId()).put(adApplyDto.getShopName(),leftQty+adApplyDto.getLeftQty());
        }
        for(String productId:productShopLeftQtyMap.keySet()){
            Long totalQty = 0l;
            for(String shopName:productShopLeftQtyMap.get(productId).keySet()){
                totalQty += productShopLeftQtyMap.get(productId).get(shopName);
            }
            productShopLeftQtyMap.get(productId).put("合计",totalQty.intValue());
        }

        //要导出的数据
        List<List<SimpleExcelColumn>> datas = Lists.newArrayList();
        Map<String, CellStyle> cellStyleMap=ExcelUtils.getCellStyleMap(workbook);
        CellStyle headCellStyle = cellStyleMap.get(ExcelCellStyle.HEADER.name());
        CellStyle dataCellStyle = cellStyleMap.get(ExcelCellStyle.DATA.name());
        //列名
        List<SimpleExcelColumn> headers = Lists.newArrayList();
        headers.add(new SimpleExcelColumn(headCellStyle,"序号"));
        headers.add(new SimpleExcelColumn(headCellStyle,"物料编码"));
        headers.add(new SimpleExcelColumn(headCellStyle,"物料名称"));
        for(String depotName:depotNameList){
            headers.add(new SimpleExcelColumn(headCellStyle,depotName));
        }
        headers.add(new SimpleExcelColumn(headCellStyle,"合计"));
        datas.add(headers);
        //内容
        Integer index = 1;
        Map<String,Product> productMap = CollectionUtil.extractToMap(productRepository.findAll(),"id");
        for(String productId:productShopLeftQtyMap.keySet()){
            List<SimpleExcelColumn> list = Lists.newArrayList();
            Integer amountQty = productShopLeftQtyMap.get(productId).get("合计");
            if(amountQty > 0){
                list.add(new SimpleExcelColumn(dataCellStyle,index.toString()));
                list.add(new SimpleExcelColumn(dataCellStyle,productMap.get(productId).getCode()));
                list.add(new SimpleExcelColumn(dataCellStyle,productMap.get(productId).getName()));
                for(String depotName:depotNameList){
                    if(productShopLeftQtyMap.get(productId).get(depotName)!=null && productShopLeftQtyMap.get(productId).get(depotName)!=0){
                        list.add(new SimpleExcelColumn(dataCellStyle,productShopLeftQtyMap.get(productId).get(depotName).toString()));
                    }else{
                        list.add(new SimpleExcelColumn(dataCellStyle,""));
                    }

                }
                list.add(new SimpleExcelColumn(dataCellStyle,productShopLeftQtyMap.get(productId).get("合计").toString()));
                datas.add(list);
                index++;
            }
        }
        simpleExcelSheetList.add(new SimpleExcelSheet("办事处统计",datas));

        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "id", "编码"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "shopName", "门店"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "productCode", "物料编码"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "productName", "物料名称"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "applyQty", "申请数"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "confirmQty", "确认数"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "leftQty", "待开单数"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "createdByName", "创建人"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "createdDate", "创建时间"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "expiryDateRemarks", "截止日期"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "remarks", "备注"));


        simpleExcelSheetList.add(new SimpleExcelSheet("POP征订", adApplyDtos, simpleExcelColumnList));
        ExcelUtils.doWrite(workbook, simpleExcelSheetList);
        return new SimpleExcelBook(workbook,"物料征订明细"+ LocalDateUtils.format(LocalDate.now())+".xlsx",simpleExcelSheetList);
    }
}
