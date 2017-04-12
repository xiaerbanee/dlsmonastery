package net.myspring.basic.modules.sys.web.form;

import net.myspring.basic.modules.sys.domain.Company;
import net.myspring.mybatis.form.BaseForm;

/**
 * Created by wangzm on 2017/4/12.
 */
public class CompanyForm implements BaseForm<Company> {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
