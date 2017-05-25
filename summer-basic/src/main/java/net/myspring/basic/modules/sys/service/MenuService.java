package net.myspring.basic.modules.sys.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.sys.domain.Menu;
import net.myspring.basic.modules.sys.domain.Permission;
import net.myspring.basic.modules.sys.dto.BackendMenuDto;
import net.myspring.basic.modules.sys.dto.MenuDto;
import net.myspring.basic.modules.sys.manager.RoleManager;
import net.myspring.basic.modules.sys.repository.BackendRepository;
import net.myspring.basic.modules.sys.repository.MenuRepository;
import net.myspring.basic.modules.sys.repository.PermissionRepository;
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
    private MenuRepository menuRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private BackendRepository backendRepository;
    @Autowired
    private AccountPermissionRepository accountPermissionRepository;
    @Value("${setting.adminIdList}")
    private String adminIdList;

    public List<MenuDto> findAll() {
        List<Menu> menuList = menuRepository.findAll();
        List<MenuDto> menuDtoList = BeanUtil.map(menuList, MenuDto.class);
        cacheUtils.initCacheInput(menuDtoList);
        return menuDtoList;
    }

    public Menu findOne(String id) {
        Menu menu = menuRepository.findOne(id);
        return menu;
    }

    public MenuDto findOne(MenuDto menuDto) {
        if (!menuDto.isCreate()) {
            Menu menu = findOne(menuDto.getId());
            if (menu != null) {
                menuDto = BeanUtil.map(menu, MenuDto.class);
                List<Permission> permissionList = permissionRepository.findByMenuId(menuDto.getId());
                String permissionStr = "";
                for (Permission permission : permissionList) {
                    permissionStr = permissionStr + permission.getName() + CharConstant.SPACE+ permission.getPermission() + CharConstant.ENTER;
                }
                menuDto.setPermissionStr(permissionStr);
            }
        }
        return menuDto;
    }

    public Page<MenuDto> findPage(Pageable pageable, MenuQuery menuQuery) {
        Page<MenuDto> menuDtoPage = menuRepository.findPage(pageable, menuQuery);
        cacheUtils.initCacheInput(menuDtoPage.getContent());
        return menuDtoPage;
    }

    public void delete(String id) {
        menuRepository.logicDeleteOne(id);
    }

    public Menu save(MenuForm menuForm) {
        Set<Permission> permissions = Sets.newHashSet();
        Set<Permission> oldPermissions = Sets.newHashSet();
        Menu menu;
        if (menuForm.isCreate()) {
            menu = BeanUtil.map(menuForm, Menu.class);
            menuRepository.save(menu);
        } else {
            menu = menuRepository.findOne(menuForm.getId());
            ReflectionUtil.copyProperties(menuForm,menu);
            menuRepository.update(menu);
            oldPermissions = Sets.newHashSet(permissionRepository.findByMenuId(menuForm.getId()));
        }
        if (StringUtils.isNotBlank(menuForm.getPermissionStr())) {
            String[] permissionRows = menuForm.getPermissionStr().split(CharConstant.ENTER);
            for (String permissionRow : permissionRows) {
                if (StringUtils.isNotBlank(permissionRow)) {
                    String[] detail = permissionRow.split(CharConstant.SPACE);
                    if (detail.length >= 2) {
                        String name = detail[0];
                        String permissionStr = detail[detail.length - 1];
                        Permission permission = permissionRepository.findByPermission(permissionStr);
                        if (permission != null) {
                            permission.setName(name);
                            permission.setMenuId(menuForm.getId());
                            permissionRepository.update(permission);
                        } else {
                            permission = new Permission();
                            permission.setName(name);
                            permission.setPermission(permissionStr);
                            permission.setMenuId(menuForm.getId());
                            permissionRepository.save(permission);
                        }
                        permissions.add(permission);
                    }
                }
            }
        }
        List<String> removePermissionIds = CollectionUtil.subtract(CollectionUtil.extractToList(oldPermissions,"id"),CollectionUtil.extractToList(permissions,"id"));
        if(CollectionUtil.isNotEmpty(removePermissionIds)){
            permissionRepository.logicDeleteByIds(removePermissionIds);
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
            List<Menu> menuList = menuRepository.findAllEnabled();
            menuIdList=CollectionUtil.extractToList(menuList,"id");
        } else {
            String roleId=roleManager.findIdByAccountId(RequestUtils.getAccountId());
            List<Permission> permissionList;
            List<String> accountPermissions=accountPermissionRepository.findPermissionIdByAccount(RequestUtils.getAccountId());
            if(CollectionUtil.isNotEmpty(accountPermissions)){
                permissionList=permissionRepository.findByRoleAndAccount(roleId, RequestUtils.getAccountId());
            }else {
                permissionList=permissionRepository.findByRoleId(roleId);
            }
            menuIdList = CollectionUtil.extractToList(permissionList, "menuId");
            List<Menu> permissionIsEmptyMenus = menuRepository.findByPermissionIsEmpty();
            menuIdList = CollectionUtil.union(menuIdList, CollectionUtil.extractToList(permissionIsEmptyMenus,"id"));
            menuIdList = Lists.newArrayList(Sets.newHashSet(menuIdList));
        }
        if(CollectionUtil.isNotEmpty(menuIdList)){
            backendList = backendRepository.findByMenuList(menuIdList);
        }
        return backendList;
    }
}
