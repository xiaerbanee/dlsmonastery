package net.myspring.basic.modules.hr.domain;

import net.myspring.basic.common.domain.CompanyEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by wangzm on 2017/5/3.
 */
@Entity
@Table(name = "hr_account_permission")
public class AccountPermission extends CompanyEntity<AccountPermission>{

    private String accountId;
    private String permissionId;

    public AccountPermission(){}

    public AccountPermission(String accountId,String permissionId){
        this.accountId=accountId;
        this.permissionId=permissionId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }
}
