package net.myspring.basic.modules.sys.web.form;

import net.myspring.basic.modules.sys.domain.DictMap;
import net.myspring.mybatis.form.BaseForm;

/**
 * Created by admin on 2017/4/1.
 */

public class DictMapForm extends BaseForm<DictMap>{
    private String id;
    private String category;
    private String name;
    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
