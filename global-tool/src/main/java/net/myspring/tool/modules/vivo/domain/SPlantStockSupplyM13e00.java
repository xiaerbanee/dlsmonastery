package net.myspring.tool.modules.vivo.domain;



//代理商库存表
public class SPlantStockSupplyM13e00 {

    private String companyId;
    private String supplyId;
    private String productId;
    private String createdTime;
    private Integer sumStock;
    private Integer useAbleStock;
    private Integer bad;
    private String accountDate;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(String supplyId) {
        this.supplyId = supplyId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getSumStock() {
        return sumStock;
    }

    public void setSumStock(Integer sumStock) {
        this.sumStock = sumStock;
    }

    public Integer getUseAbleStock() {
        return useAbleStock;
    }

    public void setUseAbleStock(Integer useAbleStock) {
        this.useAbleStock = useAbleStock;
    }

    public Integer getBad() {
        return bad;
    }

    public void setBad(Integer bad) {
        this.bad = bad;
    }

    public String getAccountDate() {
        return accountDate;
    }

    public void setAccountDate(String accountDate) {
        this.accountDate = accountDate;
    }
}
