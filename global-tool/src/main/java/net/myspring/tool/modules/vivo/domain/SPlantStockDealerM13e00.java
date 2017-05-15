package net.myspring.tool.modules.vivo.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

//经销商库存表
public class SPlantStockDealerM13e00 {
    private String companyID;
    private String dealerID;
    private String productID;
    private LocalDateTime createdTime;
    private Integer sumstock;
    private Integer useablestock;
    private Integer bad;
    private LocalDate accountDate;

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getDealerID() {
        return dealerID;
    }

    public void setDealerID(String dealerID) {
        this.dealerID = dealerID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getSumstock() {
        return sumstock;
    }

    public void setSumstock(Integer sumstock) {
        this.sumstock = sumstock;
    }

    public Integer getUseablestock() {
        return useablestock;
    }

    public void setUseablestock(Integer useablestock) {
        this.useablestock = useablestock;
    }

    public Integer getBad() {
        return bad;
    }

    public void setBad(Integer bad) {
        this.bad = bad;
    }

    public LocalDate getAccountDate() {
        return accountDate;
    }

    public void setAccountDate(LocalDate accountDate) {
        this.accountDate = accountDate;
    }
}
