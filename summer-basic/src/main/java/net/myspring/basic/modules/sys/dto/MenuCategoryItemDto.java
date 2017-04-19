package net.myspring.basic.modules.sys.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import net.myspring.basic.modules.sys.domain.Menu;
import net.myspring.basic.modules.sys.domain.MenuCategory;
import net.myspring.util.text.StringUtils;

import java.util.List;

/**
 * Created by wangzm on 2017/4/19.
 */
public class MenuCategoryItemDto {

    private MenuCategory menuCategory;
    private List<Menu> menuList= Lists.newArrayList();

    @JsonIgnore
    private String backendModuleId;

    public String getBackendModuleId() {
        if(StringUtils.isBlank(backendModuleId)&&menuCategory!=null){
            this.backendModuleId=menuCategory.getBackendModuleId();
        }
        return backendModuleId;
    }

    public void setBackendModuleId(String backendModuleId) {
        this.backendModuleId = backendModuleId;
    }

    public MenuCategory getMenuCategory() {
        return menuCategory;
    }

    public void setMenuCategory(MenuCategory menuCategory) {
        this.menuCategory = menuCategory;
    }

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }
}
