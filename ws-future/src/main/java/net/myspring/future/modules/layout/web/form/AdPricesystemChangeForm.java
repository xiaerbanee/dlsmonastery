package net.myspring.future.modules.layout.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.basic.web.form.AdPricesystemDetailForm;
import net.myspring.future.modules.layout.domain.AdPricesystemChange;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zhangyf on 2017/5/11.
 */
public class AdPricesystemChangeForm extends BaseForm<AdPricesystemChange> {

    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
