package net.myspring.future.modules.crm.web.controller;


import net.myspring.future.common.enums.OfficeRuleEnum;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.crm.domain.ReportScoreOffice;
import net.myspring.future.modules.crm.dto.ReportScoreOfficeDto;
import net.myspring.future.modules.crm.service.ReportScoreOfficeService;
import net.myspring.future.modules.crm.web.query.ReportScoreOfficeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/reportScoreOffice")
public class ReportScoreOfficeController {

    @Autowired
    ReportScoreOfficeService reportScoreOfficeService;
    @Autowired
    private OfficeClient officeClient;

    @RequestMapping(method = RequestMethod.GET)
    public Page<ReportScoreOfficeDto> list(Pageable pageable, ReportScoreOfficeQuery reportScoreOfficeQuery){
        Page<ReportScoreOfficeDto> page=reportScoreOfficeService.findPage(pageable,reportScoreOfficeQuery);
        return page;
    }

    @RequestMapping(value="getQuery",method = RequestMethod.GET)
    public ReportScoreOfficeQuery getQuery(ReportScoreOfficeQuery reportScoreOfficeQuery){
        reportScoreOfficeQuery.getExtra().put("areaList", officeClient.findByOfficeRuleName(OfficeRuleEnum.办事处.name()));
        return reportScoreOfficeQuery;
    }
}
