package net.myspring.basic.modules.sys.web.query;

import net.myspring.basic.common.query.BaseQuery;

/**
 * Created by lihx on 2017/4/7.
 */
public class MenuCategoryQuery extends BaseQuery {
    private String name;
    private  String sort;

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
}
