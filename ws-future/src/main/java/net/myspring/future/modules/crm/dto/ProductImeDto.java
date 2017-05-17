package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.ProductIme;
import java.time.LocalDateTime;

/**
 * Created by haos on 2017/5/12.
 */
public class ProductImeDto extends DataDto<ProductIme> {
    private String billId;
    private String ime;
    private String ime2;
    private LocalDateTime retailDate;
    private LocalDateTime createdTime;
    private String inputType;
    private String meid;
    private String depotName;
    private LocalDateTime productImeUploadCreateDate;
    private LocalDateTime productImeSaleCreateDate;
    private String productName;
    private String productNetType;
    private Boolean locked;

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

    public LocalDateTime getProductImeUploadCreateDate() {
        return productImeUploadCreateDate;
    }

    public void setProductImeUploadCreateDate(LocalDateTime productImeUploadCreateDate) {
        this.productImeUploadCreateDate = productImeUploadCreateDate;
    }

    public LocalDateTime getProductImeSaleCreateDate() {
        return productImeSaleCreateDate;
    }

    public void setProductImeSaleCreateDate(LocalDateTime productImeSaleCreateDate) {
        this.productImeSaleCreateDate = productImeSaleCreateDate;
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
