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
import net.myspring.future.common.enums.DepotChangeTypeEnum;
import net.myspring.future.common.excel.SimpleExcelBook;
import net.myspring.future.common.excel.SimpleExcelSheet;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.modules.crm.domain.DepotChange;
import net.myspring.future.modules.crm.service.DepotChangeService;
import net.myspring.future.modules.crm.service.PricesystemService;
import net.myspring.future.modules.crm.validator.DepotChangeValidator;
import net.myspring.future.modules.hr.domain.Account;
import net.myspring.future.modules.hr.domain.AccountTask;
import net.myspring.future.modules.hr.service.AccountTaskService;
import net.myspring.future.modules.sys.domain.ProcessType;
import net.myspring.future.modules.sys.service.ProcessTypeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/depotChange")
public class DepotChangeController {

    @Autowired
    private DepotChangeService depotChangeService;
    @Autowired
    protected TaskService taskService;
    @Autowired
    private DepotChangeValidator depotChangeValidator;
    @Autowired
    private PricesystemService pricesystemService;
    @Autowired
    private ProcessTypeService processTypeService;
    @Autowired
    private AccountTaskService accountTaskService;

    @ModelAttribute
    public DepotChange get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new DepotChange() : depotChangeService.findOne(id);
    }


    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        Page<DepotChange> page = depotChangeService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        for(DepotChange depotChange: page.getContent()){
            depotChange.setActionList(getActionList(depotChange));
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "findOne")
    public String findOne(String id){
        DepotChange depotChange= depotChangeService.findOne(id);
        return ObjectMapperUtils.writeValueAsString(depotChange);
    }


    @RequestMapping(value = "save")
    public RestResponse save(DepotChange depotChange, BindingResult result) {
        depotChangeValidator.validate(depotChange,result);
        if(result.hasErrors()){
            return new RestResponse(false, result, MessageTypeEnum.error.name());
        }
        ProcessType processType = processTypeService.findByName(DepotChange.class.getSimpleName());
        depotChange.setProcessType(processType);
        depotChangeService.save(depotChange);
        return   new RestResponse("仓库调整保存成功");
    }

    @RequestMapping(value = "delete")
    public String delete(DepotChange depotChange) {
        depotChangeService.logicDeleteOne(depotChange.getId());
        AccountTask accountTask = accountTaskService.findByNameAndExtendId("仓库调整",depotChange.getId());
        if (accountTask!=null) {
            accountTaskService.logicDeleteOne(accountTask.getId());
        }
        RestResponse restResponse =new RestResponse("删除成功");
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "getFormProperty")
    public String getFormProperty() {
        Map<String,Object> map = Maps.newHashMap();
        map.put("types", DepotChangeTypeEnum.values());
        map.put("pricesystems",pricesystemService.findAll());
        map.put("bools",BoolEnum.getMap());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "getListProperty")
    public String getListProperty() {
        Map<String,Object> map = Maps.newHashMap();
        map.put("types", DepotChangeTypeEnum.values());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public String detail(DepotChange depotChange) {
        Map<String,Object> paramMap= Maps.newHashMap();
        if (StringUtils.isNotBlank(depotChange.getProcessInstanceId())) {
            paramMap.put("activitiEntity", ActivitiUtils.getActivitiEntity(depotChange.getProcessInstanceId()));
        }
        paramMap.put("depotChange", depotChange);
        paramMap.put("bools", BoolEnum.getMap());
        return ObjectMapperUtils.writeValueAsString(paramMap);
    }

    @RequestMapping(value = "audit")
    public String audit(DepotChange depotChange, boolean pass, String comment) {
        RestResponse restResponse=new RestResponse("仓库调整成功");
        Task task = taskService.createTaskQuery().processInstanceId(depotChange.getProcessInstanceId()).singleResult();
        if (!ActivitiUtils.claim(task)) {
            restResponse=new RestResponse(false,"无法签收任务，您没有办理此任务的权限或者已经被其他人签收", MessageTypeEnum.error.name());
            return ObjectMapperUtils.writeValueAsString(restResponse);
        }
        depotChangeService.audit(depotChange,pass,comment);
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ModelAndView export(HttpServletRequest request) {
        ExcelView excelView = new ExcelView();
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        Workbook workbook = new SXSSFWorkbook(Const.DEFAULT_PAGE_SIZE);
        List<SimpleExcelSheet> simpleExcelSheetList=depotChangeService.findSimpleExcelSheets(workbook,searchEntity.getParams());
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"仓库调整.xlsx",simpleExcelSheetList);
        return new ModelAndView(excelView, "simpleExcelBook", simpleExcelBook);
    }

    private List<String> getActionList(DepotChange depotChange) {
        List<String> actionList = Lists.newArrayList();
        if(SecurityUtils.getAuthorityList().contains("crm:depotChange:view")){
            actionList.add(Const.ITEM_ACTION_DETAIL);
        }
        Account account=AccountUtils.getAccount();
        if ((depotChange.getProcessFlow() != null && depotChange.getProcessFlow().getPositionId().equals(account.getPositionId()) || Const.HR_ACCOUNT_ADMIN_LIST.contains(account.getId())) && (!"已通过".equals(depotChange.getProcessStatus())) && (!"未通过".equals(depotChange.getProcessStatus()))) {
            for(String authoritie : SecurityUtils.getAuthorityList()){
                switch (authoritie){
                    case "crm:depotChange:delete" : actionList.add(Const.ITEM_ACTION_DELETE); break;
                    case "crm:depotChange:edit" : actionList.add(Const.ITEM_ACTION_AUDIT); break;
                    default : break;
                }
            }
        }
        return actionList;
    }
}
