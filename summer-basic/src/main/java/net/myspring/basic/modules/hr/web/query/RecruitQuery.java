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
}
