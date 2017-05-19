package net.myspring.basic.modules.hr.web.controller;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.hr.dto.PositionDto;
import net.myspring.basic.modules.hr.service.PositionService;
import net.myspring.basic.modules.hr.web.form.PositionForm;
import net.myspring.basic.modules.hr.web.query.PositionQuery;
import net.myspring.basic.modules.sys.service.BackendModuleService;
import net.myspring.basic.modules.sys.service.PermissionService;
import net.myspring.basic.modules.sys.service.RoleService;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "hr/position")
public class PositionController {

    @Autowired
    private PositionService positionService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private BackendModuleService backendModuleService;
    @Autowired
    private RoleService roleService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<PositionDto> findPage(Pageable pageable, PositionQuery positionQuery) {
        Page<PositionDto> page = positionService.findPage(pageable, positionQuery);
        return page;
    }

    @RequestMapping(value = "getFormProperty")
    public PositionForm getFormProperty(PositionForm positionForm) {
        positionForm= positionService.getFormProperty(positionForm);
        positionForm.setRoleList(roleService.findAll());
        return positionForm;
    }

    @RequestMapping(value = "save")
    public RestResponse save(PositionForm positionForm, String permissionIdStr) {
        positionService.save(positionForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        positionService.delete(id);
        RestResponse restResponse = new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }


    @RequestMapping(value = "search")
    public List<PositionDto> search(String name) {
        List<PositionDto> positionDtoList =Lists.newArrayList();
        if(StringUtils.isNotBlank(name)){
            positionDtoList =positionService.findByNameLike(name);
        }
        return positionDtoList;
    }

    @RequestMapping(value = "findById")
    public PositionDto findById(String id) {
        PositionDto positionDto =new PositionDto();
        if(StringUtils.isNotBlank(id)){
            positionDto =positionService.findOne(id);
        }
        return positionDto;
    }
}
