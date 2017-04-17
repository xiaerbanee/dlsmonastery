package net.myspring.basic.common.form;

import net.myspring.mybatis.form.BaseForm;
import org.apache.commons.lang.StringUtils;

/**
 * Created by liuj on 2017/4/12.
 */
public class IdForm<T> extends BaseForm<T> {
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
