package net.myspring.basic.modules.sys.dto;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.List;
import java.util.Map;

/**
 * Created by wangzm on 2017/4/26.
 */
public class BackendModuleMenuDto {

    private String id;
    private String name;
    private String code;
    private String icon;
    private List<MenuCategoryMenuDto> menuCategoryList= Lists.newArrayList();
    @JsonIgnore
    private Map<String,MenuCategoryMenuDto> menuCategoryMenuDtoMap = Maps.newTreeMap();

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if(menuCategoryMenuDtoMap.size()>0) {
            menuCategoryList =  Lists.newArrayList(menuCategoryMenuDtoMap.values());
        }
        return menuCategoryList;
    }

    public void setMenuCategoryList(List<MenuCategoryMenuDto> menuCategoryList) {
        this.menuCategoryList = menuCategoryList;
    }

    public Map<String, MenuCategoryMenuDto> getMenuCategoryMenuDtoMap() {
        return menuCategoryMenuDtoMap;
    }

    public void setMenuCategoryMenuDtoMap(Map<String, MenuCategoryMenuDto> menuCategoryMenuDtoMap) {
        this.menuCategoryMenuDtoMap = menuCategoryMenuDtoMap;
    }
}
