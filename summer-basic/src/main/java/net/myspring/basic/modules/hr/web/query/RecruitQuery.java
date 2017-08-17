package net.myspring.basic.modules.hr.web.query;

import net.myspring.basic.common.query.BaseQuery;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;

/**
 * Created by lihx on 2017/4/7.
 */
public class RecruitQuery extends BaseQuery {
    private String name;
    private String mobilePhone;
    private String firstAppointDate;
    private String secondAppointDate;
    private String auditAppointDate;
    private String entryAppointDate;
    private String registerBy;
    private String firstAppointBy;
    private String secondAppointBy;
    private Boolean onJob;
    private LocalDate firstAppointDateStart;
    private LocalDate firstAppointDateEnd;
    private LocalDate secondAppointDateStart;
    private LocalDate secondAppointDateEnd;
    private LocalDate auditAppointDateStart;
    private LocalDate auditAppointDateEnd;
    private LocalDate entryAppointDateStart;
    private LocalDate entryAppointDateEnd;

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

    public String getFirstAppointDate() {
        return firstAppointDate;
    }

    public String getSecondAppointDate() {
        return secondAppointDate;
    }

    public String getAuditAppointDate() {
        return auditAppointDate;
    }

    public String getEntryAppointDate() {
        return entryAppointDate;
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


    public void setFirstAppointDate(String firstAppointDate) {
        this.firstAppointDate = firstAppointDate;
    }

    public void setSecondAppointDate(String secondAppointDate) {
        this.secondAppointDate = secondAppointDate;
    }

    public void setAuditAppointDate(String auditAppointDate) {
        this.auditAppointDate = auditAppointDate;
    }

    public void setEntryAppointDate(String entryAppointDate) {
        this.entryAppointDate = entryAppointDate;
    }

    public LocalDate getFirstAppointDateStart() {
        if(StringUtils.isNotBlank(firstAppointDate)) {
            return LocalDateUtils.parse(firstAppointDate.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        }
        return null;    }

    public void setFirstAppointDateStart(LocalDate firstAppointDateStart) {
        this.firstAppointDateStart = firstAppointDateStart;
    }

    public LocalDate getFirstAppointDateEnd() {
        if(StringUtils.isNotBlank(firstAppointDate)) {
            return LocalDateUtils.parse(firstAppointDate.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        }
        return null;
    }

    public void setFirstAppointDateEnd(LocalDate firstAppointDateEnd) {
        this.firstAppointDateEnd = firstAppointDateEnd;
    }

    public LocalDate getSecondAppointDateStart() {
        if(StringUtils.isNotBlank(secondAppointDate)) {
            return LocalDateUtils.parse(secondAppointDate.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        }
        return null;
    }

    public void setSecondAppointDateStart(LocalDate secondAppointDateStart) {
        this.secondAppointDateStart = secondAppointDateStart;
    }

    public LocalDate getSecondAppointDateEnd() {
        if(StringUtils.isNotBlank(secondAppointDate)) {
            return LocalDateUtils.parse(secondAppointDate.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        }
        return null;
    }

    public void setSecondAppointDateEnd(LocalDate secondAppointDateEnd) {
        this.secondAppointDateEnd = secondAppointDateEnd;
    }

    public LocalDate getAuditAppointDateStart() {
        if(StringUtils.isNotBlank(auditAppointDate)) {
            return LocalDateUtils.parse(auditAppointDate.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        }
        return null;
    }

    public void setAuditAppointDateStart(LocalDate auditAppointDateStart) {
        this.auditAppointDateStart = auditAppointDateStart;
    }

    public LocalDate getAuditAppointDateEnd() {
        if(StringUtils.isNotBlank(auditAppointDate)) {
            return LocalDateUtils.parse(auditAppointDate.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        }
        return null;    }

    public void setAuditAppointDateEnd(LocalDate auditAppointDateEnd) {
        this.auditAppointDateEnd = auditAppointDateEnd;
    }

    public LocalDate getEntryAppointDateStart() {
        if(StringUtils.isNotBlank(entryAppointDate)) {
            return LocalDateUtils.parse(entryAppointDate.split(CharConstant.DATE_RANGE_SPLITTER)[0]);
        }
        return null;
    }

    public void setEntryAppointDateStart(LocalDate entryAppointDateStart) {
        this.entryAppointDateStart = entryAppointDateStart;
    }

    public LocalDate getEntryAppointDateEnd() {
        if(StringUtils.isNotBlank(entryAppointDate)) {
            return LocalDateUtils.parse(entryAppointDate.split(CharConstant.DATE_RANGE_SPLITTER)[1]).plusDays(1);
        }
        return null;
    }

    public void setEntryAppointDateEnd(LocalDate entryAppointDateEnd) {
        this.entryAppointDateEnd = entryAppointDateEnd;
    }
}
