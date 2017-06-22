package net.myspring.basic.modules.hr.web.form;


import net.myspring.basic.modules.hr.domain.DutyWorktime;
import net.myspring.common.form.BaseForm;

/**
 * Created by admin on 2017/4/6.
 */

public class DutyWorktimeForm extends BaseForm<DutyWorktime> {
    private String mongoId;
    private String yearMonth;

    public String getMongoId() {
        return mongoId;
    }

    public void setMongoId(String mongoId) {
        this.mongoId = mongoId;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }
}
