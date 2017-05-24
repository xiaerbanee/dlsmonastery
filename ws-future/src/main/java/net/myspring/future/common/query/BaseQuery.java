package net.myspring.future.common.query;


import net.myspring.future.common.utils.RequestUtils;

import java.util.List;

/**
 * Created by liuj on 2017/5/9.
 */
public class BaseQuery {
    private String companyId;

    private List<String> officeIdList;

    private List<String> depotIdList;

    public String getCompanyId() {
        companyId = RequestUtils.getCompanyId();
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public List<String> getOfficeIdList() {
        return officeIdList;
    }

    public void setOfficeIdList(List<String> officeIdList) {
        this.officeIdList = officeIdList;
    }

    public List<String> getDepotIdList() {
        return depotIdList;
    }

    public void setDepotIdList(List<String> depotIdList) {
        this.depotIdList = depotIdList;
    }
}
