package net.myspring.basic.modules.hr.web.query;

import net.myspring.basic.common.query.BaseQuery;

public class AccountFavoriteQuery extends BaseQuery {
    private String accountId;
    private String name;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
