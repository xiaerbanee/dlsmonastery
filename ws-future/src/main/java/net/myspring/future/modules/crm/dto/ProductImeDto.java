package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.util.cahe.annotation.CacheInput;

import java.time.LocalDateTime;

/**
 * Created by haos on 2017/5/12.
 */
public class ProductImeDto extends DataDto<ProductIme> {

    private String ime;
    private String ime2;
    private String meid;
    private LocalDateTime retailDate;
    private LocalDateTime productImeUploadCreatedDate;
    private LocalDateTime productImeSaleCreatedDate;
    @CacheInput(inputKey = "products",inputInstance = "productId",outputInstance = "netType")
    private String productNetType;
    private String depotId;
    @CacheInput(inputKey = "depots",inputInstance = "depotId",outputInstance = "name")
    private String depotName;
    private String inputType;
    @CacheInput(inputKey = "products",inputInstance = "productId",outputInstance = "name")
    private String productName;
    private String productId;

    private LocalDateTime createdTime;
    private Boolean locked;
    private Boolean enabled;
    private String billId;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getProductImeUploadCreatedDate() {
        return productImeUploadCreatedDate;
    }

    public void setProductImeUploadCreatedDate(LocalDateTime productImeUploadCreatedDate) {
        this.productImeUploadCreatedDate = productImeUploadCreatedDate;
    }

    public LocalDateTime getProductImeSaleCreatedDate() {
        return productImeSaleCreatedDate;
    }

    public void setProductImeSaleCreatedDate(LocalDateTime productImeSaleCreatedDate) {
        this.productImeSaleCreatedDate = productImeSaleCreatedDate;
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

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

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNetType() {
        return productNetType;
    }

    public void setProductNetType(String productNetType) {
        this.productNetType = productNetType;
    }
}
