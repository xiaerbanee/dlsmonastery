package net.myspring.future.modules.layout.web.form;

import com.google.common.collect.Lists;
import net.myspring.common.form.DataForm;
import net.myspring.future.modules.layout.domain.ShopAllot;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by wangzm on 2017/4/21.
 */
public class ShopAllotForm extends DataForm<ShopAllot> {

    private Boolean success;
    private String message;

    private String fromShopName;
    private String fromShopId;
    private String toShopName;
    private String toShopId;
    private String businessId;
    private String outReturnCode;
    private String outSaleCode;
    private String status;
    private Boolean enabled = Boolean.TRUE;
    private BigDecimal saleTotalPrice;
    private BigDecimal returnTotalPrice;
    private BigDecimal toShopShouldGet;
    private BigDecimal fromShopShouldGet;


    public String getFromShopName() {
        return fromShopName;
    }

    public void setFromShopName(String fromShopName) {
        this.fromShopName = fromShopName;
    }

    public String getToShopName() {
        return toShopName;
    }

    public void setToShopName(String toShopName) {
        this.toShopName = toShopName;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
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



    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getFromShopId() {
        return fromShopId;
    }

    public void setFromShopId(String fromShopId) {
        this.fromShopId = fromShopId;
    }

    public String getToShopId() {
        return toShopId;
    }

    public void setToShopId(String toShopId) {
        this.toShopId = toShopId;
    }

    public List<ShopAllotDetailForm> getShopAllotDetailFormList() {
        return shopAllotDetailFormList;
    }

    public void setShopAllotDetailFormList(List<ShopAllotDetailForm> shopAllotDetailFormList) {
        this.shopAllotDetailFormList = shopAllotDetailFormList;
    }

    private List<ShopAllotDetailForm> shopAllotDetailFormList = Lists.newArrayList();


}
