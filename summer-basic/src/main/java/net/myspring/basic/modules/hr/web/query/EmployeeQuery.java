package net.myspring.basic.modules.hr.web.query;

import java.time.LocalDateTime;

/**
 * Created by lihx on 2017/4/7.
 */
public class EmployeeQuery {
    private String name;
    private String status;
    private String mobilePhone;
    private String entryDateStart;
    private String entryDateEnd;
    private String regularDateStart;
    private String regularDateEnd;
    private String leaveDateStart;
    private String leaveDateEnd;
    private String positionId;
    private String leaderName;
    private String officeId;
    private String officeIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEntryDateStart() {
        return entryDateStart;
    }

    public void setEntryDateStart(String entryDateStart) {
        this.entryDateStart = entryDateStart;
    }

    public String getEntryDateEnd() {
        return entryDateEnd;
    }

    public void setEntryDateEnd(String entryDateEnd) {
        this.entryDateEnd = entryDateEnd;
    }

    public String getRegularDateStart() {
        return regularDateStart;
    }

    public void setRegularDateStart(String regularDateStart) {
        this.regularDateStart = regularDateStart;
    }

    public String getRegularDateEnd() {
        return regularDateEnd;
    }

    public void setRegularDateEnd(String regularDateEnd) {
        this.regularDateEnd = regularDateEnd;
    }

    public String getLeaveDateStart() {
        return leaveDateStart;
    }

    public void setLeaveDateStart(String leaveDateStart) {
        this.leaveDateStart = leaveDateStart;
    }

    public String getLeaveDateEnd() {
        return leaveDateEnd;
    }

    public void setLeaveDateEnd(String leaveDateEnd) {
        this.leaveDateEnd = leaveDateEnd;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getOfficeIds() {
        return officeIds;
    }

    public void setOfficeIds(String officeIds) {
        this.officeIds = officeIds;
    }
}
