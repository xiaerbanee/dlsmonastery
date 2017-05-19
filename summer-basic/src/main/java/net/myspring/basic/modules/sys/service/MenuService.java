package net.myspring.basic.modules.sys.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.mapper.AccountMapper;
import net.myspring.basic.modules.hr.mapper.AccountPermissionMapper;
import net.myspring.basic.modules.sys.domain.Menu;
import net.myspring.basic.modules.sys.domain.Permission;
import net.myspring.basic.modules.sys.dto.BackendMenuDto;
import net.myspring.basic.modules.sys.dto.MenuDto;
import net.myspring.basic.modules.sys.manager.RoleManager;
import net.myspring.basic.modules.sys.mapper.BackendMapper;
import net.myspring.basic.modules.sys.mapper.MenuMapper;
import net.myspring.basic.modules.sys.mapper.PermissionMapper;
import net.myspring.basic.modules.sys.web.form.MenuForm;
import net.myspring.basic.modules.sys.web.query.MenuQuery;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class MenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private BackendMapper backendMapper;
    @Autowired
    private AccountPermissionMapper accountPermissionMapper;
    @Value("${setting.adminIdList}")
    private String adminIdList;

    public List<MenuDto> findAll() {
        List<Menu> menuList = menuMapper.findAll();
        List<MenuDto> menuDtoList = BeanUtil.map(menuList, MenuDto.class);
        cacheUtils.initCacheInput(menuDtoList);
        return menuDtoList;
    }

    public Menu findOne(String id) {
        Menu menu = menuMapper.findOne(id);
        return menu;
    }

    public MenuForm getFormProperty(MenuForm menuForm) {
        if (!menuForm.isCreate()) {
            Menu menu = findOne(menuForm.getId());
            if (menu != null) {
                menuForm = BeanUtil.map(menu, MenuForm.class);
                List<Permission> permissionList = permissionMapper.findByMenuId(menuForm.getId());
                String permissionStr = "";
                for (Permission permission : permissionList) {
                    permissionStr = permissionStr + permission.getName() + CharConstant.SPACE+ permission.getPermission() + CharConstant.ENTER;
                }
                menuForm.setPermissionStr(permissionStr);
            }
        }
        return menuForm;
    }

    public Page<MenuDto> findPage(Pageable pageable, MenuQuery menuQuery) {
        Page<MenuDto> menuDtoPage = menuMapper.findPage(pageable, menuQuery);
        cacheUtils.initCacheInput(menuDtoPage.getContent());
        return menuDtoPage;
    }

    public void delete(String id) {
        menuMapper.logicDeleteOne(id);
    }

    public Menu save(MenuForm menuForm) {
        Set<Permission> permissions = Sets.newHashSet();
        Set<Permission> oldPermissions = Sets.newHashSet();
        Menu menu;
        if (menuForm.isCreate()) {
            menu = BeanUtil.map(menuForm, Menu.class);
            menuMapper.save(menu);
        } else {
            menu = menuMapper.findOne(menuForm.getId());
            ReflectionUtil.copyProperties(menuForm,menu);
            menuMapper.update(menu);
            oldPermissions = Sets.newHashSet(permissionMapper.findByMenuId(menuForm.getId()));
        }
        if (StringUtils.isNotBlank(menuForm.getPermissionStr())) {
            String[] permissionRows = menuForm.getPermissionStr().split(CharConstant.ENTER);
            for (String permissionRow : permissionRows) {
                if (StringUtils.isNotBlank(permissionRow)) {
                    String[] detail = permissionRow.split(CharConstant.SPACE);
                    if (detail.length >= 2) {
                        String name = detail[0];
                        String permissionStr = detail[detail.length - 1];
                        Permission permission = permissionMapper.findByPermission(permissionStr);
                        if (permission != null) {
                            permission.setName(name);
                            permission.setMenuId(menuForm.getId());
                            permissionMapper.update(permission);
                        } else {
                            permission = new Permission();
                            permission.setName(name);
                            permission.setPermission(permissionStr);
                            permission.setMenuId(menuForm.getId());
                            permissionMapper.save(permission);
                        }
                        permissions.add(permission);
                    }
                }
            }
        }
        List<String> removePermissionIds = CollectionUtil.subtract(CollectionUtil.extractToList(oldPermissions,"id"),CollectionUtil.extractToList(permissions,"id"));
        if(CollectionUtil.isNotEmpty(removePermissionIds)){
            permissionMapper.logicDeleteByIds(removePermissionIds);
        }
        return menu;
    }

    public List<BackendMenuDto> getMenuMap() {
        return getMenusMap(false);
    }

    private List<BackendMenuDto> getMenusMap(boolean isMobile) {
        List<BackendMenuDto> backendList = Lists.newLinkedList();
        List<String> menuIdList;
        if (StringUtils.getSplitList(adminIdList, CharConstant.COMMA).contains(RequestUtils.getAccountId())) {
            List<Menu> menuList = menuMapper.findAllEnabled();
            menuIdList=CollectionUtil.extractToList(menuList,"id");
        } else {
            String roleId=roleManager.findIdByAccountId(RequestUtils.getAccountId());
            List<Permission> permissionList;
            List<String> accountPermissions=accountPermissionMapper.findPermissionIdByAccount(RequestUtils.getAccountId());
            if(CollectionUtil.isNotEmpty(accountPermissions)){
                permissionList=permissionMapper.findByRoleAndAccount(roleId, RequestUtils.getAccountId());
            }else {
                permissionList=permissionMapper.findByRoleId(roleId);
            }
            menuIdList = CollectionUtil.extractToList(permissionList, "menuId");
            List<Menu> permissionIsEmptyMenus = menuMapper.findByPermissionIsEmpty();
            menuIdList = CollectionUtil.union(menuIdList, CollectionUtil.extractToList(permissionIsEmptyMenus,"id"));
            menuIdList = Lists.newArrayList(Sets.newHashSet(menuIdList));
        }
        if(CollectionUtil.isNotEmpty(menuIdList)){
            backendList = backendMapper.findByMenuList(menuIdList);
        }
        return backendList;
    }
}
