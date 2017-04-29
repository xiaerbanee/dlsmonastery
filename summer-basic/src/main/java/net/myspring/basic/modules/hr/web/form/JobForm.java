package net.myspring.basic.modules.hr.web.form;


import net.myspring.basic.modules.hr.domain.Job;
import net.myspring.common.form.DataForm;

/**
 * Created by admin on 2017/4/5.
 */

public class JobForm extends DataForm<Job> {

    private String name;
    private String permission;
    private String remarks;

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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
