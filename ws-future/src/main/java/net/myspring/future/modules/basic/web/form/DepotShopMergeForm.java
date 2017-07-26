package net.myspring.future.modules.basic.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.basic.domain.DepotShop;

public class DepotShopMergeForm extends BaseForm<DepotShop>{

    private String fromDepotId;
    private String toDepotId;

    public String getFromDepotId() {
        return fromDepotId;
    }

    public void setFromDepotId(String fromDepotId) {
        this.fromDepotId = fromDepotId;
    }

    public String getToDepotId() {
        return toDepotId;
    }

    public void setToDepotId(String toDepotId) {
        this.toDepotId = toDepotId;
    }
}
