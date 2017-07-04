package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.crm.domain.GoodsOrder;

import java.util.List;

public class GoodsOrderBatchAddForm extends BaseForm<GoodsOrder> {

    private List<GoodsOrderBatchAddDetailForm>  goodsOrderBatchAddDetailFormList;

    public List<GoodsOrderBatchAddDetailForm> getGoodsOrderBatchAddDetailFormList() {
        return goodsOrderBatchAddDetailFormList;
    }

    public void setGoodsOrderBatchAddDetailFormList(List<GoodsOrderBatchAddDetailForm> goodsOrderBatchAddDetailFormList) {
        this.goodsOrderBatchAddDetailFormList = goodsOrderBatchAddDetailFormList;
    }
}
