package net.myspring.basic.modules.sys.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.myspring.basic.common.domain.DataEntity;

@Entity
@Table(name="sys_role_module")
public class RoleModule extends DataEntity<RoleModule> {
    private Integer version = 0;
    private String roleId;
    private String backendModuleId;

    public RoleModule(){};

    public RoleModule(String roleId, String backendModuleId){
        this.roleId=roleId;
        this.backendModuleId=backendModuleId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getBackendModuleId() {
        return backendModuleId;
    }

    public void setBackendModuleId(String backendModuleId) {
        this.backendModuleId = backendModuleId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
