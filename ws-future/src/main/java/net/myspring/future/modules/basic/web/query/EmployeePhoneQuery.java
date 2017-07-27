package net.myspring.future.modules.basic.web.query;

import net.myspring.future.common.query.BaseQuery;

public class EmployeePhoneQuery extends BaseQuery {
    private String depotName;
    private String status;
    private String employeeId;

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

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}
