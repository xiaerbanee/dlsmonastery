package net.myspring.basic.modules.sys.domain;

import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by wangzm on 2017/5/2.
 */
@Entity
@Table(name="sys_role_permission")
public class RolePermission extends DataEntity<RolePermission>{

    private String roleId;
    private String permissionId;

    public RolePermission(){};
    public RolePermission(String roleId,String permissionId){
        this.roleId=roleId;
        this.permissionId=permissionId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }
}
