package net.myspring.common.form;

import net.myspring.util.text.StringUtils;

/**
 * Created by liuj on 2017/4/12.
 */
public class BaseForm<T> {
    private String id;
    protected String remarks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean isCreate() {
        return StringUtils.isBlank(id);
    }
}
