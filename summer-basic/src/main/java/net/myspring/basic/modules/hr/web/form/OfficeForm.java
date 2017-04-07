package net.myspring.basic.modules.hr.web.form;


import net.myspring.basic.modules.hr.domain.Office;
import net.myspring.mybatis.form.BaseForm;

/**
 * Created by admin on 2017/4/6.
 */

public class OfficeForm implements BaseForm<Office> {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
