package net.myspring.basic.modules.sys.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.sys.domain.DictEnum;
import net.myspring.basic.modules.sys.dto.DictEnumDto;
import net.myspring.basic.modules.sys.service.DictEnumService;
import net.myspring.basic.modules.sys.web.form.DictEnumForm;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.util.json.ObjectMapperUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "sys/dictEnum")
public class DictEnumController {

    @Autowired
    private DictEnumService dictEnumService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        Page<DictEnumDto> page = dictEnumService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "delete")
    public String delete(String id) {
        dictEnumService.logicDeleteOne(id);
        RestResponse restResponse =new RestResponse("删除成功");
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "save")
    public String save(DictEnumForm dictEnumForm) {
        dictEnumService.save(dictEnumForm);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("保存成功"));
    }

    @RequestMapping(value = "findOne")
    public String findOne(String id){
        DictEnumDto dictEnumDto=dictEnumService.findDto(id);
        return ObjectMapperUtils.writeValueAsString(dictEnumDto);
    }

    @RequestMapping(value="getFormProperty")
    public String getFormProperty(){
        Map<String,Object> map= Maps.newHashMap();
        map.put("category", dictEnumService.findDistinctCategory());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value="getListProperty")
    public String getListProperty(){
        Map<String,Object> map= Maps.newHashMap();
        map.put("category", dictEnumService.findDistinctCategory());
        return ObjectMapperUtils.writeValueAsString(map);
    }
}
