package net.myspring.cloud.modules.kingdee.domain;

import java.time.LocalDate;
/**
 *客户表
 * Created by lihx on 2017/4/11.
 */
public class BdCustomer {
    //客户ID
    private String FCustId;
    //客户编码
    private String FNumber;
    //客户名称
    private String FName;
    //客户分组编码
    private String FPrimaryGroup;
    //客户分组名称
    private String FPrimaryGroupName;
    //修改客户时间
    private LocalDate FModifyDate;
    //部门ID
    private String FSALDEPTID;

    public String getFCustId() {
        return FCustId;
    }

    public void setFCustId(String FCustId) {
        this.FCustId = FCustId;
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

    public String getFPrimaryGroup() {
        return FPrimaryGroup;
    }

    public void setFPrimaryGroup(String FPrimaryGroup) {
        this.FPrimaryGroup = FPrimaryGroup;
    }

    public String getFPrimaryGroupName() {
        return FPrimaryGroupName;
    }

    public void setFPrimaryGroupName(String FPrimaryGroupName) {
        this.FPrimaryGroupName = FPrimaryGroupName;
    }

    public LocalDate getFModifyDate() {
        return FModifyDate;
    }

    public void setFModifyDate(LocalDate FModifyDate) {
        this.FModifyDate = FModifyDate;
    }

    public String getFSALDEPTID() {
        return FSALDEPTID;
    }

    public void setFSALDEPTID(String FSALDEPTID) {
        this.FSALDEPTID = FSALDEPTID;
    }
}