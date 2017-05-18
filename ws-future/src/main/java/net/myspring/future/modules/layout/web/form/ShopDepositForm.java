package net.myspring.future.modules.layout.web.form;

import net.myspring.common.form.DataForm;
import net.myspring.future.modules.layout.domain.ShopDeposit;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by wangzm on 2017/4/21.
 */
public class ShopDepositForm extends DataForm<ShopDeposit> {

    private BigDecimal imageAmount = BigDecimal.ZERO;
    private BigDecimal demoPhoneAmount= BigDecimal.ZERO;
    private BigDecimal marketAmount= BigDecimal.ZERO;
    private LocalDate billDate;
    private String bankId;
    private String departMent;
    private String outBillType;
    private String shopId;

    private List<String> outBillTypeList;


    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public BigDecimal getMarketAmount() {
        return marketAmount;
    }

    public void setMarketAmount(BigDecimal marketAmount) {
        this.marketAmount = marketAmount;
    }

    public BigDecimal getImageAmount() {
        return imageAmount;
    }

    public void setImageAmount(BigDecimal imageAmount) {
        this.imageAmount = imageAmount;
    }

    public BigDecimal getDemoPhoneAmount() {
        return demoPhoneAmount;
    }

    public void setDemoPhoneAmount(BigDecimal demoPhoneAmount) {
        this.demoPhoneAmount = demoPhoneAmount;
    }

    public String getDepartMent() {
        return departMent;
    }

    public void setDepartMent(String departMent) {
        this.departMent = departMent;
    }

    public String getOutBillType() {
        return outBillType;
    }

    public void setOutBillType(String outBillType) {
        this.outBillType = outBillType;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }


    public List<String> getOutBillTypeList() {
        return outBillTypeList;
    }

    public void setOutBillTypeList(List<String> outBillTypeList) {
        this.outBillTypeList = outBillTypeList;
    }


    public boolean isImageAmountPositive() {
        if (imageAmount != null && imageAmount.compareTo(BigDecimal.ZERO) > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isDemoPhoneAmountPositive() {
        if (demoPhoneAmount != null && demoPhoneAmount.compareTo(BigDecimal.ZERO) > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isMarketAmountPositive() {
        if (marketAmount != null && marketAmount.compareTo(BigDecimal.ZERO) > 0) {
            return true;
        } else {
            return false;
        }
    }


}
