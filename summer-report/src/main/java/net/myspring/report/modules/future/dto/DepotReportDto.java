package net.myspring.report.modules.future.dto;

import com.google.common.collect.Maps;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.cahe.annotation.CacheInput;

import java.time.LocalDate;
import java.util.Map;

public class DepotReportDto {
    private String productId;
    private String productName;
    private Integer qty=0;
    private String ime;
    private String depotId;
    private String depotName;
    private String saleDate;
    private String uploadDate;
    private LocalDate retailDate;
    private String employeeId;
    @CacheInput(inputKey = "employees",inputInstance = "employeeId",outputInstance = "name")
    private String employeeName;
    @CacheInput(inputKey = "employees",inputInstance = "employeeId",outputInstance = "accountId")
    private String employeeAccountId;
    @CacheInput(inputKey = "accounts",inputInstance = "employeeAccountId",outputInstance = "positionId")
    private String employeePositionId;
    @CacheInput(inputKey = "positions",inputInstance = "employeePositionId",outputInstance = "name")
    private String employeePositionName;
    private String percent;
    private String productTypeName;
    private String chainName;
    private String officeId;
    @CacheInput(inputKey = "offices",inputInstance = "officeId",outputInstance = "name")
    private String officeName;
    private String areaId;
    @CacheInput(inputKey = "offices",inputInstance = "areaId",outputInstance = "name")
    private String areaName;
    private String areaType;
    private String townId;
    private String districtId;
    private String districtName;
    @CacheInput(inputKey = "districts",inputInstance = "districtId",outputInstance = "province")
    private String provinceName;
    @CacheInput(inputKey = "districts",inputInstance = "districtId",outputInstance = "city")
    private String cityName;
    @CacheInput(inputKey = "districts",inputInstance = "districtId",outputInstance = "county")
    private String countyName;
    @CacheInput(inputKey = "towns",inputInstance = "townId",outputInstance = "townName")
    private String townName;

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    private Map<String,Object> extra= Maps.newHashMap();

    public Map<String, Object> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

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

    public String getTownId() {
        return townId;
    }

    public void setTownId(String townId) {
        this.townId = townId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public String getEmployeeAccountId() {
        return employeeAccountId;
    }

    public void setEmployeeAccountId(String employeeAccountId) {
        this.employeeAccountId = employeeAccountId;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getEmployeePositionId() {
        return employeePositionId;
    }

    public void setEmployeePositionId(String employeePositionId) {
        this.employeePositionId = employeePositionId;
    }

    public String getEmployeePositionName() {
        return employeePositionName;
    }

    public void setEmployeePositionName(String employeePositionName) {
        this.employeePositionName = employeePositionName;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public String getDistrictName() {
        return provinceName+CharConstant.COMMA+cityName+CharConstant.COMMA+countyName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }


}
