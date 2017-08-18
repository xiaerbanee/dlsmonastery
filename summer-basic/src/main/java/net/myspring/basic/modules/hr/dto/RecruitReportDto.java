package net.myspring.basic.modules.hr.dto;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.hr.domain.Recruit;
import net.myspring.common.dto.DataDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class RecruitReportDto extends DataDto<Recruit> {

    private LocalDate exportTime;
    private String year;
    private String month;

    private String mainPerson;  //邀约人，初试人，复试人
    private String  applyPositionName;
    private String applyFrom;

    //邀约
    private String toRegisterCount; //   通知面试得人数
    private String registerFirstCount;//初试到达人数
    private String registerToSecondCount;//推复试人数
    private String registerEntryCount;//入职人数

    //面试复试
    private String firstCount;  //初试人数
    private String toSecondCount;//推复试人数
    private String secondCount;//复试人数
    private String hireCount;//通过复试人数
    private String auditCount;//资审人数
    private String onjobCount;//入职人数

    private Float  hireRate;//录用率
    private Float  registerRate;//面试达到率



    private Float firstRate;//初试通过率
    private Float  toSecondRate;//复试到达率
    private Float secondRate;//复试通过率
    private Float onjobRate;//到岗率


    private String onjob;
    private String afterOnjob;
    private String abandon;

    public String getOnjob() {
        return onjob;
    }

    public void setOnjob(String onjob) {
        this.onjob = onjob;
    }

    public String getAfterOnjob() {
        return afterOnjob;
    }

    public void setAfterOnjob(String afterOnjob) {
        this.afterOnjob = afterOnjob;
    }

    public String getAbandon() {
        return abandon;
    }

    public void setAbandon(String abandon) {
        this.abandon = abandon;
    }

    public Float getHireRate() {
        return hireRate;
    }

    public void setHireRate(Float hireRate) {
        this.hireRate = hireRate;
    }
    public LocalDate getExportTime() {
        return exportTime;
    }

    public void setExportTime(LocalDate exportTime) {
        this.exportTime = exportTime;
    }



    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getMainPerson() {
        return mainPerson;
    }

    public void setMainPerson(String mainPerson) {
        this.mainPerson = mainPerson;
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

    public String getToRegisterCount() {
        return toRegisterCount;
    }

    public void setToRegisterCount(String toRegisterCount) {
        this.toRegisterCount = toRegisterCount;
    }

    public String getRegisterFirstCount() {
        return registerFirstCount;
    }

    public void setRegisterFirstCount(String registerFirstCount) {
        this.registerFirstCount = registerFirstCount;
    }

    public String getRegisterToSecondCount() {
        return registerToSecondCount;
    }

    public void setRegisterToSecondCount(String registerToSecondCount) {
        this.registerToSecondCount = registerToSecondCount;
    }

    public String getRegisterEntryCount() {
        return registerEntryCount;
    }

    public void setRegisterEntryCount(String registerEntryCount) {
        this.registerEntryCount = registerEntryCount;
    }

    public String getFirstCount() {
        return firstCount;
    }

    public void setFirstCount(String firstCount) {
        this.firstCount = firstCount;
    }

    public String getToSecondCount() {
        return toSecondCount;
    }

    public void setToSecondCount(String toSecondCount) {
        this.toSecondCount = toSecondCount;
    }

    public Float getRegisterRate() {
        return registerRate;
    }

    public void setRegisterRate(Float registerRate) {
        this.registerRate = registerRate;
    }

    public String getSecondCount() {

        return secondCount;
    }

    public void setSecondCount(String secondCount) {
        this.secondCount = secondCount;
    }

    public String getHireCount() {
        return hireCount;
    }

    public void setHireCount(String hireCount) {
        this.hireCount = hireCount;
    }

    public String getAuditCount() {
        return auditCount;
    }

    public void setAuditCount(String auditCount) {
        this.auditCount = auditCount;
    }

    public String getOnjobCount() {
        return onjobCount;
    }

    public void setOnjobCount(String onjobCount) {
        this.onjobCount = onjobCount;
    }


    public Float getFirstRate() {
        return firstRate;
    }

    public void setFirstRate(Float firstRate) {
        this.firstRate = firstRate;
    }

    public Float getToSecondRate() {
        return toSecondRate;
    }

    public void setToSecondRate(Float toSecondRate) {
        this.toSecondRate = toSecondRate;
    }

    public Float getSecondRate() {
        return secondRate;
    }

    public void setSecondRate(Float secondRate) {
        this.secondRate = secondRate;
    }

    public Float getOnjobRate() {
        return onjobRate;
    }

    public void setOnjobRate(Float onjobRate) {
        this.onjobRate = onjobRate;
    }


}
