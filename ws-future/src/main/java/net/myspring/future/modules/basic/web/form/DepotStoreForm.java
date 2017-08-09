package net.myspring.future.modules.basic.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.common.enums.DepotStoreTypeEnum;
import net.myspring.future.modules.basic.domain.DepotStore;

/**
 * Created by lihx on 2017/4/18.
 */
public class DepotStoreForm extends BaseForm<DepotStore> {

    private String depotId;
    private String officeId;
    //是否是广告仓库
    private Boolean popShop = false;
    private String type = DepotStoreTypeEnum.代理库.name();
    private String jointLevel;

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public Boolean getPopShop() {
        return popShop;
    }

    public void setPopShop(Boolean popShop) {
        this.popShop = popShop;
    }

    public String getJointLevel() {
        return jointLevel;
    }

    public void setJointLevel(String jointLevel) {
        this.jointLevel = jointLevel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

}
