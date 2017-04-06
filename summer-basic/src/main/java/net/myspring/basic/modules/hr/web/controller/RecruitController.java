package net.myspring.basic.modules.hr.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.enums.DictEnumCategoryEnum;
import net.myspring.basic.common.utils.Const;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.domain.Recruit;
import net.myspring.basic.modules.hr.dto.RecruitDto;
import net.myspring.basic.modules.hr.service.PositionService;
import net.myspring.basic.modules.hr.service.RecruitService;
import net.myspring.basic.modules.hr.web.form.RecruitForm;
import net.myspring.basic.modules.sys.domain.DictEnum;
import net.myspring.basic.modules.sys.service.DictEnumService;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public String findPage(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        Page<RecruitDto> page = recruitService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value="getListProperty")
    public String getListProperty(){
        Map<String,Object> map = Maps.newHashMap();
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "getFormProperty")
    public String getFormProperty(){
        Map<String,Object> map = Maps.newHashMap();
        map.put("positions",positionService.findAll());
        map.put("educationsList", dictEnumService.findByCategory(DictEnumCategoryEnum.EDUCATION.getValue()));
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "findOne")
    public String findOne(String id){
        RecruitDto recruitDto=recruitService.findDto(id);
        return ObjectMapperUtils.writeValueAsString(recruitDto);
    }

    @RequestMapping(value = "save")
    public String save(RecruitForm recruitForm) {
        recruitService.save(recruitForm);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("保存成功", ResponseCodeEnum.saved.name()));
    }

    @RequestMapping(value = "delete")
    public String delete(RecruitForm recruitForm) {
        recruitService.delete(recruitForm);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("删除成功",ResponseCodeEnum.removed.name()));
    }

    @RequestMapping(value = "checkMobilePhone")
    public String checkMobilePhone(String mobilePhone) {
        Recruit recruit = recruitService.findByMobilePhone(mobilePhone);
        if(recruit!=null){
            return ObjectMapperUtils.writeValueAsString(new RestResponse("手机号码已存在", null));
        }
        return ObjectMapperUtils.writeValueAsString(recruit);
    }

    @RequestMapping(value = "findNameByIds")
    public String findNameByIds(@RequestParam(value = "ids[]") String[] ids) {
        List<String> nameList = recruitService.findNameByIds(Arrays.asList(ids));
        return ObjectMapperUtils.writeValueAsString(StringUtils.join(nameList, Const.CHAR_COMMA));
    }

    @RequestMapping(value = "update")
    public String update(Recruit recruit) {
        recruitService.update(recruit);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("删除成功",ResponseCodeEnum.removed.name()));
    }
}
