package net.myspring.report.modules.future.dto;

import net.myspring.common.dto.DataDto;

public class ProductMonthPriceSumDto  extends DataDto{
    private String month;
    private String areaId;
    private String officeId;
    private String depotName;
    private String employeeId;
    private String employeeSaleName;
    private String employeeIdcard;
    private Integer qtySum;
    private Integer baokaSum;
    private Integer saleSum;
    private Integer realSaleSum;
    private String employeePositionName;
    private String employeeName;
    private String productTypeName;
    private String productName;
    private String ime;
    private String uploadDepotName;
    private String imeDepotName;
    private String imeShipDepotName;
    private String accountDepotNames;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeSaleName() {
        return employeeSaleName;
    }

    public void setEmployeeSaleName(String employeeSaleName) {
        this.employeeSaleName = employeeSaleName;
    }

    public String getEmployeeIdcard() {
        return employeeIdcard;
    }

    public void setEmployeeIdcard(String employeeIdcard) {
        this.employeeIdcard = employeeIdcard;
    }

    public Integer getQtySum() {
        return qtySum;
    }

    public void setQtySum(Integer qtySum) {
        this.qtySum = qtySum;
    }

    public Integer getBaokaSum() {
        return baokaSum;
    }

    public void setBaokaSum(Integer baokaSum) {
        this.baokaSum = baokaSum;
    }

    public Integer getSaleSum() {
        return saleSum;
    }

    public void setSaleSum(Integer saleSum) {
        this.saleSum = saleSum;
    }

    public Integer getRealSaleSum() {
        return realSaleSum;
    }

    public void setRealSaleSum(Integer realSaleSum) {
        this.realSaleSum = realSaleSum;
    }

    public String getEmployeePositionName() {
        return employeePositionName;
    }

    public void setEmployeePositionName(String employeePositionName) {
        this.employeePositionName = employeePositionName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
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

    public String getUploadDepotName() {
        return uploadDepotName;
    }

    public void setUploadDepotName(String uploadDepotName) {
        this.uploadDepotName = uploadDepotName;
    }

    public String getImeDepotName() {
        return imeDepotName;
    }

    public void setImeDepotName(String imeDepotName) {
        this.imeDepotName = imeDepotName;
    }

    public String getImeShipDepotName() {
        return imeShipDepotName;
    }

    public void setImeShipDepotName(String imeShipDepotName) {
        this.imeShipDepotName = imeShipDepotName;
    }

    public String getAccountDepotNames() {
        return accountDepotNames;
    }

    public void setAccountDepotNames(String accountDepotNames) {
        this.accountDepotNames = accountDepotNames;
    }
}
