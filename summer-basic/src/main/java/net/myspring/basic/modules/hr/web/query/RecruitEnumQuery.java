package net.myspring.basic.modules.hr.web.query;

import net.myspring.basic.common.query.BaseQuery;

public class RecruitEnumQuery extends BaseQuery{

    private String category;
    private String value;
    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

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
