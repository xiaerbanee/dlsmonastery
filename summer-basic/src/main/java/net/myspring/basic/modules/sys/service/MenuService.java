package net.myspring.basic.modules.sys.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.Const;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.domain.PositionBackend;
import net.myspring.basic.modules.hr.manager.AccountManager;
import net.myspring.basic.modules.hr.mapper.AccountMapper;
import net.myspring.basic.modules.hr.mapper.PositionBackendMapper;
import net.myspring.basic.modules.sys.domain.*;
import net.myspring.basic.modules.sys.dto.BackendMenuDto;
import net.myspring.basic.modules.sys.dto.BackendModuleMenuItemDto;
import net.myspring.basic.modules.sys.dto.MenuCategoryItemDto;
import net.myspring.basic.modules.sys.dto.MenuDto;
import net.myspring.basic.modules.sys.manager.MenuManager;
import net.myspring.basic.modules.sys.mapper.*;
import net.myspring.basic.modules.sys.model.MenuCategoryItem;
import net.myspring.basic.modules.sys.model.MenuItem;
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

    public List<Map<String, Object>> findMobileMenus(String accountId) {
        Map<MenuCategory, List<Menu>> map = Maps.newHashMap();
        Account account = accountManager.findOne(accountId);
        if (Const.XCXAUDIT.equals(account.getLoginName())) {
            List<String> menuIds = StringUtils.getSplitList(weixinAuditMenuId, Const.CHAR_COMMA);
            List<Menu> menus = menuMapper.findByIds(menuIds);
            map = getMenuMap(menus);
        } else {
            map = getMenuMap(account, true);
        }
        List<Map<String, Object>> list = Lists.newArrayList();
        for (MenuCategory menuCategory : map.keySet()) {
            Map<String, Object> item = Maps.newHashMap();
            item.put("category", menuCategory);
            item.put("menus", map.get(menuCategory));
            list.add(item);
        }
        return list;
    }

    public List<MenuCategoryItem> findMenus(String accountId) {
        List<MenuCategoryItem> menuCategoryItems = Lists.newArrayList();
        Account account = accountManager.findOne(accountId);
        Map<MenuCategory, List<Menu>> map = getMenuMap(account, false);
        for (MenuCategory menuCategory : map.keySet()) {
            MenuCategoryItem menuCategoryItem = new MenuCategoryItem();
            menuCategoryItem.setMenuCategory(menuCategory);
            for (Menu menu : map.get(menuCategory)) {
                if (!menuCategoryItem.getMenuMap().containsKey(menu.getCategoryCode())) {
                    menuCategoryItem.getMenuMap().put(menu.getCategoryCode(), Lists.newArrayList());
                }
                menuCategoryItem.getMenuMap().get(menu.getCategoryCode()).add(menu);
            }
            if (CollectionUtil.isNotEmpty(menuCategoryItem.getMenuMap())) {
                for (String groupName : menuCategoryItem.getMenuMap().keySet()) {
                    MenuItem menuItem = new MenuItem();
                    menuItem.setGroupName(groupName);
                    menuItem.setMenus(menuCategoryItem.getMenuMap().get(groupName));
                    menuCategoryItem.getMenuItems().add(menuItem);
                }
                menuCategoryItems.add(menuCategoryItem);
            }
        }
        return menuCategoryItems;
    }


    private Map<MenuCategory, List<Menu>> getMenuMap(Account account, Boolean isMobile) {
        List<Menu> menus = Lists.newArrayList();
        List<Menu> menuList;
        if (Const.HR_ACCOUNT_ADMIN_LIST.contains(account.getId())) {
            menuList = menuMapper.findAll();
        } else {
            String positionId = account.getPositionId();
            List<Permission> permissions = permissionMapper.findByPositionId(positionId);
            List<String> menuIds = CollectionUtil.extractToList(permissions, "menuId");
            menuList = menuMapper.findByIds(menuIds);
            List<Menu> permissionIsEmptyMenus = menuMapper.findByPermissionIsEmpty();
            if (CollectionUtil.isNotEmpty(permissionIsEmptyMenus)) {
                menuList.addAll(permissionIsEmptyMenus);
                menuList = Lists.newArrayList(Sets.newHashSet(menuList));
            }
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
        return getMenuMap(menus);
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

    public List<String> findDistinctCategory() {
        return menuMapper.findDistinctCategory();
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
        List<BackendMenuDto> backendMenuDtoList = Lists.newArrayList();
        Account account = accountManager.findOne(accountId);
        List<Backend> backendList;
        if (Const.HR_ACCOUNT_ADMIN_LIST.contains(account.getId())) {
            backendList= backendMapper.findAllEnabled();
        } else {
            List<PositionBackend> positionBackendList = positionBackendMapper.findByPositionId(account.getPositionId());
            backendList = backendMapper.findByIds(CollectionUtil.extractToList(positionBackendList, "id"));
        }
        if (CollectionUtil.isNotEmpty(backendList)) {
            List<BackendModuleMenuItemDto> backendModuleMenuItemDtoList = getBackendModuleMenuItem(account, backendList);
            Map<String, List<BackendModuleMenuItemDto>> backendModuleMenuItemDtoMap = CollectionUtil.extractToMapList(backendModuleMenuItemDtoList, "backendId");
            for (Backend backend : backendList) {
                BackendMenuDto backendMenuDto = new BackendMenuDto();
                backendMenuDto.setBackend(backend);
                Map<String,List<BackendModuleMenuItemDto>> map=Maps.newHashMap();
                map.put(backend.getId(),backendModuleMenuItemDtoMap.get(backend.getId()));
                backendMenuDto.setBackendModuleMenuItemDtoMap(map);
                backendMenuDtoList.add(backendMenuDto);
            }
        }
        return backendMenuDtoList;
    }

    private List<BackendModuleMenuItemDto> getBackendModuleMenuItem(Account account, List<Backend> backendList) {
        List<BackendModuleMenuItemDto> backendModuleMenuItemDtoList = Lists.newArrayList();
        List<BackendModule> backendModuleList = backendModuleMapper.findByBackendIds(CollectionUtil.extractToList(backendList, "id"));
        if(CollectionUtil.isNotEmpty(backendModuleList)){
            List<MenuCategoryItemDto> menuCategoryItemDtoList = getMenuCategoryItemDtoList(account,backendModuleList);
            Map<String, List<MenuCategoryItemDto>> menuCategoryItemDtoMap = CollectionUtil.extractToMapList(menuCategoryItemDtoList, "backendModuleId");
            for (BackendModule backendModule : backendModuleList) {
                BackendModuleMenuItemDto backendModuleMenuItemDto = new BackendModuleMenuItemDto();
                backendModuleMenuItemDto.setBackendModule(backendModule);
                backendModuleMenuItemDto.setMenuCategoryItemDtoList(menuCategoryItemDtoMap.get(backendModule.getId()));
                backendModuleMenuItemDtoList.add(backendModuleMenuItemDto);
            }
        }
        return backendModuleMenuItemDtoList;
    }

    private List<MenuCategoryItemDto> getMenuCategoryItemDtoList(Account account,List<BackendModule> backendModuleList) {
        List<MenuCategoryItemDto> menuCategoryItemDtoList = Lists.newArrayList();
        List<Menu> menuList;
        if (Const.HR_ACCOUNT_ADMIN_LIST.contains(account.getId())) {
            menuList = menuMapper.findAllEnabled();
        } else {
            String positionId = account.getPositionId();
            List<Permission> permissions = permissionMapper.findByPositionId(positionId);
            List<String> menuIds = CollectionUtil.extractToList(permissions, "menuId");
            menuList = menuMapper.findByIds(menuIds);
            List<Menu> permissionIsEmptyMenus = menuMapper.findByPermissionIsEmpty();
            if (CollectionUtil.isNotEmpty(permissionIsEmptyMenus)) {
                menuList.addAll(permissionIsEmptyMenus);
                menuList = Lists.newArrayList(Sets.newHashSet(menuList));
            }
        }
        if (CollectionUtil.isNotEmpty(menuList)) {
            List<MenuCategory> menuCategoryList = menuCategoryMapper.findByBackendModuleIds(CollectionUtil.extractToList(backendModuleList,"id"));
            Map<String, List<Menu>> menuMap = CollectionUtil.extractToMapList(menuList, "menuCategoryId");
            for (MenuCategory menuCategory : menuCategoryList) {
                MenuCategoryItemDto menuCategoryItemDto = new MenuCategoryItemDto();
                menuCategoryItemDto.setMenuCategory(menuCategory);
                menuCategoryItemDto.setMenuList(menuMap.get(menuCategory.getId()));
                menuCategoryItemDtoList.add(menuCategoryItemDto);
            }
        }
        return menuCategoryItemDtoList;
    }
}
