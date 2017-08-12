package net.myspring.basic.modules.hr.domain;


import net.myspring.basic.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="hr_recruit_enum")
public class RecruitEnum extends DataEntity<RecruitEnum>{

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
