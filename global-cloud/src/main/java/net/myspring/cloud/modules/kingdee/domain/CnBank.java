package net.myspring.cloud.modules.kingdee.domain;

import java.time.LocalDate;

public class CnBank {
    private String fbankAcntId;
    private String fnumber;
    private String fname;
    private LocalDate fmodeifyDate;

    public String getFbankAcntId() {
        return fbankAcntId;
    }

    public void setFbankAcntId(String fbankAcntId) {
        this.fbankAcntId = fbankAcntId;
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

    public LocalDate getFmodeifyDate() {
        return fmodeifyDate;
    }

    public void setFmodeifyDate(LocalDate fmodeifyDate) {
        this.fmodeifyDate = fmodeifyDate;
    }
}
