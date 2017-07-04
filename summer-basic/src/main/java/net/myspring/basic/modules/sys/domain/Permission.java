package net.myspring.basic.modules.sys.domain;

import net.myspring.basic.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="sys_permission")
public class Permission extends DataEntity<Permission> {
    private String name;
    private String permission;
    private Integer version = 0;
    private String menuId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
