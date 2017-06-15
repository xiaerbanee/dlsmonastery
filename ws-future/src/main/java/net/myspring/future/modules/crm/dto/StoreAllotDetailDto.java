package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.IdDto;
import net.myspring.future.modules.crm.domain.StoreAllotDetail;
import net.myspring.util.cahe.annotation.CacheInput;

public class StoreAllotDetailDto extends IdDto<StoreAllotDetail> {

    private String productId;
    @CacheInput(inputKey = "products",inputInstance = "productId",outputInstance = "name")
    private String productName;

    private Integer billQty;
    private Integer cloudQty;

    private Integer qty;
    private Integer shippedQty;
    private String storeAllotId;
    private String outId;
    private Boolean productHasIme;
    private Integer shipQty;

    public Integer getShipQty() {
        return shipQty;
    }

    public void setShipQty(Integer shipQty) {
        this.shipQty = shipQty;
    }

    public Boolean getProductHasIme() {
        return productHasIme;
    }

    public Integer getLeftQty(){
        if(billQty==null || shippedQty==null || shipQty==null){
            return null;
        }
        return billQty - shippedQty - shipQty;
    }

    public void setProductHasIme(Boolean productHasIme) {
        this.productHasIme = productHasIme;
    }

    public Integer getCloudQty() {
        return cloudQty;
    }

    public void setCloudQty(Integer cloudQty) {
        this.cloudQty = cloudQty;
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

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
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

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }


}
