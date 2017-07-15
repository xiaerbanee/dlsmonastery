package net.myspring.future.modules.third.dto;

import java.time.LocalDateTime;

public class VivoPlantElectronicsn  {
    private String snImei;
    private LocalDateTime retailDate;
    private LocalDateTime createTime;
    private String companyId;
    private String productId;

    public String getSnImei() {
        return snImei;
    }

    public void setSnImei(String snImei) {
        this.snImei = snImei;
    }

    public LocalDateTime getRetailDate() {
        return retailDate;
    }

    public void setRetailDate(LocalDateTime retailDate) {
        this.retailDate = retailDate;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
