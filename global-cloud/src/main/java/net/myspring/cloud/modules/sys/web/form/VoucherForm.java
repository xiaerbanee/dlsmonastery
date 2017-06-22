package net.myspring.cloud.modules.sys.web.form;

import com.google.common.collect.Lists;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.cloud.modules.sys.domain.Voucher;
import net.myspring.cloud.modules.sys.dto.VoucherEntryDto;
import net.myspring.common.form.BaseForm;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by lihx on 2017/4/12.
 */
public class VoucherForm extends BaseForm<Voucher> {
    private String FDate;
    private String createdName;
    private String companyId;
    private String status;
    private String outCode;
    private String kingdeeBookId;


    private LocalDate billDate;
    private String json;

    public String getFDate() {
        return FDate;
    }

    public void setFDate(String FDate) {
        this.FDate = FDate;
    }

    public String getCreatedName() {
        return createdName;
    }

    public void setCreatedName(String createdName) {
        this.createdName = createdName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOutCode() {
        return outCode;
    }

    public void setOutCode(String outCode) {
        this.outCode = outCode;
    }

    public String getKingdeeBookId() {
        return kingdeeBookId;
    }

    public void setKingdeeBookId(String kingdeeBookId) {
        this.kingdeeBookId = kingdeeBookId;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
