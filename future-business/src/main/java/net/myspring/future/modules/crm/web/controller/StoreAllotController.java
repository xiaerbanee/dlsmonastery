package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.enums.MessageTypeEnum;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.future.common.config.ExcelView;
import net.myspring.future.common.enums.*;
import net.myspring.future.common.excel.SimpleExcelBook;
import net.myspring.future.common.excel.SimpleExcelSheet;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.DepotUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.common.utils.StringUtils;
import net.myspring.future.modules.api.service.K3cloudSynService;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.domain.StoreAllot;
import net.myspring.future.modules.crm.domain.StoreAllotDetail;
import net.myspring.future.modules.crm.service.DepotService;
import net.myspring.future.modules.crm.service.ExpressCompanyService;
import net.myspring.future.modules.crm.service.ExpressOrderService;
import net.myspring.future.modules.crm.service.StoreAllotService;
import net.myspring.future.modules.crm.validator.StoreAllotValidator;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/storeAllot")
public class StoreAllotController {

    @Autowired
    private StoreAllotService storeAllotService;
    @Autowired
    private DepotService depotService;
    @Autowired
    private ExpressCompanyService expressCompanyService;
    @Autowired
    private ExpressOrderService expressOrderService;
    @Autowired
    private K3cloudSynService k3cloudSynService;
    @Autowired
    private StoreAllotValidator storeAllotValidator;

    @ModelAttribute
    public StoreAllot get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new StoreAllot() : storeAllotService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        Page<StoreAllot> page = storeAllotService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        for(StoreAllot storeAllot: page.getContent()){
            storeAllot.setActionList(getActionList(storeAllot));
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "save")
    public RestResponse save(StoreAllot storeAllot) {
        if(AccountUtils.getAccount().getOutId()==null) {
            return new RestResponse(false,"您没有和财务关联，无法开单。", MessageTypeEnum.error.name());
        }
        StoreAllot store = storeAllotService.save(storeAllot);
        if(storeAllot.getSyn()){
            k3cloudSynService.syn(store.getId(), CloudSynExtendTypeEnum.大库调拨.name());
            if(store.getExpressOrder()!=null){
                ExpressOrder expressOrder = store.getExpressOrder();
                String outCode = k3cloudSynService.getOutCode(store.getId(), CloudSynExtendTypeEnum.大库调拨.name());
                expressOrder.setOutCode(outCode);
                expressOrderService.update(expressOrder);
                store.setOutCode(outCode);
                storeAllotService.update(store);
            }
        }
        return new RestResponse("保存成功");
    }

    @RequestMapping(value = "findOne")
    public String findOne(StoreAllot storeAllot){
        return ObjectMapperUtils.writeValueAsString(storeAllot);
    }

    @RequestMapping(value = "getStoreAllotData")
    public String getStoreAllotData(StoreAllot storeAllot){
        storeAllot = storeAllotService.getStoreAllotData(storeAllot);
        return ObjectMapperUtils.writeValueAsString(storeAllot);
    }

    @RequestMapping(value = "getCloudQty")
    public String getCloudQty(String productId,String fromStoreId){
        Integer cloudQty=0;
        if(StringUtils.isNoneBlank(productId)&&StringUtils.isNotBlank(fromStoreId)){
            cloudQty=storeAllotService.getCloudQty(productId,fromStoreId);
        }
        return ObjectMapperUtils.writeValueAsString(cloudQty);
    }

    @RequestMapping(value="getFormProperty")
    public String getFormProperty(StoreAllot storeAllot){
        Map<String,Object> map= Maps.newHashMap();
        map.put("status", StoreAllotStatus.values());
        map.put("stores", depotService.findByTypes(DepotUtils.getTypeValueByCategory(DepotCategoryEnum.STORE.name())));
        map.put("shipTypes", ShipTypeEnum.values());
        map.put("expressCompanys",expressCompanyService.findAll());
        map.put("storeAllotTypes", StoreAllotTypeEnum.values());
        map.put("bools", BoolEnum.getMap());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value="getListProperty")
    public String getListProperty(){
        Map<String,Object> map= Maps.newHashMap();
        map.put("status", StoreAllotStatus.values());
        map.put("stores", depotService.findByTypes(DepotUtils.getTypeValueByCategory(DepotCategoryEnum.STORE.name())));
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ModelAndView export(HttpServletRequest request) {
        ExcelView excelView = new ExcelView();
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        Workbook workbook = new SXSSFWorkbook(Const.DEFAULT_PAGE_SIZE);
        List<SimpleExcelSheet> simpleExcelSheetList=storeAllotService.findSimpleExcelSheets(workbook,searchEntity.getParams());
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"大库调拨.xlsx",simpleExcelSheetList);
        return new ModelAndView(excelView, "simpleExcelBook", simpleExcelBook);
    }

    @RequestMapping(value = "ship", method=RequestMethod.GET)
    public String shipForm(StoreAllot storeAllot) {
        for(StoreAllotDetail storeAllotDetail:storeAllot.getStoreAllotDetailList()){
            storeAllotDetail.getExtendMap().put("waitShipQty",storeAllotDetail.getBillQty()-(storeAllotDetail.getShippedQty()==null?0:storeAllotDetail.getShippedQty()));
        }
        return ObjectMapperUtils.writeValueAsString(storeAllot);
    }

    @RequestMapping(value = "shipBoxAndIme", method = RequestMethod.GET)
    public String shipBoxAndIme(StoreAllot storeAllot, String boxStr, String imeStr) {
        storeAllot=storeAllotService.shipBoxAndIme(storeAllot,boxStr,imeStr);
        return ObjectMapperUtils.writeValueAsString(storeAllot);
    }

    @RequestMapping(value = "ship", method=RequestMethod.POST)
    public RestResponse ship(StoreAllot storeAllot, BindingResult bindingResult) {
        storeAllot.setCurrentAction("ship");
        storeAllotValidator.validate(storeAllot,bindingResult);
        if(bindingResult.hasErrors()){
            return new RestResponse(false,bindingResult, MessageTypeEnum.error.name());
        }
        storeAllotService.ship(storeAllot);
        if(storeAllot.getExpressOrder()!=null&&StringUtils.isNotBlank(storeAllot.getExpressOrder().getExpressCodes())&&storeAllot.getExpressOrder().getExpressCompanyId()!=null){
            expressOrderService.save(ExpressOrderTypeEnum.大库调拨.name(), storeAllot.getId(), storeAllot.getExpressOrder().getExpressCodes(),storeAllot.getExpressOrder().getExpressCompanyId());
        }
        return new RestResponse("发货成功");
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(StoreAllot storeAllot, RedirectAttributes redirectAttributes) {
        storeAllotService.delete(storeAllot);
        return new RestResponse("删除成功");
    }

    private List<String> getActionList(StoreAllot storeAllot) {
        List<String> actionList = Lists.newArrayList();
        for(String authoritie : SecurityUtils.getAuthorityList()){
            switch (authoritie){
                case "crm:storeAllot:delete" : actionList.add(Const.ITEM_ACTION_DELETE); break;
                case "crm:storeAllot:view" : actionList.add(Const.ITEM_ACTION_DETAIL); break;
                case "crm:storeAllot:ship" : actionList.add(Const.ITEM_ACTION_SHIP); break;
                default : break;
            }
        }
        return actionList;
    }

}
