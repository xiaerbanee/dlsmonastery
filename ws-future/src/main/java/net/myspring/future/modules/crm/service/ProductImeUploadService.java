package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.enums.AuditStatusEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.domain.ProductImeUpload;
import net.myspring.future.modules.crm.dto.ProductImeDto;
import net.myspring.future.modules.crm.dto.ProductImeSaleDto;
import net.myspring.future.modules.crm.dto.ProductImeUploadDto;
import net.myspring.future.modules.crm.repository.ProductImeRepository;
import net.myspring.future.modules.crm.repository.ProductImeSaleRepository;
import net.myspring.future.modules.crm.repository.ProductImeUploadRepository;
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

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
    private DepotManager depotManager;
    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private CacheUtils cacheUtils;


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

        String employeeId = RequestUtils.getEmployeeId();

        List<ProductIme> productImeList = productImeRepository.findByEnabledIsTrueAndImeIn(imeList);
        for (ProductIme productIme : productImeList) {
            if(productIme.getProductImeUploadId()!=null){
                continue;
            }
            ProductImeUpload productImeUpload = new ProductImeUpload();
            productImeUpload.setMonth(productImeUploadForm.getMonth());
            productImeUpload.setRemarks(productImeUploadForm.getRemarks());
            productImeUpload.setEmployeeId(employeeId);
            productImeUpload.setShopId(productImeUploadForm.getShopId());
            productImeUpload.setSaleShopId(productIme.getDepotId());
            productImeUpload.setProductImeId(productIme.getId());
            productImeUpload.setStatus(AuditStatusEnum.申请中.name());

//            TODO 需要设置下面的字段
//            if(depotId==null){
//                productImeUpload.setGoodsOrderShop(null);
//            }else{
//                productImeUpload.setGoodsOrderShop(depotDao.findOne(depotId));
//            }
//            if(employee==null){
//                productImeUpload.setAccountShopIds(null);
//            }else{
//                productImeUpload.setAccountShopIds(employee.getDepotIds());
//            }

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

        for(String ime:imeList){
            ProductImeDto productImeDto = imeMap.get(ime);
            if(productImeDto == null) {
                sb.append("串码：").append(ime).append("在系统中不存在；");
            } else {
                if(productImeDto.getProductImeUploadId() == null) {
                    sb.append("串码：").append(ime).append("未上报，不能退回；");
                }else if(AuditStatusEnum.已通过.name().equals(productImeDto.getProductImeUploadStatus())) {
                    sb.append("串码：").append(ime).append("的上报记录已经审核通过，不能退回；");
                } else if (!depotManager.isAccess(productImeDto.getProductImeUploadShopId(), true, RequestUtils.getAccountId(),RequestUtils.getOfficeId())) {
                    sb.append("您没有串码：").append(ime).append("所在门店的上报退回权限；");
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
        if (CollectionUtil.isEmpty(productImeSaleDtoList)) {
            return uploadQty;
        }
        Map<String, ProductIme> productImeMap = productImeRepository.findMap(CollectionUtil.extractToList(productImeSaleDtoList, "productImeId"));

        for (ProductImeSaleDto productImeSaleDto : productImeSaleDtoList) {

            if (StringUtils.isBlank(productImeSaleDto.getProductImeProductImeUploadId())) {
                ProductImeUpload productImeUpload = new ProductImeUpload();
                productImeUpload.setStatus(AuditStatusEnum.申请中.name());
                productImeUpload.setMonth(productImeBatchUploadForm.getMonth());
                productImeUpload.setRemarks("批量上报");
                productImeUpload.setEmployeeId(productImeSaleDto.getEmployeeId());
                productImeUpload.setShopId(productImeSaleDto.getShopId());
                productImeUpload.setProductImeId(productImeSaleDto.getProductImeId());
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
}
