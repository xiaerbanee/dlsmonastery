package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.future.common.enums.ExpressOrderTypeEnum;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.StringUtils;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.domain.ProductImeSale;
import net.myspring.future.modules.crm.service.DepotService;
import net.myspring.future.modules.crm.service.ExpressCompanyService;
import net.myspring.future.modules.crm.service.ExpressOrderService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/expressOrder")
public class ExpressOrderController {

    @Autowired
    private ExpressOrderService expressOrderService;
    @Autowired
    private DepotService depotService;
    @Autowired
    private ExpressCompanyService expressCompanyService;

    @ModelAttribute
    public ExpressOrder get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new ExpressOrder() : expressOrderService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        Page<ExpressOrder> page = expressOrderService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        for(ExpressOrder expressOrder: page.getContent()){
            expressOrder.setActionList(getActionList());
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }
    @RequestMapping(value = "getListProperty")
    public String getListProperty() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("extendTypes", ExpressOrderTypeEnum.getList());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "getFormProperty")
    public String getFormProperty(ProductImeSale productImeSale) {
        Map<String,Object> map = Maps.newHashMap();
        map.put("fromDepots",depotService.findStores());
        map.put("expressCompanys",expressCompanyService.findAll());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "update")
    public String save(ExpressOrder expressOrder){
        expressOrderService.update(expressOrder);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("保存成功"));
    }

    @RequestMapping(value = "resetPrintStatus")
    public String delete(ExpressOrder expressOrder, BindingResult bindingResult) {
        expressOrderService.resetPrintStatus(expressOrder);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("重置成功"));
    }

    @RequestMapping(value = "findOne")
    public String findOne(ExpressOrder expressOrder){
        return ObjectMapperUtils.writeValueAsString(expressOrder);
    }

    private List<String> getActionList() {
        List<String> actionList = Lists.newArrayList();
        actionList.add(Const.ITEM_ACTION_EDIT);
        actionList.add(Const.ITEM_ACTION_RESET);
        return actionList;
    }

}
