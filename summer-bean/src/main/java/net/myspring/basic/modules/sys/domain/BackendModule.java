package net.myspring.basic.modules.sys.domain;

import com.google.common.collect.Lists;
import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="sys_backend_module")
public class BackendModule extends DataEntity<BackendModule> {
    private String name;
    private Integer version = 0;
    private String backendId;
    private String code;
    private String icon;
    private List<String> positionModuleIdList = Lists.newArrayList();
    private List<String> menuCategoryIdList = Lists.newArrayList();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getBackendId() {
        return backendId;
    }

    public void setBackendId(String backendId) {
        this.backendId = backendId;
    }

    public List<String> getPositionModuleIdList() {
        return positionModuleIdList;
    }

    public void setPositionModuleIdList(List<String> positionModuleIdList) {
        this.positionModuleIdList = positionModuleIdList;
    }

    public List<String> getMenuCategoryIdList() {
        return menuCategoryIdList;
    }

    public void setMenuCategoryIdList(List<String> menuCategoryIdList) {
        this.menuCategoryIdList = menuCategoryIdList;
    }
}
