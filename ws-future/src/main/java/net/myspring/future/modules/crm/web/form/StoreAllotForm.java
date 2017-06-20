package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.crm.domain.StoreAllot;

import java.util.ArrayList;
import java.util.List;

public class StoreAllotForm extends BaseForm<StoreAllot> {

    private String allotType;

    private String fromStoreId;
    private String toStoreId;
    private String shipType;
    private String expressCompanyId;

    private Boolean syn = Boolean.TRUE;  //默认同步至财务
    private List<String> shipTypeList = new ArrayList<>();
    private List<String> allotTypeList = new ArrayList<>();
    private List<StoreAllotDetailForm> storeAllotDetailList = new ArrayList<>();
    private Boolean showAllotType = Boolean.TRUE;

    public String getAllotType() {
        return allotType;
    }

    public void setAllotType(String allotType) {
        this.allotType = allotType;
    }

    public Boolean getShowAllotType() {
        return showAllotType;
    }

    public void setShowAllotType(Boolean showAllotType) {
        this.showAllotType = showAllotType;
    }

    public Boolean getSyn() {
        return syn;
    }

    public void setSyn(Boolean syn) {
        this.syn = syn;
    }

    public String getFromStoreId() {
        return fromStoreId;
    }

    public void setFromStoreId(String fromStoreId) {
        this.fromStoreId = fromStoreId;
    }

    public String getToStoreId() {
        return toStoreId;
    }

    public void setToStoreId(String toStoreId) {
        this.toStoreId = toStoreId;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public String getExpressCompanyId() {
        return expressCompanyId;
    }

    public void setExpressCompanyId(String expressCompanyId) {
        this.expressCompanyId = expressCompanyId;
    }



    public List<String> getShipTypeList() {
        return shipTypeList;
    }

    public void setShipTypeList(List<String> shipTypeList) {
        this.shipTypeList = shipTypeList;
    }

    public List<String> getAllotTypeList() {
        return allotTypeList;
    }

    public void setAllotTypeList(List<String> allotTypeList) {
        this.allotTypeList = allotTypeList;
    }

    public List<StoreAllotDetailForm> getStoreAllotDetailList() {
        return storeAllotDetailList;
    }

    public void setStoreAllotDetailList(List<StoreAllotDetailForm> storeAllotDetailList) {
        this.storeAllotDetailList = storeAllotDetailList;
    }


}
