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
    private LocalDate billDate;
    private String json;


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
