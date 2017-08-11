package net.myspring.basic.modules.hr.web.form;

import net.myspring.common.form.BaseForm;

public class RecruitEnumForm extends BaseForm<RecruitForm> {

    private String category;
    private String value;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
