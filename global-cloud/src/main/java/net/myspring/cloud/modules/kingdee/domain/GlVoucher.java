package net.myspring.cloud.modules.kingdee.domain;

/**
 * 凭证
 * Created by lihx on 2017/7/4.
 */
public class GlVoucher {
    //凭证号
    private String FVoucherGroupNo;
    //序号
    private String FBillNO;
    //审核状态
    private String FDocumentStatus;

    public String getFVoucherGroupNo() {
        return FVoucherGroupNo;
    }

    public void setFVoucherGroupNo(String FVoucherGroupNo) {
        this.FVoucherGroupNo = FVoucherGroupNo;
    }

    public String getFBillNO() {
        return FBillNO;
    }

    public void setFBillNO(String FBillNO) {
        this.FBillNO = FBillNO;
    }

    public String getFDocumentStatus() {
        return FDocumentStatus;
    }

    public void setFDocumentStatus(String FDocumentStatus) {
        this.FDocumentStatus = FDocumentStatus;
    }
}
