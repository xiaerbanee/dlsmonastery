package net.myspring.future.modules.layout.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.layout.domain.AdGoodsOrder;

import java.time.LocalDate;
import java.util.List;


public class AdGoodsOrderBillForm extends BaseForm<AdGoodsOrder> {

    private LocalDate billDate;
    private String storeId;
    private String expressOrderContactor;
    private String expressOrderAddress;
    private String billAddress;
    private String expressOrderExpressCompanyId;
    private Boolean splitBill;
    private String expressOrderMobilePhone;
    private Boolean syn;
    private String billRemarks;

    private List<AdGoodsOrderBillDetailForm> adGoodsOrderDetailList;

    public List<AdGoodsOrderBillDetailForm> getAdGoodsOrderDetailList() {
        return adGoodsOrderDetailList;
    }

    public void setAdGoodsOrderDetailList(List<AdGoodsOrderBillDetailForm> adGoodsOrderDetailList) {
        this.adGoodsOrderDetailList = adGoodsOrderDetailList;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getExpressOrderContactor() {
        return expressOrderContactor;
    }

    public void setExpressOrderContactor(String expressOrderContactor) {
        this.expressOrderContactor = expressOrderContactor;
    }

    public String getExpressOrderAddress() {
        return expressOrderAddress;
    }

    public void setExpressOrderAddress(String expressOrderAddress) {
        this.expressOrderAddress = expressOrderAddress;
    }

    public String getBillAddress() {
        return billAddress;
    }

    public void setBillAddress(String billAddress) {
        this.billAddress = billAddress;
    }

    public String getExpressOrderExpressCompanyId() {
        return expressOrderExpressCompanyId;
    }

    public void setExpressOrderExpressCompanyId(String expressOrderExpressCompanyId) {
        this.expressOrderExpressCompanyId = expressOrderExpressCompanyId;
    }

    public Boolean getSplitBill() {
        return splitBill;
    }

    public void setSplitBill(Boolean splitBill) {
        this.splitBill = splitBill;
    }

    public String getExpressOrderMobilePhone() {
        return expressOrderMobilePhone;
    }

    public void setExpressOrderMobilePhone(String expressOrderMobilePhone) {
        this.expressOrderMobilePhone = expressOrderMobilePhone;
    }

    public Boolean getSyn() {
        return syn;
    }

    public void setSyn(Boolean syn) {
        this.syn = syn;
    }

    public String getBillRemarks() {
        return billRemarks;
    }

    public void setBillRemarks(String billRemarks) {
        this.billRemarks = billRemarks;
    }
}
