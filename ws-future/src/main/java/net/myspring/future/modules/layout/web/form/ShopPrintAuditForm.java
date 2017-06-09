package net.myspring.future.modules.layout.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.layout.domain.ShopPrint;

/**
 * Created by zhangyf on 2017/6/8.
 */
public class ShopPrintAuditForm extends BaseForm<ShopPrint>{

    private Boolean pass = false;

    private String passRemarks;

    public Boolean getPass() {
        return pass;
    }

    public void setPass(Boolean pass) {
        this.pass = pass;
    }

    public String getPassRemarks() {
        return passRemarks;
    }

    public void setPassRemarks(String passRemarks) {
        this.passRemarks = passRemarks;
    }
}
