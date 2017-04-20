package net.myspring.common.domain;

import javax.persistence.Column;

/**
 * Created by liuj on 2017/4/12.
 */
public class CompanyEntity<T> extends DataEntity<T> {

    @Column(updatable = false)
    private String companyId="1";

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

}
