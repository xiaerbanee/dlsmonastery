package net.myspring.basic.modules.sys.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.modules.sys.domain.Permission;
import net.myspring.basic.modules.sys.dto.PermissionDto;
import net.myspring.basic.modules.sys.service.MenuService;
import net.myspring.basic.modules.sys.service.PermissionService;
import net.myspring.basic.modules.sys.service.RoleService;
import net.myspring.basic.modules.sys.web.form.PermissionForm;
import net.myspring.basic.modules.sys.web.query.PermissionQuery;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.json.ObjectMapperUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "sys/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'sys:permission:view')")
    public Page<PermissionDto> list(Pageable pageable, PermissionQuery permissionQuery){
        Page<PermissionDto> page = permissionService.findPage(pageable,permissionQuery);
        return page;
    }

    @RequestMapping(value = "delete")
    @PreAuthorize("hasPermission(null,'sys:permission:delete')")
    public RestResponse delete(PermissionForm permissionForm) {
        permissionService.logicDelete(permissionForm);
        RestResponse restResponse =new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "save")
    @PreAuthorize("hasPermission(null,'sys:permission:edit')")
    public RestResponse save(PermissionForm permissionForm) {
        permissionService.save(permissionForm);
        return new RestResponse("保存成功",ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findOne")
    public PermissionDto findOne(PermissionDto permissionDto) {
        permissionDto=permissionService.findOne(permissionDto);
        return permissionDto;
    }

    @RequestMapping(value = "getForm")
    public PermissionForm getForm(PermissionForm permissionForm) {
        permissionForm.getExtra().put("menuList",menuService.findAll());
        permissionForm.getExtra().put("roleList",roleService.findAll());
        return permissionForm;
    }

    @RequestMapping(value = "getQuery")
    public PermissionQuery getForm(PermissionQuery permissionQuery) {
        return permissionQuery;
    }

    @RequestMapping(value = "search")
    public List<PermissionDto> search(String query){
        List<PermissionDto> permissions=permissionService.findByPermissionLike(query);
        return permissions;
    }
}
