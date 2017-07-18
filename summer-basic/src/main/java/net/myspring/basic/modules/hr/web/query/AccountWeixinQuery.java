package net.myspring.basic.modules.hr.web.query;

import net.myspring.basic.common.query.BaseQuery;

/**
 * Created by wangzm on 2017/7/18.
 */
public class AccountWeixinQuery extends BaseQuery {
    private String accountName;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
