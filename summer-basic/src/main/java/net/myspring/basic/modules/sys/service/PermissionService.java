package net.myspring.basic.modules.sys.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.myspring.basic.common.dto.NameValueDto;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.domain.Position;
import net.myspring.basic.modules.sys.domain.*;
import net.myspring.basic.modules.sys.dto.PermissionDto;
import net.myspring.basic.modules.sys.manager.MenuCategoryManager;
import net.myspring.basic.modules.sys.manager.MenuManager;
import net.myspring.basic.modules.sys.manager.PermissionManager;
import net.myspring.basic.modules.sys.mapper.*;
import net.myspring.basic.modules.sys.web.form.PermissionForm;
import net.myspring.basic.modules.sys.web.query.PermissionQuery;
import net.myspring.common.tree.TreeNode;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
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
    private PermissionManager permissionManager;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private MenuCategoryManager menuCategoryManager;
    @Autowired
    private MenuCategoryMapper menuCategoryMapper;
    @Autowired
    private MenuManager menuManager;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private BackendMapper backendMapper;
    @Autowired
    private BackendModuleMapper backendModuleMapper;

    public List<PermissionDto> findByMenuId(String menuId) {
        List<Permission> permissionList = permissionMapper.findByMenuId(menuId);
        List<PermissionDto> permissionDtoList = BeanUtil.map(permissionList, PermissionDto.class);
        return permissionDtoList;
    }

    public List<Permission> findByPositionId(String positionId) {
        return permissionMapper.findByPositionId(positionId);
    }

    public Permission findOne(String id) {
        Permission permission = permissionManager.findOne(id);
        return permission;
    }

    public PermissionForm findForm(PermissionForm permissionForm) {
        if (!permissionForm.isCreate()) {
            Permission permission = permissionManager.findOne(permissionForm.getId());
            permissionForm = BeanUtil.map(permission, PermissionForm.class);
            List<NameValueDto> nameValueDtoList = permissionMapper.findNameValueByPositionId(Lists.newArrayList(permission.getId()));
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
            permission = permissionManager.save(permission);
            if (CollectionUtil.isNotEmpty(permissionForm.getPositionIdList())) {
                permissionMapper.savePermissionPosition(permissionForm.getId(), permissionForm.getPositionIdList());
            }
        } else {
            permission = permissionManager.updateForm(permissionForm);
            permissionMapper.deletePermissionPosition(permissionForm.getId());
            if (CollectionUtil.isNotEmpty(permissionForm.getPositionIdList())) {
                permissionMapper.savePermissionPosition(permissionForm.getId(), permissionForm.getPositionIdList());
            }
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

    public TreeNode findPermissionTree(List<String> permissionIds) {
        Set<String> permissionIdSet = Sets.newHashSet(permissionIds);
        TreeNode treeNode = new TreeNode("0", "权限列表");
        List<BackendModule> backendModuleList = backendModuleMapper.findByPositionId(SecurityUtils.getPositionId());
        List<Backend> backendList=backendMapper.findByIds(CollectionUtil.extractToList(backendModuleList,"backendId"));
        Map<String,List<BackendModule>> backendModuleMap=CollectionUtil.extractToMapList(backendModuleList,"backendId");
        List<MenuCategory> menuCategories = menuCategoryMapper.findByBackendModuleIds(CollectionUtil.extractToList(backendModuleList, "id"));
        Map<String, List<MenuCategory>> menuCategoryMap = CollectionUtil.extractToMapList(menuCategories, "backendModuleId");
        List<Menu> menus = menuMapper.findByPermissionIsNotEmpty();
        Map<String, List<Menu>> menuMap = CollectionUtil.extractToMapList(menus, "menuCategoryId");
        List<Permission> permissions = permissionMapper.findAllEnabled();
        Map<String, List<Permission>> permissionMap = CollectionUtil.extractToMapList(permissions, "menuId");
        for(Backend backend:backendList){
            TreeNode backendTree = new TreeNode("b" + backend.getId(), backend.getName());
            List<BackendModule> backendModules=backendModuleMap.get(backend.getId());
            for (BackendModule backendModule : backendModules) {
                TreeNode backendModuleTree = new TreeNode("p" + backendModule.getId(), backendModule.getName());
                for (MenuCategory menuCategory : menuCategoryMap.get(backendModule.getId())) {
                    TreeNode menuCategoryTree = new TreeNode("c" + menuCategory.getId(), menuCategory.getName());
                    List<Menu> menuList = menuMap.get(menuCategory.getId());
                    if(menuList!=null){
                        for (Menu menu : menuList) {
                            TreeNode menuTree = new TreeNode("m" + menu.getId(), menu.getName());
                            for (Permission permission : permissionMap.get(menu.getId())) {
                                TreeNode permissionTree = new TreeNode(permission.getId(), permission.getName());
                                menuTree.getChildren().add(permissionTree);
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

    public void logicDeleteOne(String id) {
        permissionMapper.logicDeleteOne(id);
    }

}
