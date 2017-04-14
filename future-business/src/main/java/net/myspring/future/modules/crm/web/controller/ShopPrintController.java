package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.enums.MessageTypeEnum;
import net.myspring.common.utils.Collections3;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.common.utils.StringUtils;
import net.myspring.future.common.activiti.ActivitiUtils;
import net.myspring.future.common.enums.BoolEnum;
import net.myspring.future.common.enums.DictMapCategoryEnum;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.CacheUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.modules.crm.domain.ShopPrint;
import net.myspring.future.modules.crm.service.ShopPrintService;
import net.myspring.future.modules.hr.service.OfficeService;
import net.myspring.future.modules.sys.domain.ProcessFlow;
import net.myspring.future.modules.sys.domain.ProcessType;
import net.myspring.future.modules.sys.service.ProcessFlowService;
import net.myspring.future.modules.sys.service.ProcessTypeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/shopPrint")
public class ShopPrintController {

    @Autowired
    private ShopPrintService shopPrintService;
    @Autowired
    protected TaskService taskService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private ProcessTypeService  processTypeService;
    @Autowired
    private ProcessFlowService processFlowService;

    @ModelAttribute
    public ShopPrint get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new ShopPrint() : shopPrintService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getOfficeFilter(AccountUtils.getAccountId()));
        Page<ShopPrint> page = shopPrintService.findPage(searchEntity.getPageable(), searchEntity.getParams());
        for (ShopPrint shopPrint : page.getContent()) {
            shopPrint.setActionList(getActionList(shopPrint));
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }


    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save( ShopPrint shopPrint){
        shopPrintService.save(shopPrint);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("保存成功"));
    }

    @RequestMapping(value="getListProperty",method = RequestMethod.GET)
    public String getListProperty(){
        Map<String ,Object> map=Maps.newHashMap();
        map.put("areas",officeService.findByType(Const.OFFICE_TYPE_AREA));
        map.put("printTypes", CacheUtils.findDictMapByCategory(DictMapCategoryEnum.门店_广告印刷.name()));
        ProcessType processType=processTypeService.findByName(ShopPrint.class.getSimpleName());
        List<ProcessFlow> processFlows=processFlowService.findByProcessType(processType.getId());
        map.put("processStatus", Collections3.extractToList(processFlows,"name"));
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value="getFormProperty",method = RequestMethod.GET)
    public String getFormProperty(){
        Map<String ,Object> map=Maps.newHashMap();
        map.put("areas",officeService.findByType(Const.OFFICE_TYPE_AREA));
        map.put("printTypes", CacheUtils.findDictMapByCategory(DictMapCategoryEnum.门店_广告印刷.name()));
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public String detail(ShopPrint shopPrint) {
        Map<String,Object> paramMap= Maps.newHashMap();
        if (StringUtils.isNotBlank(shopPrint.getProcessInstanceId())) {
            paramMap.put("activitiEntity", ActivitiUtils.getActivitiEntity(shopPrint.getProcessInstanceId()));
        }
        paramMap.put("shopPrint", shopPrint);
        paramMap.put("bools", BoolEnum.getMap());
        return ObjectMapperUtils.writeValueAsString(paramMap);
    }

    @RequestMapping(value = "audit", method = RequestMethod.GET)
    public String audit(ShopPrint shopPrint, boolean pass, String comment) {
        RestResponse restResponse=new RestResponse("广告印刷审核成功");
        Task task = taskService.createTaskQuery().processInstanceId(shopPrint.getProcessInstanceId()).singleResult();
        if (!ActivitiUtils.claim(task)) {
            restResponse=new RestResponse(false,"无法签收任务，您没有办理此任务的权限或者已经被其他人签收", MessageTypeEnum.error.name());
        }
        shopPrintService.audit(shopPrint,pass,comment);
        shopPrintService.notify(shopPrint);
        return  ObjectMapperUtils.writeValueAsString(restResponse);
    }

    private List<String> getActionList(ShopPrint shopPrint) {
        List<String> actionList = Lists.newArrayList();
        if(SecurityUtils.getAuthorityList().contains("crm:shopPrint:view")){
            actionList.add(Const.ITEM_ACTION_DETAIL);
        }
        return actionList;
    }
}
