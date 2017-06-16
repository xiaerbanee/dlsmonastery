package net.myspring.general.modules.sys.domain;


/**
 * Created by lihx on 2017/4/6.
 */
public class BdDepartment {
    private String FDeptId;
    private String FNumber;
    private String FFullName;
    //审核状态
    private String FDocumentStatus;
    //禁用状态
    private String FForbidStatus;

    public String getFDeptId() {
        return FDeptId;
    }

    public void setFDeptId(String FDeptId) {
        this.FDeptId = FDeptId;
    }

    public String getFNumber() {
        return FNumber;
    }

    public void setFNumber(String FNumber) {
        this.FNumber = FNumber;
    }

    public String getFFullName() {
        return FFullName;
    }

    public void setFFullName(String FFullName) {
        this.FFullName = FFullName;
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
