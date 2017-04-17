package net.myspring.basic.modules.sys.web.form;

import net.myspring.basic.common.form.DataForm;
import net.myspring.basic.modules.sys.domain.CompanyConfig;

/**
 * Created by zhucc on 2017/4/17.
 */
public class CompanyConfigForm extends DataForm<CompanyConfig>{
    private String name;
    private String code;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
