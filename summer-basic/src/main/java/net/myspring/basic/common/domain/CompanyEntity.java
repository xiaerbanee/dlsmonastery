package net.myspring.basic.common.domain;

import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.common.domain.DataEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Created by liuj on 2017/4/12.
 */
@MappedSuperclass
public class CompanyEntity<T> extends DataEntity<T> {

    @Column(updatable = false)
    private String companyId;

    public String getCompanyId() {
        if(StringUtils.isBlank(companyId) && StringUtils.isBlank(id)) {
            companyId = RequestUtils.getCompanyId();
        }
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

}
