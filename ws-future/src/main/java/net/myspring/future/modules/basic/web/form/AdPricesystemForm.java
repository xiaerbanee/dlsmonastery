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

    private List<String> officeIdList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getOfficeIdList() {
        return officeIdList;
    }

    public void setOfficeIdList(List<String> officeIdList) {
        this.officeIdList = officeIdList;
    }
}
