package net.myspring.general.modules.sys.web.query;

import com.google.common.collect.Lists;
import net.myspring.general.common.query.BaseQuery;

import java.util.List;

/**
 * Created by wangzm on 2017/6/5.
 */
public class ProcessTaskQuery extends BaseQuery{
    private String positionId;
    private String name;
    private String status;
    private List<String> officeIds= Lists.newArrayList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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
