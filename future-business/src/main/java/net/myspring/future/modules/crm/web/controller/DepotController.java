package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.enums.MessageTypeEnum;
import net.myspring.common.utils.Collections3;
import net.myspring.common.utils.DateUtils;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.future.common.config.ExcelView;
import net.myspring.future.common.enums.DepotCategoryEnum;
import net.myspring.future.common.enums.DepotTypeEnum;
import net.myspring.future.common.excel.SimpleExcelBook;
import net.myspring.future.common.excel.SimpleExcelColumn;
import net.myspring.future.common.excel.SimpleExcelSheet;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.DepotUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.common.utils.StringUtils;
import net.myspring.future.modules.crm.domain.Depot;
import net.myspring.future.modules.crm.model.DepotInventoryModel;
import net.myspring.future.modules.crm.model.ReceivableReportForDetail;
import net.myspring.future.modules.crm.service.DepotService;
import net.myspring.future.modules.crm.service.ShopAttributeService;
import net.myspring.future.modules.crm.validator.DepotValidator;
import net.myspring.future.modules.hr.service.OfficeService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/depot")
public class DepotController {

    @Autowired
    private DepotService depotService;
    @Autowired
    private ShopAttributeService shopAttributeService;
    @Autowired
    private DepotValidator depotValidator;
    @Autowired
    private OfficeService officeService;

