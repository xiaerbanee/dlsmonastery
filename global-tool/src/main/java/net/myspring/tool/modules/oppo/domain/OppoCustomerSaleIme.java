package net.myspring.tool.modules.oppo.domain;

import java.time.LocalDateTime;

public class OppoCustomerSaleIme {
    private String imei;
    private LocalDateTime saletime;
    private String custname;
    private String custmobile;
    private String custsex;
    private String salepromoter;
    private String shopcode;
    private String shopname;
    private String agentcode;
    private String agentname;
    private String province;
    private String city;

    public String getAgentcode() {
        return agentcode;
    }

    public void setAgentcode(String agentcode) {
        this.agentcode = agentcode;
    }

    public String getAgentname() {
        return agentname;
    }

    public void setAgentname(String agentname) {
        this.agentname = agentname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCustmobile() {
        return custmobile;
    }

    public void setCustmobile(String custmobile) {
        this.custmobile = custmobile;
    }

    public String getCustname() {
        return custname;
    }

    public void setCustname(String custname) {
        this.custname = custname;
    }

    public String getCustsex() {
        return custsex;
    }

    public void setCustsex(String custsex) {
        this.custsex = custsex;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getSalepromoter() {
        return salepromoter;
    }

    public void setSalepromoter(String salepromoter) {
        this.salepromoter = salepromoter;
    }

    public LocalDateTime getSaletime() {
        return saletime;
    }

    public void setSaletime(LocalDateTime saletime) {
        this.saletime = saletime;
    }

    public String getShopcode() {
        return shopcode;
    }

    public void setShopcode(String shopcode) {
        this.shopcode = shopcode;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }
}
