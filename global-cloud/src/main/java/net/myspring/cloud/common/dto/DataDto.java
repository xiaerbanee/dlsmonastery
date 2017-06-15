package net.myspring.cloud.common.dto;

import net.myspring.common.dto.IdDto;

import java.time.LocalDateTime;

/**
 * Created by lihx on 2017/6/15.
 */
public class  DataDto<T> extends IdDto<T> {
    protected String createdBy;
    protected LocalDateTime createdDate;
    protected String lastModifiedBy;
    protected LocalDateTime lastModifiedDate;
    protected String remarks;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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
