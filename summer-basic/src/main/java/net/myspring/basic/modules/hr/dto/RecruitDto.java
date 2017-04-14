package net.myspring.basic.modules.hr.dto;

import net.myspring.basic.common.dto.DataDto;
import net.myspring.basic.modules.hr.domain.Recruit;

import java.time.LocalDate;

/**
 * Created by admin on 2017/4/5.
 */
public class RecruitDto extends DataDto<Recruit> {
    private String name;
    private String sex;
    private String education;
    private String mobilePhone;
    private LocalDate firstAppointDate;
    private LocalDate secondAppointDate;
    private LocalDate physicalAppointDate;

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

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
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

    public LocalDate getPhysicalAppointDate() {
        return physicalAppointDate;
    }

    public void setPhysicalAppointDate(LocalDate physicalAppointDate) {
        this.physicalAppointDate = physicalAppointDate;
    }
}
