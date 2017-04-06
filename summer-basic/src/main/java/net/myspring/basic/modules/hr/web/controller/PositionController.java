package net.myspring.basic.modules.hr.web.controller;

import com.google.common.collect.Maps;
import net.myspring.basic.common.enums.DataScopeEnum;
import net.myspring.basic.common.utils.Const;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.domain.Position;
import net.myspring.basic.modules.hr.dto.PositionDto;
import net.myspring.basic.modules.hr.service.JobService;
import net.myspring.basic.modules.hr.web.form.PositionForm;
import net.myspring.basic.modules.sys.service.PermissionService;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import net.myspring.basic.modules.hr.service.PositionService;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "hr/position")
public class PositionController {

    @Autowired
    private PositionService positionService;
    @Autowired
    private JobService jobService;
    @Autowired
    private PermissionService permissionService;

    @RequestMapping(method = RequestMethod.GET)
    public String findPage(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        Page<PositionDto> page = positionService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value="getListProperty")
    public String getListProperty(){
        Map<String,Object> map = Maps.newHashMap();
        map.put("jobList",jobService.findAll());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "getFormProperty")
    public String getFormProperty(Position position){
        Map<String,Object> map = Maps.newHashMap();
        List<String> permissionIdList = position.getId()==null?new ArrayList<>():positionService.findPermissionByPosition(position.getId());
        map.put("permissionTree",permissionService.findPermissionTree(permissionIdList));
        map.put("jobList",jobService.findAll());
        map.put("dataScopeMap", DataScopeEnum.getMap());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "findOne")
    public String findOne(String id) {
        PositionDto positionDto=positionService.findDto(id);
        return ObjectMapperUtils.writeValueAsString(positionDto);
    }

    @RequestMapping(value = "save")
    public String save(PositionForm positionForm, String permissionIdStr) {
        positionForm.setPermissionIdList(StringUtils.getSplitList(permissionIdStr, Const.CHAR_COMMA));
        positionService.save(positionForm);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("保存成功",ResponseCodeEnum.saved.name()));
    }

    @RequestMapping(value = "delete")
    public String delete(String id) {
        positionService.delete(id);
        RestResponse restResponse =new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }


    @RequestMapping(value = "search")
    public String search(String name) {
        List<PositionDto> positionDtoList  = positionService.findByNameLike(name);
        return ObjectMapperUtils.writeValueAsString(positionDtoList);
    }

}
