package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.enums.MessageTypeEnum;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.common.utils.StringUtils;
import net.myspring.future.common.enums.CompanyConfigCodeEnum;
import net.myspring.future.common.enums.ShopPromotionActiveTypeEnum;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.CacheUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.modules.crm.domain.Depot;
import net.myspring.future.modules.crm.domain.ShopPromotion;
import net.myspring.future.modules.crm.service.ShopPromotionService;
import net.myspring.future.modules.crm.validator.ShopPromotionValidator;
import net.myspring.future.modules.sys.service.CompanyConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/shopPromotion")
public class ShopPromotionController {

    @Autowired
    private ShopPromotionService shopPromotionService;
    @Autowired
    private ShopPromotionValidator shopPromotionValidator;
    @Autowired
    private CompanyConfigService companyConfigService;

    @ModelAttribute
    public ShopPromotion get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new ShopPromotion() : shopPromotionService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        Page <ShopPromotion> page = shopPromotionService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        for(ShopPromotion shopPromotion:page.getContent()) {
            shopPromotion.setActionList(getActionList(shopPromotion));
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "findOne")
    public String findOne(ShopPromotion shopPromotion){
        return ObjectMapperUtils.writeValueAsString(shopPromotion);
    }

    @RequestMapping(value="getFormProperty")
    public String getFormProperty(){
        Map<String , Object>map= Maps.newHashMap();
        map.put("activityType", ShopPromotionActiveTypeEnum.values());
        map.put("brandName",companyConfigService.getCompanyConfig(CompanyConfigCodeEnum.BRAND.getCode()));
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value="getListProperty")
    public String getListProperty(){
        Map<String , Object>map= Maps.newHashMap();
        map.put("activityType", ShopPromotionActiveTypeEnum.values());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value="save")
    public String save(ShopPromotion shopPromotion, BindingResult bindingResult){
        shopPromotionValidator.validate(shopPromotion,bindingResult);
        RestResponse restResponse= new RestResponse("保存成功");
        if(bindingResult.hasErrors()){
            restResponse=new RestResponse(false,bindingResult, MessageTypeEnum.error.name());
        }else{
            shopPromotionService.save(shopPromotion);
        }
        String response= ObjectMapperUtils.writeValueAsString(restResponse);
        return response;
    }

    @RequestMapping(value="detail")
    public String detail(ShopPromotion shopPromotion){
        return ObjectMapperUtils.writeValueAsString(shopPromotion);
    }

    @RequestMapping(value="delete")
    @ResponseBody
    public String delete(ShopPromotion shopPromotion){
        shopPromotionService.logicDeleteOne(shopPromotion.getId());
        RestResponse restResponse = new RestResponse("删除成功");
        return  ObjectMapperUtils.writeValueAsString(restResponse);
    }

    private List<String> getActionList(ShopPromotion shopPromotion) {
        List<String> actionList = Lists.newArrayList();
        Depot shop = CacheUtils.getDepot(shopPromotion.getShopId());
        if(FilterUtils.getOfficeFilter(AccountUtils.getAccountId()).containsValue(shop.getOfficeId())&&!shopPromotion.getLocked()) {
            for(String authoritie : SecurityUtils.getAuthorityList()){
                switch (authoritie){
                    case "crm:shopPromotion:delete" : actionList.add(Const.ITEM_ACTION_DELETE); break;
                    case "crm:shopPromotion:edit" : actionList.add(Const.ITEM_ACTION_EDIT); break;
                    default : break;
                }
            }
       }
        return actionList;
    }
}
