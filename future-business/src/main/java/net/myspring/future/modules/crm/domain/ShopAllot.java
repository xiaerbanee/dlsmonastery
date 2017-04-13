package net.myspring.future.modules.crm.domain;


import com.google.common.collect.Lists;
import net.myspring.common.domain.AuditEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.List;


@Entity
@Table(name="crm_shop_allot")
public class ShopAllot extends AuditEntity<ShopAllot> {
    private String fromShopId;
    private String toShopId;
    private String outReturnCode;
    private Long allotAmount;
    private Integer version = 0;
    private String businessId;
    private String outSaleCode;
    private String returnCloudSynId;
    private String saleCloudSynId;
    private List<ShopAllotDetail> shopAllotDetailList = Lists.newArrayList();
    private List<String> shopAllotDetailIdList = Lists.newArrayList();


    private Depot fromShop;
    private Depot toShop;

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

    public String getOutReturnCode() {
        return outReturnCode;
    }

    public void setOutReturnCode(String outReturnCode) {
        this.outReturnCode = outReturnCode;
    }

    public Long getAllotAmount() {
        return allotAmount;
    }

    public void setAllotAmount(Long allotAmount) {
        this.allotAmount = allotAmount;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getOutSaleCode() {
        return outSaleCode;
    }

    public void setOutSaleCode(String outSaleCode) {
        this.outSaleCode = outSaleCode;
    }

    public String getReturnCloudSynId() {
        return returnCloudSynId;
    }

    public void setReturnCloudSynId(String returnCloudSynId) {
        this.returnCloudSynId = returnCloudSynId;
    }

    public String getSaleCloudSynId() {
        return saleCloudSynId;
    }

    public void setSaleCloudSynId(String saleCloudSynId) {
        this.saleCloudSynId = saleCloudSynId;
    }

    public List<ShopAllotDetail> getShopAllotDetailList() {
        return shopAllotDetailList;
    }

    public void setShopAllotDetailList(List<ShopAllotDetail> shopAllotDetailList) {
        this.shopAllotDetailList = shopAllotDetailList;
    }

    public List<String> getShopAllotDetailIdList() {
        return shopAllotDetailIdList;
    }

    public void setShopAllotDetailIdList(List<String> shopAllotDetailIdList) {
        this.shopAllotDetailIdList = shopAllotDetailIdList;
    }

    public Depot getFromShop() {
        return fromShop;
    }

    public void setFromShop(Depot fromShop) {
        this.fromShop = fromShop;
    }

    public Depot getToShop() {
        return toShop;
    }

    public void setToShop(Depot toShop) {
        this.toShop = toShop;
    }
}
