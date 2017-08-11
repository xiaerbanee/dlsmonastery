package net.myspring.basic.modules.hr.dto;

import net.myspring.basic.modules.hr.domain.RecruitEnum;
import net.myspring.common.dto.DataDto;

public class RecruitEnumDto extends DataDto<RecruitEnum>{

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
