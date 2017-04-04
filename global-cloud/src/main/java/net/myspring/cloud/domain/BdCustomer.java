package net.myspring.cloud.domain;

import java.time.LocalDate;

public class BdCustomer {

    private String fcustId;
    private String fnumber;
    private String fname;
    //客户分组
    private Long fprimaryGroup;
    private String fprimaryGroupName;
    private LocalDate fmodifyDate;

    public String getFcustId() {
        return fcustId;
    }

    public void setFcustId(String fcustId) {
        this.fcustId = fcustId;
    }

    public String getFnumber() {
        return fnumber;
    }

    public void setFnumber(String fnumber) {
        this.fnumber = fnumber;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public Long getFprimaryGroup() {
        return fprimaryGroup;
    }

    public void setFprimaryGroup(Long fprimaryGroup) {
        this.fprimaryGroup = fprimaryGroup;
    }

    public String getFprimaryGroupName() {
        return fprimaryGroupName;
    }

    public void setFprimaryGroupName(String fprimaryGroupName) {
        this.fprimaryGroupName = fprimaryGroupName;
    }

    public LocalDate getFmodifyDate() {
        return fmodifyDate;
    }

    public void setFmodifyDate(LocalDate fmodifyDate) {
        this.fmodifyDate = fmodifyDate;
    }
}