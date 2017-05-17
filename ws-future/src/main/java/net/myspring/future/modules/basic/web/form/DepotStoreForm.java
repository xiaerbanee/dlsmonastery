package net.myspring.future.modules.basic.web.form;

import com.google.common.collect.Lists;
import net.myspring.common.form.DataForm;
import net.myspring.future.modules.basic.domain.Depot;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lihx on 2017/4/18.
 */
public class DepotStoreForm extends DataForm<Depot>{

    private String depotId;
    private DepotForm depotForm;
    private String type;
    private String group;
    private List<String> depotStoreTypeList= Lists.newArrayList();

    public List<String> getDepotStoreTypeList() {
        return depotStoreTypeList;
    }

    public void setDepotStoreTypeList(List<String> depotStoreTypeList) {
        this.depotStoreTypeList = depotStoreTypeList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
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
