package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.common.utils.StringUtils;
import net.myspring.future.modules.crm.domain.DemoPhoneType;
import net.myspring.future.modules.crm.service.DemoPhoneTypeService;
import net.myspring.future.modules.crm.service.ProductTypeService;
import net.myspring.future.modules.hr.service.OfficeService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/demoPhoneType")
public class DemoPhoneTypeController {

    @Autowired
    private DemoPhoneTypeService demoPhoneTypeService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private ProductTypeService productTypeService;

    @ModelAttribute
    public DemoPhoneType get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new DemoPhoneType() : demoPhoneTypeService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        Page<DemoPhoneType> page=demoPhoneTypeService.findPage(searchEntity.getPageable(), searchEntity.getParams());
        for(DemoPhoneType demoPhoneType:page.getContent()){
            demoPhoneType.setActionList(getActionList(demoPhoneType));
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "getFormProperty", method = RequestMethod.GET)
    public String form() {
        Map<String,Object> map= Maps.newHashMap();
        map.put("productTypes", productTypeService.findAll());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(DemoPhoneType demoPhoneType){
        demoPhoneTypeService.save(demoPhoneType);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("保存成功"));
    }

    @RequestMapping(value = "findOne")
    public String findOne(DemoPhoneType demoPhoneType){
        demoPhoneTypeService.initDemoPhoneTypeDetails(demoPhoneType);
        return ObjectMapperUtils.writeValueAsString(demoPhoneType);
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public String detail( DemoPhoneType demoPhoneType){
        return ObjectMapperUtils.writeValueAsString(demoPhoneType);
    }


    @RequestMapping(value = "delete")
    public String delete(DemoPhoneType demoPhoneType, RedirectAttributes redirectAttributes) {
        demoPhoneTypeService.delete(demoPhoneType);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("删除成功"));
    }

    private List<String> getActionList(DemoPhoneType DemoPhoneType) {
        List<String> actionList = Lists.newArrayList();
        for(String authoritie : SecurityUtils.getAuthorityList()){
            switch (authoritie){
                case "crm:demoPhoneType:delete" : actionList.add(Const.ITEM_ACTION_DELETE); break;
                case "crm:demoPhoneType:edit" : actionList.add(Const.ITEM_ACTION_EDIT); break;
                default : break;
            }
        }
        return actionList;
    }
}
