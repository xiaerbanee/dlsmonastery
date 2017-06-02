package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.AuditDto;
import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.PriceChangeIme;
import net.myspring.util.cahe.annotation.CacheInput;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by zhangyf on 2017/5/15.
 */
public class PriceChangeImeDto extends AuditDto<PriceChangeIme> {
    private String image;
    private LocalDateTime saleDate;
    private LocalDateTime auditDate;
    private String shopId;
    @CacheInput(inputKey = "depots",inputInstance = "shopId",outputInstance = "name")
    private String shopName;
    private String officeId;
    @CacheInput(inputKey = "depots",inputInstance = "officeId",outputInstance = "name")
    private String officeName;
    private Boolean isCheck;
    private String productImeId;
    private String ime;
    private String priceChangeId;
    private String priceChangeName;
    private String productId;
    @CacheInput(inputKey = "products",inputInstance = "productId",outputInstance = "name")
    private String productName;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }


    public String getPriceChangeName() {
        return priceChangeName;
    }

    public void setPriceChangeName(String priceChangeName) {
        this.priceChangeName = priceChangeName;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }

    public LocalDateTime getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(LocalDateTime auditDate) {
        this.auditDate = auditDate;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public Boolean getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(Boolean isCheck) {
        this.isCheck = isCheck;
    }

    public String getProductImeId() {
        return productImeId;
    }

    public void setProductImeId(String productImeId) {
        this.productImeId = productImeId;
    }

    public String getPriceChangeId() {
        return priceChangeId;
    }

    public void setPriceChangeId(String priceChangeId) {
        this.priceChangeId = priceChangeId;
    }
}
