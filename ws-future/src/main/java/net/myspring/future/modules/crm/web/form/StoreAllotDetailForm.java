package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.DataForm;
import net.myspring.future.modules.crm.domain.StoreAllotDetail;

/**
 * Created by wangzm on 2017/4/21.
 */
public class StoreAllotDetailForm extends DataForm<StoreAllotDetail> {

    private String productId;

    private Integer billQty;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getBillQty() {
        return billQty;
    }

    public void setBillQty(Integer billQty) {
        this.billQty = billQty;
    }



}
