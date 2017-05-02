package net.myspring.cloud.common.domain;

import com.ctc.wstx.util.StringUtil;
import net.myspring.cloud.common.utils.SecurityUtils;
import net.myspring.common.domain.DataEntity;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Column;

/**
 * Created by liuj on 2017/4/12.
 */
public class CompanyEntity<T> extends DataEntity<T> {

    @Column(updatable = false)
    private String companyId;

    public String getCompanyId() {
        if(StringUtils.isBlank(companyId) && StringUtils.isBlank(id)) {
            companyId = SecurityUtils.getCompanyId();
        }
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

}
