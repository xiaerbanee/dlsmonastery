package net.myspring.common.domain;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Transient;
import java.util.List;

/**
 * Created by liuj on 2016-07-25.
 */
public class TreeEntity<T> extends CompanyEntity<T> {
    private String parentId;
    private String parentIds;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }
}
