package net.myspring.tool.modules.vivo.dto;

import net.myspring.util.cahe.annotation.CacheInput;

public class SPlantCustomerStockDto {
    private String customerId;
    private String productId;
    private Integer useAbleStock;
    private String areaId;
    @CacheInput(inputKey = "offices",inputInstance = "areaId" ,outputInstance = "agentCode")
    private String agentCode;
    private Integer customerLevel;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getUseAbleStock() {
        return useAbleStock;
    }

    public void setUseAbleStock(Integer useAbleStock) {
        this.useAbleStock = useAbleStock;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public Integer getCustomerLevel() {
        return customerLevel;
    }

    public void setCustomerLevel(Integer customerLevel) {
        this.customerLevel = customerLevel;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }
}
