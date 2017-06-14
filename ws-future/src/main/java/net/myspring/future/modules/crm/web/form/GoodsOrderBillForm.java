package net.myspring.future.modules.crm.web.form;

import com.google.common.collect.Lists;
import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.dto.ExpressCompanyDto;
import net.myspring.future.modules.crm.domain.GoodsOrder;

import java.time.LocalDate;
import java.util.List;

public class GoodsOrderBillForm extends BaseForm<GoodsOrder> {
    private String storeId;
    private LocalDate billDate;
    private String expressCompanyId;
    private Boolean syn;

    private String contator;
    private String address;
    private String mobilePhone;

    private String remarks;

    private List<GoodsOrderBillDetailForm> goodsOrderBillDetailFormList = Lists.newArrayList();

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
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

    public Boolean getSyn() {
        return syn;
    }

    public void setSyn(Boolean syn) {
        this.syn = syn;
    }

    public String getContator() {
        return contator;
    }

    public void setContator(String contator) {
        this.contator = contator;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    @Override
    public String getRemarks() {
        return remarks;
    }

    @Override
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<GoodsOrderBillDetailForm> getGoodsOrderBillDetailFormList() {
        return goodsOrderBillDetailFormList;
    }

    public void setGoodsOrderBillDetailFormList(List<GoodsOrderBillDetailForm> goodsOrderBillDetailFormList) {
        this.goodsOrderBillDetailFormList = goodsOrderBillDetailFormList;
    }
}
