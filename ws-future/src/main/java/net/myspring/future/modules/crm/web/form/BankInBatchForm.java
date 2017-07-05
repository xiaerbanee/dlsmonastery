package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.crm.domain.BankIn;

import java.util.List;

public class BankInBatchForm extends BaseForm<BankIn> {

    private List<BankInBatchDetailForm> bankInBatchDetailFormList;

    public List<BankInBatchDetailForm> getBankInBatchDetailFormList() {
        return bankInBatchDetailFormList;
    }

    public void setBankInBatchDetailFormList(List<BankInBatchDetailForm> bankInBatchDetailFormList) {
        this.bankInBatchDetailFormList = bankInBatchDetailFormList;
    }
}
