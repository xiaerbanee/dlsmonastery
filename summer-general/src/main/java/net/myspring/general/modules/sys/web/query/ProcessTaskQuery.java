package net.myspring.general.modules.sys.web.query;

import com.google.common.collect.Lists;
import net.myspring.general.common.query.BaseQuery;

import java.util.List;

/**
 * Created by wangzm on 2017/6/5.
 */
public class ProcessTaskQuery extends BaseQuery{

    private String name;
    private String status;
    private List<String> positionIdList=Lists.newArrayList();
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

    public List<String> getPositionIdList() {
        return positionIdList;
    }

    public void setPositionIdList(List<String> positionIdList) {
        this.positionIdList = positionIdList;
    }

    public List<String> getOfficeIds() {
        return officeIds;
    }

    public void setOfficeIds(List<String> officeIds) {
        this.officeIds = officeIds;
    }
}
