package net.myspring.basic.modules.hr.web.controller;

import net.myspring.basic.common.enums.RecruitTypeEnum;
import net.myspring.basic.modules.hr.dto.RecruitDto;
import net.myspring.basic.modules.hr.service.AccountService;
import net.myspring.basic.modules.hr.service.RecruitEnumService;
import net.myspring.basic.modules.hr.service.RecruitService;
import net.myspring.basic.modules.hr.web.form.RecruitForm;
import net.myspring.basic.modules.hr.web.query.EmployeeQuery;
import net.myspring.basic.modules.hr.web.query.RecruitQuery;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.excel.ExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@RestController
@RequestMapping(value = "hr/recruit")
public class RecruitController {

    @Autowired
    private RecruitService recruitService;
    @Autowired
    private RecruitEnumService recruitEnumService;
    @Autowired
    private AccountService accountService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<RecruitDto> list(Pageable pageable, RecruitQuery recruitQuery){
        Page<RecruitDto> page=recruitService.findPage(pageable,recruitQuery);
        return page;
    }

    @RequestMapping(value = "getQuery")
    public RecruitQuery findQuery(RecruitQuery recruitQuery){
        recruitQuery.getExtra().put("firstAppointByList", accountService.findByIds(recruitEnumService.findValueByCategory((RecruitTypeEnum.初试人.name()))));
        recruitQuery.getExtra().put("secondAppointByList",accountService.findByIds(recruitEnumService.findValueByCategory((RecruitTypeEnum.复试人.name()))));
        return recruitQuery;
    }

    @RequestMapping(value = "exportReport",method = RequestMethod.GET)
    public ModelAndView exportReport(RecruitQuery recruitQuery) throws IOException {

        return new ModelAndView(new ExcelView(),"simpleExcelBook",recruitService.exportReport(recruitQuery));
    }



    @RequestMapping(value = "getForm")
    public RecruitForm findForm(RecruitForm recruitForm){
        recruitForm.getExtra().put("applyFromList",recruitEnumService.findValueByCategory(RecruitTypeEnum.来源.name()) );
        recruitForm.getExtra().put("applyPositionList",recruitEnumService.findValueByCategory(RecruitTypeEnum.岗位.name()) );
        recruitForm.getExtra().put("firstAppointByList", accountService.findByIds(recruitEnumService.findValueByCategory((RecruitTypeEnum.初试人.name()))));
        recruitForm.getExtra().put("secondAppointByList",accountService.findByIds(recruitEnumService.findValueByCategory((RecruitTypeEnum.复试人.name()))));
        recruitForm.getExtra().put("workCategoryList",recruitEnumService.findValueByCategory(RecruitTypeEnum.品类.name()) );
        recruitForm.getExtra().put("marriageStatusList",recruitEnumService.findValueByCategory(RecruitTypeEnum.婚育状况.name()) );
        recruitForm.getExtra().put("educationsList",recruitEnumService.findValueByCategory(RecruitTypeEnum.最高学历.name()) );
        return recruitForm;
    }

    @RequestMapping(value = "findOne")
    public RecruitDto findOne(RecruitDto recruitDto){
        recruitDto=recruitService.findOne(recruitDto);
        return recruitDto;
    }

    @RequestMapping(value = "save")
    public RestResponse save(RecruitForm recruitForm){
        recruitService.save(recruitForm);
        return new RestResponse("保存成功",null);
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id){
        recruitService.delete(id);
        return new RestResponse("删除成功", ResponseCodeEnum.removed.name());
    }
}
