package net.myspring.basic.modules.hr.domain;

import net.myspring.basic.common.domain.TreeEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="hr_account_favorite")
public class AccountFavorite extends TreeEntity<AccountFavorite> {
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
