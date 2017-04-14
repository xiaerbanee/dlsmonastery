package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.common.utils.StringUtils;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.crm.domain.ProductMonthPrice;
import net.myspring.future.modules.crm.service.ProductMonthPriceService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/productMonthPrice")
public class ProductMonthPriceController {

    @Autowired
    private ProductMonthPriceService productMonthPriceService;

    @ModelAttribute
    public ProductMonthPrice get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new ProductMonthPrice() : productMonthPriceService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        Page<ProductMonthPrice> page = productMonthPriceService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        for(ProductMonthPrice productMonthPrice: page.getContent()){
            productMonthPrice.setActionList(getActionList(productMonthPrice));
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "getListProperty")
    public String getListProperty() {
        Map<String,Object> map = Maps.newHashMap();
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "getFormProperty")
    public String getFormProperty() {
        Map<String,Object> map = Maps.newHashMap();
        map.put("productMonthPriceDetailList",productMonthPriceService.getProductTypeList());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "findOne")
    public String findOne(ProductMonthPrice productMonthPrice){
        return ObjectMapperUtils.writeValueAsString(productMonthPrice);
    }


    @RequestMapping(value = "checkMonth")
    public String checkMonth(String month) {
        RestResponse restResponse = productMonthPriceService.checkMonth(month);
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "save")
    public String save(ProductMonthPrice productMonthPrice) {
        productMonthPriceService.save(productMonthPrice);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("保存成功"));
    }

    private List<String> getActionList(ProductMonthPrice productMonthPrice) {
        List<String> actionList = Lists.newArrayList();
        actionList.add(Const.ITEM_ACTION_EDIT);
        return actionList;
    }

}
