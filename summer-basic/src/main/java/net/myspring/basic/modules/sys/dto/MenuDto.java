package net.myspring.basic.modules.sys.dto;

import com.google.common.collect.Lists;
import net.myspring.basic.common.dto.DataDto;
import net.myspring.basic.common.utils.Const;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.sys.domain.Menu;
import net.myspring.basic.modules.sys.domain.MenuCategory;
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
    @CacheInput(inputKey = "menuCategorys",inputInstance = "menuCategoryId",outputInstance = "name")
    private String menuCategoryName;

    private String permissionStr;
    private List<Account> accountList = Lists.newArrayList();
    private List<String> accountIdList = Lists.newArrayList();
    private MenuCategory menuCategory;
    private List<Permission> permissionList = Lists.newArrayList();
    private List<String> permissionIdList = Lists.newArrayList();

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    public List<String> getAccountIdList() {
        return accountIdList;
    }

    public void setAccountIdList(List<String> accountIdList) {
        this.accountIdList = accountIdList;
    }

    public MenuCategory getMenuCategory() {
        return menuCategory;
    }

    public void setMenuCategory(MenuCategory menuCategory) {
        this.menuCategory = menuCategory;
    }

    public List<Permission> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<Permission> permissionList) {
        this.permissionList = permissionList;
    }

    public List<String> getPermissionIdList() {
        return permissionIdList;
    }

    public void setPermissionIdList(List<String> permissionIdList) {
        this.permissionIdList = permissionIdList;
    }

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
