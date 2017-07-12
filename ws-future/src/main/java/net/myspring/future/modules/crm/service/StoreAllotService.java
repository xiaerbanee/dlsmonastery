package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.basic.modules.sys.dto.CompanyConfigCacheDto;
import net.myspring.cloud.modules.kingdee.domain.StkInventory;
import net.myspring.cloud.modules.sys.dto.KingdeeSynReturnDto;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.common.exception.ServiceException;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestErrorField;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.ExpressOrderTypeEnum;
import net.myspring.future.common.enums.ShipTypeEnum;
import net.myspring.future.common.enums.StoreAllotStatusEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.ExpressUtils;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.DepotStore;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.manager.StkTransferDirectManager;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.DepotStoreRepository;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.crm.domain.*;
import net.myspring.future.modules.crm.dto.StoreAllotDetailDto;
import net.myspring.future.modules.crm.dto.StoreAllotDetailSimpleDto;
import net.myspring.future.modules.crm.dto.StoreAllotDto;
import net.myspring.future.modules.crm.dto.StoreAllotImeDto;
import net.myspring.future.modules.crm.manager.ExpressOrderManager;
import net.myspring.future.modules.crm.manager.RedisIdManager;
import net.myspring.future.modules.crm.repository.*;
import net.myspring.future.modules.crm.web.form.StoreAllotDetailForm;
import net.myspring.future.modules.crm.web.form.StoreAllotForm;
import net.myspring.future.modules.crm.web.form.StoreAllotShipForm;
import net.myspring.future.modules.crm.web.query.ProductImeShipQuery;
import net.myspring.future.modules.crm.web.query.StoreAllotQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class StoreAllotService {

    @Autowired
    private StoreAllotRepository storeAllotRepository;
    @Autowired
    private StoreAllotDetailRepository storeAllotDetailRepository;
    @Autowired
    private StoreAllotImeRepository storeAllotImeRepository;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ExpressOrderRepository expressOrderRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private ProductImeRepository productImeRepository;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CloudClient cloudClient;
    @Autowired
    private ExpressUtils expressUtils;
    @Autowired
    private DepotStoreRepository depotStoreRepository;
    @Autowired
    private StkTransferDirectManager stkTransferDirectManager;
    @Autowired
    private ExpressOrderManager expressOrderManager;
    @Autowired
    private RedisIdManager redisIdManager;

    public StoreAllotDto findDto(String id) {
        StoreAllotDto storeAllotDto = storeAllotRepository.findDto(id);
        cacheUtils.initCacheInput(storeAllotDto);
        return storeAllotDto;
    }

    @Transactional
    public void ship(StoreAllotShipForm storeAllotShipForm) {
        if(StringUtils.isBlank(storeAllotShipForm.getId())){
            throw new ServiceException("发货的调拨单id不能为空");
        }
        StoreAllot storeAllot = storeAllotRepository.findOne(storeAllotShipForm.getId());
        if(!StoreAllotStatusEnum.待发货.name().equals(storeAllot.getStatus()) && !StoreAllotStatusEnum.发货中.name().equals(storeAllot.getStatus())){
            throw new ServiceException("该调拨单的状态不为待发货或发货中，不能发货");
        }
        Map<String, StoreAllotDetail> storeAllotDetailMap = Maps.newHashMap();
        List<StoreAllotDetail> storeAllotDetailList = storeAllotDetailRepository.findByStoreAllotIdIn(Lists.newArrayList(storeAllotShipForm.getId()));
        Map<String,Product> productMap = productRepository.findMap(CollectionUtil.extractToList(storeAllotDetailList,"productId"));
        for (StoreAllotDetail storeAllotDetail : storeAllotDetailList) {
            if (storeAllotDetail.getBillQty() > 0 && !storeAllotDetail.getBillQty().equals( storeAllotDetail.getShippedQty())) {
                storeAllotDetailMap.put(storeAllotDetail.getProductId(), storeAllotDetail);
            }
            //对没有串码的货品全部发货
            Product product = productMap.get(storeAllotDetail.getProductId());
            if (!product.getHasIme() && storeAllotDetail.getShippedQty() == 0) {
                storeAllotDetail.setShippedQty(storeAllotDetail.getBillQty());
                storeAllotDetailRepository.save(storeAllotDetail);
            }
        }
        //搜索串码
        if (StringUtils.isNotBlank(storeAllotShipForm.getBoxImeStr()) || StringUtils.isNotBlank(storeAllotShipForm.getImeStr())) {
            ProductImeShipQuery productImeShipQuery = new ProductImeShipQuery();
            productImeShipQuery.setBoxImeList(storeAllotShipForm.getBoxImeList());
            productImeShipQuery.setImeList(storeAllotShipForm.getImeList());
            productImeShipQuery.setDepotId(storeAllot.getFromStoreId());

            List<ProductIme> productImeList = productImeRepository.findShipList(productImeShipQuery);
            if (CollectionUtil.isNotEmpty(productImeList)) {
                for (ProductIme productIme : productImeList) {
                    String productId = productIme.getProductId();
                    StoreAllotDetail storeAllotDetail = storeAllotDetailMap.get(productId);
                    if (storeAllotDetail != null) {
                        storeAllotDetail.setShippedQty(storeAllotDetail.getShippedQty() == null ? 0 : storeAllotDetail.getShippedQty() + 1);
                        StoreAllotIme storeAllotIme = new StoreAllotIme();
                        storeAllotIme.setStoreAllotId(storeAllot.getId());
                        storeAllotIme.setProductImeId(productIme.getId());
                        storeAllotIme.setProductId(productIme.getProductId());
                        storeAllotIme.setRemarks(storeAllotShipForm.getShipRemarks());
                        storeAllotImeRepository.save(storeAllotIme);
                        //串码调拨
                        productIme.setDepotId(storeAllot.getToStoreId());
                        productIme.setRetailShopId(storeAllot.getToStoreId());
                        productImeRepository.save(productIme);
                    }
                }
            }
        }
        storeAllotDetailRepository.save(storeAllotDetailMap.values());

        //如果所有发货完成，修改订单状态
        boolean isAllShipped = true;
        for (StoreAllotDetail storeAllotDetail : storeAllotDetailMap.values()) {
            if (!storeAllotDetail.getShippedQty().equals(storeAllotDetail.getBillQty())) {
                isAllShipped = false;
                break;
            }
        }
        if (isAllShipped) {
            storeAllot.setStatus(StoreAllotStatusEnum.已完成.name());
        } else {
            storeAllot.setStatus(StoreAllotStatusEnum.发货中.name());
        }
        storeAllot.setShipDate(LocalDateTime.now());
        storeAllotRepository.save(storeAllot);
        //设置快递单
        ExpressOrder expressOrder = expressOrderRepository.findOne(storeAllot.getExpressOrderId());
        expressOrderManager.save(ExpressOrderTypeEnum.大库调拨.name(),storeAllot.getId(), storeAllotShipForm.getExpressOrderExpressCodes(), expressOrder.getExpressCompanyId());
    }

    public Page<StoreAllotDto> findPage(Pageable pageable, StoreAllotQuery storeAllotQuery) {
        Page<StoreAllotDto> page = storeAllotRepository.findPage(pageable,storeAllotQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public SimpleExcelBook export(StoreAllotQuery storeAllotQuery) {

        Workbook workbook = new SXSSFWorkbook(10000);

        List<SimpleExcelSheet> simpleExcelSheetList = Lists.newArrayList();
        List<SimpleExcelColumn> storeAllotColumnList = Lists.newArrayList();
        storeAllotColumnList.add(new SimpleExcelColumn(workbook, "formatId", "单据编号"));
        storeAllotColumnList.add(new SimpleExcelColumn(workbook, "fromStoreName", "调拨前"));
        storeAllotColumnList.add(new SimpleExcelColumn(workbook, "toStoreName", "调拨后"));
        storeAllotColumnList.add(new SimpleExcelColumn(workbook, "outCode", "外部编号"));
        storeAllotColumnList.add(new SimpleExcelColumn(workbook, "status", "状态"));
        storeAllotColumnList.add(new SimpleExcelColumn(workbook, "createdByName", "创建人"));
        storeAllotColumnList.add(new SimpleExcelColumn(workbook, "createdDate", "创建时间"));
        storeAllotColumnList.add(new SimpleExcelColumn(workbook, "lastModifiedByName", "更新人"));
        storeAllotColumnList.add(new SimpleExcelColumn(workbook, "lastModifiedDate", "更新时间"));
        storeAllotColumnList.add(new SimpleExcelColumn(workbook, "remarks", "备注"));
        List<StoreAllotDto> storeAllotDtoList = findPage(new PageRequest(0,10000), storeAllotQuery).getContent();
        simpleExcelSheetList.add(new SimpleExcelSheet("调拨单", storeAllotDtoList, storeAllotColumnList));

        List<SimpleExcelColumn> storeAllotImeColumnList = Lists.newArrayList();
        storeAllotImeColumnList.add(new SimpleExcelColumn(workbook, "storeAllotFormatId", "订单编号"));
        storeAllotImeColumnList.add(new SimpleExcelColumn(workbook, "storeAllotOutCode", "外部编号"));
        storeAllotImeColumnList.add(new SimpleExcelColumn(workbook, "storeAllotBillDate", "开单日期"));
        storeAllotImeColumnList.add(new SimpleExcelColumn(workbook, "storeAllotFromStoreName", "发货仓库"));
        storeAllotImeColumnList.add(new SimpleExcelColumn(workbook, "storeAllotToStoreAreaName", "办事处"));
        storeAllotImeColumnList.add(new SimpleExcelColumn(workbook, "storeAllotToStoreName", "门店"));
        storeAllotImeColumnList.add(new SimpleExcelColumn(workbook, "productName", "产品名称"));
        storeAllotImeColumnList.add(new SimpleExcelColumn(workbook, "productImeIme", "串码"));
        List<StoreAllotImeDto> storeAllotImeDtoList = storeAllotImeRepository.findDtoListByStoreAllotIdList(CollectionUtil.extractToList(storeAllotDtoList, "id"), 10000);
        cacheUtils.initCacheInput(storeAllotImeDtoList);
        simpleExcelSheetList.add(new SimpleExcelSheet("串码", storeAllotImeDtoList, storeAllotImeColumnList));

        ExcelUtils.doWrite(workbook,simpleExcelSheetList);
        return new SimpleExcelBook(workbook,"大库调拨"+LocalDate.now()+".xlsx", simpleExcelSheetList);
    }

    @Transactional
    public void save(StoreAllotForm storeAllotForm) {
        //大库调拨单只允许新增和删除，不能修改
        if(!storeAllotForm.isCreate()){
            throw new ServiceException("大库调拨不允许修改");
        }

        Map<String, Product> productMap = productRepository.findMap(CollectionUtil.extractToList(storeAllotForm.getStoreAllotDetailList(), "productId"));
        StoreAllot storeAllot = saveStoreAllot(storeAllotForm);
        List<StoreAllotDetail> storeAllotDetailList = saveStoreAllotDetails(storeAllot.getId(), storeAllotForm.getStoreAllotDetailList());
        ExpressOrder expressOrder = saveExpressOrder(storeAllot, storeAllotForm, productMap);

        if(Boolean.TRUE.equals(storeAllotForm.getSyn())){
            synToCloud(storeAllot, storeAllotDetailList, expressOrder, productMap);
        }
    }

    @Transactional
    private void synToCloud(StoreAllot storeAllot, List<StoreAllotDetail> detailList, ExpressOrder expressOrder, Map<String, Product> productMap){
        KingdeeSynReturnDto kingdeeSynReturnDto = stkTransferDirectManager.synForStoreAllot(storeAllot,detailList,productMap);

        storeAllot.setCloudSynId(kingdeeSynReturnDto.getId());
        storeAllot.setOutCode(kingdeeSynReturnDto.getBillNo());
        storeAllotRepository.save(storeAllot);

        expressOrder.setOutCode(kingdeeSynReturnDto.getBillNo());
        expressOrderRepository.save(expressOrder);

    }

    @Transactional
    private StoreAllot saveStoreAllot(StoreAllotForm storeAllotForm) {
        StoreAllot storeAllot = new StoreAllot();
        storeAllot.setFromStoreId(storeAllotForm.getFromStoreId());
        storeAllot.setToStoreId(storeAllotForm.getToStoreId());
        storeAllot.setShipType(storeAllotForm.getShipType());
        storeAllot.setRemarks(storeAllotForm.getRemarks());
        LocalDate now = LocalDate.now();
        storeAllot.setBusinessId(redisIdManager.getNextStoreAllotBusinessId(now));
        storeAllot.setBillDate(now);
        storeAllot.setStatus(StoreAllotStatusEnum.待发货.name());

        storeAllotRepository.save(storeAllot);
        return storeAllot;
    }

    @Transactional
    private List<StoreAllotDetail> saveStoreAllotDetails(String storeAllotId, List<StoreAllotDetailForm> detailFormList) {
        //大库调拨只有新增，没有修改

        List<StoreAllotDetail> toBeSaved = Lists.newArrayList();
        for(StoreAllotDetailForm storeAllotDetailForm : detailFormList){
            if(storeAllotDetailForm == null || storeAllotDetailForm.getBillQty() == null || storeAllotDetailForm.getBillQty() <=0){
                throw new ServiceException("大库的调拨明细里，调拨数量必须大于0");
            }
            StoreAllotDetail storeAllotDetail = new StoreAllotDetail();
            storeAllotDetail.setStoreAllotId(storeAllotId);
            storeAllotDetail.setBillQty(storeAllotDetailForm.getBillQty());
            storeAllotDetail.setQty(storeAllotDetailForm.getBillQty());
            storeAllotDetail.setShippedQty(0);
            storeAllotDetail.setProductId(storeAllotDetailForm.getProductId());

            toBeSaved.add(storeAllotDetail);
        }
        storeAllotDetailRepository.save(toBeSaved);

        return toBeSaved;
    }

    @Transactional
    private ExpressOrder saveExpressOrder(StoreAllot storeAllot, StoreAllotForm storeAllotForm, Map<String, Product> productMap) {

        ExpressOrder expressOrder = new ExpressOrder();
        expressOrder.setExtendBusinessId(storeAllot.getBusinessId());
        expressOrder.setExtendId(storeAllot.getId());
        expressOrder.setExtendType(ExpressOrderTypeEnum.大库调拨.name());
        expressOrder.setExpressCompanyId(storeAllotForm.getExpressCompanyId());
        expressOrder.setFromDepotId(storeAllot.getFromStoreId());
        expressOrder.setToDepotId(storeAllot.getToStoreId());
        expressOrder.setPrintDate(storeAllot.getBillDate());
        expressOrder.setOutCode(storeAllot.getOutCode());

        Depot toStore = depotRepository.findOne(storeAllot.getToStoreId());
        expressOrder.setAddress(toStore.getAddress());
        expressOrder.setMobilePhone(toStore.getMobilePhone());
        expressOrder.setContator(toStore.getContator());

        Integer totalBillQty = 0;
        Integer mobileQty = 0;
        for(int i = storeAllotForm.getStoreAllotDetailList().size()-1; i>=0; i--){
            StoreAllotDetailForm storeAllotDetailForm = storeAllotForm.getStoreAllotDetailList().get(i);
            if(storeAllotDetailForm.getBillQty()!=null && storeAllotDetailForm.getBillQty()>0) {
                totalBillQty = totalBillQty + storeAllotDetailForm.getBillQty();
                if(productMap.get(storeAllotDetailForm.getProductId()) != null && productMap.get(storeAllotDetailForm.getProductId()).getHasIme()) {
                    mobileQty = mobileQty + storeAllotDetailForm.getBillQty();
                }
            }
        }
        expressOrder.setTotalQty(totalBillQty);
        expressOrder.setMobileQty(mobileQty);

        //设置需要打印的快递单个数
        Integer expressPrintQty = 0;
        if (ShipTypeEnum.总部发货.name().equals(storeAllot.getShipType())) {
            expressPrintQty = expressUtils.getExpressPrintQty(totalBillQty);
        }
        expressOrder.setExpressPrintQty(expressPrintQty);
        expressOrderRepository.save(expressOrder);

        storeAllot.setExpressOrderId(expressOrder.getId());
        storeAllotRepository.save(storeAllot);

        return expressOrder;
    }

    public StoreAllotDto findStoreAllotDtoById(String id) {
        StoreAllotDto result = storeAllotRepository.findDto(id);
        cacheUtils.initCacheInput(result);
        return result;

    }

    @Transactional
    public void delete(String id) {
        storeAllotRepository.logicDelete(id);
    }

    public List<StoreAllotDetailSimpleDto> findDetailListForCommonAllot(String fromStoreId) {
        List<StoreAllotDetailSimpleDto> result =  storeAllotDetailRepository.findStoreAllotDetailListForNew();
        cacheUtils.initCacheInput(result);
        if(StringUtils.isNotBlank(fromStoreId)){
            fulfillCloudQty(fromStoreId, result);
        }
        return result;
    }

    public List<String> getMergeStoreIds(){

        CompanyConfigCacheDto companyConfigCacheDto =  CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.MERGE_STORE_IDS.name());
        if(companyConfigCacheDto == null || StringUtils.isBlank(companyConfigCacheDto.getValue())){
            return null;
        }

        return StringUtils.getSplitList(companyConfigCacheDto.getValue(), ",");
    }

    public List<StoreAllotDetailSimpleDto> findDetailListForFastAllot() {

        List<String> mergeIdList = getMergeStoreIds();
        if(mergeIdList == null || mergeIdList.size() <=1){
            throw  new ServiceException("没有正确配置该公司的MERGE_STORE_IDS");
        }
        String fromStoreId =mergeIdList.get(0);
        String toStoreId =mergeIdList.get(1);

        List<StoreAllotDetailSimpleDto> result = storeAllotDetailRepository.findStoreAllotDetailsForFastAllot(LocalDate.now(), toStoreId, "待发货");
        cacheUtils.initCacheInput(result);
        if(StringUtils.isNotBlank(fromStoreId)){
            fulfillCloudQty(fromStoreId, result);
        }

        result.forEach((each)->{
            if(each.getBillQty() != null && each.getBillQty()==0 ) {
                each.setBillQty(null);
            }
        });

        return result;
    }

    private void fulfillCloudQty(String fromStoreId, List<StoreAllotDetailSimpleDto> list) {
        DepotStore depotStore = depotStoreRepository.findByEnabledIsTrueAndDepotId(fromStoreId);
        List<StkInventory> inventoryList = cloudClient.findInventoriesByDepotStoreOutIds(Collections.singletonList(depotStore.getOutId()));
        Map<String, StkInventory> inventoryMap = CollectionUtil.extractToMap(inventoryList, "FMaterialId");
        for(StoreAllotDetailSimpleDto storeAllotDetailSimpleDto : list){
            if(inventoryMap.containsKey(storeAllotDetailSimpleDto.getProductOutId())){
                storeAllotDetailSimpleDto.setCloudQty(inventoryMap.get(storeAllotDetailSimpleDto.getProductOutId()).getFBaseQty());
            }
        }
    }

    public Boolean getShowAllotType() {
        return getMergeStoreIds() != null;
    }

    @Transactional
    public StoreAllotDto print(String id) {
        StoreAllot storeAllot = storeAllotRepository.findOne(id);

        if(StringUtils.isNotBlank(storeAllot.getExpressOrderId())){
            ExpressOrder expressOrder = expressOrderRepository.findOne(storeAllot.getExpressOrderId());
            if(expressOrder != null && expressOrder.getOutPrintDate() == null){
                expressOrder.setOutPrintDate(LocalDateTime.now());
                expressOrderRepository.saveAndFlush(expressOrder);
            }
        }

        return findDto(id);
    }

    @Transactional
    public StoreAllotDto shipPrint(String id) {
        StoreAllot storeAllot = storeAllotRepository.findOne(id);

        if(StringUtils.isNotBlank(storeAllot.getExpressOrderId())){
            ExpressOrder expressOrder = expressOrderRepository.findOne(storeAllot.getExpressOrderId());
            if(expressOrder != null && expressOrder.getExpressPrintDate() == null){
                expressOrder.setExpressPrintDate(LocalDateTime.now());
                expressOrderRepository.saveAndFlush(expressOrder);
            }
        }

        return findDto(id);
    }

    public Map<String,Object> shipCheck(StoreAllotShipForm storeAllotShipForm) {
        RestResponse restResponse =  new RestResponse("valid", ResponseCodeEnum.valid.name());
        Integer totalShouldShipQty = 0;
        Integer totalShippedQty = 0;
        StoreAllot storeAllot = storeAllotRepository.findOne(storeAllotShipForm.getId());
        List<StoreAllotDetail> storeAllotDetailList  = storeAllotDetailRepository.findByStoreAllotIdIn(Lists.newArrayList(storeAllot.getId()));
        List<StoreAllotDetailDto> storeAllotDetailDtoList = BeanUtil.map(storeAllotDetailList,StoreAllotDetailDto.class);
        Map<String,StoreAllotDetailDto> storeAllotDetailMap  = CollectionUtil.extractToMap(storeAllotDetailDtoList,"productId");
        Map<String,Product> productMap = productRepository.findMap(CollectionUtil.extractToList(storeAllotDetailList,"productId"));
        for (StoreAllotDetailDto storeAllotDetailDto : storeAllotDetailDtoList) {
            if (storeAllotDetailDto.getBillQty() > 0 && productMap.get(storeAllotDetailDto.getProductId()).getHasIme()) {
                storeAllotDetailDto.setShipQty(0);
            }
        }
        ProductImeShipQuery productImeShipQuery = new ProductImeShipQuery();
        productImeShipQuery.setDepotId(storeAllot.getFromStoreId());
        productImeShipQuery.setImeList(storeAllotShipForm.getImeList());
        productImeShipQuery.setBoxImeList(storeAllotShipForm.getBoxImeList());
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
            if(product==null || !storeAllotDetailMap.containsKey(product.getId()) ){
                restResponse.getErrors().add(new RestErrorField("箱号：" + productIme.getBoxIme() +"，串码：" + productIme.getIme() + "的货品不在调拨范围内","ime_error","imeStr"));
            } else {
                storeAllotDetailMap.get(product.getId()).setShipQty(storeAllotDetailMap.get(product.getId()).getShipQty() + 1);
            }

        }
        for (String boxIme : storeAllotShipForm.getBoxImeList()) {
            if (!boxImeSet.contains(boxIme)) {
                restResponse.getErrors().add(new RestErrorField("箱号：" + boxIme  + "，在仓库不存在","box_ime_error","boxIme"));
            }
        }
        for (String ime : storeAllotShipForm.getImeList()) {
            if (!imeSet.contains(ime)) {
                restResponse.getErrors().add(new RestErrorField("串码：" + ime + "，在仓库不存在","ime_error","ime"));
            }
        }
        for (StoreAllotDetailDto storeAllotDetailDto: storeAllotDetailDtoList) {
            //仅比对包含串码的产品的发货数量，不包含串码的产品，默认全部发货
            if(productMap.get(storeAllotDetailDto.getProductId()).getHasIme()){
                totalShouldShipQty = totalShouldShipQty +storeAllotDetailDto.getBillQty();
                totalShippedQty = totalShippedQty + storeAllotDetailDto.getShippedQty() + storeAllotDetailDto.getShipQty();
            }

            Integer qty = storeAllotDetailDto.getShippedQty() + storeAllotDetailDto.getShipQty();
            if (qty > storeAllotDetailDto.getBillQty()) {
                StringBuilder errorMessage = new StringBuilder("货品:"+ productMap.get(storeAllotDetailDto.getProductId()).getName() +"总发货数："+ qty+ "大于实际调拨数："  + storeAllotDetailDto.getBillQty() + "串码：");
                //显示发货不对的串码
                for (ProductIme productIme : productImeList) {
                    if (productIme.getProductId().equals(storeAllotDetailDto.getProductId())) {
                        errorMessage.append(productIme.getIme()).append(CharConstant.TAB);
                    }
                }
                restResponse.getErrors().add(new RestErrorField(errorMessage.toString(),"qty_error","ime"));
            }
        }
        Map<String, Object> result = Maps.newHashMap();
        if (!totalShouldShipQty.equals(totalShippedQty)) {
            result.put("warnMsg", "货品总调拨数"+totalShouldShipQty+ ",和实际发货数"+totalShippedQty + "不一致");
        }
        Map<String, Integer> shipQtyMap = Maps.newHashMap();
        for (StoreAllotDetailDto storeAllotDetailDto : storeAllotDetailMap.values()) {
            shipQtyMap.put(storeAllotDetailDto.getProductId(), storeAllotDetailDto.getShipQty());
        }
        result.put("restResponse", restResponse);
        result.put("shipQtyMap", shipQtyMap);
        result.put("totalQty", productImeList.size());
        return result;
    }
}
