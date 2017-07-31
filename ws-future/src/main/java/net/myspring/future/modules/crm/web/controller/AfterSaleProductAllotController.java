package net.myspring.future.modules.crm.web.controller;

import net.myspring.future.modules.crm.dto.AfterSaleDto;
import net.myspring.future.modules.crm.dto.AfterSaleProductAllotDto;
import net.myspring.future.modules.crm.service.AfterSaleProductAllotService;
import net.myspring.future.modules.crm.web.query.AfterSaleProductAllotQuery;
import net.myspring.future.modules.crm.web.query.AfterSaleQuery;
import net.myspring.future.modules.crm.web.query.AfterSaleStoreAllotQuery;
import net.myspring.util.excel.ExcelView;
import net.myspring.util.excel.SimpleExcelBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * Created by zhucc on 2017/7/4.
 */
@RestController
@RequestMapping("crm/afterSaleProductAllot")
public class AfterSaleProductAllotController  {

    @Autowired
    private AfterSaleProductAllotService afterSaleProductAllotService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<AfterSaleProductAllotDto> list(Pageable pageable, AfterSaleProductAllotQuery afterSaleProductAllotQuery) {
        Page<AfterSaleProductAllotDto> page = afterSaleProductAllotService.findPage(pageable, afterSaleProductAllotQuery);
        return page;
    }

    @RequestMapping(value="getQuery")
    public AfterSaleProductAllotQuery  getQuery(AfterSaleProductAllotQuery afterSaleProductAllotQuery){
        return afterSaleProductAllotQuery;
    }
    @RequestMapping(value="export",method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'crm:afterSaleProductAllot:view')")
    public ModelAndView export(AfterSaleProductAllotQuery afterSaleProductAllotQuery) throws IOException {
        SimpleExcelBook simpleExcelBook =afterSaleProductAllotService.findSimpleExcelSheet(afterSaleProductAllotQuery);
        ExcelView excelView = new ExcelView();
        return new ModelAndView(excelView,"simpleExcelBook",simpleExcelBook);
    }

}
