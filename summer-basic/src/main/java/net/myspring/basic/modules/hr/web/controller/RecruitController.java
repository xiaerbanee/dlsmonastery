package net.myspring.basic.modules.hr.web.controller;

import com.google.common.collect.Maps;
import net.myspring.basic.common.enums.DictEnumCategoryEnum;
import net.myspring.basic.common.utils.Const;
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

    @RequestMapping(value="getListProperty")
    public Map<String,Object> getListProperty(){
        Map<String,Object> map = Maps.newHashMap();
        return map;
    }

    @RequestMapping(value = "getFormProperty")
    public Map<String,Object> getFormProperty(){
        Map<String,Object> map = Maps.newHashMap();
        map.put("positions",positionService.findAll());
        map.put("educationsList", dictEnumService.findByCategory(DictEnumCategoryEnum.EDUCATION.getValue()));
        return map;
    }

    @RequestMapping(value = "findOne")
    public RecruitDto findOne(String id){
        RecruitDto recruitDto=recruitService.findDto(id);
        return recruitDto;
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
