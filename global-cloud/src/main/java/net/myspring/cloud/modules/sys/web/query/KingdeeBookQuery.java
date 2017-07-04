package net.myspring.cloud.modules.sys.web.query;

import net.myspring.cloud.common.query.BaseQuery;

import java.util.List;

/**
 * Created by lihx on 2017/4/12.
 */
public class KingdeeBookQuery extends BaseQuery {
    private String name;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
