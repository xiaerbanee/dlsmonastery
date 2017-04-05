package net.myspring.cloud.modules.sys.domain;


import net.myspring.common.domain.DataEntity;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="api_cloud_product")
public class CloudProduct extends DataEntity<CloudProduct> {
    private String name;
    private String code;
    private BigDecimal price1;
    private String outId;
    private String returnOutId;
    private LocalDateTime outDate;
    private Integer version = 0;
    private Company company;
    private String companyId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getPrice1() {
        return price1;
    }

    public void setPrice1(BigDecimal price1) {
        this.price1 = price1;
    }

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }

    public String getReturnOutId() {
        return returnOutId;
    }

    public void setReturnOutId(String returnOutId) {
        this.returnOutId = returnOutId;
    }

    public LocalDateTime getOutDate() {
        return outDate;
    }

    public void setOutDate(LocalDateTime outDate) {
        this.outDate = outDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getCompanyId() {
        if(StringUtils.isBlank(companyId) && company!=null) {
            companyId = company.getId();
        }
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
