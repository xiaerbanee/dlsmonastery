package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.AfterSaleDetail;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by Lenovo on 2017/6/2.
 */
public class AfterSaleDetailDto extends DataDto<AfterSaleDetail> {
    //售后节点：地区录入，总部录入，工厂录入(坏机返厂，好机返库)
    private String type;
    private String fromDepotId;
    private String toDepotId;
    private LocalDate inputDate;
    private LocalDate replaceDate;
    private String replaceProductImeId;
    private String replaceProductId;
    private BigDecimal replaceAmount;
    private String  afterSaleId;
    private String fromDepotName;
    private String toDepotName;
    private String replaceProductIme;
    private String replaceProductName;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFromDepotId() {
        return fromDepotId;
    }

    public void setFromDepotId(String fromDepotId) {
        this.fromDepotId = fromDepotId;
    }

    public String getToDepotId() {
        return toDepotId;
    }

    public void setToDepotId(String toDepotId) {
        this.toDepotId = toDepotId;
    }

    public LocalDate getInputDate() {
        return inputDate;
    }

    public void setInputDate(LocalDate inputDate) {
        this.inputDate = inputDate;
    }

    public LocalDate getReplaceDate() {
        return replaceDate;
    }

    public void setReplaceDate(LocalDate replaceDate) {
        this.replaceDate = replaceDate;
    }

    public String getReplaceProductImeId() {
        return replaceProductImeId;
    }

    public void setReplaceProductImeId(String replaceProductImeId) {
        this.replaceProductImeId = replaceProductImeId;
    }

    public String getReplaceProductId() {
        return replaceProductId;
    }

    public void setReplaceProductId(String replaceProductId) {
        this.replaceProductId = replaceProductId;
    }

    public BigDecimal getReplaceAmount() {
        return replaceAmount;
    }

    public void setReplaceAmount(BigDecimal replaceAmount) {
        this.replaceAmount = replaceAmount;
    }

    public String getAfterSaleId() {
        return afterSaleId;
    }

    public void setAfterSaleId(String afterSaleId) {
        this.afterSaleId = afterSaleId;
    }

    public String getFromDepotName() {
        return fromDepotName;
    }

    public void setFromDepotName(String fromDepotName) {
        this.fromDepotName = fromDepotName;
    }

    public String getToDepotName() {
        return toDepotName;
    }

    public void setToDepotName(String toDepotName) {
        this.toDepotName = toDepotName;
    }

    public String getReplaceProductIme() {
        return replaceProductIme;
    }

    public void setReplaceProductIme(String replaceProductIme) {
        this.replaceProductIme = replaceProductIme;
    }

    public String getReplaceProductName() {
        return replaceProductName;
    }

    public void setReplaceProductName(String replaceProductName) {
        this.replaceProductName = replaceProductName;
    }
}
