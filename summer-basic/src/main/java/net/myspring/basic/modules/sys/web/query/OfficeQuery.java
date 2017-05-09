package net.myspring.basic.modules.sys.web.query;

import com.google.common.collect.Lists;
import net.myspring.basic.common.query.BaseQuery;

import java.util.List;

/**
 * Created by lihx on 2017/4/7.
 */
public class OfficeQuery extends BaseQuery {
    private String id;
    private List<String> officeIds= Lists.newArrayList();
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getOfficeIds() {
        return officeIds;
    }

    public void setOfficeIds(List<String> officeIds) {
        this.officeIds = officeIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
