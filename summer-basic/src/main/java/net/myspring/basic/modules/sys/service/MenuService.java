package net.myspring.basic.modules.sys.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.Const;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.mapper.AccountMapper;
import net.myspring.basic.modules.hr.mapper.AccountPermissionMapper;
import net.myspring.basic.modules.sys.domain.Menu;
import net.myspring.basic.modules.sys.domain.Permission;
import net.myspring.basic.modules.sys.dto.BackendMenuDto;
import net.myspring.basic.modules.sys.dto.MenuDto;
import net.myspring.basic.modules.sys.manager.MenuManager;
import net.myspring.basic.modules.sys.mapper.BackendMapper;
import net.myspring.basic.modules.sys.mapper.MenuMapper;
import net.myspring.basic.modules.sys.mapper.PermissionMapper;
import net.myspring.basic.modules.sys.web.form.MenuForm;
import net.myspring.basic.modules.sys.web.query.MenuQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    private MenuManager menuManager;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private BackendMapper backendMapper;
    @Autowired
    private AccountPermissionMapper accountPermissionMapper;

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

    public MenuForm findForm(MenuForm menuForm) {
        if (!menuForm.isCreate()) {
            Menu menu = findOne(menuForm.getId());
            if (menu != null) {
                menuForm = BeanUtil.map(menu, MenuForm.class);
                List<Permission> permissionList = permissionMapper.findByMenuId(menuForm.getId());
                String permissionStr = "";
                for (Permission permission : permissionList) {
                    permissionStr = permissionStr + permission.getName() + Const.CHAR_SPACE + permission.getPermission() + Const.CHAR_ENTER;
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
        menuMapper.deleteById(id);
    }

    public Menu save(MenuForm menuForm) {
        Set<Permission> permissions = Sets.newHashSet();
        Set<Permission> oldPermissions = Sets.newHashSet();
        Menu menu;
        if (menuForm.isCreate()) {
            menu = BeanUtil.map(menuForm, Menu.class);
            menu = menuManager.save(menu);
        } else {
            menu = menuManager.updateForm(menuForm);
            oldPermissions = Sets.newHashSet(permissionMapper.findByMenuId(menuForm.getId()));
        }
        if (StringUtils.isNotBlank(menuForm.getPermissionStr())) {
            String[] permissionRows = menuForm.getPermissionStr().split(Const.CHAR_ENTER);
            for (String permissionRow : permissionRows) {
                if (StringUtils.isNotBlank(permissionRow)) {
                    String[] detail = permissionRow.split(Const.CHAR_SPACE);
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
        if (CollectionUtil.isNotEmpty(oldPermissions)) {
            for (Permission permission : oldPermissions) {
                if (!permissions.contains(permission)) {
                    permissionMapper.deleteById(permission.getId());
                }
            }
        }
        return menu;
    }

    public List<BackendMenuDto> getMenuMap(String accountId) {
        Account account = accountMapper.findOne(accountId);
        return getMenusMap(account, false);
    }

    private List<BackendMenuDto> getMenusMap(Account account, boolean isMobile) {
        List<BackendMenuDto> backendList = Lists.newLinkedList();
        List<String> menuIdList;
        if (Const.HR_ACCOUNT_ADMIN_LIST.contains(account.getId())) {
            List<Menu> menuList = menuMapper.findAllEnabled();
            menuIdList=CollectionUtil.extractToList(menuList,"id");
        } else {
            String roleId = account.getPositionId();
            List<Permission> permissionList;
            List<String> accountPermissions=accountPermissionMapper.findPermissionIdByAccount(SecurityUtils.getAccountId());
            if(CollectionUtil.isNotEmpty(accountPermissions)){
                permissionList=permissionMapper.findByRoleAndAccount(roleId,SecurityUtils.getAccountId());
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
