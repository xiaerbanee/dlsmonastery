package net.myspring.cloud.modules.input.dto;

import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeSyn;
import net.myspring.common.dto.DataDto;

/**
 * Created by liuj on 2016-06-20.
 */
public class KingdeeSynDto extends DataDto<KingdeeSyn> {
    //业务系统单据Id
    private String extendId;
    //业务系统单据类型
    private String extendType;
    private Boolean success;
    private String formId;
    private String content;
    private String billNo;
    private String result;
    private Boolean autoAudit = true;
    private KingdeeBook kingdeeBook;

    public KingdeeSynDto(String extendId,String extendType,String formId, String content,KingdeeBook kingdeeBook) {
        this.extendId = extendId;
        this.extendType = extendType;
        this.formId = formId;
        this.content = content;
        this.kingdeeBook = kingdeeBook;
    }

    public KingdeeSynDto(Boolean success, String result) {
        this.success = success;
        this.result = result;
    }

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

    public KingdeeSynDto() {
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Boolean getAutoAudit() {
        return autoAudit;
    }

    public void setAutoAudit(Boolean autoAudit) {
        this.autoAudit = autoAudit;
    }
    public KingdeeBook getKingdeeBook() {
        return kingdeeBook;
    }

    public void setKingdeeBook(KingdeeBook kingdeeBook) {
        this.kingdeeBook = kingdeeBook;
    }

    public String getKingdeeBookId() {
        if (kingdeeBook != null){
            return kingdeeBook.getId();
        }
        return null;
    }
}
