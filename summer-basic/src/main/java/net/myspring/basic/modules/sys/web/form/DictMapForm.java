package net.myspring.basic.modules.sys.web.form;

import net.myspring.basic.modules.sys.domain.DictMap;

/**
 * Created by admin on 2017/4/1.
 */
public class DictMapForm extends DictMap{

    private String category;
    private String name;
    private String value;

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }
}
