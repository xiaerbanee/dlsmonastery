package net.myspring.common.domain;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Transient;
import java.util.List;

/**
 * Created by liuj on 2016-07-25.
 */
public class TreeEntity<T> extends DataEntity<T> {
    @Transient
    private T parent;
    private String parentId;
    private String parentIds;
    private List<T> childList = Lists.newArrayList();
    @Transient
    private String levelName;

    public T getParent() {
        return parent;
    }

    public void setParent(T parent) {
        this.parent = parent;
    }

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

    public List<T> getChildList() {
        return childList;
    }

    public void setChildList(List<T> childList) {
        this.childList = childList;
    }

    public List<String> getParentIdList() {
        List<String> parentIdList = Lists.newArrayList();
        if(StringUtils.isNotBlank(parentIds)) {
            for(String parentId:parentIds.split(",")) {
                parentIdList.add(parentId);
            }
        }
        return parentIdList;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
}
