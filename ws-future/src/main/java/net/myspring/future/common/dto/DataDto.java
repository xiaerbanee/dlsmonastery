package net.myspring.future.common.dto;

import net.myspring.util.cahe.annotation.CacheInput;

import java.time.LocalDateTime;

/**
 * Created by liuj on 2017/4/7.
 */
public class DataDto<T> extends IdDto<T> {
    protected String createdBy;
    @CacheInput(inputKey = "accounts",inputInstance = "createdBy",outputInstance = "loginName")
    protected String createdByName;
    protected LocalDateTime createdDate;
    protected String lastModifiedBy;
    @CacheInput(inputKey = "accounts",inputInstance = "lastModifiedBy",outputInstance = "loginName")
    protected String lastModifiedByName;
    protected LocalDateTime lastModifiedDate;
    protected String remarks;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getLastModifiedByName() {
        return lastModifiedByName;
    }

    public void setLastModifiedByName(String lastModifiedByName) {
        this.lastModifiedByName = lastModifiedByName;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
