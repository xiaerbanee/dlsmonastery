package net.myspring.basic.common.domain;

import net.myspring.basic.common.utils.RequestUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

/**
 * Created by liuj on 2017/4/12.
 */
@MappedSuperclass
@FilterDef(name = "companyFilter", parameters = { @ParamDef(name = "companyId", type = "string") })
@Filters({ @Filter(name = "companyFilter", condition = "company_id =:companyId") })
public class CompanyEntity<T> extends DataEntity<T> {

    @Column(updatable = false)
    private String companyId;

    @PrePersist
    public void prePersist() {
        this.companyId = RequestUtils.getCompanyId();
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

}
