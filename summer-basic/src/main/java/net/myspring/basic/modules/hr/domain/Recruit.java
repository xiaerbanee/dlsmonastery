package net.myspring.basic.modules.hr.domain;

import net.myspring.basic.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="hr_recruit")
public class Recruit extends DataEntity<Recruit> {
    //预约时间
    private String name;
    private String sex;
    private String mobilePhone;
    private String applyPositionId;
    private String applyPositionName;
    private String applyFrom;
    private String registerBy;
    private String registerRemarks;

    //初试预约时间
    private LocalDateTime firstAppointDate;

    //初试时间
    private LocalDateTime firstRealDate;
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
    private String firstAppointRemarks;
    //复试预约时间
    private LocalDateTime secondAppointDate;
    //复试时间
    private LocalDateTime secondRealDate;
    private String secondAppointBy;
    private String secondComment;
    private Boolean toStorage;
    private String storageRemarks;
    private String secondAppointRemarks;

    private LocalDateTime auditAppointDate;
    private LocalDateTime auditRealDate;
    private String auditAppointRemarks;

    private LocalDateTime entryAppointDate;
    private LocalDateTime entryRealDate;
    private String  entryAppointRemarks;

    private Boolean onJob;
    private String leaveJobRemarks;
    private Integer version = 0;

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

    public String getApplyPositionId() {
        return applyPositionId;
    }

    public void setApplyPositionId(String applyPositionId) {
        this.applyPositionId = applyPositionId;
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

    public String getRegisterRemarks() {
        return registerRemarks;
    }

    public void setRegisterRemarks(String registerRemarks) {
        this.registerRemarks = registerRemarks;
    }

    public LocalDateTime getFirstAppointDate() {
        return firstAppointDate;
    }

    public void setFirstAppointDate(LocalDateTime firstAppointDate) {
        this.firstAppointDate = firstAppointDate;
    }

    public LocalDateTime getFirstRealDate() {
        return firstRealDate;
    }

    public void setFirstRealDate(LocalDateTime firstRealDate) {
        this.firstRealDate = firstRealDate;
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

    public LocalDateTime getSecondRealDate() {
        return secondRealDate;
    }

    public void setSecondRealDate(LocalDateTime secondRealDate) {
        this.secondRealDate = secondRealDate;
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

    public LocalDateTime getAuditRealDate() {
        return auditRealDate;
    }

    public void setAuditRealDate(LocalDateTime auditRealDate) {
        this.auditRealDate = auditRealDate;
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

    public LocalDateTime getEntryRealDate() {
        return entryRealDate;
    }

    public void setEntryRealDate(LocalDateTime entryRealDate) {
        this.entryRealDate = entryRealDate;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
