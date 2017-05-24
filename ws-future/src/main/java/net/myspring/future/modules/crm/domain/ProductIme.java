package net.myspring.future.modules.crm.domain;

import net.myspring.future.common.domain.CompanyEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="crm_product_ime")
public class ProductIme extends CompanyEntity<ProductIme> {
    private String billId;
    private String ime;
    private String ime2;
    private String boxIme;
    private String imeReverse;
    private LocalDateTime retailDate;
    private LocalDateTime createdTime;
    private String inputType;
    private String meid;
    private Integer version = 0;
    private String itemNumber;
    private String colorId;
    private String retailShopId;

    private BigDecimal baokaPrice;
    private BigDecimal price3;

    private String depotId;
    private String productImeUploadId;
    private String productImeSaleId;

    private String productId;

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getIme2() {
        return ime2;
    }

    public void setIme2(String ime2) {
        this.ime2 = ime2;
    }

    public String getBoxIme() {
        return boxIme;
    }

    public void setBoxIme(String boxIme) {
        this.boxIme = boxIme;
    }

    public String getImeReverse() {
        return imeReverse;
    }

    public void setImeReverse(String imeReverse) {
        this.imeReverse = imeReverse;
    }

    public LocalDateTime getRetailDate() {
        return retailDate;
    }

    public void setRetailDate(LocalDateTime retailDate) {
        this.retailDate = retailDate;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getMeid() {
        return meid;
    }

    public void setMeid(String meid) {
        this.meid = meid;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    public String getRetailShopId() {
        return retailShopId;
    }

    public void setRetailShopId(String retailShopId) {
        this.retailShopId = retailShopId;
    }

    public BigDecimal getBaokaPrice() {
        return baokaPrice;
    }

    public void setBaokaPrice(BigDecimal baokaPrice) {
        this.baokaPrice = baokaPrice;
    }

    public BigDecimal getPrice3() {
        return price3;
    }

    public void setPrice3(BigDecimal price3) {
        this.price3 = price3;
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    public String getProductImeUploadId() {
        return productImeUploadId;
    }

    public void setProductImeUploadId(String productImeUploadId) {
        this.productImeUploadId = productImeUploadId;
    }

    public String getProductImeSaleId() {
        return productImeSaleId;
    }

    public void setProductImeSaleId(String productImeSaleId) {
        this.productImeSaleId = productImeSaleId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
