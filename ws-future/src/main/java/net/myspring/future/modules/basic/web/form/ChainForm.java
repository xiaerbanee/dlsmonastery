package net.myspring.future.modules.basic.web.form;

import net.myspring.future.common.form.DataForm;
import net.myspring.future.modules.basic.domain.Chain;

/**
 * Created by lihx on 2017/4/18.
 */
public class ChainForm extends DataForm<Chain> {
    private String name;
    private String type;
    private String areaType;
    private String officeId;
    private String adPricesystemId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getAdPricesystemId() {
        return adPricesystemId;
    }

    public void setAdPricesystemId(String adPricesystemId) {
        this.adPricesystemId = adPricesystemId;
    }
}
