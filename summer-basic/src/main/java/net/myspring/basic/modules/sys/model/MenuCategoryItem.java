package net.myspring.basic.modules.sys.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.modules.sys.domain.Menu;
import net.myspring.basic.modules.sys.domain.MenuCategory;

import java.util.List;
import java.util.Map;

/**
 * Created by liuj on 2017/1/20.
 */
public class MenuCategoryItem {
    //菜单分组
    private MenuCategory menuCategory;
    //菜单列表
    private List<MenuItem> menuItems = Lists.newArrayList();
    @JsonIgnore
    private Map<String,List<Menu>> menuMap = Maps.newLinkedHashMap();

    public MenuCategory getMenuCategory() {
        return menuCategory;
    }

    public void setMenuCategory(MenuCategory menuCategory) {
        this.menuCategory = menuCategory;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public Map<String, List<Menu>> getMenuMap() {
        return menuMap;
    }

    public void setMenuMap(Map<String, List<Menu>> menuMap) {
        this.menuMap = menuMap;
    }
}
