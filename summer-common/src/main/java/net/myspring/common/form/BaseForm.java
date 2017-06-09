package net.myspring.common.form;

import com.google.common.collect.Maps;
import net.myspring.util.text.StringUtils;

import java.util.Map;

/**
 * Created by liuj on 2017/4/12.
 */
public class BaseForm<T> {
    private String id;
    protected String remarks;

    private Map<String,Object> extra = Maps.newHashMap();

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

    public Map<String, Object> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
    }
}
