package net.myspring.future.modules.api.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.api.domain.CarrierShop;

/**
 * Created by wangzm on 2017/7/5.
 */
public class CarrierShopForm extends BaseForm<CarrierShop>{
    private String name;
    private String code;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
