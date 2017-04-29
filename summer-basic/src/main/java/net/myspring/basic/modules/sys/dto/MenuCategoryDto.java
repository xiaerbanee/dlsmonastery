package net.myspring.basic.modules.sys.dto;

import com.google.common.collect.Lists;
import net.myspring.basic.common.dto.DataDto;
import net.myspring.basic.modules.sys.domain.Menu;
import net.myspring.basic.modules.sys.domain.MenuCategory;
import net.myspring.util.cahe.annotation.CacheInput;

import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */
public class MenuCategoryDto extends DataDto<MenuCategory> {
    private String name;
    private Integer sort;
    private String code;
    private String backendModuleId;
    @CacheInput(inputKey = "backendModules",inputInstance = "backendModuleId",outputInstance = "code")
    private String backendModuleCode;
    @CacheInput(inputKey = "backendModules",inputInstance = "backendModuleId",outputInstance = "name")
    private String backendModuleName;
    private List<Menu> menuList = Lists.newArrayList();
    private List<String> menuIdList = Lists.newArrayList();

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBackendModuleId() {
        return backendModuleId;
    }

    public void setBackendModuleId(String backendModuleId) {
        this.backendModuleId = backendModuleId;
    }

    public String getBackendModuleCode() {
        return backendModuleCode;
    }

    public void setBackendModuleCode(String backendModuleCode) {
        this.backendModuleCode = backendModuleCode;
    }

    public String getBackendModuleName() {
        return backendModuleName;
    }

    public void setBackendModuleName(String backendModuleName) {
        this.backendModuleName = backendModuleName;
    }

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public List<String> getMenuIdList() {
        return menuIdList;
    }

    public void setMenuIdList(List<String> menuIdList) {
        this.menuIdList = menuIdList;
    }
}
