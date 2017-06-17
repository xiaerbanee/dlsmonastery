package net.myspring.future.modules.crm.dto;

import com.google.common.collect.Lists;

import java.time.LocalDate;
import java.util.List;

public class GoodsOrderPrintDto {
    private String outCode;
    private LocalDate billData;
    private String storeName;
    private String businessId;
    private String shopName;
    private String remarks;
    private String expressOrderCode;
    private List<ProductPrintDto> productList= Lists.newArrayList();

    private String contator;
    private String mobilePhone;
    private String address;

    public String getContator() {
        return contator;
    }

    public void setContator(String contator) {
        this.contator = contator;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getExpressOrderCode() {
        return expressOrderCode;
    }

    public void setExpressOrderCode(String expressOrderCode) {
        this.expressOrderCode = expressOrderCode;
    }

    public String getOutCode() {
        return outCode;
    }

    public void setOutCode(String outCode) {
        this.outCode = outCode;
    }

    public LocalDate getBillData() {
        return billData;
    }

    public void setBillData(LocalDate billData) {
        this.billData = billData;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    public List<ProductPrintDto> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductPrintDto> productList) {
        this.productList = productList;
    }
}
