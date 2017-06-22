package net.myspring.future.modules.crm.web.controller;


import net.myspring.future.modules.crm.dto.DepotDetailDto;
import net.myspring.future.modules.crm.service.DepotDetailService;
import net.myspring.future.modules.crm.web.query.DepotDetailQuery;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/depotDetail")
public class DepotDetailController {

    @Autowired
    private DepotDetailService depotDetailService;


    @RequestMapping(method = RequestMethod.GET)
    public Page<DepotDetailDto> list(Pageable pageable, DepotDetailQuery depotDetailQuery){
        Page<DepotDetailDto> page= depotDetailService.findPage(pageable,depotDetailQuery);
        return page;
    }

    @RequestMapping(value="getQuery")
    public String getQuery(){
        return null;
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ModelAndView export(HttpServletRequest request) {
        return null;
    }

}
