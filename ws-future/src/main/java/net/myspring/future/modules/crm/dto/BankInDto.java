package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.common.constant.FormatterConstant;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.crm.domain.BankIn;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.text.IdUtils;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BankInDto extends DataDto<BankIn> {

    @CacheInput(inputKey = "depots",inputInstance = "shopId",outputInstance = "name")
    private String shopName;
    private String shopId;
    private String shopClientId;
    @CacheInput(inputKey = "clients",inputInstance = "shopClientId",outputInstance = "name")
    private String shopClientName;
    @CacheInput(inputKey = "banks",inputInstance = "bankId",outputInstance = "name")
    private String bankName;
    private String bankId;

    private BigDecimal amount;
    private String serialNumber;
    private LocalDate billDate;
    private LocalDate inputDate;
    private String outCode;
    private String processStatus;
    private Boolean locked;
    private String processInstanceId;

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getFormatId() {
        return IdUtils.getFormatId(getId(), FormatterConstant.BANK_IN);
    }

    public String getShopClientId() {
        return shopClientId;
    }

    public void setShopClientId(String shopClientId) {
        this.shopClientId = shopClientId;
    }

    public String getShopClientName() {
        return shopClientName;
    }

    public void setShopClientName(String shopClientName) {
        this.shopClientName = shopClientName;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }


    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public LocalDate getInputDate() {
        return inputDate;
    }

    public void setInputDate(LocalDate inputDate) {
        this.inputDate = inputDate;
    }

    public String getOutCode() {
        return outCode;
    }

    public void setOutCode(String outCode) {
        this.outCode = outCode;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public Boolean getAuditable(){

        return !getFinished();
    }

    public Boolean getEditable(){
        //TODO 判斷editable要修改
        if ((!getLocked() && !getFinished()) && RequestUtils.getAccountId() != null && (RequestUtils.getAccountId().equals(getCreatedBy()) )) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public Boolean getFinished() {
        return "已通过".equals(processStatus) || "未通过".equals(processStatus);
    }
}
