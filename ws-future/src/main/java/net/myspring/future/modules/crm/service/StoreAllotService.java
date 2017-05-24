package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mongodb.gridfs.GridFSFile;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.enums.ExpressOrderTypeEnum;
import net.myspring.future.common.enums.ShipTypeEnum;
import net.myspring.future.common.enums.StoreAllotStatusEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.basic.mapper.ProductMapper;
import net.myspring.future.modules.basic.repository.*;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.domain.StoreAllot;
import net.myspring.future.modules.crm.domain.StoreAllotDetail;
import net.myspring.future.modules.crm.dto.SimpleStoreAllotDetailDto;
import net.myspring.future.modules.crm.dto.StoreAllotDetailDto;
import net.myspring.future.modules.crm.dto.StoreAllotDto;
import net.myspring.future.modules.crm.dto.StoreAllotImeDto;
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
import net.myspring.util.time.LocalDateUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
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
    private DepotMapper depotMapper;
    @Autowired
    private ProductMapper productMapper;


    @Autowired
    private ExpressOrderRepository expressOrderRepository;

    @Autowired
    private CacheUtils cacheUtils;

    @Autowired
    private GridFsTemplate tempGridFsTemplate;

    @Autowired
    private ProductImeRepository productImeRepository;


    public StoreAllotDto findDto(String id) {
        StoreAllotDto storeAllotDto = storeAllotRepository.findStoreAllotDtoById(id);
        cacheUtils.initCacheInput(storeAllotDto);
        return storeAllotDto;
    }

    public Map<String, Object> shipBoxAndIme(String storeAllotId, String boxImeStr, String imeStr) {

        StringBuffer stringBuffer = new StringBuffer();
        Integer totalShouldShipQty = 0;
        Integer totalShippedQty=0;
        StoreAllotDto storeAllotDto = storeAllotRepository.findStoreAllotDtoById(storeAllotId);
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
                stringBuffer.append("箱号  "+productIme.getBoxIme()+",串码  "+ productIme.getIme()+" 不属于此次要调拨的产品；");
            } else {
                storeAllotDetailMap.get(productIme.getProductId()).setShipQty(storeAllotDetailMap.get(productIme.getProductId()).getShipQty()+1);
            }
        }
        for(String boxIme:boxImeList) {
            if(!boxImeSet.contains(boxIme)) {
                stringBuffer.append("箱号  " + boxIme + " 不在选定的仓库  "+storeAllotDto.getFromStoreName()+"；");
            }
        }
        for(String ime : imeList){
            if(!imeSet.contains(ime)) {
                stringBuffer.append("串码  " + ime + " 不在选定的仓库  "+storeAllotDto.getFromStoreName()+"；");
            }
        }
        for(StoreAllotDetailDto storeAllotDetailDto : storeAllotDetailMap.values()) {
            totalShouldShipQty = totalShouldShipQty + storeAllotDetailDto.getBillQty();
            totalShippedQty = totalShippedQty + storeAllotDetailDto.getShippedQty()+storeAllotDetailDto.getShipQty();
            Integer qty = storeAllotDetailDto.getShippedQty()+storeAllotDetailDto.getShipQty();
            if(qty > storeAllotDetailDto.getBillQty()){
                stringBuffer.append("产品 " + storeAllotDetailDto.getProductName()+" 已经发货 " + qty.toString() + " 台，超过该产品的调拨量  "+storeAllotDetailDto.getBillQty().toString()+" 台；");
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
        result.put("errMsg", stringBuffer.toString());
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


        Specification<StoreAllot> specification = (root, query, cb) -> {
            List<Predicate> predicates  = Lists.newArrayList();
            predicates.add(cb.equal(root.get("companyId").as(String.class), RequestUtils.getCompanyId()));
            if(storeAllotQuery.getCreatedDateStart() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdDate").as(LocalDateTime.class),storeAllotQuery.getCreatedDateStart()));
            }
            if(storeAllotQuery.getCreatedDateEnd() != null) {
                predicates.add(cb.lessThan(root.get("createdDate").as(LocalDateTime.class),storeAllotQuery.getCreatedDateEnd()));
            }
            if(storeAllotQuery.getOutCode() != null) {
                predicates.add(cb.like(root.get("outCode").as(String.class), storeAllotQuery.getOutCode()));
            }
            if(storeAllotQuery.getToStoreId() != null) {
                predicates.add(cb.equal(root.get("toStoreId").as(String.class), storeAllotQuery.getToStoreId()));
            }
            if(storeAllotQuery.getFromStoreId() != null) {
                predicates.add(cb.equal(root.get("fromStoreId").as(String.class), storeAllotQuery.getFromStoreId()));
            }
            if(storeAllotQuery.getCreatedBy() != null) {
                predicates.add(cb.equal(root.get("createdBy").as(String.class), storeAllotQuery.getCreatedBy()));
            }
            if(storeAllotQuery.getStatus() != null) {
                predicates.add(cb.equal(root.get("status").as(String.class), storeAllotQuery.getStatus()));
            }
            if(storeAllotQuery.getRemarks() != null) {
                predicates.add(cb.like(root.get("remarks").as(String.class), storeAllotQuery.getRemarks()));
            }
            if(storeAllotQuery.getBusinessIdList() != null) {
                predicates.add(cb.isTrue(root.get("businessId").as(String.class).in(storeAllotQuery.getBusinessIdList())));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };

        Page<StoreAllotDto> page = BeanUtil.map(storeAllotRepository.findAll(specification, pageable), StoreAllotDto.class);
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
            throw new ServiceException("error.storeAllot.cantEdit");
        }

        if(storeAllotForm.getSyn()){
            //TODO 金蝶接口调用，调用成功之后设置cloudSynId
            // cloudSynId=
        }

        StoreAllot storeAllot = saveStoreAllot(storeAllotForm);
        saveStoreAllotDetails(storeAllot.getId(), storeAllotForm.getStoreAllotDetailList());
        ExpressOrder expressOrder = saveExpressOrder(storeAllot, storeAllotForm);

        storeAllot.setExpressOrderId(expressOrder.getId());
//       TODO  需要设置财务返回的id storeAllot.setCloudSynId();
        storeAllotRepository.save(storeAllot);

        return storeAllot;
    }

    private StoreAllot saveStoreAllot(StoreAllotForm storeAllotForm) {
        StoreAllot storeAllot = new StoreAllot();
        storeAllot.setFromStoreId(storeAllotForm.getFromStoreId());
        storeAllot.setToStoreId(storeAllotForm.getToStoreId());
        storeAllot.setShipType(storeAllotForm.getShipType());
        storeAllot.setRemarks(storeAllotForm.getRemarks());
        String maxBusinessId = storeAllotRepository.findMaxBusinessId(LocalDate.now());
        storeAllot.setBusinessId(IdUtils.getNextBusinessId(maxBusinessId));
        storeAllot.setBillDate(LocalDate.now());
        storeAllot.setStatus(StoreAllotStatusEnum.待发货.name());

        storeAllotRepository.save(storeAllot);
        return storeAllot;
    }

    private void saveStoreAllotDetails(String storeAllotId, List<StoreAllotDetailForm> storeAllotDetailList) {
        storeAllotDetailRepository.deleteByStoreAllotId(storeAllotId);
        if(storeAllotDetailList == null){
            return;
        }

        List<StoreAllotDetail> toBeSaved = Lists.newArrayList();
        for(StoreAllotDetailForm storeAllotDetailForm : storeAllotDetailList){
            if(storeAllotDetailForm == null || storeAllotDetailForm.getBillQty() == null || storeAllotDetailForm.getBillQty() <=0){
                continue;
            }
            StoreAllotDetail storeAllotDetail = new StoreAllotDetail();
            storeAllotDetail.setStoreAllotId(storeAllotId);
            storeAllotDetail.setBillQty(storeAllotDetailForm.getBillQty());
            storeAllotDetail.setProductId(storeAllotDetailForm.getProductId());

            toBeSaved.add(storeAllotDetail);
        }
        if(!toBeSaved.isEmpty()){
            storeAllotDetailRepository.save(toBeSaved);
        }

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

        Depot toStore = depotMapper.findOne(storeAllot.getToStoreId());
        expressOrder.setAddress(toStore.getAddress());
        expressOrder.setMobilePhone(toStore.getMobilePhone());
        expressOrder.setContator(toStore.getContator());

        Integer totalBillQty = 0;
        Integer mobileQty = 0;
        for(int i = storeAllotForm.getStoreAllotDetailList().size()-1; i>=0; i--){
            StoreAllotDetailForm storeAllotDetailForm = storeAllotForm.getStoreAllotDetailList().get(i);
            if(storeAllotDetailForm.getBillQty()!=null && storeAllotDetailForm.getBillQty()>0) {
                totalBillQty = totalBillQty + storeAllotDetailForm.getBillQty();
                Product product = productMapper.findOne(storeAllotDetailForm.getProductId());
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


    public static Integer getExpressPrintQty(Integer totalBillQty) {
        //TODO 需要完善该方法，
        //String companyName= RequestUtils.getCompanyName();
        Integer expressPrintQty = 1;
//        if(CompanyNameEnum.JXOPPO.name().equals(companyName)){
//            expressPrintQty=Const.OPPO_ORDER_EXPRESS_PRODUCT_QTY;
//        }else if(CompanyNameEnum.JXVIVO.name().equals(companyName)) {
//            expressPrintQty = Const.VIVO_ORDER_EXPRESS_PRODUCT_QTY;
//        }else if(CompanyNameEnum.JXIMOO.name().equals(companyName)){
//            expressPrintQty = Const.IMOO_ORDER_EXPRESS_PRODUCT_QTY;
//        }else{
//            expressPrintQty=Const.LX_ORDER_EXPRESS_PRODUCT_QTY;
//        }
        if(0 == totalBillQty % expressPrintQty){
            expressPrintQty = totalBillQty / expressPrintQty;
        } else{
            expressPrintQty = totalBillQty / expressPrintQty + 1;
        }
        return expressPrintQty;
    }


    public StoreAllotDto findStoreAllotDtoById(String id) {
        StoreAllotDto result = storeAllotRepository.findStoreAllotDtoById(id);
        cacheUtils.initCacheInput(result);
        return result;

    }

    public void delete(String id) {
        storeAllotRepository.logicDeleteOne(id);
    }

    public List<SimpleStoreAllotDetailDto> findDetailListForCommonAllot(String fromStoreId) {

        List<SimpleStoreAllotDetailDto> result =  storeAllotDetailRepository.findStoreAllotDetailListForNew(RequestUtils.getCompanyId());


        cacheUtils.initCacheInput(result);


        if(StringUtils.isNotBlank(fromStoreId)){
//        TODO 初始化财务存量
        }

        return result;
    }

    public List<String> getMergeStoreIds(){
        String mergeStoreIds ="3489,9873";//TODO 需要调用配置参数
        return StringUtils.getSplitList(mergeStoreIds, ",");
    }

    public List<SimpleStoreAllotDetailDto> findDetailListForFastAllot() {

        LocalDate billDate = LocalDateUtils.parse(LocalDateUtils.format(LocalDate.now()));
        String toStoreId =getMergeStoreIds().get(1);

        List<SimpleStoreAllotDetailDto> result = storeAllotDetailRepository.findStoreAllotDetailsForFastAllot(billDate, toStoreId, "待发货", RequestUtils.getCompanyId());
        if(result!=null){
            cacheUtils.initCacheInput(result);
            //TODO 初始化财务存量

            result.stream().forEach((each)->{
                if(each.getBillQty() != null && each.getBillQty()<=0 ) {
                    each.setBillQty(null);
                }
            });
        }

        return result;

    }

    public Boolean getShowAllotType() {
        return getMergeStoreIds() != null;
    }
}
