package net.myspring.cloud.modules.input.dto;


import net.myspring.cloud.common.utils.ThreadLocalContext;
import net.myspring.cloud.modules.sys.dto.AccountDto;

/**
 * Created by liuj on 2016-06-20.
 */
public class K3CloudSave {
    private AccountDto accountDto;
    private String formId;
    private String content;
    private String billNo;
    private Boolean autoAudit = true;

    public K3CloudSave(String formId, String content) {
        this.formId = formId;
        this.content = content;
    }

    public K3CloudSave(String formId, String content, AccountDto accountDto) {
        this.formId = formId;
        this.content = content;
        this.setAccountDto(accountDto);
    }

    public AccountDto getAccount() {
        if (accountDto == null) {
            accountDto = ThreadLocalContext.get().getAccountDto();
        }
        return accountDto;
    }

    public void setAccountDto(AccountDto account) {
        this.accountDto = account;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Boolean getAutoAudit() {
        return autoAudit;
    }

    public void setAutoAudit(Boolean autoAudit) {
        this.autoAudit = autoAudit;
    }
}
