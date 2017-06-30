package net.myspring.future.modules.basic.web.query;

import com.google.common.collect.Lists;
import net.myspring.future.common.enums.ShipTypeEnum;
import net.myspring.future.modules.basic.web.form.PrintConfigForm;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class PrintConfigQuery {
    private String orderType;
    private Integer qty;
    private String storeNames;
    private String orderIds;
    private String pickUpType;
    private String printDate;
    private List<String> depotIdList=Lists.newArrayList();
    private String configType;

    public enum PrintPickUpType {
        自提,非自提,全部
    }

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public List<String> getDepotIdList() {
        return depotIdList;
    }

    public void setDepotIdList(List<String> depotIdList) {
        this.depotIdList = depotIdList;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getStoreNames() {
        return storeNames;
    }

    public void setStoreNames(String storeNames) {
        this.storeNames = storeNames;
    }

    public String getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(String orderIds) {
        this.orderIds = orderIds;
    }

    public String getPickUpType() {
        return pickUpType;
    }

    public void setPickUpType(String pickUpType) {
        this.pickUpType = pickUpType;
    }

    public String getPrintDate() {
        return printDate;
    }

    public void setPrintDate(String printDate) {
        this.printDate = printDate;
    }

    public List<String> getShipTypeList(){
        List<String> shipTypeList= Lists.newArrayList();
        if(PrintConfigForm.PrintPickUpType.自提.name().equals(pickUpType)) {
            shipTypeList.addAll(Arrays.asList(ShipTypeEnum.总部自提.name(),ShipTypeEnum.地区自提.name()));
        } else if (PrintConfigForm.PrintPickUpType.非自提.name().equals(pickUpType)) {
            shipTypeList.addAll(Arrays.asList(ShipTypeEnum.总部发货.name(),ShipTypeEnum.地区发货.name()));
        }
        return shipTypeList;
    }
}
