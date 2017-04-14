package net.myspring.basic.common.form;

import net.myspring.basic.common.utils.SecurityUtils;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDateTime;

/**
 * Created by liuj on 2017/4/12.
 */
public class CompanyForm<T> extends DataForm<T> {
    private String companyId ;

    public String getCompanyId() {
        if(StringUtils.isBlank(companyId)) {
            companyId = SecurityUtils.getCompanyId();
        }
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
