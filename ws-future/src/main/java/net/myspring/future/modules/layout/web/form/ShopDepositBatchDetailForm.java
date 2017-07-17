package net.myspring.future.modules.layout.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.layout.domain.ShopDeposit;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ShopDepositBatchDetailForm extends BaseForm<ShopDeposit> {

    private BigDecimal imageAmount;
    private BigDecimal marketAmount;
    private LocalDate billDate;
    private String bankName;
    private String departMentName;
    private String outBillType;
    private String shopName;

    public BigDecimal getImageAmount() {
        return imageAmount;
    }

    public void setImageAmount(BigDecimal imageAmount) {
        this.imageAmount = imageAmount;
    }

    public BigDecimal getMarketAmount() {
        return marketAmount;
    }

    public void setMarketAmount(BigDecimal marketAmount) {
        this.marketAmount = marketAmount;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getDepartMentName() {
        return departMentName;
    }

    public void setDepartMentName(String departMentName) {
        this.departMentName = departMentName;
    }

    public String getOutBillType() {
        return outBillType;
    }

    public void setOutBillType(String outBillType) {
        this.outBillType = outBillType;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

}
