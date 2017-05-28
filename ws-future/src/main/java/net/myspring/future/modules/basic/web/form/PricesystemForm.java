package net.myspring.future.modules.basic.web.form;

import com.google.common.collect.Lists;
import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.basic.domain.Pricesystem;

import java.util.List;

/**
 * Created by lihx on 2017/4/18.
 */
public class PricesystemForm extends BaseForm<Pricesystem> {
    private String name;
    private Integer sort;
    private boolean enabled;
    private List<PricesystemDetailForm> pricesystemDetailList= Lists.newArrayList();

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PricesystemDetailForm> getPricesystemDetailList() {
        return pricesystemDetailList;
    }

    public void setPricesystemDetailList(List<PricesystemDetailForm> pricesystemDetailList) {
        this.pricesystemDetailList = pricesystemDetailList;
    }
}
