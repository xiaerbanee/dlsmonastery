package net.myspring.basic.modules.sys.web.query;

import net.myspring.basic.common.query.BaseQuery;

/**
 * Created by wangzm on 2017/5/2.
 */
public class RoleQuery  extends BaseQuery {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
