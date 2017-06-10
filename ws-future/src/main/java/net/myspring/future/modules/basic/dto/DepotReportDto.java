package net.myspring.future.modules.basic.dto;

import net.myspring.util.cahe.annotation.CacheInput;

import java.time.LocalDate;

public class DepotReportDto {
    private String productId;
    private String productName;
    private Integer qty;
    private String ime;
    private String depotId;
    private String depotName;
    private String saleDate;
    private LocalDate retailDate;
    private String employeeId;
    @CacheInput(inputKey = "employees",inputInstance = "employeeId",outputInstance = "name")
    private String employeeName;
    private String percent;

    public Integer getQty() {
        if(qty==null){
            qty=0;
        }
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }

    public LocalDate getRetailDate() {
        return retailDate;
    }

    public void setRetailDate(LocalDate retailDate) {
        this.retailDate = retailDate;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

}
