package net.myspring.future.modules.basic.web.form;

import com.google.common.collect.Lists;
import net.myspring.common.form.DataForm;
import net.myspring.future.modules.basic.domain.Pricesystem;
import net.myspring.future.modules.basic.domain.PricesystemDetail;
import net.myspring.future.modules.basic.dto.PricesystemDetailDto;

import java.util.List;

/**
 * Created by lihx on 2017/4/18.
 */
public class PricesystemForm extends DataForm<Pricesystem> {
    private String name;
    private String sort;
    private boolean enabled;
    private List<PricesystemDetailForm> pricesystemDetailList= Lists.newArrayList();

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
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
