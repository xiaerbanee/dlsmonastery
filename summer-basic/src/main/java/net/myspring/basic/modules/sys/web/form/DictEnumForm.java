package net.myspring.basic.modules.sys.web.form;

import net.myspring.basic.common.form.DataForm;
import net.myspring.basic.modules.sys.domain.DictEnum;
import net.myspring.mybatis.form.BaseForm;

/**
 * Created by admin on 2017/4/5.
 */

public class DictEnumForm extends DataForm<DictEnum> {
    private String id;
    private Integer sort;
    private String category;
    private String value;

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
