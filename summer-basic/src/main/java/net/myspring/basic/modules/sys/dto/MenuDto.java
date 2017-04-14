package net.myspring.basic.modules.sys.dto;

import net.myspring.basic.common.dto.DataDto;
import net.myspring.basic.common.utils.Const;
import net.myspring.basic.modules.sys.domain.Menu;
import net.myspring.basic.modules.sys.domain.Permission;
import net.myspring.util.cahe.annotation.CacheInput;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */
public class MenuDto extends DataDto<Menu> {
    private String menuCategoryId;
    private String category;
    private String name;
    private String href;
    private Integer sort;
    private Boolean mobile;
    private String menuCode;
    private String categoryCode;
    private boolean locked;
    private boolean enabled;
    private String remarks;
    @CacheInput(inputKey = "menuCategorys",inputInstance = "menuCategoryId",outputInstance = "name")
    private String menuCategoryName;

    private String permissionStr;

    public String getMenuCategoryId() {
        return menuCategoryId;
    }

    public void setMenuCategoryId(String menuCategoryId) {
        this.menuCategoryId = menuCategoryId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
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

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
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

    @Override
    public String getRemarks() {
        return remarks;
    }

    @Override
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getMenuCategoryName() {
        return menuCategoryName;
    }

    public void setMenuCategoryName(String menuCategoryName) {
        this.menuCategoryName = menuCategoryName;
    }

    public void setPermissionStr(String permissionStr) {
        this.permissionStr = permissionStr;
    }

    public String getPermissionStr() {
        return permissionStr;
    }
}
