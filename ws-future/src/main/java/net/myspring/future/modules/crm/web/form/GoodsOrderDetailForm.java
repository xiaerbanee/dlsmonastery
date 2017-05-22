package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.DataForm;
import net.myspring.future.modules.crm.domain.GoodsOrderDetail;

/**
 * Created by wangzm on 2017/4/21.
 */
public class GoodsOrderDetailForm extends DataForm<GoodsOrderDetail> {

    private String productId;
    private Integer qty;


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }


    public Integer getQty() {
        return qty;
    }


    public void setQty(Integer qty) {
        this.qty = qty;
    }


}
