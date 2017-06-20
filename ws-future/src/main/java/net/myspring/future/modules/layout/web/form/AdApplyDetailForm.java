package net.myspring.future.modules.layout.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.layout.domain.AdApply;

/**
 * Created by zhangyf on 2017/6/15.
 */
public class AdApplyDetailForm extends BaseForm<AdApply>{

    private String shopId;
    private Integer nowBilledQty;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public Integer getNowBilledQty() {
        return nowBilledQty;
    }

    public void setNowBilledQty(Integer nowBilledQty) {
        this.nowBilledQty = nowBilledQty;
    }
}
