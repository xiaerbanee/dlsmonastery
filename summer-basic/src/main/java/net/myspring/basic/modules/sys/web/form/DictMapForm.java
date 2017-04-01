package net.myspring.basic.modules.sys.web.form;

import net.myspring.basic.modules.sys.domain.DictMap;

/**
 * Created by admin on 2017/4/1.
 */
public class DictMapForm extends DictMap{

    private String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
