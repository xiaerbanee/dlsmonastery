package net.myspring.tool.modules.oppo.web.controller;


import net.myspring.tool.modules.oppo.service.OppoPlantSendImeiPpselService;
import net.myspring.util.excel.ExcelView;
import net.myspring.util.time.LocalDateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping(value = "factory/oppo/oppoPlantSendImeiPpsel")
public class OppoPlantSendImeiPpselController {

    @Autowired
    private OppoPlantSendImeiPpselService oppoPlantSendImeiPpselService;

    @RequestMapping(value = "export",method = RequestMethod.GET)
    public ModelAndView export(String date) throws IOException{
        if(StringUtils.isBlank(date)){
            date = LocalDateUtils.format(LocalDate.now(),"yyyy-MM-dd");
        }
        return new ModelAndView(new ExcelView(),"simpleExcelBook",oppoPlantSendImeiPpselService.export(date));
    }



}