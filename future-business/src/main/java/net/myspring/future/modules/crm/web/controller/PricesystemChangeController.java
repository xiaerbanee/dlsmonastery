package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.future.common.enums.AuditTypeEnum;
import net.myspring.future.common.enums.CompanyConfigCodeEnum;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.modules.crm.domain.PricesystemChange;
import net.myspring.future.modules.crm.domain.Product;
import net.myspring.future.modules.crm.service.PricesystemChangeService;
import net.myspring.future.modules.crm.service.PricesystemService;
import net.myspring.future.modules.crm.service.ProductService;
import net.myspring.future.modules.sys.domain.CompanyConfig;
import net.myspring.future.modules.sys.service.CompanyConfigService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/pricesystemChange")
public class PricesystemChangeController {

    @Autowired
    private PricesystemChangeService pricesystemChangeService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CompanyConfigService companyConfigService;
    @Autowired
    private PricesystemService pricesystemService;


    @ModelAttribute
    public PricesystemChange get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new PricesystemChange() : pricesystemChangeService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getOfficeFilter(AccountUtils.getAccountId()));
        Page<PricesystemChange> page  = pricesystemChangeService.findPage(searchEntity.getPageable(), searchEntity.getParams());
        for(PricesystemChange pricesystemChange:page.getContent()) {
            pricesystemChange.setActionList(getActionList(pricesystemChange));
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value="getListProperty")
    public String form(){
        Map<String,Object> map= Maps.newHashMap();
        map.put("status", AuditTypeEnum.getValues());
        map.put("pricesystems", pricesystemService.findAll());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value="getFormProperty")
    public String getFormProperty(){
        Map<String,Object> map= Maps.newHashMap();
        CompanyConfig companyConfig = companyConfigService.findByCode(CompanyConfigCodeEnum.PRODUCT_GOODS_GROUP_IDS.getCode());
        List<String> outGroupIds = IdUtils.getIdList(companyConfig==null?"":companyConfig.getValue());
        List<Product> productList = productService.findByOutGroupIds(outGroupIds);
        map.put("products",productList);
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "save")
    public String save(PricesystemChange pricesystemChange, BindingResult bindingResult) {
        pricesystemChangeService.save(pricesystemChange);
        RestResponse restResponse =new RestResponse("保存成功");
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "audit")
    public String audit(@RequestParam(value = "ids[]") String[] ids,Boolean pass){
        if(ids != null && ids.length > 0){
            pricesystemChangeService.audit(ids,pass);
        }
        RestResponse restResponse =new RestResponse("审核成功");
        String response = ObjectMapperUtils.writeValueAsString(restResponse);
        return response;
    }

    @RequestMapping(value = "getPricesystemDetail")
    public String getPricesystemDetail(@RequestParam(value = "productIdList[]") String[] proudctIdList){
        return ObjectMapperUtils.writeValueAsString(pricesystemChangeService.getPricesystemDetail(Arrays.asList(proudctIdList)));
    }

    private List<String> getActionList(PricesystemChange pricesystemChange) {
        List<String> actionList = Lists.newArrayList();
        if(pricesystemChange.getCreatedBy().equals(ThreadLocalContext.get().getAccount().getId()) && AuditTypeEnum.APPLY.getValue().equals(pricesystemChange.getStatus()) && SecurityUtils.getAuthorityList().contains("crm:pricesystemChange:edit")) {
            actionList.add(Const.ITEM_ACTION_PASS);
            actionList.add(Const.ITEM_ACTION_NOT_PASS);
        }
        return actionList;
    }
}
