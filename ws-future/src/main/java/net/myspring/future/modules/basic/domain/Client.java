package net.myspring.future.modules.basic.domain;


import net.myspring.future.common.domain.CompanyEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name="crm_client")
public class Client extends CompanyEntity<Client> {
    private String name;
    private String mobilePhone;
    private Integer version = 0;
    // 财务编号
    private String outId;
    //财务分组
    private Long outGroupId;
    //财务分组名称
    private String outGroupName;
    //财务同步日期
    private LocalDate outDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }

    public Long getOutGroupId() {
        return outGroupId;
    }

    public void setOutGroupId(Long outGroupId) {
        this.outGroupId = outGroupId;
    }

    public String getOutGroupName() {
        return outGroupName;
    }

    public void setOutGroupName(String outGroupName) {
        this.outGroupName = outGroupName;
    }

    public LocalDate getOutDate() {
        return outDate;
    }

    public void setOutDate(LocalDate outDate) {
        this.outDate = outDate;
    }
}
