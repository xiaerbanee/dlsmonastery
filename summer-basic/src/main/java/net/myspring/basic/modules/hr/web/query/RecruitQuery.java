package net.myspring.basic.modules.hr.web.query;

import net.myspring.basic.common.query.BaseQuery;

import java.time.LocalDate;

/**
 * Created by lihx on 2017/4/7.
 */
public class RecruitQuery extends BaseQuery {
    private String name;
    private String mobilePhone;
    private LocalDate firstAppointDate;
    private LocalDate secondAppointDate;
    private LocalDate auditAppointDate;
    private LocalDate entryAppointDate;
    private String registerBy;
    private String firstAppointBy;
    private String secondAppointBy;
    private Boolean onJob;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public LocalDate getFirstAppointDate() {
        return firstAppointDate;
    }

    public void setFirstAppointDate(LocalDate firstAppointDate) {
        this.firstAppointDate = firstAppointDate;
    }

    public LocalDate getSecondAppointDate() {
        return secondAppointDate;
    }

    public void setSecondAppointDate(LocalDate secondAppointDate) {
        this.secondAppointDate = secondAppointDate;
    }

    public LocalDate getAuditAppointDate() {
        return auditAppointDate;
    }

    public void setAuditAppointDate(LocalDate auditAppointDate) {
        this.auditAppointDate = auditAppointDate;
    }

    public LocalDate getEntryAppointDate() {
        return entryAppointDate;
    }

    public void setEntryAppointDate(LocalDate entryAppointDate) {
        this.entryAppointDate = entryAppointDate;
    }

    public String getRegisterBy() {
        return registerBy;
    }

    public void setRegisterBy(String registerBy) {
        this.registerBy = registerBy;
    }

    public String getFirstAppointBy() {
        return firstAppointBy;
    }

    public void setFirstAppointBy(String firstAppointBy) {
        this.firstAppointBy = firstAppointBy;
    }

    public String getSecondAppointBy() {
        return secondAppointBy;
    }

    public void setSecondAppointBy(String secondAppointBy) {
        this.secondAppointBy = secondAppointBy;
    }

    public Boolean getOnJob() {
        return onJob;
    }

    public void setOnJob(Boolean onJob) {
        this.onJob = onJob;
    }
}
