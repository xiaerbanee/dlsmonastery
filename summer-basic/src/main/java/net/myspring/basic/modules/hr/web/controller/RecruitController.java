package net.myspring.basic.modules.hr.web.controller;

import com.google.common.collect.Maps;
import net.myspring.basic.common.enums.DictEnumCategoryEnum;
import net.myspring.basic.common.utils.Const;
import net.myspring.basic.common.utils.Global;
import net.myspring.basic.modules.hr.domain.Recruit;
import net.myspring.basic.modules.hr.dto.RecruitDto;
import net.myspring.basic.modules.hr.service.PositionService;
import net.myspring.basic.modules.hr.service.RecruitService;
import net.myspring.basic.modules.hr.web.form.RecruitForm;
import net.myspring.basic.modules.hr.web.query.RecruitQuery;
import net.myspring.basic.modules.sys.service.DictEnumService;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "hr/recruit")
public class RecruitController {

    @Autowired
    private RecruitService recruitService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private DictEnumService dictEnumService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<RecruitDto> findPage(Pageable pageable, RecruitQuery recruitQuery){
        Page<RecruitDto> page = recruitService.findPage(pageable,recruitQuery);
        return page;
    }


    @RequestMapping(value = "findOne")
    public RecruitForm findOne(RecruitForm recruitForm){
        recruitForm=recruitService.findForm(recruitForm);
        recruitForm.setPositionList(positionService.findAll());
        recruitForm.setEducationList(Global.getDictEnumValueList(DictEnumCategoryEnum.EDUCATION.getValue()));
        return recruitForm;
    }

    @RequestMapping(value = "save")
    public RestResponse save(RecruitForm recruitForm) {
        recruitService.save(recruitForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        recruitService.logicDeleteOne(id);
        return new RestResponse("删除成功",ResponseCodeEnum.removed.name());
    }

    @RequestMapping(value = "checkMobilePhone")
    public Recruit checkMobilePhone(String mobilePhone) {
        Recruit recruit = recruitService.findByMobilePhone(mobilePhone);
        return recruit;
    }

    @RequestMapping(value = "findNameByIds")
    public String findNameByIds(@RequestParam(value = "ids[]") String[] ids) {
        List<String> nameList = recruitService.findNameByIds(Arrays.asList(ids));
        return ObjectMapperUtils.writeValueAsString(StringUtils.join(nameList, Const.CHAR_COMMA));
    }

    @RequestMapping(value = "update")
    public RestResponse update(Recruit recruit) {
        recruitService.update(recruit);
        return new RestResponse("删除成功",ResponseCodeEnum.removed.name());
    }
}
