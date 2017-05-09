package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.DataForm;
import net.myspring.future.modules.basic.dto.BankDto;
import net.myspring.future.modules.crm.domain.BankIn;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class BankInForm extends DataForm<BankIn> {

    private String shopId;
    private String type;
    private List<String>  typeList;
    private String bankId;
    private List<BankDto> bankDtoList;
    private LocalDate inputDate;
    private BigDecimal amount;
    private String serialNumber;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<String> typeList) {
        this.typeList = typeList;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public List<BankDto> getBankDtoList() {
        return bankDtoList;
    }

    public void setBankDtoList(List<BankDto> bankDtoList) {
        this.bankDtoList = bankDtoList;
    }

    public LocalDate getInputDate() {
        return inputDate;
    }

    public void setInputDate(LocalDate inputDate) {
        this.inputDate = inputDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

}