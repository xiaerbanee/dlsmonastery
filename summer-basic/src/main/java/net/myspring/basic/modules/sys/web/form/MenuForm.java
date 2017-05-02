package net.myspring.basic.modules.sys.web.form;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.modules.sys.domain.Menu;
import net.myspring.common.form.DataForm;
import net.myspring.basic.modules.sys.dto.MenuCategoryDto;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/4/5.
 */

public class MenuForm extends DataForm<Menu>{
    private String menuCategoryId;
    private String permissionStr;
    private String remarks;
    private boolean mobile;
    private boolean visible;
    private String sort;
    private String name;
    private String code;
    private List<MenuCategoryDto> menuCategoryList= Lists.newArrayList();
    private List<String> categoryList= Lists.newArrayList();
    private Map<String,String> boolMap= Maps.newHashMap();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    public Map<String, String> getBoolMap() {
        return boolMap;
    }

    public void setBoolMap(Map<String, String> boolMap) {
        this.boolMap = boolMap;
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

    public String getMenuCategoryId() {
        return menuCategoryId;
    }

    public void setMenuCategoryId(String menuCategoryId) {
        this.menuCategoryId = menuCategoryId;
    }

    public String getPermissionStr() {
        return permissionStr;
    }

    public void setPermissionStr(String permissionStr) {
        this.permissionStr = permissionStr;
    }
}
