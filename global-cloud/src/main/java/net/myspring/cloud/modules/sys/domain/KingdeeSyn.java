package net.myspring.cloud.modules.sys.domain;

import net.myspring.cloud.common.domain.CompanyEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by lihx on 2017/6/21.
 */
@Entity
@Table(name="sys_kingdee_syn")
public class KingdeeSyn extends CompanyEntity<KingdeeSyn> {
    private String extendId;
    private String extendType;
    private String formId;
    private String nextFormId;
    private String content;
    private Boolean success;
    private String result;
    private String billNo;
    private String nextBillNo;
    private Boolean autoAudit = true;
    private String kingdeeBookId;//
    private Integer version = 0;

    public String getExtendId() {
        return extendId;
    }

    public void setExtendId(String extendId) {
        this.extendId = extendId;
    }

    public String getExtendType() {
        return extendType;
    }

    public void setExtendType(String extendType) {
        this.extendType = extendType;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getNextFormId() {
        return nextFormId;
    }

    public void setNextFormId(String nextFormId) {
        this.nextFormId = nextFormId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getNextBillNo() {
        return nextBillNo;
    }

    public void setNextBillNo(String nextBillNo) {
        this.nextBillNo = nextBillNo;
    }

    public Boolean getAutoAudit() {
        return autoAudit;
    }

    public void setAutoAudit(Boolean autoAudit) {
        this.autoAudit = autoAudit;
    }

    public String getKingdeeBookId() {
        return kingdeeBookId;
    }

    public void setKingdeeBookId(String kingdeeBookId) {
        this.kingdeeBookId = kingdeeBookId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
