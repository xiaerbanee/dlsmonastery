package net.myspring.common.domain;

import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by liuj on 2016-08-24.
 */
public class SearchEntity implements Serializable {
    private Pageable pageable;
    private Map<String,Object> params;

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

}
