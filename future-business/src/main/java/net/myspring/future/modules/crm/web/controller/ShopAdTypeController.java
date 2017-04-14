package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.future.common.enums.TotalPriceTypeEnum;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.common.utils.StringUtils;
import net.myspring.future.modules.crm.domain.ShopAdType;
import net.myspring.future.modules.crm.service.ShopAdTypeService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/shopAdType")
public class ShopAdTypeController {

    @Autowired
    private ShopAdTypeService shopAdTypeService;

    @ModelAttribute
    public ShopAdType get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new ShopAdType() : shopAdTypeService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        Page<ShopAdType> page = shopAdTypeService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        for(ShopAdType shopAdType: page.getContent()){
            shopAdType.setActionList(getActionList());
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "delete")
    public String delete(ShopAdType shopAdType) {
        shopAdTypeService.delete(shopAdType);
        RestResponse restResponse =new RestResponse("删除成功");
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "save")
    public String save(ShopAdType shopAdType) {
        shopAdTypeService.save(shopAdType);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("保存成功"));
    }

    @RequestMapping(value = "findOne")
    public String findOne(ShopAdType shopAdType){
        return ObjectMapperUtils.writeValueAsString(shopAdType);
    }

    @RequestMapping(value="getFormProperty")
    public String getFormProperty(){
        Map<String,Object> map= Maps.newHashMap();
        map.put("totalPriceTypes", TotalPriceTypeEnum.getValues());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value="getListProperty")
    public String getListProperty(){
        Map<String,Object> map= Maps.newHashMap();
        map.put("totalPriceTypes", TotalPriceTypeEnum.getValues());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    private List<String> getActionList() {
        List<String> actionList = Lists.newArrayList();
        for(String authoritie : SecurityUtils.getAuthorityList()){
            switch (authoritie){
                case "crm:shopAdType:delete" : actionList.add(Const.ITEM_ACTION_DELETE); break;
                case "crm:shopAdType:edit" : actionList.add(Const.ITEM_ACTION_EDIT); break;
                default : break;
            }
        }
        return actionList;
    }


}
