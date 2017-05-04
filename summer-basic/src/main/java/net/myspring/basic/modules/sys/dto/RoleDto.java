package net.myspring.basic.modules.sys.dto;

import net.myspring.basic.modules.sys.domain.Role;
import net.myspring.common.dto.DataDto;
import net.myspring.util.cahe.annotation.CacheInput;

/**
 * Created by wangzm on 2017/5/2.
 */
public class RoleDto extends DataDto<Role> {

    private String name;
    private String permission;
    private boolean locked;

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
