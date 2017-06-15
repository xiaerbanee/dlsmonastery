package net.myspring.future.modules.layout.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.common.enums.BillTypeEnum;
import net.myspring.future.modules.basic.web.form.ProductAdForm;
import net.myspring.future.modules.basic.web.form.ProductForm;
import net.myspring.future.modules.layout.domain.AdApply;

import java.util.List;

/**
 * Created by wangzm on 2017/4/21.
 */
public class AdApplyForm extends BaseForm<AdApply> {
    private String billType = BillTypeEnum.POP.name();
    private String shopId;

    private List<ProductAdForm> productAdForms;


    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public List<ProductAdForm> getProductAdForms() {
        return productAdForms;
    }

    public void setProductAdForms(List<ProductAdForm> productAdForms) {
        this.productAdForms = productAdForms;
    }
}
