package net.myspring.basic.modules.hr.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.basic.modules.hr.domain.Position;
import net.myspring.util.cahe.annotation.CacheInput;

/**
 * Created by admin on 2017/4/5.
 */
public class PositionDto extends DataDto<Position> {
    @CacheInput(inputKey = "roles",inputInstance = "roleId",outputInstance = "name")
    protected String roleName;
    private String name;
    private String permission;
    private boolean locked;
    private String roleId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

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

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

}
