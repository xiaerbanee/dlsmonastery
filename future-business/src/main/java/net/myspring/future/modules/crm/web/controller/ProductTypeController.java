package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.future.common.config.ExcelView;
import net.myspring.future.common.enums.BoolEnum;
import net.myspring.future.common.excel.SimpleExcelBook;
import net.myspring.future.common.excel.SimpleExcelSheet;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.common.utils.StringUtils;
import net.myspring.future.modules.crm.domain.ProductType;
import net.myspring.future.modules.crm.service.ProductTypeService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/productType")
public class ProductTypeController {

    @Autowired
    private ProductTypeService productTypeService;

    @ModelAttribute
    public ProductType get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new ProductType() : productTypeService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        Page<ProductType> page = productTypeService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        for(ProductType productType: page.getContent()){
            productType.setActionList(getActionList());
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "delete")
    public String delete(String id) {
        productTypeService.logicDeleteOne(id);
        RestResponse restResponse =new RestResponse("删除成功");
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "save")
    public String save(ProductType productType) {
        productTypeService.save(productType);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("保存成功"));
    }

    @RequestMapping(value = "findOne")
    public String findOne(ProductType productType){
        return ObjectMapperUtils.writeValueAsString(productType);
    }

    @RequestMapping(value="getFormProperty")
    public String getFormProperty(){
        Map<String,Object> map= Maps.newHashMap();
        map.put("bools", BoolEnum.getMap());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value="getListProperty")
    public String getListProperty(){
        Map<String,Object> map= Maps.newHashMap();
        map.put("bools", BoolEnum.getMap());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "search")
    public String search(String name){
        List<ProductType> productTypeList = Lists.newArrayList();
        if(StringUtils.isNotBlank(name)){
            productTypeList = productTypeService.findByNameLike(name);
        }
        return ObjectMapperUtils.writeValueAsString(productTypeList);
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ModelAndView export(HttpServletRequest request) {
        ExcelView excelView = new ExcelView();
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        Workbook workbook = new SXSSFWorkbook(Const.DEFAULT_PAGE_SIZE);
        List<SimpleExcelSheet> simpleExcelSheetList=productTypeService.findSimpleExcelSheets(workbook,searchEntity.getParams());
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"产品型号.xlsx",simpleExcelSheetList);
        return new ModelAndView(excelView, "simpleExcelBook", simpleExcelBook);
    }

    private List<String> getActionList() {
        List<String> actionList = Lists.newArrayList();
        for(String authoritie : SecurityUtils.getAuthorityList()){
            switch (authoritie){
                case "crm:productType:delete" : actionList.add(Const.ITEM_ACTION_DELETE); break;
                case "crm:productType:edit" : actionList.add(Const.ITEM_ACTION_EDIT); break;
                default : break;
            }
        }
        return actionList;
    }
}
