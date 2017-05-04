package net.myspring.future.modules.layout.web.form;

import net.myspring.common.form.DataForm;
import net.myspring.future.modules.layout.domain.ShopPromotion;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by zhangyf on 2017/5/4.
 */
public class ShopPromotionForm extends DataForm<ShopPromotion>{

    private String shopId;
    private String shopName;
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

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
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
}
