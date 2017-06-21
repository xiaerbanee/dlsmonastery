package net.myspring.cloud.modules.sys.dto;

import net.myspring.cloud.common.dto.DataDto;
import net.myspring.cloud.modules.sys.domain.KingdeeSyn;

/**
 * Created by lihx on 2017/6/21.
 */
public class KingdeeSynDto extends DataDto<KingdeeSyn>{
    private String extendId;
    private String extendType;
    private String formId;
    private String nextFormId;
    private String content;
    private boolean success;
    private String result;
    private String billNo;
    private String nextBillNo;
    private boolean autoAudit;
    private String kingdeeBookId;
    private String companyId;

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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
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

    public boolean isAutoAudit() {
        return autoAudit;
    }

    public void setAutoAudit(boolean autoAudit) {
        this.autoAudit = autoAudit;
    }

    public String getKingdeeBookId() {
        return kingdeeBookId;
    }

    public void setKingdeeBookId(String kingdeeBookId) {
        this.kingdeeBookId = kingdeeBookId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
