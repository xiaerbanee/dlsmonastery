package net.myspring.future.modules.crm.domain;


import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name="crm_shop_build")
public class ShopBuild extends DataEntity<ShopBuild> {
    private String shopId;
    private String shopType;
    private LocalDate openDate;
    private String fixtureType;
    private BigDecimal oldLength;
    private BigDecimal oldWidth;
    private BigDecimal newLength;
    private BigDecimal newWidth;
    private Integer monthSaleQty;
    private String openType;
    private String buildType;
    private String applyAccountId;
    private String nearBrand;
    private String mainSaleBrand;
    private String scenePhoto;
    private String confirmPhoto;
    private String content;
    private Integer version = 0;
    private String processInstanceId;
    private String processStatus;
    private String oldContents;
    private String newContents;
    private Boolean isUrgent;
    private ExpressCompany expressCompany;
    private String expressCompanyId;
    private String processTypeId;
    private String processFlowId;
    private Depot shop;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public LocalDate getOpenDate() {
        return openDate;
    }

    public void setOpenDate(LocalDate openDate) {
        this.openDate = openDate;
    }

    public String getFixtureType() {
        return fixtureType;
    }

    public void setFixtureType(String fixtureType) {
        this.fixtureType = fixtureType;
    }

    public BigDecimal getOldLength() {
        return oldLength;
    }

    public void setOldLength(BigDecimal oldLength) {
        this.oldLength = oldLength;
    }

    public BigDecimal getOldWidth() {
        return oldWidth;
    }

    public void setOldWidth(BigDecimal oldWidth) {
        this.oldWidth = oldWidth;
    }

    public BigDecimal getNewLength() {
        return newLength;
    }

    public void setNewLength(BigDecimal newLength) {
        this.newLength = newLength;
    }

    public BigDecimal getNewWidth() {
        return newWidth;
    }

    public void setNewWidth(BigDecimal newWidth) {
        this.newWidth = newWidth;
    }

    public Integer getMonthSaleQty() {
        return monthSaleQty;
    }

    public void setMonthSaleQty(Integer monthSaleQty) {
        this.monthSaleQty = monthSaleQty;
    }

    public String getOpenType() {
        return openType;
    }

    public void setOpenType(String openType) {
        this.openType = openType;
    }

    public String getBuildType() {
        return buildType;
    }

    public void setBuildType(String buildType) {
        this.buildType = buildType;
    }

    public String getApplyAccountId() {
        return applyAccountId;
    }

    public void setApplyAccountId(String applyAccountId) {
        this.applyAccountId = applyAccountId;
    }

    public String getNearBrand() {
        return nearBrand;
    }

    public void setNearBrand(String nearBrand) {
        this.nearBrand = nearBrand;
    }

    public String getMainSaleBrand() {
        return mainSaleBrand;
    }

    public void setMainSaleBrand(String mainSaleBrand) {
        this.mainSaleBrand = mainSaleBrand;
    }

    public String getScenePhoto() {
        return scenePhoto;
    }

    public void setScenePhoto(String scenePhoto) {
        this.scenePhoto = scenePhoto;
    }

    public String getConfirmPhoto() {
        return confirmPhoto;
    }

    public void setConfirmPhoto(String confirmPhoto) {
        this.confirmPhoto = confirmPhoto;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getOldContents() {
        return oldContents;
    }

    public void setOldContents(String oldContents) {
        this.oldContents = oldContents;
    }

    public String getNewContents() {
        return newContents;
    }

    public void setNewContents(String newContents) {
        this.newContents = newContents;
    }

    public Boolean getUrgent() {
        return isUrgent;
    }

    public void setUrgent(Boolean urgent) {
        isUrgent = urgent;
    }

    public ExpressCompany getExpressCompany() {
        return expressCompany;
    }

    public void setExpressCompany(ExpressCompany expressCompany) {
        this.expressCompany = expressCompany;
    }

    public String getExpressCompanyId() {
        return expressCompanyId;
    }

    public void setExpressCompanyId(String expressCompanyId) {
        this.expressCompanyId = expressCompanyId;
    }

    public String getProcessTypeId() {
        return processTypeId;
    }

    public void setProcessTypeId(String processTypeId) {
        this.processTypeId = processTypeId;
    }

    public String getProcessFlowId() {
        return processFlowId;
    }

    public void setProcessFlowId(String processFlowId) {
        this.processFlowId = processFlowId;
    }

    public Depot getShop() {
        return shop;
    }

    public void setShop(Depot shop) {
        this.shop = shop;
    }
}
