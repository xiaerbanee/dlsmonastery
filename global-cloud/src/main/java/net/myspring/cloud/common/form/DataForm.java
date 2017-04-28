package net.myspring.cloud.common.form;

import net.myspring.cloud.common.utils.SecurityUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

/**
 * Created by liuj on 2017/4/12.
 */
public class DataForm<T> extends IdForm<T> {
    @JsonIgnore
    protected String lastModifiedBy;
    @JsonIgnore
    protected LocalDateTime lastModifiedDate;

    protected String remarks;

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

    public String getCompanyId() {
        return SecurityUtils.getCompanyId();
    }
}
