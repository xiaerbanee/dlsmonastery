package net.myspring.basic.modules.sys.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.Const;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.manager.AccountManager;
import net.myspring.basic.modules.hr.mapper.AccountMapper;
import net.myspring.basic.modules.hr.mapper.PositionBackendMapper;
import net.myspring.basic.modules.sys.domain.*;
import net.myspring.basic.modules.sys.dto.BackendMenuDto;
import net.myspring.basic.modules.sys.dto.MenuDto;
import net.myspring.basic.modules.sys.manager.MenuManager;
import net.myspring.basic.modules.sys.mapper.*;
import net.myspring.basic.modules.sys.web.form.MenuForm;
import net.myspring.basic.modules.sys.web.query.MenuQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class MenuService {

    @Value("${weixin.audit.menuId}")
    private String weixinAuditMenuId;
    @Autowired
    private MenuManager menuManager;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private MenuCategoryMapper menuCategoryMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private BackendMapper backendMapper;
    @Autowired
    private BackendModuleMapper backendModuleMapper;
    @Autowired
    private PositionBackendMapper positionBackendMapper;
    @Autowired
    private AccountManager accountManager;

    public List<MenuDto> findAll() {
        List<Menu> menuList = menuMapper.findAll();
        List<MenuDto> menuDtoList = BeanUtil.map(menuList, MenuDto.class);
        cacheUtils.initCacheInput(menuDtoList);
        return menuDtoList;
    }

    private Map<MenuCategory, List<Menu>> getMenuMap(List<Menu> menus) {
        List<MenuCategory> menuCategories = menuCategoryMapper.findAll();
        Map<String, MenuCategory> menuCategoryMap = CollectionUtil.extractToMap(menuCategories, "id");
        Map<MenuCategory, List<Menu>> map = Maps.newHashMap();
        if (CollectionUtil.isNotEmpty(menus)) {
            for (Menu menu : menus) {
                MenuCategory menuCategory = menuCategoryMap.get(menu.getMenuCategoryId());
                if (!map.containsKey(menuCategory)) {
                    map.put(menuCategory, Lists.newArrayList());
                }
                map.get(menuCategory).add(menu);
            }
        }
        return map;
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

    public BackendMenuDto getMenuMap(String accountId) {
        BackendMenuDto backendMenuDto = new BackendMenuDto();
        Account account = accountManager.findOne(accountId);
        Map<Backend, List<Menu>> backendMenuMap = getMenusMap(account,false);
        backendMenuDto.setBackendList(Lists.newArrayList(backendMenuMap.keySet()));
        Map<String, List<BackendModule>> backendModuleMap = Maps.newHashMap();
        for (Backend backend : backendMenuMap.keySet()) {
            backendModuleMap.put(backend.getId(), backend.getBackendModuleList());
        }
        backendMenuDto.setBackendModuleMap(backendModuleMap);
        return backendMenuDto;
    }

    public List<Map<String, Object>> findMobileMenuMap(String accountId) {
        Map<Backend, List<Menu>> backendMenuMap = Maps.newHashMap();
        Account account = accountManager.findOne(accountId);
        if (Const.XCXAUDIT.equals(account.getLoginName())) {
            List<String> menuIds = StringUtils.getSplitList(weixinAuditMenuId, Const.CHAR_COMMA);
            List<Menu> menus = menuMapper.findByIds(menuIds);
            backendMenuMap = getMenusMap(menus);
        } else {
            backendMenuMap = getMenusMap(account,true);
        }
        List<Map<String, Object>> list = Lists.newArrayList();
        for (Backend backend : backendMenuMap.keySet()) {
            Map<String, Object> item = Maps.newHashMap();
            item.put("category", backend);
            item.put("menus", backendMenuMap.get(backend));
            list.add(item);
        }
        return list;
    }

    private Map<Backend, List<Menu>> getMenusMap(Account account,boolean isMobile) {
        List<Menu> menus=Lists.newArrayList();
        List<Menu> menuList;
        if (Const.HR_ACCOUNT_ADMIN_LIST.contains(account.getId())) {
            menuList = menuMapper.findAllEnabled();
        } else {
            String positionId = account.getPositionId();
            List<Permission> permissions = permissionMapper.findByPositionId(positionId);
            List<String> menuIds = CollectionUtil.extractToList(permissions, "menuId");
            menuList = menuMapper.findByIds(menuIds);
            List<Menu> permissionIsEmptyMenus = menuMapper.findByPermissionIsEmpty();
            List<Menu> backendMenus = menuMapper.findBackendMenuByPosition(account.getPositionId());
            menuList = CollectionUtil.union(menuList, permissionIsEmptyMenus);
            menuList = CollectionUtil.intersection(menuList, backendMenus);
            menuList = Lists.newArrayList(Sets.newHashSet(menuList));
        }
        for (Menu menu : menuList) {
            if (menu.getVisible()) {
                if (isMobile) {
                    if (menu.getMobile() && StringUtils.isNotBlank(menu.getMobileHref())) {
                        menus.add(menu);
                    }
                } else {
                    menus.add(menu);
                }
            }
        }
        return getMenusMap(menus);
    }

    private Map<Backend, List<Menu>> getMenusMap(List<Menu> menuList) {
        Map<Backend, List<Menu>> backendMenuMap = Maps.newHashMap();
        if (CollectionUtil.isNotEmpty(menuList)) {
            Map<String, List<Menu>> menuMap = CollectionUtil.extractToMapList(menuList, "menuCategoryId");
            List<MenuCategory> menuCategoryList = menuCategoryMapper.findByIds(Lists.newArrayList(menuMap.keySet()));
            for (MenuCategory menuCategory : menuCategoryList) {
                menuCategory.setMenuList(menuMap.get(menuCategory.getId()));
            }
            Map<String, List<MenuCategory>> menuCategoryMap = CollectionUtil.extractToMapList(menuCategoryList, "backendModuleId");
            if (CollectionUtil.isNotEmpty(menuCategoryMap)) {
                List<BackendModule> backendModuleList = backendModuleMapper.findByIds(Lists.newArrayList(menuCategoryMap.keySet()));
                for (BackendModule backendModule : backendModuleList) {
                    backendModule.setMenuCategoryList(menuCategoryMap.get(backendModule.getId()));
                }
                Map<String, List<BackendModule>> backendModuleMap = CollectionUtil.extractToMapList(backendModuleList, "backendId");
                if (CollectionUtil.isNotEmpty(backendModuleMap)) {
                    List<Backend> backendList = backendMapper.findByIds(Lists.newArrayList(backendModuleMap.keySet()));
                    for (Backend backend : backendList) {
                        backend.setBackendModuleList(backendModuleMap.get(backend.getId()));
                        List<Menu> menus = Lists.newArrayList();
                        for (BackendModule backendModule : backend.getBackendModuleList()) {
                            for (MenuCategory menuCategory : backendModule.getMenuCategoryList()) {
                                menus.addAll(menuCategory.getMenuList());
                            }
                        }
                        backendMenuMap.put(backend, menus);
                    }
                }
            }
        }
        return backendMenuMap;
    }
}
