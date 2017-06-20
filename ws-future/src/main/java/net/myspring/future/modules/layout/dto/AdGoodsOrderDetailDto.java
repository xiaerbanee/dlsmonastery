package net.myspring.future.modules.layout.dto;

import net.myspring.common.constant.CharConstant;
import net.myspring.common.dto.IdDto;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.layout.domain.AdGoodsOrderDetail;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.text.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;


public class AdGoodsOrderDetailDto extends IdDto<AdGoodsOrderDetail> {

    private String adGoodsOrderRemarks;
    private String adGoodsOrderParentId;
    private String adGoodsOrderId;
    private LocalDate adGoodsOrderCreatedDate;
    private LocalDate adGoodsOrderBillDate;
    private String adGoodsOrderBillType;
    private String adGoodsOrderProcessStatus;
    private String adGoodsOrderShopAreaId;
    @CacheInput(inputKey = "offices", inputInstance = "adGoodsOrderShopAreaId", outputInstance = "name")
    private String adGoodsOrderShopAreaName;
    private String adGoodsOrderShopId;
    @CacheInput(inputKey = "depots", inputInstance = "adGoodsOrderShopId", outputInstance = "name")
    private String adGoodsOrderShopName;

    private String productId;
    private String productName;
    private String productCode;
    private BigDecimal productPrice2;

    private Integer qty;
    private Integer confirmQty;
    private Integer billQty;

    public String getAdGoodsOrderFormatId(){
        if(StringUtils.isBlank(adGoodsOrderParentId) || adGoodsOrderParentId.equals(adGoodsOrderId)){
            return RequestUtils.getRequestEntity().getCompanyName() + StringUtils.trimToEmpty(adGoodsOrderId);
        }
        return RequestUtils.getRequestEntity().getCompanyName() + StringUtils.trimToEmpty(adGoodsOrderParentId)+ CharConstant.UNDER_LINE + StringUtils.trimToEmpty(adGoodsOrderId);
    }

    public String getAdGoodsOrderRemarks() {
        return adGoodsOrderRemarks;
    }

    public void setAdGoodsOrderRemarks(String adGoodsOrderRemarks) {
        this.adGoodsOrderRemarks = adGoodsOrderRemarks;
    }

    public String getAdGoodsOrderParentId() {
        return adGoodsOrderParentId;
    }

    public void setAdGoodsOrderParentId(String adGoodsOrderParentId) {
        this.adGoodsOrderParentId = adGoodsOrderParentId;
    }

    public String getAdGoodsOrderId() {
        return adGoodsOrderId;
    }

    public void setAdGoodsOrderId(String adGoodsOrderId) {
        this.adGoodsOrderId = adGoodsOrderId;
    }

    public LocalDate getAdGoodsOrderCreatedDate() {
        return adGoodsOrderCreatedDate;
    }

    public void setAdGoodsOrderCreatedDate(LocalDate adGoodsOrderCreatedDate) {
        this.adGoodsOrderCreatedDate = adGoodsOrderCreatedDate;
    }

    public LocalDate getAdGoodsOrderBillDate() {
        return adGoodsOrderBillDate;
    }

    public void setAdGoodsOrderBillDate(LocalDate adGoodsOrderBillDate) {
        this.adGoodsOrderBillDate = adGoodsOrderBillDate;
    }

    public String getAdGoodsOrderBillType() {
        return adGoodsOrderBillType;
    }

    public void setAdGoodsOrderBillType(String adGoodsOrderBillType) {
        this.adGoodsOrderBillType = adGoodsOrderBillType;
    }

    public String getAdGoodsOrderProcessStatus() {
        return adGoodsOrderProcessStatus;
    }

    public void setAdGoodsOrderProcessStatus(String adGoodsOrderProcessStatus) {
        this.adGoodsOrderProcessStatus = adGoodsOrderProcessStatus;
    }

    public String getAdGoodsOrderShopAreaId() {
        return adGoodsOrderShopAreaId;
    }

    public void setAdGoodsOrderShopAreaId(String adGoodsOrderShopAreaId) {
        this.adGoodsOrderShopAreaId = adGoodsOrderShopAreaId;
    }

    public String getAdGoodsOrderShopAreaName() {
        return adGoodsOrderShopAreaName;
    }

    public void setAdGoodsOrderShopAreaName(String adGoodsOrderShopAreaName) {
        this.adGoodsOrderShopAreaName = adGoodsOrderShopAreaName;
    }

    public String getAdGoodsOrderShopId() {
        return adGoodsOrderShopId;
    }

    public void setAdGoodsOrderShopId(String adGoodsOrderShopId) {
        this.adGoodsOrderShopId = adGoodsOrderShopId;
    }

    public String getAdGoodsOrderShopName() {
        return adGoodsOrderShopName;
    }

    public void setAdGoodsOrderShopName(String adGoodsOrderShopName) {
        this.adGoodsOrderShopName = adGoodsOrderShopName;
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

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public BigDecimal getProductPrice2() {
        return productPrice2;
    }

    public void setProductPrice2(BigDecimal productPrice2) {
        this.productPrice2 = productPrice2;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getConfirmQty() {
        return confirmQty;
    }

    public void setConfirmQty(Integer confirmQty) {
        this.confirmQty = confirmQty;
    }

    public Integer getBillQty() {
        return billQty;
    }

    public void setBillQty(Integer billQty) {
        this.billQty = billQty;
    }

}
