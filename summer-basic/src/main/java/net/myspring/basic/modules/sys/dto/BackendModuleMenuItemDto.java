package net.myspring.basic.modules.sys.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.modules.sys.domain.BackendModule;
import net.myspring.basic.modules.sys.domain.Menu;
import net.myspring.basic.modules.sys.domain.MenuCategory;
import net.myspring.util.text.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by wangzm on 2017/4/19.
 */
public class BackendModuleMenuItemDto {

    //菜单分组
    private BackendModule backendModule;
    //菜单列表
    private List<MenuCategoryItemDto> menuCategoryItemDtoList = Lists.newArrayList();

    @JsonIgnore
    private String backendId;

    public String getBackendId() {
        if(StringUtils.isBlank(backendId)&&backendModule!=null){
            this.backendId=backendModule.getBackendId();
        }
        return backendId;
    }

    public void setBackendId(String backendId) {
        this.backendId = backendId;
    }

    public BackendModule getBackendModule() {
        return backendModule;
    }

    public void setBackendModule(BackendModule backendModule) {
        this.backendModule = backendModule;
    }

    public List<MenuCategoryItemDto> getMenuCategoryItemDtoList() {
        return menuCategoryItemDtoList;
    }

    public void setMenuCategoryItemDtoList(List<MenuCategoryItemDto> menuCategoryItemDtoList) {
        this.menuCategoryItemDtoList = menuCategoryItemDtoList;
    }
}
