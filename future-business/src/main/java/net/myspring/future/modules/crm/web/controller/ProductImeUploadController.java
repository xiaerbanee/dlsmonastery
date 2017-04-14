package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.enums.MessageTypeEnum;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.modules.crm.domain.ProductImeSale;
import net.myspring.future.modules.crm.domain.ProductImeUpload;
import net.myspring.future.modules.crm.service.ProductImeUploadService;
import net.myspring.future.modules.crm.validator.ProductImeUploadValidator;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/productImeUpload")
public class ProductImeUploadController {

    @Autowired
    private ProductImeUploadService productImeUploadService;
    @Autowired
    private ProductImeUploadValidator productImeUploadValidator;

    @ModelAttribute
    public ProductImeUpload get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new ProductImeUpload() : productImeUploadService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String findPage(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        Page<ProductImeUpload> page = productImeUploadService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        for(ProductImeUpload productImeUpload: page.getContent()){
            productImeUpload.setActionList(getActionList(productImeUpload));
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "getListProperty", method = RequestMethod.GET)
    public String getListProperty() {
        Map<String, Object> map = Maps.newHashMap();
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "getFormProperty")
    public String getFormProperty(ProductImeSale productImeSale) {
        Map<String,Object> propertyMap = Maps.newHashMap();
        return ObjectMapperUtils.writeValueAsString(propertyMap);
    }

    @RequestMapping(value = "save")
    public String save(ProductImeUpload productImeUpload, BindingResult bindingResult) {
        productImeUploadValidator.validate(productImeUpload, bindingResult);
        RestResponse restResponse =new RestResponse("保存成功");
        if (bindingResult.hasErrors()) {
            restResponse =new RestResponse(false,bindingResult, MessageTypeEnum.error.name());
        } else {
            productImeUploadService.save(productImeUpload);
        }
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "delete")
    public String delete(ProductImeUpload productImeUpload, BindingResult bindingResult){
        RestResponse restResponse =new RestResponse("删除成功");
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "audit")
    public String audit(@RequestParam(value = "ids[]") String[] ids,Boolean pass){
        if(ids != null && ids.length > 0){
            productImeUploadService.audit(ids, pass);
        }
        return ObjectMapperUtils.writeValueAsString(new RestResponse("审核成功"));
    }

    private List<String> getActionList(ProductImeUpload productImeUpload) {
        List<String> actionList = Lists.newArrayList();
        if(SecurityUtils.getAuthorityList().contains("crm:productImeUpload:edit")){
            actionList.add(Const.ITEM_ACTION_DELETE);
        }
        return actionList;
    }

}
