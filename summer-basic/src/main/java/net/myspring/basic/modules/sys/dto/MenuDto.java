package net.myspring.basic.modules.sys.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.basic.modules.sys.domain.Menu;
import net.myspring.util.cahe.annotation.CacheInput;

/**
 * Created by admin on 2017/4/5.
 */
public class MenuDto extends DataDto<Menu> {
    private String menuCategoryId;
    private String name;
    private Integer sort;
    private Boolean mobile;
    private Boolean visible;
    private String code;
    private boolean locked;
    private boolean enabled;
    @CacheInput(inputKey = "menuCategorys",inputInstance = "menuCategoryId",outputInstance = "name")
    private String menuCategoryName;

    private String permissionStr;

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMenuCategoryId() {
        return menuCategoryId;
    }

    public void setMenuCategoryId(String menuCategoryId) {
        this.menuCategoryId = menuCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Boolean getMobile() {
        return mobile;
    }

    public void setMobile(Boolean mobile) {
        this.mobile = mobile;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getMenuCategoryName() {
        return menuCategoryName;
    }

    public void setMenuCategoryName(String menuCategoryName) {
        this.menuCategoryName = menuCategoryName;
    }

    public String getPermissionStr() {
        return permissionStr;
    }

    public void setPermissionStr(String permissionStr) {
        this.permissionStr = permissionStr;
    }
}