    @ModelAttribute
    public Depot get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new Depot() : depotService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        Page<Depot> page = depotService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        for(Depot depot: page.getContent()){
            depot.setActionList(getActionList(depot));
        }
            return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "detail")
    public String detail(Depot depot) {
        depot.setShopAttributeList(shopAttributeService.findByShopId(depot.getId()));
        return ObjectMapperUtils.writeValueAsString(depot);
    }

    @RequestMapping(value = "inventory")
    public String inventory(HttpServletRequest request) {
        SearchEntity searchEntity=RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        Page<Depot> page=depotService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        searchEntity.getParams().put("depotIds", Collections3.extractToList(page.getContent(),"id"));
        depotService.setInventory(page.getContent(),searchEntity.getParams());
        for(Depot depot:page.getContent()){
            depot.setActionList(getInventoryActionList(depot));
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "inventoryDetail")
    public String inventoryDetail(Depot depot, HttpServletRequest request) {
        SearchEntity searchEntity=RequestUtils.getSearchEntity(request);
        searchEntity.getParams().put("depotIds", Lists.newArrayList(depot.getId()));
        depotService.setInventoryDetail(depot,searchEntity.getParams());
        return ObjectMapperUtils.writeValueAsString(depot);
    }

    @RequestMapping(value = "save")
    public String save(Depot depot, BindingResult bindingResult) {
        depotValidator.validate(depot, bindingResult);
        RestResponse restResponse =new RestResponse("保存成功");
        if (bindingResult.hasErrors()) {
            restResponse =new RestResponse(false,bindingResult, MessageTypeEnum.error.name());
        } else {
            depotService.save(depot);
        }
        String response = ObjectMapperUtils.writeValueAsString(restResponse);
        return response;
    }

    @RequestMapping(value = "syn")
    public String syn(){
        depotService.synStore();
        depotService.synShop();
        return ObjectMapperUtils.writeValueAsString(new RestResponse("同步成功"));
    }

    @RequestMapping(value = "getFormProperty")
    public String getFormProperty() {
        Map<String,Object> propertyMap = depotService.getFormProperty();
        return ObjectMapperUtils.writeValueAsString(propertyMap);
    }

    @RequestMapping(value = "getListProperty")
    public String getListProperty() {
        Map<String,Object> searchPropertyMap = depotService.getListProperty();
        searchPropertyMap.put("offices",officeService.findSortList());
        String dateStart=DateUtils.formatLocalDate(LocalDate.now().plusDays(-70));
        String dateEnd=DateUtils.formatLocalDate(LocalDate.now().plusMonths(1));
        String dateRange=dateStart+" - "+dateEnd;
        searchPropertyMap.put("dateRange",dateRange);
        return ObjectMapperUtils.writeValueAsString(searchPropertyMap);
    }

    @RequestMapping(value = "findOne")
    public String findOne(Depot depot){
        return ObjectMapperUtils.writeValueAsString(depot);
    }

    @RequestMapping(value = "search")
    public String search(String name,String category) {
        List<Depot> depotList = Lists.newArrayList();
        Map<String,Object> filter = FilterUtils.getDepotFilter(AccountUtils.getAccountId());
        if(StringUtils.isNotBlank(name) || filter.containsKey("depotIds") && Collections3.isNotEmpty((Collection) filter.get("depotIds"))) {
            Map<String, Object> map = Maps.newHashMap(filter);
            map.put("name", name);
            if (StringUtils.isNotBlank(category)) {
                map.put("types", DepotUtils.getTypeMapByCategory(category));
            }
            depotList = depotService.findByFilter(map);
        }
        return ObjectMapperUtils.writeValueAsString(depotList);
    }

    @RequestMapping(value = "proxyShop")
    public String adShop(String key) {
        List<Map<String, String>> list = Lists.newArrayList();
        if(StringUtils.isNotBlank(key)) {
            Map<String,Object> paramMap=Maps.newHashMap();
            paramMap.put("depotName",key);
            paramMap.put("type", DepotTypeEnum.门店_代理.getValue());
            List<Depot> depots = depotService.findProxyShop(paramMap);
            for (Depot depot : depots) {
                Map<String, String> map = Maps.newHashMap();
                map.put("id", depot.getId());
                map.put("fullName", depot.getArea().getName()+Const.CHAR_SLASH_LINE+depot.getName());
                map.put("name", depot.getArea().getName()+Const.CHAR_SLASH_LINE+depot.getName());
                list.add(map);
            }
        }
        return ObjectMapperUtils.writeValueAsString(list);
    }

    @RequestMapping(value = "adShop")
    public String adShop(String adShopName,String billType){
        List<Map<String, String>> list = Lists.newArrayList();
        if(StringUtils.isNotBlank(adShopName)) {
            List<Depot> depots = depotService.findAdDepot(adShopName,billType);
            for (Depot depot : depots) {
                Map<String, String> map = Maps.newHashMap();
                map.put("id", depot.getId());
                map.put("typeLabel",depot.getTypeLabel());
                if(depot.getAreaId() != null){
                    map.put("name", depot.getArea().getName() + Const.CHAR_SLASH_LINE + depot.getName());
                }
                list.add(map);
            }
        }
        return ObjectMapperUtils.writeValueAsString(list);
    }

    @RequestMapping(value = "adShopBsc")
    public String adShopBsc(String key) {
        List<Map<String, String>> list = Lists.newArrayList();
        List<Depot> depots=Lists.newArrayList();
        if(StringUtils.isNotBlank(key)) {
            depots=depotService.findAdShopBsc(key);
            for (Depot depot : depots) {
                Map<String, String> map = Maps.newHashMap();
                map.put("id", depot.getId());
                map.put("fullName", depot.getArea().getName()+Const.CHAR_SLASH_LINE+depot.getName());
                map.put("name", depot.getArea().getName()+Const.CHAR_SLASH_LINE+depot.getName());
                list.add(map);
            }
        }
        return ObjectMapperUtils.writeValueAsString(list);
    }

    @RequestMapping(value = "shop")
    public String shop(String name) {
        List<Depot> shopList = Lists.newArrayList();
        Map<String,Object> filter = FilterUtils.getDepotFilter(AccountUtils.getAccountId());
        if(StringUtils.isNotBlank(name)|| filter.containsKey("depotIds") && Collections3.isNotEmpty((Collection) filter.get("depotIds"))) {
            Map<String, Object> map = Maps.newHashMap(filter);
            map.put("name", name);
            shopList = depotService.findByFilter(map);
        }
        return ObjectMapperUtils.writeValueAsString(shopList);
    }

    private List<String> getInventoryActionList(Depot depot){
        List<String> list= Lists.newArrayList();
        DepotInventoryModel depotInventoryModel= (DepotInventoryModel) depot.getExtendMap().get("depotInventoryModel");
        if(depotInventoryModel!=null&&(depotInventoryModel.getBaoKaSalesQty()>0||depotInventoryModel.getBaoKaStockQty()>0||depotInventoryModel.getSaleSalesQty()>0||depotInventoryModel.getSaleStockQty()>0)){
            list.add(Const.ITEM_ACTION_DETAIL);
        }
        return list;
    }

    private List<String> getActionList(Depot depot) {
        List<String> actionList = Lists.newArrayList();
        //门店才允许在手机端修改和终端统计
        if(DepotUtils.getTypeValueByCategory(DepotCategoryEnum.SHOP_PROXY_STORE.name()).contains(depot.getType()) && SecurityUtils.getAuthorityList().contains("crm:depot:edit")) {
            actionList.add(Const.ITEM_ACTION_EDIT);
        }
        return actionList;
    }

    @RequestMapping(value = "depotAccountData",method = RequestMethod.GET)
    public String depotAccountData(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        String dutyDateStart = (String)searchEntity.getParams().get("dutyDateStart");
        String dutyDateEnd = (String)searchEntity.getParams().get("dutyDateEnd");
        if(StringUtils.isBlank(dutyDateStart) && StringUtils.isBlank(dutyDateEnd)){
            dutyDateStart=DateUtils.formatLocalDate(LocalDate.now().plusDays(-70));
            dutyDateEnd=DateUtils.formatLocalDate(LocalDate.now().plusMonths(1));
        }
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        Page<Depot> page = depotService.findShopAccount(searchEntity.getPageable(),searchEntity.getParams(),dutyDateStart,dutyDateEnd);
        for(Depot depot: page.getContent()){
            depot.setActionList( Lists.newArrayList(Const.ITEM_ACTION_DETAIL));
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "depotAccountDetail",method = RequestMethod.GET)
    public String depotAccountDetail(Depot depot, HttpServletRequest request) {
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        String dateStart=DateUtils.formatLocalDate(LocalDate.now().plusDays(-70));
        String dateEnd=DateUtils.formatLocalDate(LocalDate.now().plusMonths(1));
        String dateRange = (String)searchEntity.getParams().get("dateRange");
        if(StringUtils.isNotBlank(dateRange)) {
            String[] dateRangeBetween = dateRange.split(" - ");
            dateStart = dateRangeBetween[0];
            dateEnd = dateRangeBetween[1];
        }
         dateRange=dateStart+"~"+dateEnd;
        List<ReceivableReportForDetail> receivableReportForDetail=depotService.findShopAccountDetail(depot.getOutId(),dateRange);
        return  ObjectMapperUtils.writeValueAsString(receivableReportForDetail);
    }

    //导出所有直营门店的应收
    @RequestMapping(value = "shopExport", method = RequestMethod.GET)
    public ModelAndView shopExport(HttpServletRequest request) {
        ExcelView excelView = new ExcelView();
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        String dutyDateStart = (String)searchEntity.getParams().get("dutyDateStart");
        String dutyDateEnd = (String)searchEntity.getParams().get("dutyDateEnd");
        if(StringUtils.isBlank(dutyDateStart) && StringUtils.isBlank(dutyDateEnd)){
            dutyDateStart=DateUtils.formatLocalDate(LocalDate.now().plusDays(-70));
            dutyDateEnd=DateUtils.formatLocalDate(LocalDate.now().plusMonths(1));
        }
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        Workbook workbook = new SXSSFWorkbook(Const.DEFAULT_PAGE_SIZE);
        List<Depot> depotList=depotService.findSimpleExcelSheets(workbook,searchEntity.getParams(),dutyDateStart,dutyDateEnd);
        List<SimpleExcelSheet> simpleExcelSheetList = Lists.newArrayList();
        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "name", "门店"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "office.name", "机构"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "area.name", "办事处"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "extendMap.qcys", "期初应收"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "extendMap.qmys", "期末应收"));
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("应收报表~所有门店", depotList, simpleExcelColumnList);
        simpleExcelSheetList.add(simpleExcelSheet);
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"应收报表.xlsx",simpleExcelSheetList);
        return new ModelAndView(excelView, "simpleExcelBook", simpleExcelBook);
    }

    //导出应收(明细)
    @RequestMapping(value = "accountExport", method = RequestMethod.GET)
    public ModelAndView accountExport(HttpServletRequest request) {
        ExcelView excelView = new ExcelView();
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        String dutyDateStart = (String)searchEntity.getParams().get("dutyDateStart");
        String dutyDateEnd = (String)searchEntity.getParams().get("dutyDateEnd");
        if(StringUtils.isBlank(dutyDateStart) && StringUtils.isBlank(dutyDateEnd)){
            dutyDateStart=DateUtils.formatLocalDate(LocalDate.now().plusDays(-70));
            dutyDateEnd=DateUtils.formatLocalDate(LocalDate.now().plusMonths(1));
        }
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        Workbook workbook = new SXSSFWorkbook(Const.DEFAULT_PAGE_SIZE);
        SimpleExcelBook simpleExcelBook = depotService.accountExport(workbook,searchEntity.getParams(),dutyDateStart,dutyDateEnd);
        return new ModelAndView(excelView, "simpleExcelBook", simpleExcelBook);
    }
}
