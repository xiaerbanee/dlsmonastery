package net.myspring.basic.modules.hr.dto;

import net.myspring.basic.modules.hr.domain.AccountFavorite;
import net.myspring.common.dto.DataDto;
import net.myspring.util.cahe.annotation.CacheInput;

public class AccountFavoriteDto extends DataDto<AccountFavorite> {
    private String name;
    @CacheInput(inputKey = "accounts",inputInstance = "accountId",outputInstance = "name")
    private String accountName;
    private String parentId;
    private String accountId;
    private String parentName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
