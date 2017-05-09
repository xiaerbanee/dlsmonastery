package net.myspring.basic.modules.hr.web.query;


import net.myspring.basic.common.query.BaseQuery;

import java.util.List;

/**
 * Created by liuj on 2017/3/19.
 */
public class AccountTaskQuery extends BaseQuery {
    private String positionId;
    private String status;
    private String name;
    private List<String> officeIds;

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getOfficeIds() {
        return officeIds;
    }

    public void setOfficeIds(List<String> officeIds) {
        this.officeIds = officeIds;
    }
}
