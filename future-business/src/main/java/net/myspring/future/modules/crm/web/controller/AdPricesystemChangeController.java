package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.utils.Collections3;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.StringUtils;
import net.myspring.future.modules.crm.domain.AdPricesystemChange;
import net.myspring.future.modules.crm.domain.Product;
import net.myspring.future.modules.crm.service.AdPricesystemChangeService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "crm/adPricesystemChange")
public class AdPricesystemChangeController {

    @Autowired
    private AdPricesystemChangeService adPricesystemChangeService;

    @ModelAttribute
    public AdPricesystemChange get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new AdPricesystemChange() : adPricesystemChangeService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        Page<AdPricesystemChange> page = adPricesystemChangeService.findPage(searchEntity.getPageable(), searchEntity.getParams());
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value="findFilter", method = RequestMethod.GET)
    public String findFilter(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        List<AdPricesystemChange> adPricesystemChangeList = adPricesystemChangeService.findFilter(searchEntity.getParams());
        List<Product> productList = Lists.newArrayList();
        for(AdPricesystemChange adPricesystemChange : adPricesystemChangeList){
            if(adPricesystemChange.getProductId() != null){
                productList.add(adPricesystemChange.getProduct());
            }
        }
        return ObjectMapperUtils.writeValueAsString(productList);
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(String data){
        RestResponse restResponse = new RestResponse("保存成功");
        List<List<String>> datas = ObjectMapperUtils.readValue(HtmlUtils.htmlUnescape(data), ArrayList.class);
        if (Collections3.isEmpty(datas)) {
            restResponse.setMessage("数据不能为空");
        }
        adPricesystemChangeService.save(datas);
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }


}
