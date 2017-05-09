package net.myspring.future.modules.crm.web.query;

import net.myspring.future.common.query.BaseQuery;
import net.myspring.util.time.LocalDateTimeUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class BankInQuery extends BaseQuery {


    private List<String> processStatusList;

    private String id;
    private String shopName;

    public List<String> getProcessStatusList() {
        return processStatusList;
    }

    public void setProcessStatusList(List<String> processStatusList) {
        this.processStatusList = processStatusList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCreatedDateRange() {
        return createdDateRange;
    }

    public void setCreatedDateRange(String createdDateRange) {
        if(createdDateRange!=null){
            String[] tempParamValues = createdDateRange.split(" - ");
            this.createdDateStart= LocalDateTimeUtils.parse(tempParamValues[0]+ " 00:00:00");
            this.createdDateEnd= LocalDateTimeUtils.parse(tempParamValues[1]+ " 23:59:59");
        }
        this.createdDateRange = createdDateRange;

    }

    public LocalDateTime getCreatedDateStart() {
        return createdDateStart;
    }


    public LocalDateTime getCreatedDateEnd() {
        return createdDateEnd;
    }

    public String getBillDateRange() {
        return billDateRange;
    }

    public void setBillDateRange(String billDateRange) {
        if(billDateRange!=null){
            String[] tempParamValues = billDateRange.split(" - ");
            this.billDateStart= LocalDateTimeUtils.parse(tempParamValues[0]+ " 00:00:00");
            this.billDateEnd= LocalDateTimeUtils.parse(tempParamValues[1]+ " 23:59:59");
        }
        this.billDateRange = billDateRange;

    }

    public LocalDateTime getBillDateStart() {
        return billDateStart;
    }

    public LocalDateTime getBillDateEnd() {
        return billDateEnd;
    }


    public String getInputDateRange() {
        return inputDateRange;
    }

    public void setInputDateRange(String inputDateRange) {
        if(inputDateRange!=null){
            String[] tempParamValues = inputDateRange.split(" - ");
            this.inputDateStart= LocalDateTimeUtils.parse(tempParamValues[0]+ " 00:00:00");
            this.inputDateEnd= LocalDateTimeUtils.parse(tempParamValues[1]+ " 23:59:59");
        }
        this.inputDateRange = billDateRange;

    }

    public LocalDateTime getInputDateStart() {
        return inputDateStart;
    }

    public LocalDateTime getInputDateEnd() {
        return inputDateEnd;
    }

    public String getOutCode() {
        return outCode;
    }

    public void setOutCode(String outCode) {
        this.outCode = outCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    private String createdDateRange;
    private LocalDateTime createdDateStart;
    private LocalDateTime createdDateEnd;
    private String billDateRange;
    private LocalDateTime billDateStart;
    private LocalDateTime billDateEnd;

    private BigDecimal amount;
    private String inputDateRange;
    private LocalDateTime inputDateStart;
    private LocalDateTime inputDateEnd;

    private String outCode;
    private String bankName;
    private String processStatus;
    private String serialNumber;




}
