package net.myspring.future.modules.layout.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.layout.domain.AdGoodsOrder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


public class AdGoodsOrderShipForm extends BaseForm<AdGoodsOrder> {

    private Integer smallQty;
    private Integer mediumQty;
    private Integer largeQty;
    private String expressOrderExpressCodes;
    private String expressOrderExpressCompanyId;

    private BigDecimal expressOrderShouldGet;
    private BigDecimal expressOrderRealPay;
    private BigDecimal expressOrderShouldPay;

    private List<AdGoodsOrderShipDetailForm> adGoodsOrderDetailList;

    public String getExpressOrderExpressCompanyId() {
        return expressOrderExpressCompanyId;
    }

    public void setExpressOrderExpressCompanyId(String expressOrderExpressCompanyId) {
        this.expressOrderExpressCompanyId = expressOrderExpressCompanyId;
    }

    public BigDecimal getExpressOrderShouldPay() {
        return expressOrderShouldPay;
    }

    public void setExpressOrderShouldPay(BigDecimal expressOrderShouldPay) {
        this.expressOrderShouldPay = expressOrderShouldPay;
    }

    public Integer getSmallQty() {
        return smallQty;
    }

    public void setSmallQty(Integer smallQty) {
        this.smallQty = smallQty;
    }

    public Integer getMediumQty() {
        return mediumQty;
    }

    public void setMediumQty(Integer mediumQty) {
        this.mediumQty = mediumQty;
    }

    public Integer getLargeQty() {
        return largeQty;
    }

    public void setLargeQty(Integer largeQty) {
        this.largeQty = largeQty;
    }

    public String getExpressOrderExpressCodes() {
        return expressOrderExpressCodes;
    }

    public void setExpressOrderExpressCodes(String expressOrderExpressCodes) {
        this.expressOrderExpressCodes = expressOrderExpressCodes;
    }

    public BigDecimal getExpressOrderShouldGet() {
        return expressOrderShouldGet;
    }

    public void setExpressOrderShouldGet(BigDecimal expressOrderShouldGet) {
        this.expressOrderShouldGet = expressOrderShouldGet;
    }

    public BigDecimal getExpressOrderRealPay() {
        return expressOrderRealPay;
    }

    public void setExpressOrderRealPay(BigDecimal expressOrderRealPay) {
        this.expressOrderRealPay = expressOrderRealPay;
    }

    public List<AdGoodsOrderShipDetailForm> getAdGoodsOrderDetailList() {
        return adGoodsOrderDetailList;
    }

    public void setAdGoodsOrderDetailList(List<AdGoodsOrderShipDetailForm> adGoodsOrderDetailList) {
        this.adGoodsOrderDetailList = adGoodsOrderDetailList;
    }
}
