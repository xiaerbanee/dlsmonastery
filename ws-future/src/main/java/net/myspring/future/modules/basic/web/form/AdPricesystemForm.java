package net.myspring.future.modules.basic.web.form;

import com.google.common.collect.Lists;
import net.myspring.common.form.DataForm;
import net.myspring.future.modules.basic.domain.AdPricesystem;

import java.util.List;

/**
 * Created by lihx on 2017/4/17.
 */
public class AdPricesystemForm extends DataForm<AdPricesystem> {
    private String name;
    private Boolean enabled;
    private List<String> pageIds = Lists.newArrayList();
    private List<String> newDepotIds = Lists.newArrayList();

    public List<String> getNewDepotIds() {
        return newDepotIds;
    }

    public void setNewDepotIds(List<String> newDepotIds) {
        this.newDepotIds = newDepotIds;
    }

    public List<String> getPageIds() {
        return pageIds;
    }

    public void setPageIds(List<String> pageIds) {
        this.pageIds = pageIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
