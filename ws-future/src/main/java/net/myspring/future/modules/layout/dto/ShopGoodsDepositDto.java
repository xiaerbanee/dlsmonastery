package net.myspring.future.modules.layout.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.common.constant.FormatterConstant;
import net.myspring.future.modules.layout.domain.ShopGoodsDeposit;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.text.IdUtils;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ShopGoodsDepositDto extends DataDto<ShopGoodsDeposit> {


    private String shopId;

    @CacheInput(inputKey = "depots",inputInstance = "shopId",outputInstance = "name")
    private String shopName;
    private String shopAreaId;
    @CacheInput(inputKey = "offices",inputInstance = "shopAreaId",outputInstance = "name")
    private String shopAreaName;
    private String shopOfficeId;

    private String departMentName;
    private String departMent;

    private BigDecimal amount;
    private String outCode;
    private String outBillType;
    private LocalDate billDate;
    private String status;
    private Boolean locked;

    private String bankId;
    @CacheInput(inputKey = "banks",inputInstance = "bankId",outputInstance = "name")
    private String bankName;

    private String shopClientOutId;

    public String getShopClientOutId() {
        return shopClientOutId;
    }

    public void setShopClientOutId(String shopClientOutId) {
        this.shopClientOutId = shopClientOutId;
    }

    public String getFormatId(){
        return IdUtils.getFormatId(getId(), FormatterConstant.SHOP_AD);
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getDepartMentName() {
        return departMentName;
    }

    public void setDepartMentName(String departMentName) {
        this.departMentName = departMentName;
    }

    public String getDepartMent() {
        return departMent;
    }

    public void setDepartMent(String departMent) {
        this.departMent = departMent;
    }


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

    public String getShopAreaId() {
        return shopAreaId;
    }

    public void setShopAreaId(String shopAreaId) {
        this.shopAreaId = shopAreaId;
    }

    public String getShopAreaName() {
        return shopAreaName;
    }

    public void setShopAreaName(String shopAreaName) {
        this.shopAreaName = shopAreaName;
    }

    public String getShopOfficeId() {
        return shopOfficeId;
    }

    public void setShopOfficeId(String shopOfficeId) {
        this.shopOfficeId = shopOfficeId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOutCode() {
        return outCode;
    }

    public void setOutCode(String outCode) {
        this.outCode = outCode;
    }

    public String getOutBillType() {
        return outBillType;
    }

    public void setOutBillType(String outBillType) {
        this.outBillType = outBillType;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getEditable(){
        return "省公司审核".equals(status);
    }



}
