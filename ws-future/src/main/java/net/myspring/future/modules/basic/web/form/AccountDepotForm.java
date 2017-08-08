package net.myspring.future.modules.basic.web.form;

import com.google.common.collect.Lists;

import java.util.List;

public class AccountDepotForm {
    private String accountId;
    private List<String> depotIdList= Lists.newArrayList();

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public List<String> getDepotIdList() {
        return depotIdList;
    }

    public void setDepotIdList(List<String> depotIdList) {
        this.depotIdList = depotIdList;
    }
}
