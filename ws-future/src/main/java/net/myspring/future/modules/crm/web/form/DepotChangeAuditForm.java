package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.crm.domain.DepotChange;

/**
 * Created by zhangyf on 2017/6/14.
 */
public class DepotChangeAuditForm extends BaseForm<DepotChange> {
    private Boolean pass;
    private String auditRemarks;

    public Boolean getPass() {
        return pass;
    }

    public void setPass(Boolean pass) {
        this.pass = pass;
    }

    public String getAuditRemarks() {
        return auditRemarks;
    }

    public void setAuditRemarks(String auditRemarks) {
        this.auditRemarks = auditRemarks;
    }
}
