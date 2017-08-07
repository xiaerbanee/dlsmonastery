package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.sys.dto.AccountCommonDto;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.enums.AuditStatusEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.AccountClient;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.future.modules.crm.domain.GoodsOrderIme;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.domain.ProductImeUpload;
import net.myspring.future.modules.crm.dto.ProductImeDto;
import net.myspring.future.modules.crm.dto.ProductImeSaleDto;
import net.myspring.future.modules.crm.dto.ProductImeUploadDto;
import net.myspring.future.modules.crm.repository.*;
import net.myspring.future.modules.crm.web.form.ProductImeBatchUploadForm;
import net.myspring.future.modules.crm.web.form.ProductImeUploadForm;
import net.myspring.future.modules.crm.web.query.ProductImeUploadQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.text.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class ProductImeUploadService {

    @Autowired
    private ProductImeUploadRepository productImeUploadRepository;
    @Autowired
    private ProductImeSaleRepository productImeSaleRepository;
    @Autowired
    private ProductImeRepository productImeRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DepotManager depotManager;
    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private GoodsOrderImeRepository goodsOrderImeRepository;
    @Autowired
    private GoodsOrderRepository goodsOrderRepository;
    @Autowired
    private AccountClient accountClient;


    public Page<ProductImeUploadDto> findPage(Pageable pageable, ProductImeUploadQuery productImeUploadQuery) {
        productImeUploadQuery.setDepotIdList(depotManager.filterDepotIds(RequestUtils.getAccountId()));
        Page<ProductImeUploadDto> page = productImeUploadRepository.findPage(pageable, productImeUploadQuery);
        cacheUtils.initCacheInput(page.getContent());

        return page;
    }

    public ProductImeUploadDto findDto(String id) {
        ProductImeUploadDto productImeUploadDto = productImeUploadRepository.findDto(id);
        cacheUtils.initCacheInput(productImeUploadDto);
        return productImeUploadDto;
    }

    public String checkForUpload(List<String> imeList) {

        StringBuilder sb = new StringBuilder();
        List<ProductImeDto> productImeDtoList = productImeRepository.findDtoListByImeList(imeList);
        cacheUtils.initCacheInput(productImeDtoList);
        Map<String, ProductImeDto> imeMap = CollectionUtil.extractToMap(productImeDtoList, "ime");

        for(String ime:imeList){
            ProductImeDto productImeDto = imeMap.get(ime);
            if(productImeDto == null) {
                sb.append("串码：").append(ime).append("在系统中不存在；");
            } else {
                if(productImeDto.getProductImeUploadId() != null) {
                    sb.append("串码：").append(ime).append("已上报；");
                }
                if (productImeDto.getProductTypeId() != null && !Boolean.TRUE.equals(productImeDto.getProductTypeScoreType())) {
                    sb.append("串码：").append(ime).append("不是打分机型，不能上报；");
                }
            }
        }

        return sb.toString();
    }

    @Transactional
    public void upload(ProductImeUploadForm productImeUploadForm) {

        List<String> imeList = productImeUploadForm.getImeList();

        String errMsg = checkForUpload(imeList);
        if(StringUtils.isNotBlank(errMsg)){
            throw new ServiceException(errMsg);
        }

        List<ProductIme> productImeList = productImeRepository.findByEnabledIsTrueAndImeIn(imeList);
        Map<String, Product> productMap = productRepository.findMap(CollectionUtil.extractToList(productImeList, "productId"));
        List<GoodsOrderIme> goodsOrderImeList = goodsOrderImeRepository.findByEnabledIsTrueAndProductImeIdIn(CollectionUtil.extractToList(productImeList,"id"));
        Map<String, GoodsOrderIme> goodsOrderImeMap = CollectionUtil.extractToMap(goodsOrderImeList,"productImeId");
        Map<String, GoodsOrder> goodsOrderMap = goodsOrderRepository.findMap(CollectionUtil.extractToList(goodsOrderImeList,"goodsOrderId"));
        String accountShopIds = getEmployeeDepotIds(productImeUploadForm.getEmployeeId());

        for (ProductIme productIme : productImeList) {
            if(productIme.getProductImeUploadId()!=null){
                continue;
            }
            ProductImeUpload productImeUpload = new ProductImeUpload();
            productImeUpload.setMonth(productImeUploadForm.getMonth());
            productImeUpload.setRemarks(productImeUploadForm.getRemarks());
            productImeUpload.setEmployeeId(productImeUploadForm.getEmployeeId());
            productImeUpload.setShopId(productImeUploadForm.getShopId());
            productImeUpload.setSaleShopId(productIme.getDepotId());
            productImeUpload.setProductImeId(productIme.getId());
            productImeUpload.setProductTypeId(productMap.get(productIme.getProductId()).getProductTypeId());
            productImeUpload.setStatus(AuditStatusEnum.申请中.name());

            if(goodsOrderImeMap.get(productIme.getId())!=null && goodsOrderMap.get(goodsOrderImeMap.get(productIme.getId()).getGoodsOrderId())!=null){
                productImeUpload.setGoodsOrderShopId(goodsOrderMap.get(goodsOrderImeMap.get(productIme.getId()).getGoodsOrderId()).getShopId());
            }else{
                productImeUpload.setGoodsOrderShopId(null);
            }
            productImeUpload.setAccountShopIds(accountShopIds);
            productImeUploadRepository.save(productImeUpload);

            productIme.setProductImeUploadId(productImeUpload.getId());
            productIme.setRetailShopId(productImeUploadForm.getShopId());
            productImeRepository.save(productIme);

        }
    }

    public String checkForUploadBack(List<String> imeList) {

        StringBuilder sb = new StringBuilder();
        List<ProductImeDto> productImeDtoList = productImeRepository.findDtoListByImeList(imeList);
        cacheUtils.initCacheInput(productImeDtoList);
        Map<String, ProductImeDto> imeMap = CollectionUtil.extractToMap(productImeDtoList, "ime");
        List<String> filterDepotIds = depotManager.getFilterDepotIds(true,RequestUtils.getAccountId());
        for(String ime:imeList){
            ProductImeDto productImeDto = imeMap.get(ime);
            if(productImeDto == null) {
                sb.append("串码：").append(ime).append("在系统中不存在；");
            } else {
                if(productImeDto.getProductImeUploadId() == null) {
                    sb.append("串码：").append(ime).append("未上报，不能退回；");
                }else if(AuditStatusEnum.已通过.name().equals(productImeDto.getProductImeUploadStatus())) {
                    sb.append("串码：").append(ime).append("的上报记录已经审核通过，不能退回；");
                } else if (CollectionUtil.isNotEmpty(RequestUtils.getOfficeIdList())) {
                    if(!filterDepotIds.contains(productImeDto.getProductImeUploadShopId())){
                        sb.append("您没有串码：").append(ime).append("所在门店的上报退回权限；");
                    }
                }

            }
        }

        return sb.toString();
    }

    @Transactional
    public void uploadBack(List<String> imeList) {

        String errMsg = checkForUploadBack(imeList);
        if(StringUtils.isNotBlank(errMsg)){
            throw new ServiceException(errMsg);
        }

        List<ProductIme> productImeList = productImeRepository.findByEnabledIsTrueAndImeIn(imeList);
        Map<String, ProductImeUpload> productImeUploadMap = productImeUploadRepository.findMap(CollectionUtil.extractToList(productImeList, "productImeUploadId"));

        for (ProductIme productIme : productImeList) {
            ProductImeUpload productImeUpload = productImeUploadMap.get(productIme.getProductImeUploadId());
            if (productImeUpload != null && !AuditStatusEnum.已通过.name().equals(productImeUpload.getStatus())) {
                productIme.setProductImeUploadId(null);
                productIme.setRetailShopId(productIme.getDepotId());
                productImeRepository.save(productIme);

                productImeUpload.setStatus(AuditStatusEnum.未通过.toString());
                productImeUpload.setEnabled(false);
                productImeUploadRepository.save(productImeUpload);
            }
        }
    }

    public SimpleExcelBook export(ProductImeUploadQuery productImeUploadQuery) {

        Workbook workbook = new SXSSFWorkbook(10000);

        List<SimpleExcelColumn> productImeUploadColumnList = Lists.newArrayList();
        productImeUploadColumnList.add(new SimpleExcelColumn(workbook, "month", "上报月份"));
        productImeUploadColumnList.add(new SimpleExcelColumn(workbook, "shopAreaName", "办事处"));
        productImeUploadColumnList.add(new SimpleExcelColumn(workbook, "shopOfficeName", "考核区域"));
        productImeUploadColumnList.add(new SimpleExcelColumn(workbook, "shopName", "上报门店"));
        productImeUploadColumnList.add(new SimpleExcelColumn(workbook, "productImeIme", "串码"));
        productImeUploadColumnList.add(new SimpleExcelColumn(workbook, "productImeProductName", "货品名称"));
        productImeUploadColumnList.add(new SimpleExcelColumn(workbook, "employeeName", "上报人"));
        productImeUploadColumnList.add(new SimpleExcelColumn(workbook, "createdDate", "上报时间"));
        productImeUploadColumnList.add(new SimpleExcelColumn(workbook, "status", "上报状态"));
        productImeUploadColumnList.add(new SimpleExcelColumn(workbook, "createdByName", "创建人"));
        productImeUploadColumnList.add(new SimpleExcelColumn(workbook, "createdDate", "创建时间"));
        productImeUploadColumnList.add(new SimpleExcelColumn(workbook, "lastModifiedByName", "更新人"));
        productImeUploadColumnList.add(new SimpleExcelColumn(workbook, "lastModifiedDate", "更新时间"));
        productImeUploadColumnList.add(new SimpleExcelColumn(workbook, "remarks", "备注"));

        List<ProductImeUploadDto> productImeUploadDtoList = findPage(new PageRequest(0,10000), productImeUploadQuery).getContent();
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("串码上报记录", productImeUploadDtoList, productImeUploadColumnList);
        ExcelUtils.doWrite(workbook, simpleExcelSheet);
        return  new SimpleExcelBook(workbook,"串码上报记录"+ LocalDate.now()+".xlsx", simpleExcelSheet);

    }

    @Transactional
    public Long batchUpload(ProductImeBatchUploadForm productImeBatchUploadForm) {
        Long uploadQty = 0L;

        List<String> officeIds = officeClient.getChildOfficeIds(productImeBatchUploadForm.getOfficeId());

        LocalDate dateStart = LocalDate.parse(productImeBatchUploadForm.getMonth() + "-01");
        LocalDate dateEnd = dateStart.plusMonths(1);

        List<ProductImeSaleDto> productImeSaleDtoList = productImeSaleRepository.findForBatchUpload(dateStart.atStartOfDay(), dateEnd.atStartOfDay(), officeIds);
        cacheUtils.initCacheInput(productImeSaleDtoList);
        if (CollectionUtil.isEmpty(productImeSaleDtoList)) {
            return uploadQty;
        }
        List<String> productImeIdList = CollectionUtil.extractToList(productImeSaleDtoList, "productImeId");
        Map<String, ProductIme> productImeMap = productImeRepository.findMap(productImeIdList);
        Map<String, Product> productMap = productRepository.findMap(CollectionUtil.extractToList(productImeSaleDtoList, "productImeProductId"));
        List<GoodsOrderIme> goodsOrderImeList = goodsOrderImeRepository.findByEnabledIsTrueAndProductImeIdIn(productImeIdList);
        Map<String, GoodsOrderIme> goodsOrderImeMap = CollectionUtil.extractToMap(goodsOrderImeList,"productImeId");
        Map<String, GoodsOrder> goodsOrderMap = goodsOrderRepository.findMap(CollectionUtil.extractToList(goodsOrderImeList,"goodsOrderId"));

        Map<String, String> employeeDepotIdsMap = getEmployeeDepotIdsMap(new ArrayList<>(CollectionUtil.extractToSet(productImeSaleDtoList, "employeeId")));

        for (ProductImeSaleDto productImeSaleDto : productImeSaleDtoList) {

            if (StringUtils.isBlank(productImeSaleDto.getProductImeProductImeUploadId())) {
                ProductImeUpload productImeUpload = new ProductImeUpload();
                productImeUpload.setStatus(AuditStatusEnum.申请中.name());
                productImeUpload.setMonth(productImeBatchUploadForm.getMonth());
                productImeUpload.setRemarks("批量上报");
                productImeUpload.setEmployeeId(productImeSaleDto.getEmployeeId());
                productImeUpload.setShopId(productImeSaleDto.getShopId());
                productImeUpload.setProductImeId(productImeSaleDto.getProductImeId());
                productImeUpload.setProductTypeId(productMap.get(productImeSaleDto.getProductImeProductId()).getProductTypeId());

                if(goodsOrderImeMap.get(productImeSaleDto.getProductImeId())!=null && goodsOrderMap.get(goodsOrderImeMap.get(productImeSaleDto.getProductImeId()).getGoodsOrderId())!=null){
                    productImeUpload.setGoodsOrderShopId(goodsOrderMap.get(goodsOrderImeMap.get(productImeSaleDto.getProductImeId()).getGoodsOrderId()).getShopId());
                }else{
                    productImeUpload.setGoodsOrderShopId(null);
                }
                productImeUpload.setAccountShopIds(employeeDepotIdsMap.get(productImeSaleDto.getEmployeeId()));
                productImeUploadRepository.save(productImeUpload);

                ProductIme productIme = productImeMap.get(productImeSaleDto.getProductImeId());
                productIme.setProductImeUploadId(productImeUpload.getId());
                productIme.setRetailShopId(productImeSaleDto.getShopId());
                productImeRepository.save(productIme);
                uploadQty ++;
            }
        }
        return uploadQty;
    }

    private Map<String, String> getEmployeeDepotIdsMap(List<String> employeeIdList) {
       if(CollectionUtil.isEmpty(employeeIdList)){
           return new HashMap<>();
       }
       List<AccountCommonDto> accountCommonDtoList = accountClient.findByEmployeeIdList(employeeIdList);
       Map<String, String> interMap = productImeUploadRepository.findAccountDepotIdsMap(new ArrayList<>(CollectionUtil.extractToSet(accountCommonDtoList, "id")));

       Map<String, String> result = new HashMap<>();
       for(AccountCommonDto accountCommonDto : accountCommonDtoList){
           result.put(accountCommonDto.getEmployeeId(), interMap.get(accountCommonDto.getId()));
       }
       return result;
    }

    private String getEmployeeDepotIds(String employeeId) {
        if(StringUtils.isBlank(employeeId)){
            return null;
        }
        return getEmployeeDepotIdsMap(Collections.singletonList(employeeId)).get(employeeId);
    }
}
