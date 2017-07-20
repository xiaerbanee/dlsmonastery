package net.myspring.tool.modules.vivo.dto;

import net.myspring.util.cahe.annotation.CacheInput;

public class SCustomerDto {
    private String customerId;
    private String customerName;
    @CacheInput(inputKey = "offices",inputInstance = "zoneId" ,outputInstance = "name")
    private String areaName;
    @CacheInput(inputKey = "offices",inputInstance = "zoneId" ,outputInstance = "agentCode")
    private String agentCode;
    private String zoneId;
    private String recordDate;
    private Integer customerLevel;
    private String customerStr1;
    private String customerStr4;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getCustomerLevel() {
        return customerLevel;
    }

    public void setCustomerLevel(Integer customerLevel) {
        this.customerLevel = customerLevel;
    }

    public String getCustomerStr1() {
        return customerStr1;
    }

    public void setCustomerStr1(String customerStr1) {
        this.customerStr1 = customerStr1;
    }

    public String getCustomerStr4() {
        return customerStr4;
    }

    public void setCustomerStr4(String customerStr4) {
        this.customerStr4 = customerStr4;
    }
}
