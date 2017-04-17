package net.myspring.future.modules.crm.model;

import com.google.common.collect.Lists;
import net.myspring.future.modules.layout.domain.AdApply;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by admin on 2017/3/16.
 */
public class AdApplyModel {
    private String shopId;
    private String billType;
    private String remarks;
    private String billRemarks;
    private String expressCompanyId;
    private LocalDate billDate;
    private List<AdApply> adApplyList= Lists.newArrayList();

    public String getBillRemarks() {
        return billRemarks;
    }

    public void setBillRemarks(String billRemarks) {
        this.billRemarks = billRemarks;
    }

    public String getExpressCompanyId() {
        return expressCompanyId;
    }

    public void setExpressCompanyId(String expressCompanyId) {
        this.expressCompanyId = expressCompanyId;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<AdApply> getAdApplyList() {
        return adApplyList;
    }

    public void setAdApplyList(List<AdApply> adApplyList) {
        this.adApplyList = adApplyList;
    }
}
