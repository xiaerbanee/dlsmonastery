package net.myspring.common.dto;

import net.myspring.util.text.StringUtils;

import java.io.Serializable;

/**
 * Created by liuj on 2017/4/7.
 */
public class IdDto<T> implements Serializable {
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
