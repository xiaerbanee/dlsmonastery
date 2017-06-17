package net.myspring.future.modules.layout.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.common.constant.FormatterConstant;
import net.myspring.future.common.enums.AuditStatusEnum;
import net.myspring.future.modules.layout.domain.ShopAllot;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.text.IdUtils;

import java.math.BigDecimal;

public class ShopAllotDto extends DataDto<ShopAllot> {

    @CacheInput(inputKey = "depots",inputInstance = "fromShopId",outputInstance = "name")
    private String fromShopName;
    private String fromShopId;
    @CacheInput(inputKey = "depots",inputInstance = "toShopId",outputInstance = "name")
    private String toShopName;
    private String toShopId;

    private String businessId;
    private String outReturnCode;
    private String outSaleCode;
    private String status;
    private Boolean enabled;
    private BigDecimal saleTotalPrice;
    private BigDecimal returnTotalPrice;
    private BigDecimal fromShopShouldGet;
    private BigDecimal toShopShouldGet;

    public String getFromJointType() {
        //TODO 有一種情況是委托代銷
        return "现销";
    }

    public String getToJointType() {
        //TODO 有一種情況是委托代銷
        return "现销";
    }

    public BigDecimal getFromShopShouldGet() {
        return fromShopShouldGet;
    }

    public void setFromShopShouldGet(BigDecimal fromShopShouldGet) {
        this.fromShopShouldGet = fromShopShouldGet;
    }

    public BigDecimal getToShopShouldGet() {
        return toShopShouldGet;
    }

    public void setToShopShouldGet(BigDecimal toShopShouldGet) {
        this.toShopShouldGet = toShopShouldGet;
    }

    public BigDecimal getReturnTotalPrice() {
        return returnTotalPrice;
    }

    public void setReturnTotalPrice(BigDecimal returnTotalPrice) {
        this.returnTotalPrice = returnTotalPrice;
    }

    public BigDecimal getSaleTotalPrice() {
        return saleTotalPrice;
    }

    public void setSaleTotalPrice(BigDecimal saleTotalPrice) {
        this.saleTotalPrice = saleTotalPrice;
    }

    public String getFormatId() {
        return IdUtils.getFormatId(businessId, FormatterConstant.SHOP_ALLOT);
    }

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

    public Boolean getAuditable(){
        return AuditStatusEnum.申请中.name().equals(status);
    }

    public Boolean getEditable(){
        return AuditStatusEnum.申请中.name().equals(status);
    }


}
