package net.myspring.cloud.modules.kingdee.domain;

/**
 * 辅助资料
 * Created by lihx on 2017/4/11.
 */
public class BasAssistant {
    private String FEntryId;
    //编码--对应业务系统outCode
    private String FNumber;
    //名称
    private String FDataValue;
    //审核状态
    private String FDocumentStatus;
    //禁用状态
    private String FForbidStatus;

    public String getFEntryId() {
        return FEntryId;
    }

    public void setFEntryId(String FEntryId) {
        this.FEntryId = FEntryId;
    }

    public String getFNumber() {
        return FNumber;
    }

    public void setFNumber(String FNumber) {
        this.FNumber = FNumber;
    }

    public String getFDataValue() {
        return FDataValue;
    }

    public void setFDataValue(String FDataValue) {
        this.FDataValue = FDataValue;
    }

    public String getFDocumentStatus() {
        return FDocumentStatus;
    }

    public void setFDocumentStatus(String FDocumentStatus) {
        this.FDocumentStatus = FDocumentStatus;
    }

    public String getFForbidStatus() {
        return FForbidStatus;
    }

    public void setFForbidStatus(String FForbidStatus) {
        this.FForbidStatus = FForbidStatus;
    }
}
