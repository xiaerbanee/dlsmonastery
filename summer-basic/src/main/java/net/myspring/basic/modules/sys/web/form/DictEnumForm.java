package net.myspring.basic.modules.sys.web.form;

import com.google.common.collect.Lists;
import net.myspring.common.form.BaseForm;
import net.myspring.basic.modules.sys.domain.DictEnum;

import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */

public class DictEnumForm extends BaseForm<DictEnum> {
    private Integer sort;
    private String category;
    private String value;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

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
}
