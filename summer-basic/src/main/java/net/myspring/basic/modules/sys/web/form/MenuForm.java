package net.myspring.basic.modules.sys.web.form;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.modules.sys.domain.Menu;
import net.myspring.basic.modules.sys.domain.MenuCategory;
import net.myspring.basic.common.form.DataForm;
import net.myspring.basic.modules.sys.dto.MenuCategoryDto;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/4/5.
 */

public class MenuForm extends DataForm<Menu>{

    private String menuCategoryId;
    private MenuCategory menuCategory;
    private String permissionStr;
    private String remarks;
    private Boolean mobile;
    private Boolean visible;
    private String href;
    private String sort;
    private String name;
    private String category;
    private List<MenuCategoryDto> menuCategoryList= Lists.newArrayList();
    private List<String> categoryList= Lists.newArrayList();;
    private Map<Boolean,String> bools= Maps.newHashMap();

    public void setMobile(Boolean mobile) {
        this.mobile = mobile;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public List<MenuCategoryDto> getMenuCategoryList() {
        return menuCategoryList;
    }

    public void setMenuCategoryList(List<MenuCategoryDto> menuCategoryList) {
        this.menuCategoryList = menuCategoryList;
    }

    public List<String> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<String> categoryList) {
        this.categoryList = categoryList;
    }

    public Map<Boolean, String> getBools() {
        return bools;
    }

    public void setBools(Map<Boolean, String> bools) {
        this.bools = bools;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean getMobile() {
        return mobile;
    }

    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMenuCategoryId() {
        return menuCategoryId;
    }

    public void setMenuCategoryId(String menuCategoryId) {
        this.menuCategoryId = menuCategoryId;
    }

    public MenuCategory getMenuCategory() {
        return menuCategory;
    }

    public void setMenuCategory(MenuCategory menuCategory) {
        this.menuCategory = menuCategory;
    }

    public String getPermissionStr() {
        return permissionStr;
    }

    public void setPermissionStr(String permissionStr) {
        this.permissionStr = permissionStr;
    }
}
