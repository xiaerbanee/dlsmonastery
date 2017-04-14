package net.myspring.basic.common.form;

import net.myspring.basic.common.utils.SecurityUtils;

import java.time.LocalDateTime;

/**
 * Created by liuj on 2017/4/12.
 */
public class CompanyForm<T> extends DataForm<T> {
    private String companyId = SecurityUtils.getCompanyId();

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
