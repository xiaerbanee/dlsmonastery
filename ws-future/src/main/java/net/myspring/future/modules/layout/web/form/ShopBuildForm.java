package net.myspring.future.modules.layout.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.layout.domain.ShopBuild;

/**
 * Created by zhangyf on 2017/5/6.
 */
public class ShopBuildForm extends BaseForm<ShopBuild> {

    private String shopId;
    private String shopType;
    private String fixtureType;
    private String oldContents;
    private String newContents;
    private Integer monthSaleQty;
    private String buildType;
    private String applyAccountId;

    private String content;
    private String scenePhoto;
    private String shopAgreement;
    private String confirmPhoto;
    private String investInCause;

    public String getShopAgreement() {
        return shopAgreement;
    }

    public void setShopAgreement(String shopAgreement) {
        this.shopAgreement = shopAgreement;
    }

    public String getInvestInCause() {
        return investInCause;
    }

    public void setInvestInCause(String investInCause) {
        this.investInCause = investInCause;
    }

    public String getConfirmPhoto() {
        return confirmPhoto;
    }

    public void setConfirmPhoto(String confirmPhoto) {
        this.confirmPhoto = confirmPhoto;
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
}
