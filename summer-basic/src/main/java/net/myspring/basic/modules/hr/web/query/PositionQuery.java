package net.myspring.basic.modules.hr.web.query;

import net.myspring.basic.common.query.BaseQuery;

/**
 * Created by lihx on 2017/4/7.
 */
public class PositionQuery extends BaseQuery {
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
