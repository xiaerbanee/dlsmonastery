package net.myspring.future.modules.layout.web.query;

import net.myspring.future.common.query.BaseQuery;
import net.myspring.util.time.LocalDateTimeUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ShopGoodsDepositQuery extends BaseQuery {

    private String shopName;
    private String remarks;
    private String bankName;
    private String createdDateRange;
    private LocalDateTime createdDateStart;
    private LocalDateTime createdDateEnd;
    private String status;
    private List<String> statusList;
    private BigDecimal amount;
    private String outBillType;

    private List<String> outBillTypeList;


    public String getOutBillType() {
        return outBillType;
    }

    public void setOutBillType(String outBillType) {
        this.outBillType = outBillType;
    }

    public List<String> getOutBillTypeList() {
        return outBillTypeList;
    }

    public void setOutBillTypeList(List<String> outBillTypeList) {
        this.outBillTypeList = outBillTypeList;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<String> statusList) {
        this.statusList = statusList;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCreatedDateRange(String createdDateRange) {
        if(createdDateRange!=null){
            String[] tempParamValues = createdDateRange.split(" - ");
            this.createdDateStart= LocalDateTimeUtils.parse(tempParamValues[0]+ " 00:00:00");
            this.createdDateEnd= LocalDateTimeUtils.parse(tempParamValues[1]+ " 00:00:00").plusDays(1);
        }
        this.createdDateRange = createdDateRange;

    }

    public LocalDateTime getCreatedDateStart() {
        return createdDateStart;
    }

    public LocalDateTime getCreatedDateEnd() {
        return createdDateEnd;
    }


}
