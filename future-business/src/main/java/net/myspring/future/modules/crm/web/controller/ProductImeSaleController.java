package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.enums.MessageTypeEnum;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.future.common.enums.*;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.modules.crm.domain.ProductImeSale;
import net.myspring.future.modules.crm.service.DepotService;
import net.myspring.future.modules.crm.service.ProductImeSaleService;
import net.myspring.future.modules.crm.validator.ProductImeSaleValidator;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/productImeSale")
public class ProductImeSaleController {
    @Autowired
    private ProductImeSaleService productImeSaleService;
    @Autowired
    private ProductImeSaleValidator productImeSaleValidator;
    @Autowired
    private DepotService depotService;

    @ModelAttribute
    public ProductImeSale get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new ProductImeSale() : productImeSaleService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String findPage(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        Page<ProductImeSale> page = productImeSaleService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        for(ProductImeSale productImeSale: page.getContent()){
            productImeSale.setActionList(getActionList(productImeSale));
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "getListProperty", method = RequestMethod.GET)
    public String getListProperty() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("bools", BoolEnum.getMap());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "getFormProperty")
    public String getFormProperty(ProductImeSale productImeSale) {
        Map<String,Object> propertyMap = Maps.newHashMap();
        propertyMap.put("shops",depotService.findByIds(FilterUtils.getDepotIds(AccountUtils.getAccountId())));
        propertyMap.put("companyName", ThreadLocalContext.get().getCompanyName());
        return ObjectMapperUtils.writeValueAsString(propertyMap);
    }

    @RequestMapping(value = "save")
    public String save(ProductImeSale productImeSale, BindingResult bindingResult) {
        productImeSaleValidator.validate(productImeSale, bindingResult);
        RestResponse restResponse =new RestResponse("保存成功");
        if (bindingResult.hasErrors()) {
            restResponse =new RestResponse(false,bindingResult, MessageTypeEnum.error.name());
        } else {
            productImeSale.setEmployeeId(AccountUtils.getAccount().getEmployeeId());
            productImeSaleService.save(productImeSale);
        }
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "delete")
    public String delete(ProductImeSale productImeSale, BindingResult bindingResult){
        productImeSaleService.saleBack(productImeSale);
        RestResponse restResponse =new RestResponse("删除成功");
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    private List<String> getActionList(ProductImeSale productImeSale) {
        List<String> actionList = Lists.newArrayList();
        if(!productImeSale.getIsBack() && SecurityUtils.getAuthorityList().contains("crm:productImeSale:back")){
            actionList.add(Const.ITEM_ACTION_DELETE);
        }
        return actionList;
    }
}
