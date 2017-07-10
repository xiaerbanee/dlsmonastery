package net.myspring.future.modules.layout.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.layout.domain.ShopDeposit;

import java.util.List;

public class ShopDepositBatchForm extends BaseForm<ShopDeposit> {

    private List<ShopDepositBatchDetailForm> shopDepositBatchDetailFormList;


    public List<ShopDepositBatchDetailForm> getShopDepositBatchDetailFormList() {
        return shopDepositBatchDetailFormList;
    }

    public void setShopDepositBatchDetailFormList(List<ShopDepositBatchDetailForm> shopDepositBatchDetailFormList) {
        this.shopDepositBatchDetailFormList = shopDepositBatchDetailFormList;
    }
}
