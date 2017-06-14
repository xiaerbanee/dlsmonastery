package net.myspring.basic.modules.sys.web.query;

import com.google.common.collect.Lists;
import net.myspring.basic.common.query.BaseQuery;
import net.myspring.basic.modules.sys.dto.MenuCategoryDto;

import java.util.List;

/**
 * Created by lihx on 2017/4/7.
 */
public class MenuQuery extends BaseQuery {
    private String name;
    private String menuCategoryId;
    private List<MenuCategoryDto> menuCategoryList= Lists.newArrayList();

    public List<MenuCategoryDto> getMenuCategoryList() {
        return menuCategoryList;
    }

    public void setMenuCategoryList(List<MenuCategoryDto> menuCategoryList) {
        this.menuCategoryList = menuCategoryList;
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

    public void setMenuCategoryId(String menuCategoryName) {
        this.menuCategoryId = menuCategoryName;
    }
}
