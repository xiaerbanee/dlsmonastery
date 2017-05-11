package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.DataForm;
import net.myspring.future.modules.crm.domain.Express;

import java.math.BigDecimal;

public class ExpressForm extends DataForm<Express> {


    private String expressOrderExtendType;
    private String expressOrderId;
    private String expressOrderToDepotId;
    private String code;
    private BigDecimal weight;
    private Integer qty;
    private String tracking;


    public String getExpressOrderExtendType() {
        return expressOrderExtendType;
    }

    public void setExpressOrderExtendType(String expressOrderExtendType) {
        this.expressOrderExtendType = expressOrderExtendType;
    }

    public String getExpressOrderId() {
        return expressOrderId;
    }

    public void setExpressOrderId(String expressOrderId) {
        this.expressOrderId = expressOrderId;
    }

    public String getExpressOrderToDepotId() {
        return expressOrderToDepotId;
    }

    public void setExpressOrderToDepotId(String expressOrderToDepotId) {
        this.expressOrderToDepotId = expressOrderToDepotId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }





}
