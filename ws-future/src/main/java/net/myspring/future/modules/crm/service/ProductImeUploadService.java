package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.future.common.enums.AuditStatusEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.domain.ProductImeUpload;
import net.myspring.future.modules.crm.dto.ProductImeDto;
import net.myspring.future.modules.crm.dto.ProductImeUploadDto;
import net.myspring.future.modules.crm.repository.ProductImeRepository;
import net.myspring.future.modules.crm.repository.ProductImeUploadRepository;
import net.myspring.future.modules.crm.web.form.ProductImeUploadForm;
import net.myspring.future.modules.crm.web.query.ProductImeUploadQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ProductImeUploadService {

    @Autowired
    private ProductImeUploadRepository productImeUploadRepository;
    @Autowired
    private ProductImeRepository productImeRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<ProductImeUploadDto> findPage(Pageable pageable, ProductImeUploadQuery productImeUploadQuery) {

        Page<ProductImeUploadDto> page = productImeUploadRepository.findPage(pageable, productImeUploadQuery);
        cacheUtils.initCacheInput(page.getContent());

        return page;
    }

    public void batchAudit(String[] ids, boolean pass) {

        if(ids == null || ids.length == 0){
            return;
        }

        List<ProductImeUpload> productImeUploads = productImeUploadRepository.findAll(Lists.newArrayList(ids));
        if(CollectionUtil.isEmpty(productImeUploads)){
            return;
        }

        for (ProductImeUpload each : productImeUploads) {
            each.setStatus(pass ? AuditStatusEnum.已通过.name() : AuditStatusEnum.未通过.name());

        }
        productImeUploadRepository.save(productImeUploads);
    }

    public ProductImeUploadDto findDto(String id) {
        ProductImeUploadDto productImeUploadDto = productImeUploadRepository.findDto(id);
        cacheUtils.initCacheInput(productImeUploadDto);
        return productImeUploadDto;
    }

    public String checkForUpload(List<String> imeList) {

        StringBuilder sb = new StringBuilder();
        List<ProductImeDto> productImeDtoList = productImeRepository.findDtoListByImeList(imeList, RequestUtils.getCompanyId());
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

    public String upload(ProductImeUploadForm productImeUploadForm) {

        List<String> imeList = productImeUploadForm.getImeList();

        String errMsg = checkForUpload(imeList);
        if(StringUtils.isNotBlank(errMsg)){
            return errMsg;
        }

        //TODO 获取employeeId
        String employeeId = "";

        List<ProductIme> productImeList = productImeRepository.findByImeList(imeList);
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

        return null;
    }

    public String checkForUploadBack(List<String> imeList) {

        StringBuilder sb = new StringBuilder();
        List<ProductImeDto> productImeDtoList = productImeRepository.findDtoListByImeList(imeList, RequestUtils.getCompanyId());
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
                    //TODO 判断门店是否有权限
//                } else if (!DepotUtils.isAccess(productIme.getProductImeUpload().getShop(), true)) {
//                    message.addText("message_product_ime_upload_no_ime" + ime + "message_product_ime_upload_no_ime" + productIme.getDepotName() + "message_product_ime_upload_upload_back_permission");
                }

            }
        }

        return sb.toString();
    }

    public String uploadBack(List<String> imeList) {

        String errMsg = checkForUploadBack(imeList);
        if(StringUtils.isNotBlank(errMsg)){
            return errMsg;
        }

        List<ProductIme> productImeList = productImeRepository.findByImeList(imeList);
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

        return null;
    }
}
