package net.myspring.basic.modules.sys.web.query;

import net.myspring.basic.common.query.BaseQuery;


public class OfficeQuery extends BaseQuery {

    private String id;
    private String name;
    private String officeRuleName;
    private String sort = "id,asc";

    public String getOfficeRuleName() {
        return officeRuleName;
    }

    public void setOfficeRuleName(String officeRuleName) {
        this.officeRuleName = officeRuleName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
