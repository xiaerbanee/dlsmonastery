package net.myspring.future.modules.basic.web.form;

import net.myspring.future.common.form.DataForm;
import net.myspring.future.modules.basic.domain.Pricesystem;

import java.util.List;

/**
 * Created by lihx on 2017/4/18.
 */
public class PricesystemForm extends DataForm<Pricesystem> {
    private String name;
    private String sotr;
    private List<String> pricesystemDetailList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSotr() {
        return sotr;
    }

    public void setSotr(String sotr) {
        this.sotr = sotr;
    }

    public List<String> getPricesystemDetailList() {
        return pricesystemDetailList;
    }

    public void setPricesystemDetailList(List<String> pricesystemDetailList) {
        this.pricesystemDetailList = pricesystemDetailList;
    }
}
