package net.myspring.cloud.modules.sys.domain;

import net.myspring.cloud.common.domain.AccountEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="sys_account_kingdee_book")
public class AccountKingdeeBook extends AccountEntity<AccountKingdeeBook> {
    private String username;
    private String password;
    private String kingdeeBookId;
    private Integer  version = 0;

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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
