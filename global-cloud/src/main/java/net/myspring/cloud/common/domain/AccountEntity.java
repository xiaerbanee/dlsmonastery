package net.myspring.cloud.common.domain;

import net.myspring.cloud.common.utils.RequestUtils;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

/**
 * Created by lihx on 2017/6/3.
 */
@MappedSuperclass
public class AccountEntity<T> extends DataEntity<T>{
    @Column(updatable = false)
    private String accountId;
    @Column(updatable = false)
    private String companyId;
    @PrePersist
    public void prePersist() {
        this.accountId = RequestUtils.getAccountId();
        this.companyId = RequestUtils.getCompanyId();
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
