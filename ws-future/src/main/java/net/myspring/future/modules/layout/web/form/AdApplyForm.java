package net.myspring.future.modules.layout.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.common.enums.BillTypeEnum;
import net.myspring.future.modules.layout.domain.AdApply;

import java.util.List;

/**
 * Created by wangzm on 2017/4/21.
 */
public class AdApplyForm extends BaseForm<AdApply> {
    private String billType = BillTypeEnum.POP.name();
    private String shopId;

    private List<Integer> applyQtys;
    private List<String> productIds;

    //下拉菜单
    private List<String> billTypes;

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

    public List<Integer> getApplyQtys() {
        return applyQtys;
    }

    public void setApplyQtys(List<Integer> applyQtys) {
        this.applyQtys = applyQtys;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }

    public List<String> getBillTypes() {
        return billTypes;
    }

    public void setBillTypes(List<String> billTypes) {
        this.billTypes = billTypes;
    }
}
