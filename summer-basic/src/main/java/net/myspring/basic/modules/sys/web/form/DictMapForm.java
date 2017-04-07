package net.myspring.basic.modules.sys.web.form;

import net.myspring.basic.modules.sys.domain.DictMap;
import net.myspring.mybatis.annotation.FormDomain;

/**
 * Created by admin on 2017/4/1.
 */
@FormDomain(DictMap.class)
public class DictMapForm{

    private String category;
    private String name;
    private String value;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
