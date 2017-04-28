package net.myspring.basic.modules.sys.dto;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.sys.domain.Menu;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * Created by wangzm on 2017/4/26.
 */
public class MenuCategoryMenuDto {

    private String id;
    private String code;
    private String name;
    private List<Menu> menuList= Lists.newArrayList();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }
}
