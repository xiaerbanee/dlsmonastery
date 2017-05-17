package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.DataForm;
import net.myspring.future.modules.crm.domain.BankIn;
import net.myspring.future.modules.crm.dto.BankInDto;

import java.time.LocalDate;

public class BankInDetailForm extends DataForm<BankIn> {

    private BankInDto bankInDto;
    private String syn = "1";
    private String pass = "0";
    private String auditRemarks;
    private LocalDate billDate;


    public String getSyn() {
        return syn;
    }

    public void setSyn(String syn) {
        this.syn = syn;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public BankInDto getBankInDto() {
        return bankInDto;
    }

    public void setBankInDto(BankInDto bankInDto) {
        this.bankInDto = bankInDto;
    }


    public String getAuditRemarks() {
        return auditRemarks;
    }

    public void setAuditRemarks(String auditRemarks) {
        this.auditRemarks = auditRemarks;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }
}
