package net.myspring.report.modules.future.dto;

import com.google.common.collect.Lists;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.dto.DataDto;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.text.StringUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public class ProductMonthPriceSumDto  extends DataDto{
    private String month;
    private String shopId;
    private String shopName;
    private String officeId;
    @CacheInput(inputKey = "offices",inputInstance = "officeId",outputInstance = "name")
    private String officeName;
    @CacheInput(inputKey = "offices",inputInstance = "areaId",outputInstance = "name")
    private String areaName;
    private String areaId;
    private String status;
    private String employeeId;
    @CacheInput(inputKey = "employees",inputInstance = "employeeId",outputInstance = "name")
    private String employeeName;
    @CacheInput(inputKey = "employees",inputInstance = "employeeId",outputInstance = "saleName")
    private String employeeSaleName;
    private String productImeId;
    private String productTypeName;
    private String ime;
    private String productName;
    private String productTypeId;
    private String saleShopId;
    @CacheInput(inputKey = "depots",inputInstance = "saleShopId",outputInstance = "name")
    private String saleShopName;
    private String goodsOrderShopId;
    @CacheInput(inputKey = "depots",inputInstance = "goodsOrderShopId",outputInstance = "name")
    private String goodsOrderShopName;
    private Integer qty;
    private String accountShopIds;
    private List<String> accountShopIdList= Lists.newArrayList();
    @CacheInput(inputKey = "depots",inputInstance = "accountShopIdList",outputInstance = "name")
    private List<String> accountShopName=Lists.newArrayList();

    public List<String> getAccountShopName() {
        return accountShopName;
    }

    public void setAccountShopName(List<String> accountShopName) {
        this.accountShopName = accountShopName;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSaleShopName() {
        return saleShopName;
    }

    public void setSaleShopName(String saleShopName) {
        this.saleShopName = saleShopName;
    }

    public String getGoodsOrderShopName() {
        return goodsOrderShopName;
    }

    public void setGoodsOrderShopName(String goodsOrderShopName) {
        this.goodsOrderShopName = goodsOrderShopName;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeSaleName() {
        return employeeSaleName;
    }

    public void setEmployeeSaleName(String employeeSaleName) {
        this.employeeSaleName = employeeSaleName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
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

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getProductImeId() {
        return productImeId;
    }

    public void setProductImeId(String productImeId) {
        this.productImeId = productImeId;
    }

    public String getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(String productTypeId) {
        this.productTypeId = productTypeId;
    }

    public String getSaleShopId() {
        return saleShopId;
    }

    public void setSaleShopId(String saleShopId) {
        this.saleShopId = saleShopId;
    }

    public String getGoodsOrderShopId() {
        return goodsOrderShopId;
    }

    public void setGoodsOrderShopId(String goodsOrderShopId) {
        this.goodsOrderShopId = goodsOrderShopId;
    }

    public String getAccountShopIds() {
        return accountShopIds;
    }

    public void setAccountShopIds(String accountShopIds) {
        this.accountShopIds = accountShopIds;
    }

    public List<String> getAccountShopIdList() {
        if(CollectionUtils.isEmpty(accountShopIdList)&& StringUtils.isNotBlank(accountShopIds)){
            this.accountShopIdList=StringUtils.getSplitList(accountShopIds, CharConstant.COMMA);
        }
        return accountShopIdList;
    }

    public void setAccountShopIdList(List<String> accountShopIdList) {
        this.accountShopIdList = accountShopIdList;
    }

    public String getKey(){
        return shopId+CharConstant.ENTER+employeeId;
    }
}
