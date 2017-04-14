package net.myspring.basic.modules.sys.web.form;

import net.myspring.basic.modules.sys.domain.MenuCategory;
import net.myspring.basic.common.form.DataForm;

/**
 * Created by admin on 2017/4/6.
 */

public class MenuCategoryForm extends DataForm<MenuCategory> {

    private String name;
    private String sort;
    private String remarks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
