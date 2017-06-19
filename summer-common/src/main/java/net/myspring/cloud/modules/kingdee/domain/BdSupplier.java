package net.myspring.cloud.modules.kingdee.domain;

/**
 * Created by lihx on 2017/4/11.
 */
public class BdSupplier{
    //对应业务系统的outId
    private String FSupplierId;
    //对应业务系统的outCode
    private String FNumber;
    //
    private String FName;
    //审核状态
    private String FDocumentStatus;
    //禁用状态
    private String FForbidStatus;

    public String getFSupplierId() {
        return FSupplierId;
    }

    public void setFSupplierId(String FSupplierId) {
        this.FSupplierId = FSupplierId;
    }

    public String getFNumber() {
        return FNumber;
    }

    public void setFNumber(String FNumber) {
        this.FNumber = FNumber;
    }

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
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
