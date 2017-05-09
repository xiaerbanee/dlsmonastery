package net.myspring.cloud.modules.input.dto;

import net.myspring.cloud.modules.sys.domain.KingdeeBook;

/**
 * Created by liuj on 2016-06-20.
 */
public class K3CloudSaveDto {
    private Boolean success;
    private KingdeeBook kingdeeBook;
    private String formId;
    private String content;
    private String billNo;
    private Boolean autoAudit = true;

    public K3CloudSaveDto(String formId, String content) {
        this.formId = formId;
        this.content = content;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public KingdeeBook getKingdeeBook() {
        return kingdeeBook;
    }

    public void setKingdeeBook(KingdeeBook kingdeeBook) {
        this.kingdeeBook = kingdeeBook;
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
