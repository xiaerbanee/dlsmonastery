package net.myspring.tool.common.domain;


import net.myspring.tool.common.utils.RequestUtils;

import javax.persistence.MappedSuperclass;

/**
 * Created by liuj on 2017/4/12.
 */
@MappedSuperclass
public class CompanyEntity<T> extends DataEntity<T> {
    private String companyName = RequestUtils.getCompanyName();

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}

