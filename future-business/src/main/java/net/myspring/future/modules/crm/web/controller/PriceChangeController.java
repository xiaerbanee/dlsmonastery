package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.future.common.enums.PriceChangeStatusEnum;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.common.utils.StringUtils;
import net.myspring.future.modules.crm.domain.PriceChange;
import net.myspring.future.modules.crm.service.PriceChangeService;
import net.myspring.future.modules.crm.service.ProductTypeService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/priceChange")
public class PriceChangeController {

    @Autowired
    private PriceChangeService priceChangeService;
    @Autowired
    private ProductTypeService productTypeService;


    @ModelAttribute
    public PriceChange get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new PriceChange() : priceChangeService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        Page<PriceChange> page = priceChangeService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        for(PriceChange priceChange: page.getContent()){
            priceChange.setActionList(getActionList(priceChange));
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "delete")
    public String delete(PriceChange priceChange, BindingResult bindingResult) {
        priceChangeService.delete(priceChange);
        RestResponse restResponse=new RestResponse("删除成功");
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "save")
    public String save(PriceChange priceChange) {
        priceChangeService.save(priceChange);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("保存成功"));
    }


    @RequestMapping(value = "check")
    public String check(PriceChange priceChange) {
        priceChangeService.check(priceChange);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("抽检成功"));
    }

    @RequestMapping(value = "findOne")
    public String findOne(PriceChange priceChange){
        return ObjectMapperUtils.writeValueAsString(priceChange);
    }

    @RequestMapping(value="getFormProperty")
    public String getFormProperty(){
        Map<String,Object> map= Maps.newHashMap();
        map.put("productTypes",productTypeService.findAll());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value="getListProperty")
    public String getListProperty(){
        Map<String,Object> map= Maps.newHashMap();
        return ObjectMapperUtils.writeValueAsString(map);
    }

    private List<String> getActionList(PriceChange priceChange) {
        List<String> actionList = Lists.newArrayList();
        if(SecurityUtils.getAuthorityList().contains("crm:priceChange:edit")){
            actionList.add(Const.ITEM_ACTION_EDIT);
        }
        if(PriceChangeStatusEnum.上报中.name().equals(priceChange.getStatus())){
            for(String authoritie : SecurityUtils.getAuthorityList()){
                switch (authoritie){
                    case "crm:priceChange:delete" : actionList.add(Const.ITEM_ACTION_DELETE); break;
                    case "crm:priceChange:edit" : actionList.add(Const.ITEM_ACTION_INSPECTION); break;
                    default : break;
                }
            }
        }
        return actionList;
    }

}
