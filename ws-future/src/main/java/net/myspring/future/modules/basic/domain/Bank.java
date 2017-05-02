package net.myspring.future.modules.basic.domain;


import net.myspring.future.common.domain.CompanyEntity;
import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;


@Entity
@Table(name="crm_bank")
public class Bank extends CompanyEntity<Bank> {
    private String name;
    private String code;
    private String outId;
    private LocalDateTime outDate;
    private Integer version = 0;
    private String oldName;
    private String oldOutId;

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

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
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

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getOldOutId() {
        return oldOutId;
    }

    public void setOldOutId(String oldOutId) {
        this.oldOutId = oldOutId;
    }

}
