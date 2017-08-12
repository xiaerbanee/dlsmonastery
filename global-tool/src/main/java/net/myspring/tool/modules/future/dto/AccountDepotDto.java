package net.myspring.tool.modules.future.dto;

import net.myspring.util.cahe.annotation.CacheInput;

public class AccountDepotDto {
    private String accountId;
    private String depotId;
    @CacheInput(inputKey = "accounts",inputInstance = "accountId",outputInstance = "employeeId")
    private String employeeId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}
