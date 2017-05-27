package net.myspring.basic.modules.sys.dto;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.List;
import java.util.Map;

/**
 * Created by wangzm on 2017/4/26.
 */
public class MenuCategoryMenuDto {

    private String id;
    private String code;
    private String name;
    private List<FrontendMenuDto> menuList= Lists.newArrayList();
    @JsonIgnore
    private Map<String,FrontendMenuDto> frontendMenuDtoMap = Maps.newTreeMap();

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

    public List<FrontendMenuDto> getMenuList() {
        if(frontendMenuDtoMap.size()>0) {
            menuList =  Lists.newArrayList(frontendMenuDtoMap.values());
        }
        return menuList;
    }

    public void setMenuList(List<FrontendMenuDto> menuList) {
        this.menuList = menuList;
    }

    public Map<String, FrontendMenuDto> getFrontendMenuDtoMap() {
        return frontendMenuDtoMap;
    }

    public void setFrontendMenuDtoMap(Map<String, FrontendMenuDto> frontendMenuDtoMap) {
        this.frontendMenuDtoMap = frontendMenuDtoMap;
    }
}
