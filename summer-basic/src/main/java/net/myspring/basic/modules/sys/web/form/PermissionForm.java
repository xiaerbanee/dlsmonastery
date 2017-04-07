package net.myspring.basic.modules.sys.web.form;


import net.myspring.basic.modules.sys.domain.Permission;
import net.myspring.mybatis.form.BaseForm;

import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */

public class PermissionForm extends BaseForm<Permission> {
    private String id;
    private List<String> positionIdList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getPositionIdList() {
        return positionIdList;
    }

    public void setPositionIdList(List<String> positionIdList) {
        this.positionIdList = positionIdList;
    }
}
