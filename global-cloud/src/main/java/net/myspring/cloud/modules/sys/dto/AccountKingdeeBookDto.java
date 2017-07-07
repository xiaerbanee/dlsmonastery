package net.myspring.cloud.modules.sys.dto;

import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.common.dto.DataDto;

/**
 * Created by lihx on 2017/5/9.
 */
public class AccountKingdeeBookDto extends DataDto<AccountKingdeeBook> {
    private String accountId;
    private String username;
    private String kingdeeBookName;
    private String kingdeeBookType;
    private String companyName;
    private String remarks;

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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
