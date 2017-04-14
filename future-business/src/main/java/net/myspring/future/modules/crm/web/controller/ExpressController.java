package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.future.common.enums.ExpressTypeEnum;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.common.utils.StringUtils;
import net.myspring.future.modules.crm.domain.Depot;
import net.myspring.future.modules.crm.domain.Express;
import net.myspring.future.modules.crm.service.DepotService;
import net.myspring.future.modules.crm.service.ExpressCompanyService;
import net.myspring.future.modules.crm.service.ExpressService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/express")
public class ExpressController {

    @Autowired
    private ExpressService expressService;
     @Autowired
     private ExpressCompanyService expressCompanyService;
    @Autowired
    private DepotService depotService;

    @ModelAttribute
    public Express get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new Express() : expressService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        Page<Express> page = expressService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        for(Express express: page.getContent()){
            express.setActionList(getActionList());
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "getListProperty")
    public String getListProperty(Depot depot) {
        Map<String,Object> map = Maps.newHashMap();
        map.put("expressCompanys", expressCompanyService.findAll());
        map.put("stores", depotService.findStores());
        map.put("extendTypes", ExpressTypeEnum.values());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "save")
    public String save(Express express){
        expressService.save(express);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("保存成功"));

    }

    @RequestMapping(value = "delete")
    public String delete(Express express, BindingResult bindingResult) {
        expressService.delete(express);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("删除成功"));
    }

    @RequestMapping(value = "findOne")
    public String findOne(Express express){
        return ObjectMapperUtils.writeValueAsString(express);
    }

    private List<String> getActionList() {
        List<String> actionList = Lists.newArrayList();
        for(String authoritie : SecurityUtils.getAuthorityList()){
            switch (authoritie){
                case "crm:express:delete" : actionList.add(Const.ITEM_ACTION_DELETE); break;
                case "crm:express:edit" : actionList.add(Const.ITEM_ACTION_EDIT); break;
                default : break;
            }
        }
        return actionList;
    }


}
