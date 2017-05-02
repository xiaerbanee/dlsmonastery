package net.myspring.future.modules.layout.domain;

import net.myspring.future.common.domain.CompanyEntity;
import net.myspring.common.domain.DataEntity;
import net.myspring.future.modules.basic.domain.Depot;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="crm_shop_promotion")
public class ShopPromotion extends CompanyEntity<ShopPromotion> {
    private String businessId;
    private String shopId;
    private LocalDate activityDate;
    private String activityType;
    private BigDecimal dayAmount;
    private BigDecimal amount;
    private String salerComment;
    private String materialComment;
    private String activityImage1;
    private String activityImage2;
    private String activityImage3;
    private String comment;
    private String phone;
    private Integer version = 0;

    private Depot shop;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public LocalDate getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(LocalDate activityDate) {
        this.activityDate = activityDate;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public BigDecimal getDayAmount() {
        return dayAmount;
    }

    public void setDayAmount(BigDecimal dayAmount) {
        this.dayAmount = dayAmount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSalerComment() {
        return salerComment;
    }

    public void setSalerComment(String salerComment) {
        this.salerComment = salerComment;
    }

    public String getMaterialComment() {
        return materialComment;
    }

    public void setMaterialComment(String materialComment) {
        this.materialComment = materialComment;
    }

    public String getActivityImage1() {
        return activityImage1;
    }

    public void setActivityImage1(String activityImage1) {
        this.activityImage1 = activityImage1;
    }

    public String getActivityImage2() {
        return activityImage2;
    }

    public void setActivityImage2(String activityImage2) {
        this.activityImage2 = activityImage2;
    }

    public String getActivityImage3() {
        return activityImage3;
    }

    public void setActivityImage3(String activityImage3) {
        this.activityImage3 = activityImage3;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Depot getShop() {
        return shop;
    }

    public void setShop(Depot shop) {
        this.shop = shop;
    }
}
