package net.myspring.basic.modules.sys.domain;

import com.google.common.collect.Lists;
import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="sys_menu_category")
public class MenuCategory extends DataEntity<MenuCategory> {
    private String name;
    private Integer sort;
    private Integer version = 0;
    private String code;
    private String backendModuleId;
    private List<String> menuIdList= Lists.newArrayList();

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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public List<String> getMenuIdList() {
        return menuIdList;
    }

    public void setMenuIdList(List<String> menuIdList) {
        this.menuIdList = menuIdList;
    }
}
