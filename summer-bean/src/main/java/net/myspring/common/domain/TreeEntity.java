package net.myspring.common.domain;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Transient;
import java.util.List;

/**
 * Created by liuj on 2016-07-25.
 */
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

    public List<String> getParentIdList() {
        List<String> parentIdList = Lists.newArrayList();
        if(StringUtils.isNotBlank(parentIds)) {
            for(String parentId:parentIds.split(",")) {
                parentIdList.add(parentId);
            }
        }
        return parentIdList;
    }
}
