package net.myspring.future.modules.layout.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.common.enums.AuditTypeEnum;
import net.myspring.future.modules.layout.domain.ShopAllot;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ShopAllotDto extends DataDto<ShopAllot> {

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getFromShopName() {
        return fromShopName;
    }

    public void setFromShopName(String fromShopName) {
        this.fromShopName = fromShopName;
    }

    public String getFromShopId() {
        return fromShopId;
    }

    public void setFromShopId(String fromShopId) {
        this.fromShopId = fromShopId;
    }

    public String getToShopName() {
        return toShopName;
    }

    public void setToShopName(String toShopName) {
        this.toShopName = toShopName;
    }

    public String getToShopId() {
        return toShopId;
    }

    public void setToShopId(String toShopId) {
        this.toShopId = toShopId;
    }

    public String getOutReturnCode() {
        return outReturnCode;
    }

    public void setOutReturnCode(String outReturnCode) {
        this.outReturnCode = outReturnCode;
    }

    public String getOutSaleCode() {
        return outSaleCode;
    }

    public void setOutSaleCode(String outSaleCode) {
        this.outSaleCode = outSaleCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public BigDecimal getToShopShouldGet() {
        return toShopShouldGet;
    }

    public void setToShopShouldGet(BigDecimal toShopShouldGet) {
        this.toShopShouldGet = toShopShouldGet;
    }

    public BigDecimal getFromShopShouldGet() {
        return fromShopShouldGet;
    }

    public void setFromShopShouldGet(BigDecimal fromShopShouldGet) {
        this.fromShopShouldGet = fromShopShouldGet;
    }

    public BigDecimal getSaleTotalPrice() {
        return saleTotalPrice;
    }

    public void setSaleTotalPrice(BigDecimal saleTotalPrice) {
        this.saleTotalPrice = saleTotalPrice;
    }

    public BigDecimal getReturnTotalPrice() {
        return returnTotalPrice;
    }

    public void setReturnTotalPrice(BigDecimal returnTotalPrice) {
        this.returnTotalPrice = returnTotalPrice;
    }

    public List<ShopAllotDetailDto> getShopAllotDetailList() {
        return shopAllotDetailList;
    }

    public void setShopAllotDetailList(List<ShopAllotDetailDto> shopAllotDetailList) {
        this.shopAllotDetailList = shopAllotDetailList;
    }

    public Boolean getAuditable(){
        return AuditTypeEnum.APPLYING.name().equals(status);
    }

    public Boolean getEditable(){
        return AuditTypeEnum.APPLYING.name().equals(status);
    }

    private String fromShopName;
    private String fromShopId;
    private String toShopName;
    private String toShopId;
    private String businessId;
    private String outReturnCode;
    private String outSaleCode;
    private String status;
    private Boolean enabled;
    private BigDecimal saleTotalPrice;
    private BigDecimal returnTotalPrice;
    private BigDecimal toShopShouldGet;
    private BigDecimal fromShopShouldGet;


    private List<ShopAllotDetailDto> shopAllotDetailList = new ArrayList<>();

}
