package net.myspring.basic.modules.sys.domain;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="sys_menu")
public class Menu extends DataEntity<Menu> {
    private String category;
    private String name;
    private String href;
    private String icon;
    private Integer sort;
    private Boolean mobile;
    private Integer version = 0;
    private Boolean visible;
    private String enName;
    private String inaName;
    private String enCategory;
    private String inaCategory;
    private String mobileHref;
    private String mobileIcon;
    private List<Account> accountList = Lists.newArrayList();
    private List<String> accountIdList = Lists.newArrayList();
    private MenuCategory menuCategory;
    private String menuCategoryId;
    private List<Permission> permissionList = Lists.newArrayList();
    private List<String> permissionIdList = Lists.newArrayList();
    private String menuCode;
    private String categoryCode;

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getInaName() {
        return inaName;
    }

    public void setInaName(String inaName) {
        this.inaName = inaName;
    }

    public String getEnCategory() {
        return enCategory;
    }

    public void setEnCategory(String enCategory) {
        this.enCategory = enCategory;
    }

    public String getInaCategory() {
        return inaCategory;
    }

    public void setInaCategory(String inaCategory) {
        this.inaCategory = inaCategory;
    }

    public String getMobileHref() {
        return mobileHref;
    }

    public void setMobileHref(String mobileHref) {
        this.mobileHref = mobileHref;
    }

    public String getMobileIcon() {
        return mobileIcon;
    }

    public void setMobileIcon(String mobileIcon) {
        this.mobileIcon = mobileIcon;
    }

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

    public String getMenuCategoryId() {
        return menuCategoryId;
    }

    public void setMenuCategoryId(String menuCategoryId) {
        this.menuCategoryId = menuCategoryId;
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
}
