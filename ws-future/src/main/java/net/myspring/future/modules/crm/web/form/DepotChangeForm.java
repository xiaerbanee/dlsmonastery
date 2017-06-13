package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.crm.domain.DepotChange;

import java.time.LocalDate;

/**
 * Created by zhangyf on 2017/6/13.
 */
public class DepotChangeForm extends BaseForm<DepotChange> {
    private String type;
    private LocalDate expiryDate;
    private String oldLabel;
    private String oldValue;
    private String newLabel;
    private String newValue;
    private String depotId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getOldLabel() {
        return oldLabel;
    }

    public void setOldLabel(String oldLabel) {
        this.oldLabel = oldLabel;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewLabel() {
        return newLabel;
    }

    public void setNewLabel(String newLabel) {
        this.newLabel = newLabel;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }
}
