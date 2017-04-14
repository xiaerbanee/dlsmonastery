package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.common.utils.StringUtils;
import net.myspring.future.common.config.ExcelView;
import net.myspring.future.common.enums.InputTypeEnum;
import net.myspring.future.common.excel.SimpleExcelBook;
import net.myspring.future.common.excel.SimpleExcelSheet;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.modules.crm.domain.Depot;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.model.ProductImeHistoryModel;
import net.myspring.future.modules.crm.model.ProductImeSearchResultModel;
import net.myspring.future.modules.crm.service.ProductImeService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/productIme")
public class ProductImeController {

    @Autowired
    private ProductImeService productImeService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        Page<ProductIme> page = productImeService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        for(ProductIme productIme: page.getContent()){
            productIme.setActionList(getActionList());
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "getListProperty")
    public String getListProperty(Depot depot) {
        Map<String,Object> map = Maps.newHashMap();
        map.put("inputTypes", InputTypeEnum.values());
        return ObjectMapperUtils.writeValueAsString(map);
    }


    @RequestMapping(value = "search")
    public String findByImeList(String imeStr ) {
        ProductImeSearchResultModel productImeSearchResult = null;
        if(StringUtils.isNotBlank(imeStr)) {
            List<String> imeList = StringUtils.getSplitList(imeStr, "\n");
            productImeSearchResult = productImeService.findProductImeSearchResult(imeList);
        }
        return ObjectMapperUtils.writeValueAsString(productImeSearchResult);
    }

    @RequestMapping(value = "searchIme")
    public String searchIme(String ime ) {
        List<ProductIme> productImeList=productImeService.findByImeLike(ime);
        for(ProductIme productIme:productImeList){
            productIme.setFullName(productIme.getIme()+Const.CHAR_UNDER_LINE+productIme.getProduct().getName());
        }
        return ObjectMapperUtils.writeValueAsString(productImeList);
    }

    @RequestMapping(value = "searchByStore")
    public String searchByStore(String depotId,String ime ) {
        List<ProductIme> productImeList=productImeService.findByDepotAndImeLike(depotId,ime);
        return ObjectMapperUtils.writeValueAsString(productImeList);
    }

    @RequestMapping(value = "detail")
    public String detail(String productImeId){
        ProductIme productIme = productImeService.findOne(productImeId);
        List<ProductImeHistoryModel> allotHistoryList = productImeService.findProductImeHistoryModel(productImeId);
        Map<String,Object> map = Maps.newHashMap();
        map.put("productIme",productIme);
        map.put("allotHistoryList",allotHistoryList);
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ModelAndView export(HttpServletRequest request) {
        ExcelView excelView = new ExcelView();
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        Workbook workbook = new SXSSFWorkbook(Const.DEFAULT_PAGE_SIZE);
        List<SimpleExcelSheet> simpleExcelSheetList=productImeService.findSimpleExcelSheets(workbook,searchEntity.getParams());
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"仓库调整.xlsx",simpleExcelSheetList);
        return new ModelAndView(excelView, "simpleExcelBook", simpleExcelBook);
    }

    private List<String> getActionList() {
        List<String> actionList = Lists.newArrayList();
        if(SecurityUtils.getAuthorityList().contains("crm:productIme:view")){
            actionList.add(Const.ITEM_ACTION_DETAIL);
        }
        return actionList;
    }

}
