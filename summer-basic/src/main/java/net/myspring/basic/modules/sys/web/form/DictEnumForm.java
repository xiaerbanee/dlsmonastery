package net.myspring.basic.modules.sys.web.form;

import net.myspring.basic.modules.sys.domain.DictEnum;
import net.myspring.mybatis.annotation.FormDomain;

/**
 * Created by admin on 2017/4/5.
 */
@FormDomain(DictEnum.class)
public class DictEnumForm{
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
