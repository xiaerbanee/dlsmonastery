package net.myspring.basic.modules.sys.web.form;


import com.google.common.collect.Lists;
import net.myspring.basic.modules.sys.domain.Permission;
import net.myspring.basic.common.form.DataForm;
import net.myspring.basic.modules.sys.dto.MenuDto;
import net.myspring.util.cahe.annotation.CacheInput;

import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */

public class PermissionForm extends DataForm<Permission> {

    private List<String> positionIdList=Lists.newArrayList();
    private String menuId;
    private String name;
    private String permission;
    private String remarks;
    private List<MenuDto> menuList= Lists.newArrayList();
    @CacheInput(inputKey = "positions",inputInstance = "positionIdList",outputInstance = "name")
    private List<String> positionNameList=Lists.newArrayList();

    public List<String> getPositionNameList() {
        return positionNameList;
    }

    public void setPositionNameList(List<String> positionNameList) {
        this.positionNameList = positionNameList;
    }

    public List<MenuDto> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<MenuDto> menuList) {
        this.menuList = menuList;
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
