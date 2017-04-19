package net.myspring.future.modules.basic.web.form;

import net.myspring.future.common.form.DataForm;
import net.myspring.future.modules.basic.domain.AdPricesystem;

/**
 * Created by lihx on 2017/4/17.
 */
public class AdPricesystemForm extends DataForm<AdPricesystem> {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
