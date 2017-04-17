package net.myspring.future.modules.crm.domain;


import net.myspring.common.domain.DataEntity;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.Product;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name="crm_after_sale_product_allot")
public class AfterSaleProductAllot extends DataEntity<AfterSaleProductAllot> {
    private String storeId;
    private String fromProductId;
    private String toProductId;
    private String fromOutCode;
    private String toOutCode;
    private Integer version = 0;
    private String checkId;
    private AfterSale afterSale;
    private String afterSaleId;

    @Transient
    private Product fromProduct;
    @Transient
    private Product toProduct;
    @Transient
    private Depot store;
    @Transient
    private Integer fromQty;
    @Transient
    private Integer toQty;

    public AfterSaleProductAllot() {

    }

    public AfterSaleProductAllot(String afterSaleId,String storeId,String fromProductId,String toProductId) {
        this.afterSaleId = afterSaleId;
        this.storeId = storeId;
        this.fromProductId = fromProductId;
        this.toProductId = toProductId;
    }

    public Product getFromProduct() {
        return fromProduct;
    }

    public void setFromProduct(Product fromProduct) {
        this.fromProduct = fromProduct;
    }

    public Product getToProduct() {
        return toProduct;
    }

    public void setToProduct(Product toProduct) {
        this.toProduct = toProduct;
    }

    public Depot getStore() {
        return store;
    }

    public void setStore(Depot store) {
        this.store = store;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getFromProductId() {
        return fromProductId;
    }

    public void setFromProductId(String fromProductId) {
        this.fromProductId = fromProductId;
    }

    public String getToProductId() {
        return toProductId;
    }

    public void setToProductId(String toProductId) {
        this.toProductId = toProductId;
    }

    public String getFromOutCode() {
        return fromOutCode;
    }

    public void setFromOutCode(String fromOutCode) {
        this.fromOutCode = fromOutCode;
    }

    public String getToOutCode() {
        return toOutCode;
    }

    public void setToOutCode(String toOutCode) {
        this.toOutCode = toOutCode;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public AfterSale getAfterSale() {
        return afterSale;
    }

    public void setAfterSale(AfterSale afterSale) {
        this.afterSale = afterSale;
    }

    public String getAfterSaleId() {
        return afterSaleId;
    }

    public void setAfterSaleId(String afterSaleId) {
        this.afterSaleId = afterSaleId;
    }

    public Integer getFromQty() {
        return fromQty;
    }

    public void setFromQty(Integer fromQty) {
        this.fromQty = fromQty;
    }

    public Integer getToQty() {
        return toQty;
    }

    public void setToQty(Integer toQty) {
        this.toQty = toQty;
    }
}
