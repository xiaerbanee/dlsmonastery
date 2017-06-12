package net.myspring.basic.modules.sys.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.myspring.basic.modules.sys.domain.Permission;
import net.myspring.basic.modules.sys.dto.RoleDto;
import net.myspring.basic.modules.sys.service.BackendModuleService;
import net.myspring.basic.modules.sys.service.PermissionService;
import net.myspring.basic.modules.sys.service.RoleService;
import net.myspring.basic.modules.sys.web.form.RoleForm;
import net.myspring.basic.modules.sys.web.query.RoleQuery;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.common.tree.TreeNode;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by wangzm on 2017/5/2.
 */
@RestController
@RequestMapping(value = "sys/role")
public class RoleController {
    
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private BackendModuleService backendModuleService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<RoleDto> list(Pageable pageable, RoleQuery RoleQuery){
        Page<RoleDto> page = roleService.findPage(pageable,RoleQuery);
        return page;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        roleService.logicDelete(id);
        RestResponse restResponse =new RestResponse("删除成功", ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "save")
    public RestResponse save(RoleForm roleForm) {
        roleService.save(roleForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "saveAuthorityList")
    public RestResponse saveAuthorityList(RoleForm roleForm) {
        roleService.saveRoleAndPermission(roleForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findOne")
    public RoleDto findOne(RoleDto roleDto){
        roleDto= roleService.findOne(roleDto);
        return roleDto;
    }

    @RequestMapping(value = "getForm")
    public RoleForm getForm(RoleForm roleForm){
        List<String> backendModuleIdList = roleForm.isCreate()? Lists.newArrayList() : backendModuleService.findBackendModuleIdByRoleId(roleForm.getId());
        roleForm.getExtra().put("treeNode",permissionService.findBackendTree());
        roleForm.setModuleIdList(Lists.newArrayList(Sets.newHashSet(backendModuleIdList)));
        return roleForm;
    }

    @RequestMapping(value = "getTreeNode")
    public RoleForm getTreeNode(RoleForm roleForm) {
        if(!roleForm.isCreate()){
            TreeNode treeNode=permissionService.findRoleTree(roleForm.getId());
            List<Permission> permissionList=permissionService.findByRoleId(roleForm.getId());
            List<String> permissionIds = CollectionUtil.extractToList(permissionList, "id");
            roleForm.setPermissionIdList(permissionIds);
            roleForm.getExtra().put("treeNode",treeNode);
        }
        return roleForm;
    }

    @RequestMapping(value = "search")
    public List<RoleDto> search(String name) {
        List<RoleDto> roleDtoList = Lists.newArrayList();
        if (StringUtils.isNotBlank(name)) {
            roleDtoList = roleService.findByNameLike(name);
        }
        return roleDtoList;
    }

    @RequestMapping(value = "getQuery")
    public RoleQuery getQuery(RoleQuery roleQuery){
        return roleQuery;
    }
}
