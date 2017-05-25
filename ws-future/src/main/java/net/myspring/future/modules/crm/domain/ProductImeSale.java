package net.myspring.future.modules.crm.domain;


import net.myspring.future.common.domain.CompanyEntity;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="crm_product_ime_sale")
public class ProductImeSale extends CompanyEntity<ProductImeSale> {
    private String shopId;

    private Boolean isBack;
    private Integer credit;
    private Integer leftCredit;
    private Integer version = 0;
    private String ime;
    private String buyer;
    private Integer buyerAge;
    private String buyerSex;
    private String buyerPhone;
    private String buyerSchool;
    private String buyerGrade;

    private String employeeId;

    private String productImeId;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public Boolean getBack() {
        return isBack;
    }

    public void setBack(Boolean back) {
        isBack = back;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Integer getLeftCredit() {
        return leftCredit;
    }

    public void setLeftCredit(Integer leftCredit) {
        this.leftCredit = leftCredit;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public Integer getBuyerAge() {
        return buyerAge;
    }

    public void setBuyerAge(Integer buyerAge) {
        this.buyerAge = buyerAge;
    }

    public String getBuyerSex() {
        return buyerSex;
    }

    public void setBuyerSex(String buyerSex) {
        this.buyerSex = buyerSex;
    }

    public String getBuyerPhone() {
        return buyerPhone;
    }

    public void setBuyerPhone(String buyerPhone) {
        this.buyerPhone = buyerPhone;
    }

    public String getBuyerSchool() {
        return buyerSchool;
    }

    public void setBuyerSchool(String buyerSchool) {
        this.buyerSchool = buyerSchool;
    }

    public String getBuyerGrade() {
        return buyerGrade;
    }

    public void setBuyerGrade(String buyerGrade) {
        this.buyerGrade = buyerGrade;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getProductImeId() {
        return productImeId;
    }

    public void setProductImeId(String productImeId) {
        this.productImeId = productImeId;
    }
}
