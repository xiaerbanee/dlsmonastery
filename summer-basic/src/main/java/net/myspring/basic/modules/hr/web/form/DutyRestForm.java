package net.myspring.basic.modules.hr.web.form;

import net.myspring.basic.modules.hr.domain.DutyRest;
import net.myspring.mybatis.form.BaseForm;

/**
 * Created by admin on 2017/4/6.
 */
public class DutyRestForm implements BaseForm<DutyRest> {
    private Double annualLeftHour;

    public Double getAnnualLeftHour() {
        return annualLeftHour;
    }

    public void setAnnualLeftHour(Double annualLeftHour) {
        this.annualLeftHour = annualLeftHour;
    }
}
