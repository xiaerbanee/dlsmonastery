package net.myspring.common.dto;

import com.google.common.collect.Lists;
import net.myspring.common.domain.DataEntity;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Transient;
import java.util.List;

/**
 * Created by liuj on 2016-07-25.
 */
public class TreeDto<T> extends DataDto<T> {
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
