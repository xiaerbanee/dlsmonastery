package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.enums.MessageTypeEnum;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.future.common.config.ExcelView;
import net.myspring.future.common.enums.DictEnumCategoryEnum;
import net.myspring.future.common.excel.SimpleExcelBook;
import net.myspring.future.common.excel.SimpleExcelSheet;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.common.utils.StringUtils;
import net.myspring.future.modules.crm.domain.ShopBuild;
import net.myspring.future.modules.crm.service.ShopBuildService;
import net.myspring.future.modules.crm.validator.ShopBuildValidator;
import net.myspring.future.modules.hr.domain.Account;
import net.myspring.future.modules.hr.service.OfficeService;
import net.myspring.future.modules.sys.domain.ProcessFlow;
import net.myspring.future.modules.sys.domain.ProcessType;
import net.myspring.future.modules.sys.service.DictEnumService;
import net.myspring.future.modules.sys.service.ProcessFlowService;
import net.myspring.future.modules.sys.service.ProcessTypeService;
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
@RequestMapping(value = "crm/shopBuild")
public class ShopBuildController {

    @Autowired
    private ShopBuildService shopBuildService;
    @Autowired
    private DictEnumService dictEnumService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private ProcessTypeService processTypeService;
    @Autowired
    private ProcessFlowService processFlowService;
    @Autowired
    private ShopBuildValidator shopBuildValidator;

    @ModelAttribute
    public ShopBuild find(@RequestParam(required = false)String id){
        return StringUtils.isBlank(id)?new ShopBuild():shopBuildService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        if(searchEntity.getParams().get("auditType") == null || searchEntity.getParams().get("auditType").equals("1")){
            searchEntity.getParams().put("positionId", AccountUtils.getPosition().getId());
        }
        Page<ShopBuild> page = shopBuildService.findPage(searchEntity.getPageable(), searchEntity.getParams());
        for (ShopBuild shopBuild : page.getContent()) {
            shopBuild.setActionList(getActionList(shopBuild));
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "getFormProperty")
    public String getFormProperty() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("buildTypes",dictEnumService.findValueByCategory(DictEnumCategoryEnum.BUILD_TYPE.getValue()));
        map.put("shopTypes",dictEnumService.findValueByCategory(DictEnumCategoryEnum.SHOP_TYPE.getValue()));
        map.put("fixtureTypes",dictEnumService.findValueByCategory(DictEnumCategoryEnum.FIXTURE_TYPE.getValue()));
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "getListProperty")
    public String getListProperty() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("areas",officeService.findByType(Const.OFFICE_TYPE_AREA));
        map.put("fixtureTypes",dictEnumService.findValueByCategory(DictEnumCategoryEnum.FIXTURE_TYPE.getValue()));
        ProcessType processType = processTypeService.findByName(ShopBuild.class.getSimpleName());
        List<ProcessFlow> processFlows = processFlowService.findByProcessType(processType.getId());
        map.put("processFlows", processFlows);
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(ShopBuild shopBuild, BindingResult bindingResult){
        shopBuildValidator.validate(shopBuild, bindingResult);
        RestResponse restResponse = new RestResponse("保存成功");
        if (bindingResult.hasErrors()) {
            restResponse = new RestResponse(false, bindingResult, MessageTypeEnum.error.name());
        } else {
            ProcessType processType = processTypeService.findByName(ShopBuild.class.getSimpleName());
            shopBuild.setProcessType(processType);
            shopBuild.setProcessTypeId(processType.getId());
            shopBuildService.save(shopBuild);
        }
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "batchAudit", method = RequestMethod.GET)
    public String batchAudit(Boolean pass, @RequestParam(value = "ids[]")String[] ids) {
        for(String id:ids) {
            if(StringUtils.isNotBlank(id)) {
                ShopBuild shopBuild = shopBuildService.findOne(id);
                shopBuildService.audit(shopBuild,pass,"已审核");
                shopBuildService.notify(shopBuild);
            }
        }
        RestResponse restResponse = new RestResponse("门店建设批量审核成功");
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "delete")
    public String delete(String id) {
        shopBuildService.logicDeleteOne(id);
        RestResponse restResponse =new RestResponse("删除成功");
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "detail")
    public String detail(ShopBuild shopBuild) {
        return ObjectMapperUtils.writeValueAsString(shopBuild);
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ModelAndView export(org.apache.catalina.servlet4preview.http.HttpServletRequest request) {
        ExcelView excelView = new ExcelView();
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        Workbook workbook = new SXSSFWorkbook(Const.DEFAULT_PAGE_SIZE);
        List<SimpleExcelSheet> simpleExcelSheetList=shopBuildService.findSimpleExcelSheets(workbook,searchEntity.getParams());
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"门店建设.xlsx",simpleExcelSheetList);
        return new ModelAndView(excelView, "simpleExcelBook", simpleExcelBook);
    }

    private List<String> getActionList(ShopBuild shopBuild) {
        List<String> actionList = Lists.newArrayList();
        if(SecurityUtils.getAuthorityList().contains("crm:shopBuild:view")){
            actionList.add(Const.ITEM_ACTION_DETAIL);
        }
        Account account = AccountUtils.getAccount();
        ProcessFlow processFlow = processFlowService.findOne(shopBuild.getProcessFlowId());
        if (processFlow != null && processFlow.getPositionId().equals(account.getPositionId()) || Const.HR_ACCOUNT_ADMIN_LIST.contains(account.getId())) {
            if(SecurityUtils.getAuthorityList().contains("crm:shopBuild:edit")){
                actionList.add(Const.ITEM_ACTION_AUDIT);
            }
        }
        if(account.getId().equals(shopBuild.getCreatedBy())){
            for(String authoritie : SecurityUtils.getAuthorityList()){
                switch (authoritie){
                    case "crm:shopBuild:delete" : actionList.add(Const.ITEM_ACTION_DELETE); break;
                    case "crm:shopBuild:edit" : actionList.add(Const.ITEM_ACTION_EDIT); break;
                    default : break;
                }
            }
        }
        return actionList;
    }
}
