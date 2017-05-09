package net.myspring.general.modules.sys.web.query;

import net.myspring.general.common.query.BaseQuery;

/**
 * Created by lihx on 2017/4/7.
 */
public class ProcessTypeQuery extends BaseQuery {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
