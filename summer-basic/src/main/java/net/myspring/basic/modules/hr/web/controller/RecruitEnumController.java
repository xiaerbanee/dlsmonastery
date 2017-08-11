package net.myspring.basic.modules.hr.web.controller;


import net.myspring.basic.common.enums.RecruitTypeEnum;
import net.myspring.basic.modules.hr.dto.RecruitDto;
import net.myspring.basic.modules.hr.dto.RecruitEnumDto;
import net.myspring.basic.modules.hr.service.RecruitEnumService;
import net.myspring.basic.modules.hr.web.form.RecruitEnumForm;
import net.myspring.basic.modules.hr.web.form.RecruitForm;
import net.myspring.basic.modules.hr.web.query.RecruitEnumQuery;
import net.myspring.basic.modules.hr.web.query.RecruitQuery;
import net.myspring.common.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="hr/recruitEnum")
public class RecruitEnumController {

    @Autowired
    private RecruitEnumService recruitEnumService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<RecruitEnumDto> list(Pageable pageable, RecruitEnumQuery recruitEnumQuery){
        Page<RecruitEnumDto> page=recruitEnumService.findPage(pageable,recruitEnumQuery);
        return page;
    }

    @RequestMapping(value = "getQuery")
    public RecruitEnumQuery findQuery(RecruitEnumQuery recruitEnumQuery){
        recruitEnumQuery.getExtra().put("categoryList", RecruitTypeEnum.values());
        return recruitEnumQuery;
    }

    @RequestMapping(value="getForm")
    public RecruitEnumForm getForm(RecruitEnumForm recruitEnumForm){
        recruitEnumForm.getExtra().put("categoryList", RecruitTypeEnum.values());
        return recruitEnumForm;
    }

    @RequestMapping(value = "findOne")
    public RecruitEnumDto findOne(RecruitEnumDto recruitEnumDto){
        recruitEnumDto=recruitEnumService.findOne(recruitEnumDto);
        return recruitEnumDto;
    }

    @RequestMapping(value = "save")
    public RestResponse save(RecruitEnumForm RecruitEnumForm){
        recruitEnumService.save(RecruitEnumForm);
        return new RestResponse("保存成功",null);
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id){
        recruitEnumService.delete(id);
        return new RestResponse("删除成功",null);
    }
}
