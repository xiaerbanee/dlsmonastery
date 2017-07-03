package net.myspring.basic.modules.sys.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.hr.repository.AccountPermissionRepository;
import net.myspring.basic.modules.sys.domain.Backend;
import net.myspring.basic.modules.sys.domain.BackendModule;
import net.myspring.basic.modules.sys.domain.Permission;
import net.myspring.basic.modules.sys.domain.RolePermission;
import net.myspring.basic.modules.sys.dto.*;
import net.myspring.basic.modules.sys.repository.BackendModuleRepository;
import net.myspring.basic.modules.sys.repository.BackendRepository;
import net.myspring.basic.modules.sys.repository.PermissionRepository;
import net.myspring.basic.modules.sys.repository.RolePermissionRepository;
import net.myspring.basic.modules.sys.web.form.PermissionForm;
import net.myspring.basic.modules.sys.web.query.PermissionQuery;
import net.myspring.common.tree.TreeNode;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.time.LocalDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private RolePermissionRepository rolePermissionRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private BackendRepository backendRepository;
    @Autowired
    private BackendModuleRepository backendModuleRepository;
    @Autowired
    private AccountPermissionRepository accountPermissionRepository;

    public List<PermissionDto> findByMenuId(String menuId) {
        List<Permission> permissionList = permissionRepository.findByMenuId(menuId);
        List<PermissionDto> permissionDtoList = BeanUtil.map(permissionList, PermissionDto.class);
        return permissionDtoList;
    }

    public List<Permission> findByRoleId(String roleId) {
        return permissionRepository.findByRoleId(roleId);
    }

    public PermissionDto findOne(PermissionDto permissionDto) {
        if (!permissionDto.isCreate()) {
            Permission permission = permissionRepository.findOne(permissionDto.getId());
            permissionDto = BeanUtil.map(permission, PermissionDto.class);
            permissionDto.setRoleIdList(CollectionUtil.extractToList(rolePermissionRepository.findByPermissionId(permission.getId()), "roleId"));
            cacheUtils.initCacheInput(permissionDto);
        }
        return permissionDto;
    }

    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    public List<PermissionDto> findByPermissionLike(String permissionStr) {
        List<Permission> permissionList = permissionRepository.findByPermissionLike(permissionStr);
        List<PermissionDto> permissionDtoList = BeanUtil.map(permissionList, PermissionDto.class);
        cacheUtils.initCacheInput(permissionDtoList);
        return permissionDtoList;
    }

    public PermissionDto findOne(String id) {
        Permission permission = permissionRepository.getOne(id);
        PermissionDto permissionDto = BeanUtil.map(permission, PermissionDto.class);
        cacheUtils.initCacheInput(permissionDto);
        return permissionDto;
    }

    public Page<PermissionDto> findPage(Pageable pageable, PermissionQuery permissionQuery) {
        Page<PermissionDto> permissionDtoPage = permissionRepository.findPage(pageable, permissionQuery);
        cacheUtils.initCacheInput(permissionDtoPage.getContent());
        return permissionDtoPage;
    }

    @Transactional
    public Permission save(PermissionForm permissionForm) {
        Permission permission;
        if (permissionForm.isCreate()) {
            permission = BeanUtil.map(permissionForm, Permission.class);
            permissionRepository.save(permission);
        } else {
            permission = permissionRepository.findOne(permissionForm.getId());
            ReflectionUtil.copyProperties(permissionForm, permission);
            permissionRepository.save(permission);
        }
        rolePermissionRepository.setEnabledByPermissionId(true, permission.getId());
        List<RolePermission> rolePermissionList = rolePermissionRepository.findByPermissionId(permission.getId());
        if (CollectionUtil.isNotEmpty(permissionForm.getRoleIdList())) {
            List<String> roleIdList = CollectionUtil.extractToList(rolePermissionList, "roleId");
            List<String> removeIdList = CollectionUtil.subtract(roleIdList, permissionForm.getRoleIdList());
            List<String> addIdList = CollectionUtil.subtract(permissionForm.getRoleIdList(), roleIdList);
            List<RolePermission> rolePermissions = Lists.newArrayList();
            for (String roleId : addIdList) {
                rolePermissions.add(new RolePermission(roleId, permission.getId()));
            }
            if (CollectionUtil.isNotEmpty(removeIdList)) {
                rolePermissionRepository.setEnabledByRoleIdList(false, removeIdList);
            }
            if (CollectionUtil.isNotEmpty(addIdList)) {
                rolePermissionRepository.save(rolePermissions);
            }
        } else if (CollectionUtil.isNotEmpty(rolePermissionList)) {
            rolePermissionRepository.setEnabledByPermissionId(false, permission.getId());
        }
        return permission;
    }

    public TreeNode findBackendTree() {
        TreeNode treeNode = new TreeNode("0", "项目列表");
        List<Backend> backendList = backendRepository.findAllEnabled();
        List<BackendModule> backendModuleList = backendModuleRepository.findAllEnabled();
        Map<String, List<BackendModule>> backendModuleMap = CollectionUtil.extractToMapList(backendModuleList, "backendId");
        for (Backend backend : backendList) {
            TreeNode backendTree = new TreeNode("p" + backend.getId(), backend.getName());
            List<BackendModule> backendModules = backendModuleMap.get(backend.getId());
            if (CollectionUtil.isNotEmpty(backendModules)) {
                for (BackendModule backendModule : backendModules) {
                    TreeNode backendModuleTree = new TreeNode(backendModule.getId(), backendModule.getName());
                    backendTree.getChildren().add(backendModuleTree);
                }
                treeNode.getChildren().add(backendTree);
            }
        }
        return treeNode;
    }

    public TreeNode findRoleTree(String roleId) {
        List<BackendMenuDto> backendMenuDtoList = backendRepository.findByRoleId(roleId);
        List<Permission> permissionList = permissionRepository.findAll();
        TreeNode treeNode = getTreeNode(backendMenuDtoList, permissionList);
        return treeNode;
    }

    public TreeNode findRolePermissionTree(String roleId) {
        List<BackendMenuDto> backendMenuDtoList = backendRepository.findRolePermissionByRoleId(roleId);
        List<Permission> permissionList = permissionRepository.findByRoleId(roleId);
        TreeNode treeNode = getTreeNode(backendMenuDtoList, permissionList);
        return treeNode;
    }

    private TreeNode getTreeNode(List<BackendMenuDto> backendMenuDtoList, List<Permission> permissionList) {
        TreeNode treeNode = new TreeNode("0", "权限列表");
        Map<String, List<Permission>> permissionMap = CollectionUtil.extractToMapList(permissionList, "menuId");
        for (BackendMenuDto backend : backendMenuDtoList) {
            TreeNode backendTree = new TreeNode("b" + backend.getId(), backend.getName());
            for (BackendModuleMenuDto backendModule : backend.getBackendModuleList()) {
                TreeNode backendModuleTree = new TreeNode("p" + backendModule.getId(), backendModule.getName());
                for (MenuCategoryMenuDto menuCategory : backendModule.getMenuCategoryList()) {
                    TreeNode menuCategoryTree = new TreeNode("c" + menuCategory.getId(), menuCategory.getName());
                    List<FrontendMenuDto> menuList = menuCategory.getMenuList();
                    if (menuList != null) {
                        for (FrontendMenuDto menu : menuList) {
                            TreeNode menuTree = new TreeNode("m" + menu.getId(), menu.getName());
                            List<Permission> permissions = permissionMap.get(menu.getId());
                            if (CollectionUtil.isNotEmpty(permissions)) {
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
        return treeNode;
    }

    public List<String> getAccountPermissionCheckData(String accountId) {
        List<String> accountPermissionList = accountPermissionRepository.findPermissionIdByAccountId(accountId);
        return accountPermissionList;
    }

    @Transactional
    public void logicDelete(PermissionForm permissionForm) {
        Permission permission = permissionRepository.findOne(permissionForm.getId());
        permission.setEnabled(false);
        permission.setPermission(permission.getPermission() + LocalDateTimeUtils.format(LocalDateTime.now()) + "废弃");
        permissionRepository.save(permission);
    }

}
