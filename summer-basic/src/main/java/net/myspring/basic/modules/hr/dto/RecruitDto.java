package net.myspring.basic.modules.hr.dto;

import net.myspring.basic.modules.hr.domain.Recruit;
import net.myspring.common.dto.DataDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RecruitDto extends DataDto<Recruit> {

    //邀约时间
    private LocalDate inviteDate;
    private String name;
    private String sex;
    private String mobilePhone;
    private String applyPositionName;
    private String applyFrom;
    private String registerBy;
    //初试预约时间
    private LocalDateTime firstAppointDate;
    private String registerRemarks;

    //初试时间
    private Boolean firstAppoint;
    private String firstAppointBy;
    private String workArea;
    private String workCategory;
    private String marriageStatus;
    private LocalDate birthday;
    private String originId;
    private String education;
    private String school;
    private String major;
    private String firstComment;
    private String mainCompany;
    private String firstAppointRemarks;
    //复试预约时间
    private LocalDateTime secondAppointDate;
    private Boolean secondAppoint;
    private String secondAppointBy;
    private String secondComment;
    private Boolean toStorage;
    private String storageRemarks;
    private String secondAppointRemarks;

    private LocalDateTime auditAppointDate;
    private Boolean auditAppoint;
    private String auditAppointRemarks;

    private LocalDateTime entryAppointDate;
    private Boolean entryAppoint;
    private String  entryAppointRemarks;

    private Boolean onJob;
    private String leaveJobRemarks;

    private Integer active=0;

    public LocalDate getInviteDate() {
        return inviteDate;
    }

    public void setInviteDate(LocalDate inviteDate) {
        this.inviteDate = inviteDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getApplyPositionName() {
        return applyPositionName;
    }

    public void setApplyPositionName(String applyPositionName) {
        this.applyPositionName = applyPositionName;
    }

    public String getApplyFrom() {
        return applyFrom;
    }

    public void setApplyFrom(String applyFrom) {
        this.applyFrom = applyFrom;
    }

    public String getRegisterBy() {
        return registerBy;
    }

    public void setRegisterBy(String registerBy) {
        this.registerBy = registerBy;
    }

    public LocalDateTime getFirstAppointDate() {
        return firstAppointDate;
    }

    public void setFirstAppointDate(LocalDateTime firstAppointDate) {
        this.firstAppointDate = firstAppointDate;
    }

    public String getRegisterRemarks() {
        return registerRemarks;
    }

    public void setRegisterRemarks(String registerRemarks) {
        this.registerRemarks = registerRemarks;
    }

    public Boolean getFirstAppoint() {
        return firstAppoint;
    }

    public void setFirstAppoint(Boolean firstAppoint) {
        this.firstAppoint = firstAppoint;
    }

    public String getFirstAppointBy() {
        return firstAppointBy;
    }

    public void setFirstAppointBy(String firstAppointBy) {
        this.firstAppointBy = firstAppointBy;
    }

    public String getWorkArea() {
        return workArea;
    }

    public void setWorkArea(String workArea) {
        this.workArea = workArea;
    }

    public String getWorkCategory() {
        return workCategory;
    }

    public void setWorkCategory(String workCategory) {
        this.workCategory = workCategory;
    }

    public String getMarriageStatus() {
        return marriageStatus;
    }

    public void setMarriageStatus(String marriageStatus) {
        this.marriageStatus = marriageStatus;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getFirstComment() {
        return firstComment;
    }

    public void setFirstComment(String firstComment) {
        this.firstComment = firstComment;
    }

    public String getMainCompany() {
        return mainCompany;
    }

    public void setMainCompany(String mainCompany) {
        this.mainCompany = mainCompany;
    }

    public String getFirstAppointRemarks() {
        return firstAppointRemarks;
    }

    public void setFirstAppointRemarks(String firstAppointRemarks) {
        this.firstAppointRemarks = firstAppointRemarks;
    }

    public LocalDateTime getSecondAppointDate() {
        return secondAppointDate;
    }

    public void setSecondAppointDate(LocalDateTime secondAppointDate) {
        this.secondAppointDate = secondAppointDate;
    }

    public Boolean getSecondAppoint() {
        return secondAppoint;
    }

    public void setSecondAppoint(Boolean secondAppoint) {
        this.secondAppoint = secondAppoint;
    }

    public String getSecondAppointBy() {
        return secondAppointBy;
    }

    public void setSecondAppointBy(String secondAppointBy) {
        this.secondAppointBy = secondAppointBy;
    }

    public String getSecondComment() {
        return secondComment;
    }

    public void setSecondComment(String secondComment) {
        this.secondComment = secondComment;
    }

    public Boolean getToStorage() {
        return toStorage;
    }

    public void setToStorage(Boolean toStorage) {
        this.toStorage = toStorage;
    }

    public String getStorageRemarks() {
        return storageRemarks;
    }

    public void setStorageRemarks(String storageRemarks) {
        this.storageRemarks = storageRemarks;
    }

    public String getSecondAppointRemarks() {
        return secondAppointRemarks;
    }

    public void setSecondAppointRemarks(String secondAppointRemarks) {
        this.secondAppointRemarks = secondAppointRemarks;
    }

    public LocalDateTime getAuditAppointDate() {
        return auditAppointDate;
    }

    public void setAuditAppointDate(LocalDateTime auditAppointDate) {
        this.auditAppointDate = auditAppointDate;
    }

    public Boolean getAuditAppoint() {
        return auditAppoint;
    }

    public void setAuditAppoint(Boolean auditAppoint) {
        this.auditAppoint = auditAppoint;
    }

    public String getAuditAppointRemarks() {
        return auditAppointRemarks;
    }

    public void setAuditAppointRemarks(String auditAppointRemarks) {
        this.auditAppointRemarks = auditAppointRemarks;
    }

    public LocalDateTime getEntryAppointDate() {
        return entryAppointDate;
    }

    public void setEntryAppointDate(LocalDateTime entryAppointDate) {
        this.entryAppointDate = entryAppointDate;
    }

    public Boolean getEntryAppoint() {
        return entryAppoint;
    }

    public void setEntryAppoint(Boolean entryAppoint) {
        this.entryAppoint = entryAppoint;
    }

    public String getEntryAppointRemarks() {
        return entryAppointRemarks;
    }

    public void setEntryAppointRemarks(String entryAppointRemarks) {
        this.entryAppointRemarks = entryAppointRemarks;
    }

    public Boolean getOnJob() {
        return onJob;
    }

    public void setOnJob(Boolean onJob) {
        this.onJob = onJob;
    }

    public String getLeaveJobRemarks() {
        return leaveJobRemarks;
    }

    public void setLeaveJobRemarks(String leaveJobRemarks) {
        this.leaveJobRemarks = leaveJobRemarks;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }
}
