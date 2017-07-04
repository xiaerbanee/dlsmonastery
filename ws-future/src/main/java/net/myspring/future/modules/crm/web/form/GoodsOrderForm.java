package net.myspring.future.modules.crm.web.form;

import com.google.common.collect.Lists;
import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.crm.domain.GoodsOrder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class GoodsOrderForm extends BaseForm<GoodsOrder> {

    private boolean syn;
    private String storeId;
    private String shopId;
    private String status;
    private String pullStatus;
    private String outCode;
    private BigDecimal amount;
    private String shipType;
    private String netType;
    private String expressOrderId;
    private String businessId;
    private String allotFromStockCode;
    private String allotToStockCode;
    private String carrierCodes;
    private String detailJson;
    private BigDecimal goodsDeposit;
    private LocalDate billDate;
    private Boolean lxMallOrder;
    private List<String> netTypeList;
    private List<String> shipTypeList;
    private String carrierShopId;

    public boolean isSyn() {
        return syn;
    }

    public String getCarrierShopId() {
        return carrierShopId;
    }

    public void setCarrierShopId(String carrierShopId) {
        this.carrierShopId = carrierShopId;
    }

    public Boolean getLxMallOrder() {
        return lxMallOrder;
    }

    public void setLxMallOrder(Boolean lxMallOrder) {
        this.lxMallOrder = lxMallOrder;
    }

    public String getAllotToStockCode() {
        return allotToStockCode;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public void setAllotToStockCode(String allotToStockCode) {
        this.allotToStockCode = allotToStockCode;
    }

    public BigDecimal getGoodsDeposit() {
        return goodsDeposit;
    }

    public void setGoodsDeposit(BigDecimal goodsDeposit) {
        this.goodsDeposit = goodsDeposit;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getAllotFromStockCode() {
        return allotFromStockCode;
    }

    public void setAllotFromStockCode(String allotFromStockCode) {
        this.allotFromStockCode = allotFromStockCode;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPullStatus() {
        return pullStatus;
    }

    public void setPullStatus(String pullStatus) {
        this.pullStatus = pullStatus;
    }

    public String getOutCode() {
        return outCode;
    }

    public void setOutCode(String outCode) {
        this.outCode = outCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean getSyn() {
        return syn;
    }

    public void setSyn(boolean syn) {
        this.syn = syn;
    }

    private List<GoodsOrderDetailForm> goodsOrderDetailFormList = Lists.newArrayList();

    public String getExpressOrderId() {
        return expressOrderId;
    }

    public void setExpressOrderId(String expressOrderId) {
        this.expressOrderId = expressOrderId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public List<String> getNetTypeList() {
        return netTypeList;
    }

    public void setNetTypeList(List<String> netTypeList) {
        this.netTypeList = netTypeList;
    }

    public List<String> getShipTypeList() {
        return shipTypeList;
    }

    public void setShipTypeList(List<String> shipTypeList) {
        this.shipTypeList = shipTypeList;
    }

    public List<GoodsOrderDetailForm> getGoodsOrderDetailFormList() {
        return goodsOrderDetailFormList;
    }

    public void setGoodsOrderDetailFormList(List<GoodsOrderDetailForm> goodsOrderDetailFormList) {
        this.goodsOrderDetailFormList = goodsOrderDetailFormList;
    }

    public String getCarrierCodes() {
        return carrierCodes;
    }

    public void setCarrierCodes(String carrierCodes) {
        this.carrierCodes = carrierCodes;
    }

    public String getDetailJson() {
        return detailJson;
    }

    public void setDetailJson(String detailJson) {
        this.detailJson = detailJson;
    }
}
