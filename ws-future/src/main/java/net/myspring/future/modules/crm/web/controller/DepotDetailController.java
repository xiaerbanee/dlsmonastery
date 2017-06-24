package net.myspring.future.modules.crm.web.controller;


import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.crm.dto.DepotDetailDto;
import net.myspring.future.modules.crm.service.DepotDetailService;
import net.myspring.future.modules.crm.web.query.DepotDetailQuery;
import net.myspring.future.modules.layout.web.query.ShopAttributeQuery;
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
    public DepotDetailQuery getQuery(DepotDetailQuery depotDetailQuery){
        return depotDetailQuery;
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public String export(DepotDetailQuery depotDetailQuery) {
        return depotDetailService.export(depotDetailQuery);
    }

    @RequestMapping(value="syn")
    public RestResponse syn(){
        depotDetailService.syn();
        return new RestResponse("同步成功", ResponseCodeEnum.updated.name());
    }

}
