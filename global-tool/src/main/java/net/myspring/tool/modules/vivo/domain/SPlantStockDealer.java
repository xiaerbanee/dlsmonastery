package net.myspring.tool.modules.vivo.domain;

//经销商库存表
public class SPlantStockDealer {

    private String id;
    private String companyId;
    private String dealerId;
    private String productId;
    private String createdTime;
    private Integer sumStock;
    private Integer useAbleStock;
    private Integer bad;
    private String accountDate;
    private String agentCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
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

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }
}
