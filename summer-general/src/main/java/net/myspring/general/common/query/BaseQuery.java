package net.myspring.general.common.query;


import net.myspring.general.common.utils.RequestUtils;

/**
 * Created by liuj on 2017/5/9.
 */
public class BaseQuery {
    private String companyId;

    public String getCompanyId() {
        companyId = RequestUtils.getCompanyId();
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
