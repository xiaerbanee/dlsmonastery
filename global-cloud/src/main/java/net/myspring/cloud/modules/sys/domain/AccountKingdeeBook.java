package net.myspring.cloud.modules.sys.domain;

import net.myspring.cloud.common.domain.CompanyEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by liuj on 2017/5/8.
 */

@Entity
@Table(name="sys_account_kingdee_book")
public class AccountKingdeeBook extends CompanyEntity<AccountKingdeeBook> {
    private String accountId;
    private String username;
    private String password;
    private String kingdeeBookId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKingdeeBookId() {
        return kingdeeBookId;
    }

    public void setKingdeeBookId(String kingdeeBookId) {
        this.kingdeeBookId = kingdeeBookId;
    }
}
