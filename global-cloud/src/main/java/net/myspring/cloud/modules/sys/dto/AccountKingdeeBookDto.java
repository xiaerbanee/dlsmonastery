package net.myspring.cloud.modules.sys.dto;

import net.myspring.cloud.common.domain.CompanyEntity;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.common.dto.DataDto;

/**
 * Created by lihx on 2017/5/9.
 */
public class AccountKingdeeBookDto extends CompanyEntity<AccountKingdeeBook> {
    private String accountId;
    private String username;
    private String password;
    private String kingdeeBookId;
    private String kingdeeBookName;
    private String kingdeeBookType;

    public String getKingdeeBookType() {
        return kingdeeBookType;
    }

    public void setKingdeeBookType(String kingdeeBookType) {
        this.kingdeeBookType = kingdeeBookType;
    }

    public String getKingdeeBookName() {
        return kingdeeBookName;
    }

    public void setKingdeeBookName(String kingdeeBookName) {
        this.kingdeeBookName = kingdeeBookName;
    }

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
