package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.utils.Collections3;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.future.common.config.ExcelView;
import net.myspring.future.common.excel.SimpleExcelBook;
import net.myspring.future.common.excel.SimpleExcelSheet;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.common.utils.StringUtils;
import net.myspring.future.modules.crm.domain.DemoPhone;
import net.myspring.future.modules.crm.domain.DemoPhoneType;
import net.myspring.future.modules.crm.domain.ProductType;
import net.myspring.future.modules.crm.service.DemoPhoneService;
import net.myspring.future.modules.crm.service.DemoPhoneTypeService;
import net.myspring.future.modules.crm.service.ProductImeService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/demoPhone")
public class DemoPhoneController {

    @Autowired
    private DemoPhoneService demoPhoneService;
    @Autowired
    private DemoPhoneTypeService demoPhoneTypeService;
    @Autowired
    private ProductImeService productImeService;

    @ModelAttribute
    public DemoPhone get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new DemoPhone() : demoPhoneService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        Page<DemoPhone> page=demoPhoneService.findPage(searchEntity.getPageable(), searchEntity.getParams());
        for(DemoPhone demoPhone:page.getContent()){
            demoPhone.setActionList(getActionList(demoPhone));
        }
        return  ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "getFormProperty", method = RequestMethod.GET)
    public String form(Model model) {
        Map<String,Object> map= Maps.newHashMap();
        List<DemoPhoneType> demoPhoneTypes = demoPhoneTypeService.findAllByApplyEndDate(LocalDate.now());
        List<ProductType> productTypes = Lists.newArrayList();
        for (DemoPhoneType demoPhoneType : demoPhoneTypes) {
            productTypes.addAll(demoPhoneType.getProductTypeList());
        }
        String productTypeNames = "";
        if (Collections3.isNotEmpty(productTypes)) {
            productTypeNames = StringUtils.join(Collections3.extractToList(productTypes, "name"), Const.CHAR_COMMA_FULL);
        }
        map.put("productTypeNames", productTypeNames);
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save( DemoPhone demoPhone){
        demoPhone.setProductIme(productImeService.findOne(demoPhone.getProductImeId()));
        demoPhoneService.save(demoPhone);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("保存成功"));
    }

    @RequestMapping(value = "findOne")
    public String findOne(DemoPhone demoPhone){
        return ObjectMapperUtils.writeValueAsString(demoPhone);
    }

    @RequestMapping(value = "delete")
    public String delete(DemoPhone demoPhone, RedirectAttributes redirectAttributes) {
        demoPhoneService.delete(demoPhone);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("删除成功"));
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ModelAndView export(HttpServletRequest request) {
        ExcelView excelView = new ExcelView();
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        Workbook workbook = new SXSSFWorkbook(Const.DEFAULT_PAGE_SIZE);
        List<SimpleExcelSheet> simpleExcelSheetList=demoPhoneService.findSimpleExcelSheets(workbook,searchEntity.getParams());
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"演示用机.xlsx",simpleExcelSheetList);
        return new ModelAndView(excelView, "simpleExcelBook", simpleExcelBook);
    }
    private List<String> getActionList(DemoPhone demoPhone) {
        List<String> actionList = Lists.newArrayList();
        for(String authoritie : SecurityUtils.getAuthorityList()){
            switch (authoritie){
                case "crm:demoPhone:delete" : actionList.add(Const.ITEM_ACTION_DELETE); break;
                case "crm:demoPhone:edit" : actionList.add(Const.ITEM_ACTION_EDIT); break;
                default : break;
            }
        }
        return actionList;
    }
}
