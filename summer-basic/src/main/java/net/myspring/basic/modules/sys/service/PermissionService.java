package net.myspring.basic.modules.sys.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.hr.manager.PositionManager;
import net.myspring.basic.modules.sys.domain.Menu;
import net.myspring.basic.modules.sys.domain.MenuCategory;
import net.myspring.basic.modules.sys.domain.Permission;
import net.myspring.basic.modules.sys.dto.PermissionDto;
import net.myspring.basic.modules.sys.manager.MenuCategoryManager;
import net.myspring.basic.modules.sys.manager.MenuManager;
import net.myspring.basic.modules.sys.manager.PermissionManager;
import net.myspring.basic.modules.sys.mapper.MenuCategoryMapper;
import net.myspring.basic.modules.sys.mapper.MenuMapper;
import net.myspring.basic.modules.sys.mapper.PermissionMapper;
import net.myspring.basic.modules.sys.web.form.PermissionForm;
import net.myspring.basic.modules.sys.web.query.PermissionQuery;
import net.myspring.common.tree.TreeNode;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanMapper;
import net.myspring.util.text.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
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

    public List<Permission> findByPositionId(String positionId){
        return permissionMapper.findByPositionId(positionId);
    }

    public Permission findOne(String id){
        Permission permission=permissionManager.findOne(id);
        return permission;
    }

    public PermissionDto findDto(String id){
        Permission permission=findOne(id);
        PermissionDto permissionDto=BeanMapper.convertDto(permission,PermissionDto.class);
        cacheUtils.initCacheInput(permissionDto);
        return permissionDto;
    }

    public List<Permission> findAll(){
        return permissionMapper.findAll();
    }

    public List<Permission> findByPermissionLike(String permissionStr){
        return permissionMapper.findByPermissionLike(permissionStr);
    }

    public Page<PermissionDto> findPage(Pageable pageable,PermissionQuery permissionQuery) {
        Page<Permission> page = permissionMapper.findPage(pageable, permissionQuery);
        Page<PermissionDto> permissionDtoPage= BeanMapper.convertPage(page,PermissionDto.class);
        cacheUtils.initCacheInput(permissionDtoPage.getContent());
        return permissionDtoPage;
    }

    public PermissionForm save(PermissionForm permissionForm) {
        boolean isCreate= StringUtils.isBlank(permissionForm.getId());
        if (isCreate) {
            permissionManager.saveForm(permissionForm);
            if(CollectionUtil.isNotEmpty(permissionForm.getPositionIdList())){
                permissionMapper.savePermissionPosition(permissionForm.getId(),permissionForm.getPositionIdList());
            }
        } else {
            permissionManager.updateForm(permissionForm);
            permissionMapper.deletePermissionPosition(permissionForm.getId());
            if(CollectionUtil.isNotEmpty(permissionForm.getPositionIdList())){
                permissionMapper.savePermissionPosition(permissionForm.getId(),permissionForm.getPositionIdList());
            }
        }
        return permissionForm;
    }

    public TreeNode findPermissionTree(List<String> permissionIds) {
        Set<String> permissionIdSet = Sets.newHashSet();
        if (CollectionUtil.isNotEmpty(permissionIds)) {
            for (String id : permissionIds) {
                permissionIdSet.add(id);
            }
        }
        TreeNode treeNode = new TreeNode("0","权限列表");
        List<TreeNode> list = Lists.newArrayList();
        List<MenuCategory> menuCategories = menuCategoryMapper.findAll();
        List<Menu> menus=menuMapper.findByPermissionIsNotEmpty();
        Map<String, List<Menu>> menuMap = CollectionUtil.extractToMapList(menus, "menuCategoryId");
        List<Permission> permissions=permissionMapper.findAll();
        Map<String, List<Permission>> permissionMap = CollectionUtil.extractToMapList(permissions, "menuId");


        for(MenuCategory menuCategory:menuCategories){
            TreeNode categoryTree = new TreeNode("p"+menuCategory.getId(),menuCategory.getName());
            List<Menu> menuList =menuMap.get(menuCategory.getId());
            List<TreeNode> categorychildList = Lists.newArrayList();
            for(Menu menu:menuList){
                TreeNode menuTree = new TreeNode("m"+menu.getId(),menu.getName());
                categorychildList.add(menuTree);
                List<TreeNode> menuChildList = Lists.newArrayList();
                List<Permission> permissionList = permissionMap.get(menu.getId());
                if(CollectionUtil.isNotEmpty(permissionList)){
                    for(Permission permission:permissionList){
                        TreeNode permissTree = new TreeNode(permission.getId(),permission.getName());
                        menuChildList.add(permissTree);
                    }
                }
                menuTree.setChildren(menuChildList);
            }
            categoryTree.setChildren(categorychildList);
            list.add(categoryTree);
        }
        treeNode.setChildren(list);
        treeNode.setChecked(new ArrayList<>(permissionIdSet));
        return treeNode;
    }

    public void logicDeleteOne(String id) {
        permissionMapper.logicDeleteOne(id);
    }

}
