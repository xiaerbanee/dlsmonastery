package net.myspring.basic.modules.sys.web.form;


import net.myspring.basic.modules.sys.domain.Permission;
import net.myspring.basic.common.form.DataForm;
import net.myspring.basic.modules.sys.dto.MenuDto;

import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */

public class PermissionForm extends DataForm<Permission> {

    private List<String> positionIdList;
    private String menuId;
    private String name;
    private String permission;
    private String remarks;
    private List<MenuDto> menuDtoList;

    public List<MenuDto> getMenuDtoList() {
        return menuDtoList;
    }

    public void setMenuDtoList(List<MenuDto> menuDtoList) {
        this.menuDtoList = menuDtoList;
    }

    public List<String> getPositionIdList() {
        return positionIdList;
    }

    public void setPositionIdList(List<String> positionIdList) {
        this.positionIdList = positionIdList;
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
}
