package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mongodb.gridfs.GridFSFile;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.basic.modules.sys.dto.CompanyConfigCacheDto;
import net.myspring.cloud.common.enums.ExtendTypeEnum;
import net.myspring.cloud.modules.input.dto.StkTransferDirectDto;
import net.myspring.cloud.modules.input.dto.StkTransferDirectFBillEntryDto;
import net.myspring.cloud.modules.sys.dto.KingdeeSynReturnDto;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.common.exception.ServiceException;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.ExpressOrderTypeEnum;
import net.myspring.future.common.enums.ShipTypeEnum;
import net.myspring.future.common.enums.StoreAllotStatusEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.domain.StoreAllot;
import net.myspring.future.modules.crm.domain.StoreAllotDetail;
import net.myspring.future.modules.crm.dto.StoreAllotDetailDto;
import net.myspring.future.modules.crm.dto.StoreAllotDetailSimpleDto;
import net.myspring.future.modules.crm.dto.StoreAllotDto;
import net.myspring.future.modules.crm.dto.StoreAllotImeDto;
import net.myspring.future.modules.crm.repository.*;
import net.myspring.future.modules.crm.web.form.StoreAllotDetailForm;
import net.myspring.future.modules.crm.web.form.StoreAllotForm;
import net.myspring.future.modules.crm.web.query.ProductImeShipQuery;
import net.myspring.future.modules.crm.web.query.StoreAllotQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.IdUtils;
import net.myspring.util.text.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
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
    private GridFsTemplate tempGridFsTemplate;
    @Autowired
    private ProductImeRepository productImeRepository;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CloudClient cloudClient;

    public StoreAllotDto findDto(String id) {
        StoreAllotDto storeAllotDto = storeAllotRepository.findDto(id);
        cacheUtils.initCacheInput(storeAllotDto);
        return storeAllotDto;
    }

    public Map<String, Object> shipBoxAndIme(String storeAllotId, String boxImeStr, String imeStr) {

        StringBuilder sb = new StringBuilder();
        Integer totalShouldShipQty = 0;
        Integer totalShippedQty=0;
        StoreAllotDto storeAllotDto = storeAllotRepository.findDto(storeAllotId);
        cacheUtils.initCacheInput(storeAllotDto);
        final List<String> boxImeList = StringUtils.getSplitList(boxImeStr, CharConstant.ENTER);
        final List<String> imeList = StringUtils.getSplitList(imeStr, CharConstant.ENTER);
        String regex="^\\d*";
        for(int i=0;i<imeList.size();i++){
            String ime=imeList.get(i);
            if(ime.startsWith("86")){
                Pattern pattern=Pattern.compile(regex);
                Matcher matcher=pattern.matcher(ime);
                while(matcher.find()){
                    String newStr=matcher.group();
                    imeList.set(i,newStr);
                }
            }
        }

        final String fromStoreId = storeAllotDto.getFromStoreId();
        Map<String, StoreAllotDetailDto> storeAllotDetailMap = Maps.newHashMap();
        List<StoreAllotDetailDto> storeAllotDetailDtoList = storeAllotDetailRepository.findByStoreAllotIds(Lists.newArrayList(storeAllotId));
        cacheUtils.initCacheInput(storeAllotDetailDtoList);
        for(StoreAllotDetailDto storeAllotDetailDto : storeAllotDetailDtoList) {
            if(storeAllotDetailDto.getBillQty()>0 && storeAllotDetailDto.getProductHasIme()){
                storeAllotDetailDto.setShipQty(0);
                storeAllotDetailMap.put(storeAllotDetailDto.getProductId(), storeAllotDetailDto);
            }
        }
        ProductImeShipQuery productImeShipQuery = new ProductImeShipQuery();
        productImeShipQuery.setDepotId(fromStoreId);
        productImeShipQuery.setBoxImeList(boxImeList);
        productImeShipQuery.setImeList(imeList);
        productImeShipQuery.setCompanyId(RequestUtils.getCompanyId());

        List<ProductIme> productImeList = productImeRepository.findShipList(productImeShipQuery);
        Set<String> boxImeSet = Sets.newHashSet();
        Set<String> imeSet = Sets.newHashSet();

        for(ProductIme productIme : productImeList) {
            boxImeSet.add(productIme.getBoxIme());
            imeSet.add(productIme.getIme());
            if(!storeAllotDetailMap.containsKey(productIme.getProductId())) {
                sb.append("箱号  ").append(productIme.getBoxIme()).append(",串码  ").append(productIme.getIme()).append(" 不属于此次要调拨的产品；");
            } else {
                storeAllotDetailMap.get(productIme.getProductId()).setShipQty(storeAllotDetailMap.get(productIme.getProductId()).getShipQty()+1);
            }
        }
        for(String boxIme:boxImeList) {
            if(!boxImeSet.contains(boxIme)) {
                sb.append("箱号  ").append(boxIme).append(" 不在选定的仓库  ").append(storeAllotDto.getFromStoreName()).append("；");
            }
        }
        for(String ime : imeList){
            if(!imeSet.contains(ime)) {
                sb.append("串码  ").append(ime).append(" 不在选定的仓库  ").append(storeAllotDto.getFromStoreName()).append("；");
            }
        }
        for(StoreAllotDetailDto storeAllotDetailDto : storeAllotDetailMap.values()) {
            totalShouldShipQty = totalShouldShipQty + storeAllotDetailDto.getBillQty();
            totalShippedQty = totalShippedQty + storeAllotDetailDto.getShippedQty()+storeAllotDetailDto.getShipQty();
            Integer qty = storeAllotDetailDto.getShippedQty()+storeAllotDetailDto.getShipQty();
            if(qty > storeAllotDetailDto.getBillQty()){
                sb.append("产品 ").append(storeAllotDetailDto.getProductName()).append(" 已经发货 ").append(qty.toString()).append(" 台，超过该产品的调拨量  ").append(storeAllotDetailDto.getBillQty().toString()).append(" 台；");
            }
        }
        Map<String,Object> result = Maps.newHashMap();
        if(!totalShouldShipQty.equals(totalShippedQty)) {
            result.put("warnMsg", "货品总开单数为" + totalShouldShipQty + "，与总实际发货数为"+totalShippedQty + "不一致；");
        }
        Map<String, Integer> shipQtyMap = Maps.newHashMap();
        for(StoreAllotDetailDto storeAllotDetailDto : storeAllotDetailMap.values()) {
            shipQtyMap.put(storeAllotDetailDto.getProductId(), storeAllotDetailDto.getShipQty());
        }
        result.put("errMsg", sb.toString());
        result.put("shipQtyMap", shipQtyMap);
        result.put("totalQty", productImeList.size());

        return result;
    }

    public void ship(StoreAllot storeAllot) {

    }

    public Integer getCloudQty(String productId,String fromStoreId){
        return null;
    }

    public Page<StoreAllotDto> findPage(Pageable pageable, StoreAllotQuery storeAllotQuery) {
        Page<StoreAllotDto> page = storeAllotRepository.findPage(pageable,storeAllotQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;

    }

    public String export(StoreAllotQuery storeAllotQuery) {

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

        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"大库调拨"+LocalDate.now()+".xlsx", simpleExcelSheetList);
        ByteArrayInputStream byteArrayInputStream= ExcelUtils.doWrite(simpleExcelBook.getWorkbook(),simpleExcelBook.getSimpleExcelSheets());
        GridFSFile gridFSFile = tempGridFsTemplate.store(byteArrayInputStream,simpleExcelBook.getName(),"application/octet-stream; charset=utf-8", RequestUtils.getDbObject());
        return StringUtils.toString(gridFSFile.getId());

    }

    public StoreAllot saveForm(StoreAllotForm storeAllotForm) {
        //大库调拨单只允许新增和删除，不能修改
        if(!storeAllotForm.isCreate()){
            throw new ServiceException("大库调拨不允许修改");
        }

        if(storeAllotForm.getSyn()){
            //TODO 金蝶接口调用，调用成功之后设置cloudSynId
            StoreAllotDto storeAllotDto = BeanUtil.map(storeAllotForm,StoreAllotDto.class);
            KingdeeSynReturnDto returnDto = synToCloud(storeAllotDto);
        }

        StoreAllot storeAllot = saveStoreAllot(storeAllotForm);
        saveStoreAllotDetails(storeAllot.getId(), storeAllotForm.getStoreAllotDetailList());
        ExpressOrder expressOrder = saveExpressOrder(storeAllot, storeAllotForm);

        storeAllot.setExpressOrderId(expressOrder.getId());
//       TODO  需要设置财务返回的id storeAllot.setCloudSynId();
        storeAllotRepository.save(storeAllot);

        return storeAllot;
    }

    private KingdeeSynReturnDto synToCloud(StoreAllotDto storeAllotDto){
        StkTransferDirectDto transferDirectDto = new StkTransferDirectDto();
        transferDirectDto.setExtendId(storeAllotDto.getId());
        transferDirectDto.setExtendType(ExtendTypeEnum.大库调拨.name());
        transferDirectDto.setNote(storeAllotDto.getRemarks());
        transferDirectDto.setDate(storeAllotDto.getBillDate());
        List<StoreAllotDetailDto> storeAllotDetailList = storeAllotDto.getStoreAllotDetailDtoList();
        for (StoreAllotDetailDto detailDto : storeAllotDetailList){
            StkTransferDirectFBillEntryDto entryDto = new StkTransferDirectFBillEntryDto();
            entryDto.setQty(detailDto.getQty());
            entryDto.setMaterialNumber("");
            entryDto.setSrcStockNumber("");
            entryDto.setDestStockNumber("");
            transferDirectDto.getStkTransferDirectFBillEntryDtoList().add(entryDto);
        }
        return cloudClient.synStkTransferDirect(transferDirectDto);
    }

    private StoreAllot saveStoreAllot(StoreAllotForm storeAllotForm) {
        StoreAllot storeAllot = new StoreAllot();
        storeAllot.setFromStoreId(storeAllotForm.getFromStoreId());
        storeAllot.setToStoreId(storeAllotForm.getToStoreId());
        storeAllot.setShipType(storeAllotForm.getShipType());
        storeAllot.setRemarks(storeAllotForm.getRemarks());
        String maxBusinessId = storeAllotRepository.findMaxBusinessId(LocalDate.now().atStartOfDay());
        storeAllot.setBusinessId(IdUtils.getNextBusinessId(maxBusinessId));
        storeAllot.setBillDate(LocalDate.now());
        storeAllot.setStatus(StoreAllotStatusEnum.待发货.name());

        storeAllotRepository.save(storeAllot);
        return storeAllot;
    }

    private void saveStoreAllotDetails(String storeAllotId, List<StoreAllotDetailForm> storeAllotDetailList) {
        //大库调拨只有新增，没有修改

        List<StoreAllotDetail> toBeSaved = Lists.newArrayList();
        for(StoreAllotDetailForm storeAllotDetailForm : storeAllotDetailList){
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
    }

    private ExpressOrder saveExpressOrder(StoreAllot storeAllot, StoreAllotForm storeAllotForm) {
        //增加快递单信息
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
                Product product = productRepository.findOne(storeAllotDetailForm.getProductId());
                if(product!=null && product.getHasIme()) {
                    mobileQty = mobileQty + storeAllotDetailForm.getBillQty();
                }
            }
        }
        expressOrder.setTotalQty(totalBillQty);
        expressOrder.setMobileQty(mobileQty);

        //设置需要打印的快递单个数
        Integer expressPrintQty = 0;
        if (ShipTypeEnum.总部发货.name().equals(storeAllot.getShipType())) {
            expressPrintQty = getExpressPrintQty(totalBillQty);
        }
        expressOrder.setExpressPrintQty(expressPrintQty);
        expressOrderRepository.save(expressOrder);
        return expressOrder;

    }

    private Integer getExpressPrintQty(Integer totalBillQty) {

        Integer expressPrintQty = 20; //TODO JXOPPO默認爲20  不同的公司不同，這個需要在上其它公司的時候判斷

        if(0 == totalBillQty % expressPrintQty){
            expressPrintQty = totalBillQty / expressPrintQty;
        } else{
            expressPrintQty = totalBillQty / expressPrintQty + 1;
        }
        return expressPrintQty;
    }


    public StoreAllotDto findStoreAllotDtoById(String id) {
        StoreAllotDto result = storeAllotRepository.findDto(id);
        cacheUtils.initCacheInput(result);
        return result;

    }

    public void delete(String id) {
        storeAllotRepository.logicDelete(id);
    }

    public List<StoreAllotDetailSimpleDto> findDetailListForCommonAllot(String fromStoreId) {

        List<StoreAllotDetailSimpleDto> result =  storeAllotDetailRepository.findStoreAllotDetailListForNew(RequestUtils.getCompanyId());
        cacheUtils.initCacheInput(result);

        if(StringUtils.isNotBlank(fromStoreId)){
//        TODO 初始化财务存量
        }

        return result;
    }

    public List<String> getMergeStoreIds(){

        CompanyConfigCacheDto companyConfigCacheDto =  CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.MERGE_STORE_IDS.name());
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
        String toStoreId =mergeIdList.get(1);

        List<StoreAllotDetailSimpleDto> result = storeAllotDetailRepository.findStoreAllotDetailsForFastAllot(LocalDate.now(), toStoreId, "待发货", RequestUtils.getCompanyId());

        cacheUtils.initCacheInput(result);
        //TODO 初始化财务存量

        result.forEach((each)->{
            if(each.getBillQty() != null && each.getBillQty()<=0 ) {
                each.setBillQty(null);
            }
        });


        return result;

    }

    public Boolean getShowAllotType() {
        return getMergeStoreIds() != null;
    }

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
}
