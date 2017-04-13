package net.myspring.cloud.common.form;

import net.myspring.mybatis.form.BaseForm;

/**
 * Created by liuj on 2017/4/12.
 */
public class IdForm<T> implements BaseForm<T> {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
