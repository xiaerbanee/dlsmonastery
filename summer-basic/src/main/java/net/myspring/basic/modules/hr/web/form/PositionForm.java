package net.myspring.basic.modules.hr.web.form;


import net.myspring.basic.modules.hr.domain.Position;
import net.myspring.mybatis.form.BaseForm;

import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */

public class PositionForm implements BaseForm<Position> {
    private String id;
    private List<String> permissionIdList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getPermissionIdList() {
        return permissionIdList;
    }

    public void setPermissionIdList(List<String> permissionIdList) {
        this.permissionIdList = permissionIdList;
    }
}
