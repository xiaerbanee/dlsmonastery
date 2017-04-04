package net.myspring.cloud.domain;

import java.time.LocalDate;

public class BdStock {

    private String fstockId;
    private String fnumber;
    private String fname;
    //分组
    private Long fgroup;
    private String fgroupName;
    private LocalDate fmodifyDate;

    public String getFstockId() {
        return fstockId;
    }

    public void setFstockId(String fstockId) {
        this.fstockId = fstockId;
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

    public Long getFgroup() {
        return fgroup;
    }

    public void setFgroup(Long fgroup) {
        this.fgroup = fgroup;
    }

    public String getFgroupName() {
        return fgroupName;
    }

    public void setFgroupName(String fgroupName) {
        this.fgroupName = fgroupName;
    }

    public LocalDate getFmodifyDate() {
        return fmodifyDate;
    }

    public void setFmodifyDate(LocalDate fmodifyDate) {
        this.fmodifyDate = fmodifyDate;
    }
}
