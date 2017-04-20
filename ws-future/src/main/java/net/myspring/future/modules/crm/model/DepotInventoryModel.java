package net.myspring.future.modules.crm.model;

/**
 * Created by admin on 2017/1/5.
 */
public class DepotInventoryModel {
    private String shopId;
    private String type;
    private String productTypeName;
    private Long qty;
    private Long baoKaStockQty=0l;
    private Long baoKaSalesQty=0l;
    private Long saleStockQty=0l;
    private Long saleSalesQty=0l;

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public Long getBaoKaStockQty() {
        return baoKaStockQty;
    }

    public void setBaoKaStockQty(Long baoKaStockQty) {
        this.baoKaStockQty = baoKaStockQty;
    }

    public Long getBaoKaSalesQty() {
        return baoKaSalesQty;
    }

    public void setBaoKaSalesQty(Long baoKaSalesQty) {
        this.baoKaSalesQty = baoKaSalesQty;
    }

    public Long getSaleStockQty() {
        return saleStockQty;
    }

    public void setSaleStockQty(Long saleStockQty) {
        this.saleStockQty = saleStockQty;
    }

    public Long getSaleSalesQty() {
        return saleSalesQty;
    }

    public void setSaleSalesQty(Long saleSalesQty) {
        this.saleSalesQty = saleSalesQty;
    }

    public void addBaoKaStockQty(Long baoKaStockQty){
        this.baoKaStockQty+=baoKaStockQty;
    }

    public void addBaoKaSalesQty(Long baoKaSalesQty){
        this.baoKaSalesQty+=baoKaSalesQty;
    }

    public void addSaleStockQty(Long saleStockQty){
        this.saleStockQty+=saleStockQty;
    }

    public void addSaleSalesQty(Long saleSalesQty){
        this.saleSalesQty+=saleSalesQty;
    }
}
