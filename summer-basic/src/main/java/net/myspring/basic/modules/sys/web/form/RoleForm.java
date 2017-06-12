package net.myspring.basic.modules.sys.web.form;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.sys.domain.Role;
import net.myspring.common.form.BaseForm;
import net.myspring.common.tree.TreeNode;

import java.util.List;

/**
 * Created by wangzm on 2017/5/2.
 */
public class RoleForm extends BaseForm<Role> {
    private String name;
    private String permission;
    private List<String> permissionIdList= Lists.newArrayList();
    private List<String> moduleIdList=Lists.newArrayList();

    public List<String> getModuleIdList() {
        return moduleIdList;
    }

    public void setModuleIdList(List<String> moduleIdList) {
        this.moduleIdList = moduleIdList;
    }

    public List<String> getPermissionIdList() {
        return permissionIdList;
    }

    public void setPermissionIdList(List<String> permissionIdList) {
        this.permissionIdList = permissionIdList;
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

    @Override
    public String getRemarks() {
        return remarks;
    }

    @Override
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
