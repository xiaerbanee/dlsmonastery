package net.myspring.basic.modules.hr.domain;


import net.myspring.basic.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="hr_account_weixin")
public class AccountWeixin extends DataEntity<AccountWeixin> {
    private String accountId;
    private String openId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
