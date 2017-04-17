package net.myspring.basic.modules.hr.web.query;

import net.myspring.basic.common.enums.EmployeeStatusEnum;
import net.myspring.basic.modules.hr.dto.PositionDto;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by lihx on 2017/4/7.
 */
public class EmployeeQuery {
    private List<String> officeIds;
    private String name;
    private String status;
    private String mobilePhone;
    private LocalDate entryDateStart;
    private LocalDate entryDateEnd;
    private LocalDate regularDateStart;
    private LocalDate regularDateEnd;
    private LocalDate leaveDateStart;
    private LocalDate leaveDateEnd;
    private String positionId;
    private String leaderName;
    private String officeId;
    private List<PositionDto> positionDtoList;
    private EmployeeStatusEnum[] statusList;

    public List<PositionDto> getPositionDtoList() {
        return positionDtoList;
    }

    public void setPositionDtoList(List<PositionDto> positionDtoList) {
        this.positionDtoList = positionDtoList;
    }

    public EmployeeStatusEnum[] getStatusList() {
        return statusList;
    }

    public void setStatusList(EmployeeStatusEnum[] statusList) {
        this.statusList = statusList;
    }

    public List<String> getOfficeIds() {
        return officeIds;
    }

    public void setOfficeIds(List<String> officeIds) {
        this.officeIds = officeIds;
    }

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

    public LocalDate getEntryDateStart() {
        return entryDateStart;
    }

    public void setEntryDateStart(LocalDate entryDateStart) {
        this.entryDateStart = entryDateStart;
    }

    public LocalDate getEntryDateEnd() {
        return entryDateEnd;
    }

    public void setEntryDateEnd(LocalDate entryDateEnd) {
        this.entryDateEnd = entryDateEnd;
    }

    public LocalDate getRegularDateStart() {
        return regularDateStart;
    }

    public void setRegularDateStart(LocalDate regularDateStart) {
        this.regularDateStart = regularDateStart;
    }

    public LocalDate getRegularDateEnd() {
        return regularDateEnd;
    }

    public void setRegularDateEnd(LocalDate regularDateEnd) {
        this.regularDateEnd = regularDateEnd;
    }

    public LocalDate getLeaveDateStart() {
        return leaveDateStart;
    }

    public void setLeaveDateStart(LocalDate leaveDateStart) {
        this.leaveDateStart = leaveDateStart;
    }

    public LocalDate getLeaveDateEnd() {
        return leaveDateEnd;
    }

    public void setLeaveDateEnd(LocalDate leaveDateEnd) {
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
}
