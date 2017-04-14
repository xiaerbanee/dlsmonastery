package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.utils.Collections3;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.modules.crm.domain.ProductType;
import net.myspring.future.modules.crm.domain.ReportScore;
import net.myspring.future.modules.crm.service.ProductTypeService;
import net.myspring.future.modules.crm.service.ReportScoreService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/reportScore")
public class ReportScoreController {

    @Autowired
    private ReportScoreService reportScoreService;
    @Autowired
    private ProductTypeService productTypeService;

    @ModelAttribute
    public ReportScore get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new ReportScore() : reportScoreService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String findPage(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        Page<ReportScore> page = reportScoreService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        for(ReportScore reportScore: page.getContent()){
            reportScore.setActionList(getActionList());
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "getListProperty", method = RequestMethod.GET)
    public String getListProperty() {
        Map<String, Object> map = Maps.newHashMap();
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "getFormProperty")
    public String getFormProperty() {
        Map<String,Object> propertyMap = Maps.newHashMap();
        List<ProductType> productTypeList = productTypeService.findAllScoreType();
        String productTypeNames = Collections3.extractToString(productTypeList, "name", Const.CHAR_COMMA_FULL);
        propertyMap.put("productNames",productTypeNames);
        return ObjectMapperUtils.writeValueAsString(propertyMap);
    }

    @RequestMapping(value = "save")
    public String save(ReportScore reportScore, BindingResult bindingResult) {
        reportScoreService.save(reportScore);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("保存成功"));
    }

    @RequestMapping(value = "delete")
    public String delete(ReportScore reportScore, BindingResult bindingResult){
        reportScoreService.delete(reportScore);
        RestResponse restResponse =new RestResponse("删除成功");
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    private List<String> getActionList() {
        List<String> actionList = Lists.newArrayList();
        actionList.add(Const.ITEM_ACTION_DELETE);
        return actionList;
    }

}
