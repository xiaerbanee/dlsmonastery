package net.myspring.future.modules.crm.web.controller;


import net.myspring.common.constant.CharConstant;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.basic.web.query.ProductQuery;
import net.myspring.future.modules.crm.domain.ProductImeUpload;
import net.myspring.future.modules.crm.dto.ProductImeSaleDto;
import net.myspring.future.modules.crm.dto.ProductImeUploadDto;
import net.myspring.future.modules.crm.service.ProductImeUploadService;
import net.myspring.future.modules.crm.web.form.ProductImeUploadForm;
import net.myspring.future.modules.crm.web.query.ProductImeUploadQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "crm/productImeUpload")
public class ProductImeUploadController {

    @Autowired
    private ProductImeUploadService productImeUploadService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<ProductImeUploadDto> list(Pageable pageable, ProductImeUploadQuery productImeUploadQuery){
        return productImeUploadService.findPage(pageable, productImeUploadQuery);
    }

    @RequestMapping(value = "upload")
    public RestResponse upload(ProductImeUploadForm productImeUploadForm) {

        List<String> imeList = productImeUploadForm.getImeList();
        if(CollectionUtil.isEmpty(imeList)){
            return new RestResponse("没有输入任何有效的IME", ResponseCodeEnum.invalid.name(), false);
        }

        String errMsg = productImeUploadService.upload(productImeUploadForm);
        if(StringUtils.isNotBlank(errMsg)){
            return new RestResponse(errMsg, ResponseCodeEnum.invalid.name(), false);
        }else{
            return new RestResponse("上报成功", ResponseCodeEnum.saved.name());
        }

    }

    @RequestMapping(value = "uploadBack")
    public RestResponse uploadBack(String imeStr) {

        List<String> imeList = StringUtils.getSplitList(imeStr, CharConstant.ENTER);
        if(imeList.size() == 0){
            return new RestResponse("没有输入任何有效的IME", ResponseCodeEnum.invalid.name(), false);
        }

        String errMsg = productImeUploadService.uploadBack(imeList);
        if(StringUtils.isNotBlank(errMsg)){
            return new RestResponse(errMsg, ResponseCodeEnum.invalid.name(), false);
        } else {

            return new RestResponse("退回成功", ResponseCodeEnum.saved.name());
        }

    }

    @RequestMapping(value = "getQuery")
    public ProductImeUploadQuery getQuery(ProductImeUploadQuery productImeUploadQuery){
        return productImeUploadQuery;
    }

    @RequestMapping(value = "delete")
    public String delete(ProductImeUpload productImeUpload, BindingResult bindingResult){
        return null;
    }

    @RequestMapping(value = "batchAudit")
    public RestResponse batchAudit(@RequestParam(value = "ids[]") String[] ids,boolean pass){

        productImeUploadService.batchAudit(ids, pass);
        return new RestResponse("批量审核成功", ResponseCodeEnum.audited.name());
    }

    @RequestMapping(value = "findDto")
    public ProductImeUploadDto findDto(String id) {
        if(StringUtils.isBlank(id)){
            return new ProductImeUploadDto();
        }
        return productImeUploadService.findDto(id);
    }

    @RequestMapping(value = "checkForUpload")
    public String checkForUpload(String imeStr) {
        List<String> imeList = StringUtils.getSplitList(imeStr, CharConstant.ENTER);
        if(imeList.size() == 0){
            return null;
        }
        return productImeUploadService.checkForUpload(imeList);
    }


    @RequestMapping(value = "checkForUploadBack")
    public String checkForUploadBack(String imeStr) {
        List<String> imeList = StringUtils.getSplitList(imeStr, CharConstant.ENTER);
        if(imeList.size() == 0){
            return null;
        }
        return productImeUploadService.checkForUploadBack(imeList);
    }

}
