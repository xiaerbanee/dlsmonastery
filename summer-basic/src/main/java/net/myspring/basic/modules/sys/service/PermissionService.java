package net.myspring.basic.modules.sys.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.hr.mapper.AccountPermissionMapper;
import net.myspring.basic.modules.sys.domain.*;
import net.myspring.basic.modules.sys.dto.*;
import net.myspring.basic.modules.sys.mapper.*;
import net.myspring.basic.modules.sys.web.form.PermissionForm;
import net.myspring.basic.modules.sys.web.query.PermissionQuery;
import net.myspring.common.tree.TreeNode;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class PermissionService {
    
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private BackendMapper backendMapper;
    @Autowired
    private BackendModuleMapper backendModuleMapper;
    @Autowired
    private AccountPermissionMapper accountPermissionMapper;

    public List<PermissionDto> findByMenuId(String menuId) {
        List<Permission> permissionList = permissionMapper.findByMenuId(menuId);
        List<PermissionDto> permissionDtoList = BeanUtil.map(permissionList, PermissionDto.class);
        return permissionDtoList;
    }

    public List<Permission> findByRoleId(String roleId) {
        return permissionMapper.findByRoleId(roleId);
    }

    public Permission findOne(String id) {
        Permission permission = permissionMapper.findOne(id);
        return permission;
    }

    public PermissionForm findForm(PermissionForm permissionForm) {
        if (!permissionForm.isCreate()) {
            Permission permission = permissionMapper.findOne(permissionForm.getId());
            permissionForm = BeanUtil.map(permission, PermissionForm.class);
            cacheUtils.initCacheInput(permissionForm);
        }
        return permissionForm;
    }

    public List<Permission> findAll() {
        return permissionMapper.findAll();
    }

    public List<Permission> findByPermissionLike(String permissionStr) {
        return permissionMapper.findByPermissionLike(permissionStr);
    }

    public Page<PermissionDto> findPage(Pageable pageable, PermissionQuery permissionQuery) {
        Page<PermissionDto> permissionDtoPage = permissionMapper.findPage(pageable, permissionQuery);
        cacheUtils.initCacheInput(permissionDtoPage.getContent());
        return permissionDtoPage;
    }

    public Permission save(PermissionForm permissionForm) {
        Permission permission;
        if (permissionForm.isCreate()) {
            permission = BeanUtil.map(permissionForm, Permission.class);
            permissionMapper.save(permission);
        } else {
            permission = permissionMapper.findOne(permissionForm.getId());
            ReflectionUtil.copyProperties(permissionForm,permission);
            permissionMapper.update(permission);
        }
        List<RolePermission> rolePermissionList=rolePermissionMapper.findAllByPermissionId(permission.getId());
        if (CollectionUtil.isNotEmpty(permissionForm.getRoleIdList())) {
            List<String> roleIdList = CollectionUtil.extractToList(rolePermissionList, "roleId");
            List<String> removeIdList = CollectionUtil.subtract(roleIdList, permissionForm.getRoleIdList());
            List<String> addIdList = CollectionUtil.subtract(permissionForm.getRoleIdList(), roleIdList);
            List<RolePermission> rolePermissions=Lists.newArrayList();
            for(String roleId:addIdList){
                rolePermissions.add(new RolePermission(permission.getId(),roleId));
            }
            rolePermissionMapper.setEnabledByPermissionId(true, permission.getId());
            if (CollectionUtil.isNotEmpty(removeIdList)) {
                rolePermissionMapper.setEnabledByRoleIdList(false,removeIdList);
            }
            if (CollectionUtil.isNotEmpty(addIdList)) {
                rolePermissionMapper.batchSave(rolePermissions);
            }
        } else if (CollectionUtil.isNotEmpty(rolePermissionList)) {
            rolePermissionMapper.setEnabledByPermissionId(false, permission.getId());
        }
        return permission;
    }

    public TreeNode findBackendTree(List<String> backendModuleIdList) {
        TreeNode treeNode = new TreeNode("0", "项目列表");
        List<Backend> backendList = backendMapper.findAllEnabled();
        List<BackendModule> backendModuleList = backendModuleMapper.findAllEnabled();
        Map<String, List<BackendModule>> backendModuleMap = CollectionUtil.extractToMapList(backendModuleList, "backendId");
        for (Backend backend : backendList) {
            TreeNode backendTree = new TreeNode("p" + backend.getId(), backend.getName());
            for (BackendModule backendModule : backendModuleMap.get(backend.getId())) {
                TreeNode backendModuleTree = new TreeNode(backendModule.getId(), backendModule.getName());
                backendTree.getChildren().add(backendModuleTree);
            }
            treeNode.getChildren().add(backendTree);
        }
        treeNode.setChecked(Lists.newArrayList(Sets.newHashSet(backendModuleIdList)));
        return treeNode;
    }

    public TreeNode findPermissionTree(String roleId,List<String> permissionIdList) {
        Set<String> permissionIdSet = Sets.newHashSet(permissionIdList);
        TreeNode treeNode = new TreeNode("0", "权限列表");
        List<BackendMenuDto> backendMenuDtoList = backendMapper.findByRoleId(roleId);
        List<Permission> permissionList = permissionMapper.findAllEnabled();
        Map<String,List<Permission>> permissionMap=CollectionUtil.extractToMapList(permissionList,"menuId");
        for(BackendMenuDto backend:backendMenuDtoList){
            TreeNode backendTree = new TreeNode("b" + backend.getId(), backend.getName());
            for (BackendModuleMenuDto backendModule : backend.getBackendModuleList()) {
                TreeNode backendModuleTree = new TreeNode("p" + backendModule.getId(), backendModule.getName());
                for (MenuCategoryMenuDto menuCategory : backendModule.getMenuCategoryList()) {
                    TreeNode menuCategoryTree = new TreeNode("c" + menuCategory.getId(), menuCategory.getName());
                    List<FrontendMenuDto> menuList = menuCategory.getMenuList();
                    if(menuList!=null){
                        for (FrontendMenuDto menu : menuList) {
                            TreeNode menuTree = new TreeNode("m" + menu.getId(), menu.getName());
                            List<Permission> permissions = permissionMap.get(menu.getId());
                            if(CollectionUtil.isNotEmpty(permissions)){
                                for (Permission permission : permissions) {
                                    TreeNode permissionTree = new TreeNode(permission.getId(), permission.getName());
                                    menuTree.getChildren().add(permissionTree);
                                }
                            }
                            menuCategoryTree.getChildren().add(menuTree);
                        }
                        backendModuleTree.getChildren().add(menuCategoryTree);
                    }
                }
                backendTree.getChildren().add(backendModuleTree);
            }
            treeNode.getChildren().add(backendTree);
        }
        treeNode.setChecked(Lists.newArrayList(permissionIdSet));
        return treeNode;
    }

    public TreeNode getAccountPermissionCheckData(String accountId){
        TreeNode treeNode = new TreeNode("0", "权限列表");
        List<String> accountPermissionList=accountPermissionMapper.findPermissionIdByAccount(accountId);
        treeNode.setChecked(accountPermissionList);
        return treeNode;
    }

    public void logicDeleteOne(String id) {
        permissionMapper.logicDeleteOne(id);
    }

}
