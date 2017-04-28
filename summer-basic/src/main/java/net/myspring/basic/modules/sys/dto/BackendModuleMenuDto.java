package net.myspring.basic.modules.sys.dto;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.sys.domain.Backend;
import net.myspring.basic.modules.sys.domain.MenuCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * Created by wangzm on 2017/4/26.
 */
public class BackendModuleMenuDto {

    private String id;
    private String name;
    private String backendCode;
    private String code;
    @JsonIgnore
    private List<MenuCategoryMenuDto> menuCategoryList= Lists.newArrayList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBackendCode() {
        return backendCode;
    }

    public void setBackendCode(String backendCode) {
        this.backendCode = backendCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<MenuCategoryMenuDto> getMenuCategoryList() {
        return menuCategoryList;
    }

    public void setMenuCategoryList(List<MenuCategoryMenuDto> menuCategoryList) {
        this.menuCategoryList = menuCategoryList;
    }
}
