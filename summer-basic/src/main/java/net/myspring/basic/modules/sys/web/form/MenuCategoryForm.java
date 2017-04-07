package net.myspring.basic.modules.sys.web.form;

import net.myspring.basic.modules.sys.domain.MenuCategory;
import net.myspring.mybatis.form.BaseForm;

/**
 * Created by admin on 2017/4/6.
 */

public class MenuCategoryForm implements BaseForm<MenuCategory> {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
