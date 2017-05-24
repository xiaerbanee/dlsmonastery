package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.DataForm;
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.dto.ExpressCompanyDto;
import net.myspring.future.modules.crm.domain.GoodsOrder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GoodsOrderBillForm extends DataForm<GoodsOrder> {
    private String storeId;
    private LocalDate billDate;
    private String expressCompanyId;
    private Boolean syn;

    private String expressContator;
    private String expressAddress;
    private String expressMobilePhone;

    private List<DepotDto> storeList;
    private List<ExpressCompanyDto> expressCompanyList;
    private List<String> notDepotStoreIdList;

    private List<GoodsOrderBillDetailForm> goodsOrderDetailList = new ArrayList<>();


    public List<GoodsOrderBillDetailForm> getGoodsOrderDetailList() {
        return goodsOrderDetailList;
    }

    public void setGoodsOrderDetailList(List<GoodsOrderBillDetailForm> goodsOrderDetailList) {
        this.goodsOrderDetailList = goodsOrderDetailList;
    }

    public List<String> getNotDepotStoreIdList() {
        return notDepotStoreIdList;
    }

    public void setNotDepotStoreIdList(List<String> notDepotStoreIdList) {
        this.notDepotStoreIdList = notDepotStoreIdList;
    }

    public String getExpressContator() {
        return expressContator;
    }

    public void setExpressContator(String expressContator) {
        this.expressContator = expressContator;
    }

    public String getExpressAddress() {
        return expressAddress;
    }

    public void setExpressAddress(String expressAddress) {
        this.expressAddress = expressAddress;
    }

    public String getExpressMobilePhone() {
        return expressMobilePhone;
    }

    public void setExpressMobilePhone(String expressMobilePhone) {
        this.expressMobilePhone = expressMobilePhone;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public List<DepotDto> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<DepotDto> storeList) {
        this.storeList = storeList;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public String getExpressCompanyId() {
        return expressCompanyId;
    }

    public void setExpressCompanyId(String expressCompanyId) {
        this.expressCompanyId = expressCompanyId;
    }

    public List<ExpressCompanyDto> getExpressCompanyList() {
        return expressCompanyList;
    }

    public void setExpressCompanyList(List<ExpressCompanyDto> expressCompanyList) {
        this.expressCompanyList = expressCompanyList;
    }

    public Boolean getSyn() {
        return syn;
    }

    public void setSyn(Boolean syn) {
        this.syn = syn;
    }





}
