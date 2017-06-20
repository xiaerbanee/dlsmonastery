package net.myspring.cloud.modules.input.web.query;

import net.myspring.cloud.common.enums.StkMisDeliveryTypeEnum;

import java.util.List;

/**
 * Created by lihx on 2017/5/17.
 */
public class StkMisDeliveryQuery {
    private List<String> materialNameList;
    private List<String> materialNumberList;
    private List<String> stockNameList;
    private StkMisDeliveryTypeEnum[] stkMisDeliveryTypeEnums;

    public List<String> getMaterialNameList() {
        return materialNameList;
    }

    public void setMaterialNameList(List<String> materialNameList) {
        this.materialNameList = materialNameList;
    }

    public List<String> getMaterialNumberList() {
        return materialNumberList;
    }

    public void setMaterialNumberList(List<String> materialNumberList) {
        this.materialNumberList = materialNumberList;
    }

    public List<String> getStockNameList() {
        return stockNameList;
    }

    public void setStockNameList(List<String> stockNameList) {
        this.stockNameList = stockNameList;
    }

    public StkMisDeliveryTypeEnum[] getStkMisDeliveryTypeEnums() {
        return stkMisDeliveryTypeEnums;
    }

    public void setStkMisDeliveryTypeEnums(StkMisDeliveryTypeEnum[] stkMisDeliveryTypeEnums) {
        this.stkMisDeliveryTypeEnums = stkMisDeliveryTypeEnums;
    }
}
