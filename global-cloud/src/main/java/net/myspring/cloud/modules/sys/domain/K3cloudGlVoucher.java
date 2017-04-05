package net.myspring.cloud.modules.sys.domain;


import com.google.common.collect.Lists;
import net.myspring.cloud.modules.sys.domain.Company;
import net.myspring.common.domain.DataEntity;
import net.myspring.util.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name="api_k3cloud_gl_voucher")
public class K3cloudGlVoucher extends DataEntity<K3cloudGlVoucher> {
    //业务日期
    private LocalDate fdate;
    private String createdName;
    private Integer version = 0;
    private String status;
    //返回的单号
    private String outCode;
    private Company company;
    private String companyId ;
    //明细
    private List<K3cloudGlVoucherEntry> k3cloudGlVoucherEntryList = Lists.newArrayList();
    private List<String> k3cloudGlVoucherEntryIdList = Lists.newArrayList();

    @Transient
    private Boolean deletable=false;
    @Transient
    private Boolean editable=false;

    public LocalDate getFdate() {
        return fdate;
    }

    public void setFdate(LocalDate fdate) {
        this.fdate = fdate;
    }

    public String getCreatedName() {
        return createdName;
    }

    public void setCreatedName(String createdName) {
        this.createdName = createdName;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOutCode() {
        return outCode;
    }

    public void setOutCode(String outCode) {
        this.outCode = outCode;
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

    public List<K3cloudGlVoucherEntry> getK3cloudGlVoucherEntryList() {
        return k3cloudGlVoucherEntryList;
    }

    public void setK3cloudGlVoucherEntryList(List<K3cloudGlVoucherEntry> k3cloudGlVoucherEntryList) {
        this.k3cloudGlVoucherEntryList = k3cloudGlVoucherEntryList;
    }

    public List<String> getK3cloudGlVoucherEntryIdList() {
        if(CollectionUtil.isEmpty(k3cloudGlVoucherEntryIdList) && CollectionUtil.isNotEmpty(k3cloudGlVoucherEntryList)) {
            k3cloudGlVoucherEntryIdList = CollectionUtil.extractToList(k3cloudGlVoucherEntryList,"id");
        }
        return k3cloudGlVoucherEntryIdList;
    }

    public void setK3cloudGlVoucherEntryIdList(List<String> k3cloudGlVoucherEntryIdList) {
        this.k3cloudGlVoucherEntryIdList = k3cloudGlVoucherEntryIdList;
    }

    public Boolean getDeletable() {
        return deletable;
    }

    public void setDeletable(Boolean deletable) {
        this.deletable = deletable;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }
}
