package net.myspring.general.modules.sys.web.query;

import com.google.common.collect.Lists;
import net.myspring.general.common.query.BaseQuery;
import net.myspring.general.modules.sys.domain.ProcessTask;

import java.util.List;

/**
 * Created by wangzm on 2017/6/5.
 */
public class ProcessTaskQuery {
    private String positionId;
    private List<String> officeIds= Lists.newArrayList();

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public List<String> getOfficeIds() {
        return officeIds;
    }

    public void setOfficeIds(List<String> officeIds) {
        this.officeIds = officeIds;
    }
}
