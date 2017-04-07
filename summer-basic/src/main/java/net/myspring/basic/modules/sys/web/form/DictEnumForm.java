package net.myspring.basic.modules.sys.web.form;

import net.myspring.basic.modules.sys.domain.DictEnum;
import net.myspring.mybatis.form.BaseForm;

/**
 * Created by admin on 2017/4/5.
 */

public class DictEnumForm extends BaseForm<DictEnum>{
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
