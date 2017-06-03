package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.AfterSale;

/**
 * Created by wangzm on 2017/5/12.
 */
public class AfterSaleDto extends DataDto<AfterSale> {
    private String type;
    private String badProductImeId;
    private String badProductId;
    private String badDepotId;
    private String badProductName;
    private String badDepotName;
    private String badType;
    private String packageStatus;
    private String memory;
    private String badProductIme;

    public String getBadProductImeId() {
        return badProductImeId;
    }

    public void setBadProductImeId(String badProductImeId) {
        this.badProductImeId = badProductImeId;
    }

    public String getBadProductId() {
        return badProductId;
    }

    public void setBadProductId(String badProductId) {
        this.badProductId = badProductId;
    }

    public String getBadDepotId() {
        return badDepotId;
    }

    public void setBadDepotId(String badDepotId) {
        this.badDepotId = badDepotId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBadProductName() {
        return badProductName;
    }

    public void setBadProductName(String badProductName) {
        this.badProductName = badProductName;
    }

    public String getBadDepotName() {
        return badDepotName;
    }

    public void setBadDepotName(String badDepotName) {
        this.badDepotName = badDepotName;
    }

    public String getBadType() {
        return badType;
    }

    public void setBadType(String badType) {
        this.badType = badType;
    }

    public String getPackageStatus() {
        return packageStatus;
    }

    public void setPackageStatus(String packageStatus) {
        this.packageStatus = packageStatus;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getBadProductIme() {
        return badProductIme;
    }

    public void setBadProductIme(String badProductIme) {
        this.badProductIme = badProductIme;
    }
}
