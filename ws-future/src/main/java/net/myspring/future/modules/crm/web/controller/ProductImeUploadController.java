package net.myspring.future.modules.crm.web.controller;


import net.myspring.common.constant.CharConstant;
import net.myspring.common.exception.ServiceException;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.OfficeRuleEnum;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.crm.dto.ProductImeUploadDto;
import net.myspring.future.modules.crm.service.ProductImeUploadService;
import net.myspring.future.modules.crm.web.form.ProductImeBatchUploadForm;
import net.myspring.future.modules.crm.web.form.ProductImeUploadForm;
import net.myspring.future.modules.crm.web.query.ProductImeUploadQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "crm/productImeUpload")
public class ProductImeUploadController {

    @Autowired
    private ProductImeUploadService productImeUploadService;
    @Autowired
    private OfficeClient officeClient;

    @RequestMapping(method = RequestMethod.GET)
    public Page<ProductImeUploadDto> list(Pageable pageable, ProductImeUploadQuery productImeUploadQuery){
        return productImeUploadService.findPage(pageable, productImeUploadQuery);
    }

    @RequestMapping(value = "upload")
    public RestResponse upload(ProductImeUploadForm productImeUploadForm) {

        List<String> imeList = productImeUploadForm.getImeList();
        if(CollectionUtil.isEmpty(imeList)){
            throw new ServiceException("没有输入任何有效的串码");
        }

        productImeUploadService.upload(productImeUploadForm);

        return new RestResponse("上报成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "uploadBack")
    public RestResponse uploadBack(String imeStr) {

        List<String> imeList = StringUtils.getSplitList(imeStr, CharConstant.ENTER);
        if(imeList.size() == 0){
            throw new ServiceException("没有输入任何有效的串码");
        }

        productImeUploadService.uploadBack(imeList);

        return new RestResponse("退回成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "getQuery")
    public ProductImeUploadQuery getQuery(ProductImeUploadQuery productImeUploadQuery){
        return productImeUploadQuery;
    }

    @RequestMapping(value = "findDto")
    public ProductImeUploadDto findDto(String id) {
        if(StringUtils.isBlank(id)){
            return new ProductImeUploadDto();
        }
        return productImeUploadService.findDto(id);
    }

    @RequestMapping(value = "getBatchUploadForm")
    public ProductImeBatchUploadForm getBatchUploadForm(ProductImeBatchUploadForm productImeBatchUploadForm) {

        productImeBatchUploadForm.getExtra().put("officeList", officeClient.findByOfficeRuleName(OfficeRuleEnum.办事处.name()));
        return productImeBatchUploadForm;
    }

    @RequestMapping(value = "batchUpload")
    public RestResponse batchUpload(ProductImeBatchUploadForm productImeBatchUploadForm) {

        Long uploadQty = productImeUploadService.batchUpload(productImeBatchUploadForm);

        return new RestResponse(String.format("批量上报成功，共上报%d笔",uploadQty), ResponseCodeEnum.saved.name());
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

    @RequestMapping(value="export")
    public String export(ProductImeUploadQuery productImeUploadQuery) {

        return productImeUploadService.export(productImeUploadQuery);
    }

}
