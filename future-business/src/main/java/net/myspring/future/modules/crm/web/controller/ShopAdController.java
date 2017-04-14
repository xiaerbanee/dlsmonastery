package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.enums.MessageTypeEnum;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.common.utils.StringUtils;
import net.myspring.future.common.activiti.ActivitiUtils;
import net.myspring.future.common.config.ExcelView;
import net.myspring.future.common.enums.BoolEnum;
import net.myspring.future.common.excel.SimpleExcelBook;
import net.myspring.future.common.excel.SimpleExcelSheet;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.modules.crm.domain.ShopAd;
import net.myspring.future.modules.crm.service.ShopAdService;
import net.myspring.future.modules.crm.service.ShopAdTypeService;
import net.myspring.future.modules.crm.validator.ShopAdValidator;
import net.myspring.future.modules.hr.domain.Account;
import net.myspring.future.modules.hr.service.OfficeService;
import net.myspring.future.modules.sys.domain.ProcessFlow;
import net.myspring.future.modules.sys.domain.ProcessType;
import net.myspring.future.modules.sys.service.ProcessFlowService;
import net.myspring.future.modules.sys.service.ProcessTypeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/shopAd")
public class ShopAdController {

    @Autowired
    private ShopAdService shopAdService;
    @Autowired
    private ShopAdTypeService shopAdTypeService;
    @Autowired
    private ProcessTypeService processTypeService;
    @Autowired
    private ProcessFlowService processFlowService;
    @Autowired
    private ShopAdValidator shopAdValidator;
    @Autowired
    private TaskService taskService;
    @Autowired
    private OfficeService officeService;

    @ModelAttribute
    public ShopAd get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new ShopAd() : shopAdService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        Page<ShopAd> page = shopAdService.findPage(searchEntity.getPageable(), searchEntity.getParams());
        for (ShopAd shopAd : page.getContent()) {
            shopAd.setActionList(getActionList(shopAd));
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "getFormProperty", method = RequestMethod.GET)
    public String getFormProperty(ShopAd shopAd) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("shopAdTypes", shopAdTypeService.findAllByEnabled());
        ProcessType processType = processTypeService.findByName(ShopAd.class.getSimpleName());
        List<ProcessFlow> processFlows = processFlowService.findByProcessType(processType.getId());
        map.put("processFlows", processFlows);
        map.put("shopAd", shopAd);
        map.put("bools", BoolEnum.getMap());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "getListProperty", method = RequestMethod.GET)
    public String getListProperty(ShopAd shopAd) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("shopAdTypes", shopAdTypeService.findAllByEnabled());
        ProcessType processType = processTypeService.findByName(ShopAd.class.getSimpleName());
        List<ProcessFlow> processFlows = processFlowService.findByProcessType(processType.getId());
        map.put("processFlows", processFlows);
        map.put("shopAd", shopAd);
        map.put("areas",officeService.findByType(Const.OFFICE_TYPE_AREA));
        map.put("bools", BoolEnum.getMap());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "save")
    public String save(ShopAd shopAd, BindingResult bindingResult) {
        shopAdValidator.validate(shopAd, bindingResult);
        RestResponse restResponse = new RestResponse("广告申请保存成功");
        if (bindingResult.hasErrors()) {
            restResponse = new RestResponse(false, bindingResult, MessageTypeEnum.error.name());
        } else {
            ProcessType processType = processTypeService.findByName(ShopAd.class.getSimpleName());
            shopAd.setProcessType(processType);
            shopAd.setProcessTypeId(processType.getId());
            shopAdService.save(shopAd);
        }
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "delete")
    public String delete(ShopAd shopAd) {
        shopAdService.logicDelete(shopAd.getId());
        RestResponse restResponse=new RestResponse("广告申请删除成功");
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public String detail(ShopAd shopAd) {
        Map<String, Object> map = Maps.newHashMap();
        if (StringUtils.isNotBlank(shopAd.getProcessInstanceId())) {
            map.put("activitiEntity", ActivitiUtils.getActivitiEntity(shopAd.getProcessInstanceId()));
        }
        map.put("shopAd", shopAd);
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "audit", method = RequestMethod.GET)
    public String audit(ShopAd shopAd, Boolean pass, String comment) {
        RestResponse restResponse=new RestResponse("广告申请审核成功");
        if (shopAdService.getAuditable(shopAd)) {
            Task task = taskService.createTaskQuery().processInstanceId(shopAd.getProcessInstanceId()).singleResult();
            if (!ActivitiUtils.claim(task)) {
                restResponse=new RestResponse(false,"无法签收任务，您没有办理此任务的权限或者已经被其他人签收", MessageTypeEnum.error.name());
            }else {
                shopAdService.audit(shopAd, pass, comment);
                shopAdService.notify(shopAd);
            }
        }
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "batchAudit")
    public String batchAudit(Boolean pass, @RequestParam(value = "ids[]")String[] ids) {
        for(String id:ids) {
            if(StringUtils.isNotBlank(id)) {
                ShopAd shopAd = shopAdService.findOne(id);
                shopAdService.audit(shopAd,pass,"已审核");
            }
        }
        RestResponse restResponse=new RestResponse("广告申请批量审核成功");
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ModelAndView export(org.apache.catalina.servlet4preview.http.HttpServletRequest request) {
        ExcelView excelView = new ExcelView();
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        Workbook workbook = new SXSSFWorkbook(Const.DEFAULT_PAGE_SIZE);
        List<SimpleExcelSheet> simpleExcelSheetList=shopAdService.findSimpleExcelSheets(workbook,searchEntity.getParams());
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"广告申请.xlsx",simpleExcelSheetList);
        return new ModelAndView(excelView, "simpleExcelBook", simpleExcelBook);
    }

    private List<String> getActionList(ShopAd shopAd) {
        List<String> actionList = Lists.newArrayList();
        if(SecurityUtils.getAuthorityList().contains("crm:shopAd:view")){
            actionList.add(Const.ITEM_ACTION_DETAIL);
        }
        Account account = AccountUtils.getAccount();
        ProcessFlow processFlow = processFlowService.findOne(shopAd.getProcessFlowId());
        if (processFlow != null && processFlow.getPositionId().equals(account.getPositionId()) || Const.HR_ACCOUNT_ADMIN_LIST.contains(account.getId())) {
            for(String authoritie : SecurityUtils.getAuthorityList()){
                switch (authoritie){
                    case "crm:shopAd:view" : actionList.add(Const.ITEM_ACTION_AUDIT); break;
                    case "crm:shopAd:delete" : actionList.add(Const.ITEM_ACTION_DELETE); break;
                    case "crm:shopAd:edit" : actionList.add(Const.ITEM_ACTION_EDIT); break;
                    default : break;
                }
            }
        }
        return actionList;
    }
}
