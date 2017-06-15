package net.myspring.future.modules.basic.web.query;

import net.myspring.future.common.query.BaseQuery;

public class EmployeePhoneQuery extends BaseQuery {
    private String depotName;
    private String status;

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
