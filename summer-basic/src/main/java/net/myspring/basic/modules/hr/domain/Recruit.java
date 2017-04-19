package net.myspring.basic.modules.hr.domain;

import com.google.common.collect.Lists;
import net.myspring.common.domain.CompanyEntity;
import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="hr_recruit")
public class Recruit extends CompanyEntity<Recruit> {
    private LocalDateTime contactDate;
    private String name;
    private String sex;
    private String mobilePhone;
    private String applyPositionId;
    private String applyPositionName;
    private String applyFrom;
    private LocalDateTime firstAppointDate;
    private Long contactBy;
    private LocalDateTime firstRealDate;
    private String workArea;
    private String workCategory;
    private String marriageStatus;
    private String originId;
    private LocalDate birthday;
    private String school;
    private String major;
    private String education;
    private Long firstBy;
    private Double firstPoint;
    private String firstComment;
    private LocalDateTime secondAppointDate;
    private LocalDateTime secondRealDate;
    private Long secondBy;
    private String secondComment;
    private Boolean toStorage;
    private String storageRemarks;
    private LocalDateTime physicalAppointDate;
    private LocalDateTime physicalRealDate;
    private LocalDateTime auditAppointDate;
    private LocalDateTime auditRealDate;
    private LocalDateTime entryAppointDate;
    private LocalDateTime entryRealDate;
    private String idcard;
    private Integer version = 0;

    @Transient
    private List<String> ids = Lists.newArrayList();

    public LocalDateTime getContactDate() {
        return contactDate;
    }

    public void setContactDate(LocalDateTime contactDate) {
        this.contactDate = contactDate;
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

    public LocalDateTime getFirstAppointDate() {
        return firstAppointDate;
    }

    public void setFirstAppointDate(LocalDateTime firstAppointDate) {
        this.firstAppointDate = firstAppointDate;
    }

    public Long getContactBy() {
        return contactBy;
    }

    public void setContactBy(Long contactBy) {
        this.contactBy = contactBy;
    }

    public LocalDateTime getFirstRealDate() {
        return firstRealDate;
    }

    public void setFirstRealDate(LocalDateTime firstRealDate) {
        this.firstRealDate = firstRealDate;
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

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
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

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public Long getFirstBy() {
        return firstBy;
    }

    public void setFirstBy(Long firstBy) {
        this.firstBy = firstBy;
    }

    public Double getFirstPoint() {
        return firstPoint;
    }

    public void setFirstPoint(Double firstPoint) {
        this.firstPoint = firstPoint;
    }

    public String getFirstComment() {
        return firstComment;
    }

    public void setFirstComment(String firstComment) {
        this.firstComment = firstComment;
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

    public Long getSecondBy() {
        return secondBy;
    }

    public void setSecondBy(Long secondBy) {
        this.secondBy = secondBy;
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

    public LocalDateTime getPhysicalAppointDate() {
        return physicalAppointDate;
    }

    public void setPhysicalAppointDate(LocalDateTime physicalAppointDate) {
        this.physicalAppointDate = physicalAppointDate;
    }

    public LocalDateTime getPhysicalRealDate() {
        return physicalRealDate;
    }

    public void setPhysicalRealDate(LocalDateTime physicalRealDate) {
        this.physicalRealDate = physicalRealDate;
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

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
