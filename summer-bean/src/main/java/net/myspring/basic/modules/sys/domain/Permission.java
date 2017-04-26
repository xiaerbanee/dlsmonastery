package net.myspring.basic.modules.sys.domain;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.hr.domain.Position;
import net.myspring.common.domain.CompanyEntity;
import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="sys_permission")
public class Permission extends CompanyEntity<Permission> {
    private String name;
    private String permission;
    private Integer version = 0;
    private String menuId;

    private String url;
    private String method;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
