package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.DataForm;
import net.myspring.future.modules.crm.domain.StoreAllotDetail;

/**
 * Created by wangzm on 2017/4/21.
 */
public class StoreAllotDetailForm extends DataForm<StoreAllotDetail> {

    private String productId;
    private String productName;
    private Integer billQty;
    private Integer shippedQty;
    private String storeAllotId;

    private String outId;
    private Integer cloudQty;


    public Integer getCloudQty() {
        return cloudQty;
    }

    public void setCloudQty(Integer cloudQty) {
        this.cloudQty = cloudQty;
    }


    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getBillQty() {
        return billQty;
    }

    public void setBillQty(Integer billQty) {
        this.billQty = billQty;
    }

    public Integer getShippedQty() {
        return shippedQty;
    }

    public void setShippedQty(Integer shippedQty) {
        this.shippedQty = shippedQty;
    }

    public String getStoreAllotId() {
        return storeAllotId;
    }

    public void setStoreAllotId(String storeAllotId) {
        this.storeAllotId = storeAllotId;
    }


}
