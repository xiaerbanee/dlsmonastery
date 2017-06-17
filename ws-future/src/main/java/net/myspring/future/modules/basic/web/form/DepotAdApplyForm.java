package net.myspring.future.modules.basic.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.basic.domain.Depot;

/**
 * Created by zhangyf on 2017/6/16.
 */
public class DepotAdApplyForm extends BaseForm<Depot>{
    private String name;
    private Integer applyQty;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getApplyQty() {
        return applyQty;
    }

    public void setApplyQty(Integer applyQty) {
        this.applyQty = applyQty;
    }
}
