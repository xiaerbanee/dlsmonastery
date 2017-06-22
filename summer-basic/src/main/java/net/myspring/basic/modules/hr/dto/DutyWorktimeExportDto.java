package net.myspring.basic.modules.hr.dto;

import com.google.common.collect.Lists;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class DutyWorktimeExportDto {
    private String startTime;
    private String endTime;
    private String weekOfDay;
    private String firstTime;
    private String lastTime;
    private LocalDate dutyDate;

    private List<String> worktimes = Lists.newArrayList();
    private List<String> overtimes = Lists.newArrayList();
    private List<String> leaves =Lists.newArrayList();
    private List<String> overtimeRests = Lists.newArrayList();
    private List<String> annualRests = Lists.newArrayList();
    private List<String> frees=Lists.newArrayList();
    private List<String> publicFrees = Lists.newArrayList();
    private List<String> trips=Lists.newArrayList();

    private String overtimeCellType;
    private String leaveCellType;
    private String overtimeRestCellType;
    private String annualRestCellType;
    private String freeCellType;
    private String publicFreeCellType;
    private String tripCellType;


    private String style;
    private String status;
    private String employeeId;
    private String employeeName;
    private String officeName;

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public LocalDate getDutyDate() {
        return dutyDate;
    }

    public void setDutyDate(LocalDate dutyDate) {
        this.dutyDate = dutyDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getWeekOfDay() {
        if(dutyDate!=null){
            this.weekOfDay= LocalDateUtils.getDayOfWeek(dutyDate);
        }
        return weekOfDay;
    }

    public void setWeekOfDay(String weekOfDay) {
        this.weekOfDay = weekOfDay;
    }

    public String getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(String firstTime) {
        this.firstTime = firstTime;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeetId) {
        this.employeeId = employeetId;
    }

    public List<String> getWorktimes() {
        return worktimes;
    }

    public void setWorktimes(List<String> worktimes) {
        this.worktimes = worktimes;
    }

    public List<String> getOvertimes() {
        return overtimes;
    }

    public void setOvertimes(List<String> overtimes) {
        this.overtimes = overtimes;
    }

    public List<String> getLeaves() {
        return leaves;
    }

    public void setLeaves(List<String> leaves) {
        this.leaves = leaves;
    }

    public List<String> getOvertimeRests() {
        return overtimeRests;
    }

    public void setOvertimeRests(List<String> overtimeRests) {
        this.overtimeRests = overtimeRests;
    }

    public List<String> getAnnualRests() {
        return annualRests;
    }

    public void setAnnualRests(List<String> annualRests) {
        this.annualRests = annualRests;
    }

    public List<String> getFrees() {
        return frees;
    }

    public void setFrees(List<String> frees) {
        this.frees = frees;
    }

    public List<String> getPublicFrees() {
        return publicFrees;
    }

    public void setPublicFrees(List<String> publicFrees) {
        this.publicFrees = publicFrees;
    }

    public List<String> getTrips() {
        return trips;
    }

    public void setTrips(List<String> trips) {
        this.trips = trips;
    }

    public String getOvertimeCellType() {
        return overtimeCellType;
    }

    public void setOvertimeCellType(String overtimeCellType) {
        this.overtimeCellType = overtimeCellType;
    }

    public String getLeaveCellType() {
        return leaveCellType;
    }

    public void setLeaveCellType(String leaveCellType) {
        this.leaveCellType = leaveCellType;
    }

    public String getOvertimeRestCellType() {
        return overtimeRestCellType;
    }

    public void setOvertimeRestCellType(String overtimeRestCellType) {
        this.overtimeRestCellType = overtimeRestCellType;
    }

    public String getAnnualRestCellType() {
        return annualRestCellType;
    }

    public void setAnnualRestCellType(String annualRestCellType) {
        this.annualRestCellType = annualRestCellType;
    }

    public String getFreeCellType() {
        return freeCellType;
    }

    public void setFreeCellType(String freeCellType) {
        this.freeCellType = freeCellType;
    }

    public String getPublicFreeCellType() {
        return publicFreeCellType;
    }

    public void setPublicFreeCellType(String publicFreeCellType) {
        this.publicFreeCellType = publicFreeCellType;
    }

    public String getTripCellType() {
        return tripCellType;
    }

    public void setTripCellType(String tripCellType) {
        this.tripCellType = tripCellType;
    }
}
