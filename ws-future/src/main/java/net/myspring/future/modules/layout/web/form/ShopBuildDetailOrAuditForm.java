package net.myspring.future.modules.layout.web.form;

import net.myspring.common.form.DataForm;
import net.myspring.future.modules.layout.domain.ShopBuild;
import net.myspring.future.modules.layout.dto.ShopBuildDto;

import java.util.List;

/**
 * Created by zhangyf on 2017/5/18.
 */
public class ShopBuildDetailOrAuditForm extends DataForm<ShopBuild>{

    private Boolean pass;

    private String passRemarks;

    private List<String> ids;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

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
