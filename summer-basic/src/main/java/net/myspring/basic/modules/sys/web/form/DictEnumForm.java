package net.myspring.basic.modules.sys.web.form;

import com.google.common.collect.Lists;
import net.myspring.common.form.DataForm;
import net.myspring.basic.modules.sys.domain.DictEnum;

import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */

public class DictEnumForm extends DataForm<DictEnum> {
    private Integer sort;
    private String category;
    private String value;
    private List<String> categoryList= Lists.newArrayList();

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

    public List<String> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<String> categoryList) {
        this.categoryList = categoryList;
    }
}
