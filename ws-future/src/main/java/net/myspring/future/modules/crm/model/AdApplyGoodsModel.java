package net.myspring.future.modules.crm.model;


import net.myspring.future.modules.layout.domain.AdApply;

import java.util.List;

/**
 * Created by lihx on 2017/3/23.
 */
public class AdApplyGoodsModel {
    String productId;
    String remarks;
    List<AdApply> adApplyList;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<AdApply> getAdApplyList() {
        return adApplyList;
    }

    public void setAdApplyList(List<AdApply> adApplyList) {
        this.adApplyList = adApplyList;
    }
}
