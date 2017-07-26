package net.myspring.basic.modules.salary.dto;

import net.myspring.basic.modules.salary.domain.SalaryTemplate;
import net.myspring.common.dto.DataDto;

public class SalaryTemplateDto extends DataDto<SalaryTemplate> {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
