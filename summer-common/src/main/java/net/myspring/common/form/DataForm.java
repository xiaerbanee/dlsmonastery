package net.myspring.common.form;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

/**
 * Created by liuj on 2017/4/12.
 */
public class DataForm<T> extends IdForm<T> {

    protected String remarks;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
