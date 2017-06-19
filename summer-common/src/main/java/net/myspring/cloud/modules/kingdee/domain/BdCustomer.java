package net.myspring.cloud.modules.kingdee.domain;

import java.time.LocalDateTime;

/**
 *客户表
 * Created by lihx on 2017/4/11.
 */
public class BdCustomer {
    //客户ID--对应业务系统客户outId
    private String FCustId;
    //客户编码--对应业务系统客户outCode
    private String FNumber;
    //客户名称
    private String FName;
    //客户分组编码
    private String FPrimaryGroup;
    //客户分组名称
    private String FPrimaryGroupName;
    //修改客户时间
    private LocalDateTime FModifyDate;
    //部门ID--对应业务系统部门outId
    private String FSalDeptId;
    //审核状态
    private String FDocumentStatus;
    //禁用状态
    private String FForbidStatus;

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

    public LocalDateTime getFModifyDate() {
        return FModifyDate;
    }

    public void setFModifyDate(LocalDateTime FModifyDate) {
        this.FModifyDate = FModifyDate;
    }

    public String getFSalDeptId() {
        return FSalDeptId;
    }

    public void setFSalDeptId(String FSalDeptId) {
        this.FSalDeptId = FSalDeptId;
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