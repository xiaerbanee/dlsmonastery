package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Maps;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.future.common.config.ExcelView;
import net.myspring.future.common.enums.BoolEnum;
import net.myspring.future.common.excel.SimpleExcelBook;
import net.myspring.future.common.excel.SimpleExcelSheet;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.StringUtils;
import net.myspring.future.modules.crm.domain.DepotDetail;
import net.myspring.future.modules.crm.service.DepotDetailService;
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
@RequestMapping(value = "crm/depotDetail")
public class DepotDetailController {

    @Autowired
    private DepotDetailService depotDetailService;

    @ModelAttribute
    public DepotDetail get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new DepotDetail() : depotDetailService.findOne(id);
    }


    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        Page<DepotDetail> page = depotDetailService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value="getListProperty")
    public String getListProperty(){
        Map<String,Object> map= Maps.newHashMap();
        map.put("bools",BoolEnum.getMap());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ModelAndView export(HttpServletRequest request) {
        ExcelView excelView = new ExcelView();
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        Workbook workbook = new SXSSFWorkbook(Const.DEFAULT_PAGE_SIZE);
        List<SimpleExcelSheet> simpleExcelSheetList=depotDetailService.findSimpleExcelSheets(workbook,searchEntity.getParams());
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"货品库存.xlsx",simpleExcelSheetList);
        return new ModelAndView(excelView, "simpleExcelBook", simpleExcelBook);
    }

}
