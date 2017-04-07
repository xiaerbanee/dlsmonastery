package net.myspring.basic.modules.hr.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.enums.DictMapCategoryEnum;
import net.myspring.basic.common.enums.JointTypeEnum;
import net.myspring.basic.modules.hr.domain.Office;
import net.myspring.basic.modules.hr.dto.OfficeDto;
import net.myspring.basic.modules.hr.service.OfficeService;
import net.myspring.basic.modules.hr.web.form.OfficeForm;
import net.myspring.basic.modules.hr.web.query.OfficeQuery;
import net.myspring.basic.modules.sys.service.DictEnumService;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "hr/office")
public class OfficeController {

    @Autowired
    private OfficeService officeService;
    @Autowired
    private DictEnumService  dictEnumService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Pageable pageable, OfficeQuery officeQuery) {
        Page<OfficeDto> page = officeService.findPage(pageable,officeQuery);
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "search")
    public String search(String name,String officeType,String parentOfficeId) {
        List<OfficeDto> officeDtos = Lists.newArrayList();
        if(StringUtils.isNotBlank(name)) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("name", name);
            if (StringUtils.isNotBlank(parentOfficeId)) {
                map.put("parentOfficeId",parentOfficeId);
            }
            officeDtos = officeService.findByFilter(map);
        }
         return ObjectMapperUtils.writeValueAsString(officeDtos);
    }


    @RequestMapping(value = "save")
    public RestResponse save(OfficeForm officeForm, BindingResult bindingResult) {
        officeService.save(officeForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }


    @RequestMapping(value = "findOne")
    public String findOne(String id){
        OfficeDto officeDto=officeService.findDto(id);
        return ObjectMapperUtils.writeValueAsString(officeDto);
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(Office office,BindingResult bindingResult) {
        officeService.logicDeleteOne(office);
        return new RestResponse("删除成功",ResponseCodeEnum.removed.name());
    }

    @RequestMapping(value="getFormProperty")
    public String getFormProperty(){
        Map<String,Object> map= Maps.newHashMap();
        map.put("officeTypes", dictEnumService.findByCategory(DictMapCategoryEnum.机构分类.name()));
        map.put("jointTypes", JointTypeEnum.getList());
        return ObjectMapperUtils.writeValueAsString(map);
    }
}
