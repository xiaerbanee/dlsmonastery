package net.myspring.future.modules.basic.web.query;

import net.myspring.future.common.query.BaseQuery;


public class DepotQuery extends BaseQuery {
    private String name;
    private String areaId;
    private String officeId;
    //直营，代理
    private Boolean clientIsNull;
    //pop门店
    private Boolean popShop;
    //广告门店（物料柜台对应的财务门店）
    private Boolean adShop;
    //直营仓库
    private Boolean outIdIsNull;

    private String shipType;
    private Boolean includeHidden;

    public Boolean getIncludeHidden() {
        return includeHidden;
    }

    public void setIncludeHidden(Boolean includeHidden) {
        this.includeHidden = includeHidden;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getClientIsNull() {
        return clientIsNull;
    }

    public void setClientIsNull(Boolean clientIsNull) {
        this.clientIsNull = clientIsNull;
    }

    public Boolean getPopShop() {
        return popShop;
    }

    public void setPopShop(Boolean popShop) {
        this.popShop = popShop;
    }

    public Boolean getAdShop() {
        return adShop;
    }

    public void setAdShop(Boolean adShop) {
        this.adShop = adShop;
    }


    public Boolean getOutIdIsNull() {
        return outIdIsNull;
    }

    public void setOutIdIsNull(Boolean outIdIsNull) {
        this.outIdIsNull = outIdIsNull;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }
}
