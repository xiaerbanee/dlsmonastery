package net.myspring.basic.modules.sys.web.form;


import com.google.common.collect.Lists;
import net.myspring.basic.modules.sys.domain.Permission;
import net.myspring.basic.modules.sys.dto.RoleDto;
import net.myspring.common.form.BaseForm;
import net.myspring.basic.modules.sys.dto.MenuDto;

import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */

public class PermissionForm extends BaseForm<Permission> {

    private String menuId;
    private String name;
    private String permission;
    private String remarks;
    private List<MenuDto> menuList= Lists.newArrayList();
    private List<RoleDto> roleList = Lists.newArrayList();
    private List<String> roleIdList = Lists.newArrayList();

    public List<String> getRoleIdList() {
        return roleIdList;
    }

    public void setRoleIdList(List<String> roleIdList) {
        this.roleIdList = roleIdList;
    }

    public List<MenuDto> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<MenuDto> menuList) {
        this.menuList = menuList;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<RoleDto> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<RoleDto> roleList) {
        this.roleList = roleList;
    }
}
