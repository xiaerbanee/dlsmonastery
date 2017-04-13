package net.myspring.basic.modules.sys.web.form;


import net.myspring.basic.modules.sys.domain.Permission;
import net.myspring.basic.common.form.DataForm;

import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */

public class PermissionForm extends DataForm<Permission> {
    private List<String> positionIdList;

    public List<String> getPositionIdList() {
        return positionIdList;
    }

    public void setPositionIdList(List<String> positionIdList) {
        this.positionIdList = positionIdList;
    }
}
