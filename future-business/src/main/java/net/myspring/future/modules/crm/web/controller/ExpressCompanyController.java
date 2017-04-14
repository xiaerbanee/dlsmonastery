package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.future.common.enums.ExpressCompanyType;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.common.utils.StringUtils;
import net.myspring.future.modules.crm.domain.ExpressCompany;
import net.myspring.future.modules.crm.service.ExpressCompanyService;
import net.myspring.future.modules.sys.service.DistrictService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/expressCompany")
public class ExpressCompanyController {

    @Autowired
    private ExpressCompanyService expressCompanyService;
    @Autowired
    private DistrictService districtService;

    @ModelAttribute
    public ExpressCompany get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new ExpressCompany() : expressCompanyService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        Page<ExpressCompany> page = expressCompanyService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        for(ExpressCompany expressCompany: page.getContent()){
            expressCompany.setActionList(getActionList());
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "delete")
    public String delete(ExpressCompany expressCompany, BindingResult bindingResult) {
        expressCompanyService.delete(expressCompany);
        RestResponse restResponse=new RestResponse("删除成功");
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "save")
    public String save(ExpressCompany expressCompany) {
        expressCompanyService.save(expressCompany);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("保存成功"));
    }

    @RequestMapping(value = "findOne")
    public String findOne(ExpressCompany expressCompany){
        return ObjectMapperUtils.writeValueAsString(expressCompany);
    }

    @RequestMapping(value="getFormProperty")
    public String getFormProperty(){
        Map<String,Object> map= Maps.newHashMap();
        map.put("expressType", ExpressCompanyType.values());
        map.put("provinces",districtService.findByProvince());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value="getListProperty")
    public String getListProperty(){
        Map<String,Object> map= Maps.newHashMap();
        return ObjectMapperUtils.writeValueAsString(map);
    }

    private List<String> getActionList() {
        List<String> actionList = Lists.newArrayList();
        for(String authoritie : SecurityUtils.getAuthorityList()){
            switch (authoritie){
                case "crm:expressCompany:delete" : actionList.add(Const.ITEM_ACTION_DELETE); break;
                case "crm:expressCompany:edit" : actionList.add(Const.ITEM_ACTION_EDIT); break;
                default : break;
            }
        }
        return actionList;
    }
}
