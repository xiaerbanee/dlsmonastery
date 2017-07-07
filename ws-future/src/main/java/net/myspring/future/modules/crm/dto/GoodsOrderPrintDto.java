package net.myspring.future.modules.crm.dto;

import com.google.common.collect.Lists;
import net.myspring.future.common.constant.FormatterConstant;
import net.myspring.util.text.IdUtils;

import java.time.LocalDate;
import java.util.List;

public class GoodsOrderPrintDto {
    private String outCode;
    private LocalDate billDate;
    private String storeName;
    private String businessId;
    private String shopName;
    private String shopClientName;
    private String remarks;
    private String expressOrderCode;
    private List<ProductPrintDto> productList= Lists.newArrayList();

    private String contator;
    private String mobilePhone;
    private String address;
    private Boolean lxMallOrder;

    public String getFormatId(){
        return IdUtils.getFormatId(businessId, FormatterConstant.GOODS_ORDER);
    }

    public Boolean getLxMallOrder() {
        return lxMallOrder;
    }

    public void setLxMallOrder(Boolean lxMallOrder) {
        this.lxMallOrder = lxMallOrder;
    }

    public String getShopClientName() {
        return shopClientName;
    }

    public void setShopClientName(String shopClientName) {
        this.shopClientName = shopClientName;
    }

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

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
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
