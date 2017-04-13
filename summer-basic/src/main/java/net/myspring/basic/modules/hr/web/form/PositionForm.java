package net.myspring.basic.modules.hr.web.form;


import net.myspring.basic.modules.hr.domain.Position;
import net.myspring.basic.common.form.DataForm;

import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */

public class PositionForm extends DataForm<Position> {
    private List<String> permissionIdList;

    public List<String> getPermissionIdList() {
        return permissionIdList;
    }

    public void setPermissionIdList(List<String> permissionIdList) {
        this.permissionIdList = permissionIdList;
    }
}
