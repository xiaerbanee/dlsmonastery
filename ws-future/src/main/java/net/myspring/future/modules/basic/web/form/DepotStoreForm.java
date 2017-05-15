package net.myspring.future.modules.basic.web.form;

import net.myspring.common.form.DataForm;
import net.myspring.future.modules.basic.domain.Depot;

import java.math.BigDecimal;

/**
 * Created by lihx on 2017/4/18.
 */
public class DepotStoreForm extends DataForm<Depot>{
    private String depotId;

    private DepotForm depotForm;

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
