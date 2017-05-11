package net.myspring.future.modules.layout.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.layout.domain.ShopGoodsDeposit;
import net.myspring.util.cahe.annotation.CacheInput;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ShopGoodsDepositDto extends DataDto<ShopGoodsDeposit> {


    private Boolean selected;
    private String shopId;

    private String shopName;
    @CacheInput(inputKey = "offices",inputInstance = "areaId",outputInstance = "name")
    private String areaName;
    private String areaId;

    private String departMentName;
    private String departMent;


    private String bankName;

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


    private BigDecimal amount;
    private String outCode;
    private String outBillType;
    private LocalDate billDate;
    private String status;
    private Boolean locked;

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
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

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
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
