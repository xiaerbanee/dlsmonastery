package net.myspring.future.modules.basic.web.form;

import com.google.common.collect.Lists;
import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.basic.domain.DepotStore;

import java.util.List;

/**
 * Created by lihx on 2017/4/18.
 */
public class DepotStoreForm extends BaseForm<DepotStore> {

    private String depotId;
    private DepotForm depotForm;
    private String type;
    private String storeGroup;

    public String getStoreGroup() {
        return storeGroup;
    }

    public void setStoreGroup(String storeGroup) {
        this.storeGroup = storeGroup;
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

    public DepotForm getDepotForm() {
        return depotForm;
    }

    public void setDepotForm(DepotForm depotForm) {
        this.depotForm = depotForm;
    }
}
