package net.myspring.general.common.domain;

import javax.persistence.MappedSuperclass;

/**
 * Created by liuj on 2016-07-25.
 */
@MappedSuperclass
public class TreeEntity<T> extends DataEntity<T> {
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
