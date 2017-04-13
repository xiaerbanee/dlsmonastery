package net.myspring.basic.common.form;

import java.time.LocalDateTime;

/**
 * Created by liuj on 2017/4/12.
 */
public class DataForm<T> extends IdForm<T> {
    protected String lastModifiedBy;
    protected LocalDateTime lastModifiedDate;

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
}
