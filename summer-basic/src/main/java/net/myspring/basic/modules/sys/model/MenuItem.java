package net.myspring.basic.modules.sys.model;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.sys.domain.Menu;

import java.util.List;

/**
 * Created by liuj on 2017/1/20.
 */
public class MenuItem {
    //菜单分组
    private String groupName;
    //菜单列表
    private List<Menu> menus = Lists.newArrayList();

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }
}
