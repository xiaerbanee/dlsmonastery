package net.myspring.future.modules.layout.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.common.enums.BillTypeEnum;
import net.myspring.future.modules.basic.web.form.ProductAdForm;
import net.myspring.future.modules.layout.domain.AdApply;
import net.myspring.future.modules.layout.dto.AdApplyDto;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by zhangyf on 2017/5/22.
 */
public class AdApplyBillForm extends BaseForm<AdApply> {

    private String billType = BillTypeEnum.POP.name();
    private LocalDate billDate = LocalDate.now();
    private String expressCompanyId;
    private String storeId;

    private List<AdApplyDetailForm> adApplyDetailForms;


    public List<AdApplyDetailForm> getAdApplyDetailForms() {
        return adApplyDetailForms;
    }

    public void setAdApplyDetailForms(List<AdApplyDetailForm> adApplyDetailForms) {
        this.adApplyDetailForms = adApplyDetailForms;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
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

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

}
