package net.myspring.common.form;

import net.myspring.util.text.StringUtils;

/**
 * Created by liuj on 2017/4/12.
 */
public class IdForm<T> {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean isCreate() {
        return StringUtils.isBlank(id);
    }
}
