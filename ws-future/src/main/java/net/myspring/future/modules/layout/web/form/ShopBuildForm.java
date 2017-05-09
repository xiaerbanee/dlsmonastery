package net.myspring.future.modules.layout.web.form;

import net.myspring.common.form.DataForm;
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.layout.domain.ShopBuild;
import net.myspring.util.cahe.annotation.CacheInput;

import java.time.LocalDateTime;

/**
 * Created by zhangyf on 2017/5/6.
 */
public class ShopBuildForm extends DataForm<ShopBuild>{

    private String shopId;
    @CacheInput(inputKey = "depots", inputInstance = "shopId", outputInstance = "name")
    private String shopName;
    @CacheInput(inputKey = "depots", inputInstance = "shopId", outputInstance = "address")
    private String address;
    @CacheInput(inputKey = "depots", inputInstance = "shopId", outputInstance = "officeId")
    private String officeId;
    private String officeName;
    private String shopType;
    private String fixtureType;
    private String oldContents;
    private String newContents;
    private Integer monthSaleQty;
    private String buildType;
    private String applyAccountId;
    @CacheInput(inputKey = "accounts", inputInstance = "applyAccountId", outputInstance = "loginName")
    private String applyAccountName;
    @CacheInput(inputKey = "accounts", inputInstance = "applyAccountId", outputInstance = "loginName")
    private String applyAccountPhone;
    private String content;
    private String scenePhoto;
    private String confirmPhoto;
    private String createdByName;
    private LocalDateTime createdDate;
    private String processInstanceId;
    private String processStatus;

    public String getConfirmPhoto() {
        return confirmPhoto;
    }

    public void setConfirmPhoto(String confirmPhoto) {
        this.confirmPhoto = confirmPhoto;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getFixtureType() {
        return fixtureType;
    }

    public void setFixtureType(String fixtureType) {
        this.fixtureType = fixtureType;
    }

    public String getNewContents() {
        return newContents;
    }

    public void setNewContents(String newContents) {
        this.newContents = newContents;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getScenePhoto() {
        return scenePhoto;
    }

    public void setScenePhoto(String scenePhoto) {
        this.scenePhoto = scenePhoto;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getOldContents() {
        return oldContents;
    }

    public void setOldContents(String oldContents) {
        this.oldContents = oldContents;
    }

    public Integer getMonthSaleQty() {
        return monthSaleQty;
    }

    public void setMonthSaleQty(Integer monthSaleQty) {
        this.monthSaleQty = monthSaleQty;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getApplyAccountName() {
        return applyAccountName;
    }

    public void setApplyAccountName(String applyAccountName) {
        this.applyAccountName = applyAccountName;
    }

    public String getApplyAccountPhone() {
        return applyAccountPhone;
    }

    public void setApplyAccountPhone(String applyAccountPhone) {
        this.applyAccountPhone = applyAccountPhone;
    }
}
