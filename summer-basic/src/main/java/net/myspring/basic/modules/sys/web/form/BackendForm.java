package net.myspring.basic.modules.sys.web.form;

import net.myspring.basic.common.form.DataForm;
import net.myspring.basic.modules.sys.domain.Backend;

/**
 * Created by wangzm on 2017/4/19.
 */
public class BackendForm extends DataForm<Backend> {
    private  String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
