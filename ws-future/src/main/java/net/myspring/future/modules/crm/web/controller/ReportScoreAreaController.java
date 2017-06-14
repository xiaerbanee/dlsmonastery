package net.myspring.future.modules.crm.web.controller;


import net.myspring.future.common.enums.OfficeRuleEnum;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.crm.dto.ReportScoreAreaDto;
import net.myspring.future.modules.crm.dto.ReportScoreOfficeDto;
import net.myspring.future.modules.crm.service.ReportScoreAreaService;
import net.myspring.future.modules.crm.service.ReportScoreOfficeService;
import net.myspring.future.modules.crm.web.query.ReportScoreAreaQuery;
import net.myspring.future.modules.crm.web.query.ReportScoreOfficeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/reportScoreArea")
public class ReportScoreAreaController {

    @Autowired
    private ReportScoreAreaService reportScoreOfficeService;
    @Autowired
    private OfficeClient officeClient;

    @RequestMapping
    public Page list(Pageable pageable,ReportScoreAreaQuery reportScoreAreaQuery ){
        Page<ReportScoreAreaDto> page=reportScoreOfficeService.findPage(pageable,reportScoreAreaQuery);
        return page;
    }


    @RequestMapping(value="getQuery",method = RequestMethod.GET)
    public ReportScoreAreaQuery getQuery(ReportScoreAreaQuery reportScoreAreaQuery){
        reportScoreAreaQuery.getExtra().put("areaList", officeClient.findByOfficeRuleName(OfficeRuleEnum.办事处.name()));
        return reportScoreAreaQuery;
    }
}
