package net.myspring.general.modules.sys.domain;

/**
 *应收单
 * Created by lihx on 2017/4/11.
 */
public class ArReceivable {
    //单据No
    private String FBillNo;
    //上游单据No
    private String FSourceBillNo;
    //审核状态
    private String FDocumentStatus;

    public String getFBillNo() {
        return FBillNo;
    }

    public void setFBillNo(String FBillNo) {
        this.FBillNo = FBillNo;
    }

    public String getFSourceBillNo() {
        return FSourceBillNo;
    }

    public void setFSourceBillNo(String FSourceBillNo) {
        this.FSourceBillNo = FSourceBillNo;
    }

    public String getFDocumentStatus() {
        return FDocumentStatus;
    }

    public void setFDocumentStatus(String FDocumentStatus) {
        this.FDocumentStatus = FDocumentStatus;
    }
}
