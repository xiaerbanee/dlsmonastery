package net.myspring.basic.modules.hr.web.controller;

import net.myspring.basic.modules.hr.dto.RecruitDto;
import net.myspring.basic.modules.hr.service.RecruitService;
import net.myspring.basic.modules.hr.web.form.RecruitForm;
import net.myspring.basic.modules.hr.web.query.RecruitQuery;
import net.myspring.common.enums.DictEnumCategoryEnum;
import net.myspring.common.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "hr/recruit")
public class RecruitController {

    @Autowired
    private RecruitService recruitService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<RecruitDto> list(Pageable pageable, RecruitQuery recruitQuery){
        Page<RecruitDto> page=recruitService.findPage(pageable,recruitQuery);
        return page;
    }

    @RequestMapping(value = "getQuery")
    public RecruitQuery findQuery(RecruitQuery recruitQuery){
        return recruitQuery;
    }

    @RequestMapping(value = "getForm")
    public RecruitForm findForm(RecruitForm recruitForm){
        recruitForm.getExtra().put("educationsList", DictEnumCategoryEnum.EDUCATION.getValue());
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
        return new RestResponse("删除成功",null);
    }
}
