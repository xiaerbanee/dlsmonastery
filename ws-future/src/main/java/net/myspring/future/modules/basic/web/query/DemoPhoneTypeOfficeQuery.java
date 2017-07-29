package net.myspring.future.modules.basic.web.query;

import net.myspring.future.common.query.BaseQuery;

/**
 * Created by zhangyf on 2017/6/8.
 */
public class DemoPhoneTypeOfficeQuery extends BaseQuery{

    private String areaId;
    private String demoPhoneTypeId;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getDemoPhoneTypeId() {
        return demoPhoneTypeId;
    }

    public void setDemoPhoneTypeId(String demoPhoneTypeId) {
        this.demoPhoneTypeId = demoPhoneTypeId;
    }
}
