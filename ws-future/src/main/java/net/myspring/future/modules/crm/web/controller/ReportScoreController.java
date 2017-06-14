package net.myspring.future.modules.crm.web.controller;


import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.crm.domain.ReportScore;
import net.myspring.future.modules.crm.dto.ImeAllotDto;
import net.myspring.future.modules.crm.dto.ReportScoreDto;
import net.myspring.future.modules.crm.service.ReportScoreService;
import net.myspring.future.modules.crm.web.form.ReportScoreForm;
import net.myspring.future.modules.crm.web.query.ImeAllotQuery;
import net.myspring.future.modules.crm.web.query.ReportScoreQuery;
import net.myspring.util.text.StringUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/reportScore")
public class ReportScoreController {


    @Autowired
    private ReportScoreService  reportScoreService;


    @RequestMapping(method = RequestMethod.GET)
    public Page<ReportScoreDto> list(Pageable pageable, ReportScoreQuery reportScoreQuery) {
        return reportScoreService.findPage(pageable, reportScoreQuery);
    }


    @RequestMapping(value = "getQuery", method = RequestMethod.GET)
    public ReportScoreQuery getQuery(ReportScoreQuery reportScoreQuery) {
        return reportScoreQuery;
    }

    @RequestMapping(value = "getForm")
    public ReportScoreForm getForm(ReportScoreForm reportScoreForm) {
        return reportScoreForm;
    }

    @RequestMapping(value = "findOne")
    public ReportScoreDto findOne(ReportScoreDto reportScoreDto) {
        reportScoreDto=reportScoreService.findOne(reportScoreDto);
        reportScoreDto.setProductTypeNameStr(reportScoreService.getProductTypeNames());
        return reportScoreDto;
    }

    @RequestMapping(value = "save")
    public RestResponse save(ReportScoreForm reportScoreForm) {
        reportScoreService.save(reportScoreForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(ReportScoreForm reportScoreForm) {
        reportScoreService.delete(reportScoreForm);
        return new RestResponse("删除成功", ResponseCodeEnum.removed.name());
    }
}
