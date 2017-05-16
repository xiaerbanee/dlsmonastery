package net.myspring.future.modules.crm.web.query;

import net.myspring.future.common.query.BaseQuery;

import java.time.LocalDateTime;
import java.util.List;

public class GoodsOrderDetailQuery extends BaseQuery {

    private String goodsOrderId;
    private String pricesystemId;
    private String netType;
    private Boolean showAll;
    private String areaId;

    //用来计算该区间内的办事处已订货数
    private LocalDateTime createDateStart;
    private LocalDateTime createDateEnd;

    //用来计算该区间内的办事处已开单数
    private LocalDateTime billDateStart;
    private LocalDateTime billDateEnd;

    private List<String> shipTypeList;


    public String getGoodsOrderId() {
        return goodsOrderId;
    }

    public void setGoodsOrderId(String goodsOrderId) {
        this.goodsOrderId = goodsOrderId;
    }

    public String getPricesystemId() {
        return pricesystemId;
    }

    public void setPricesystemId(String pricesystemId) {
        this.pricesystemId = pricesystemId;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public Boolean getShowAll() {
        return showAll;
    }

    public void setShowAll(Boolean showAll) {
        this.showAll = showAll;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public LocalDateTime getCreateDateStart() {
        return createDateStart;
    }

    public void setCreateDateStart(LocalDateTime createDateStart) {
        this.createDateStart = createDateStart;
    }

    public LocalDateTime getCreateDateEnd() {
        return createDateEnd;
    }

    public void setCreateDateEnd(LocalDateTime createDateEnd) {
        this.createDateEnd = createDateEnd;
    }

    public LocalDateTime getBillDateStart() {
        return billDateStart;
    }

    public void setBillDateStart(LocalDateTime billDateStart) {
        this.billDateStart = billDateStart;
    }

    public LocalDateTime getBillDateEnd() {
        return billDateEnd;
    }

    public void setBillDateEnd(LocalDateTime billDateEnd) {
        this.billDateEnd = billDateEnd;
    }

    public List<String> getShipTypeList() {
        return shipTypeList;
    }

    public void setShipTypeList(List<String> shipTypeList) {
        this.shipTypeList = shipTypeList;
    }
}
