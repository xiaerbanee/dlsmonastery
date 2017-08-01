package net.myspring.basic.modules.salary.dto;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.salary.domain.SalaryTemplate;
import net.myspring.common.dto.DataDto;

import java.util.List;

public class SalaryTemplateDto extends DataDto<SalaryTemplate> {
    private String name;
    private List<SalaryTemplateDetailDto> salaryTemplateDetailDtoList= Lists.newArrayList();
    private String salaryTemplateDetails;

    public List<SalaryTemplateDetailDto> getSalaryTemplateDetailDtoList() {
        return salaryTemplateDetailDtoList;
    }

    public void setSalaryTemplateDetailDtoList(List<SalaryTemplateDetailDto> salaryTemplateDetailDtoList) {
        this.salaryTemplateDetailDtoList = salaryTemplateDetailDtoList;
    }

    public String getSalaryTemplateDetails() {
        return salaryTemplateDetails;
    }

    public void setSalaryTemplateDetails(String salaryTemplateDetails) {
        this.salaryTemplateDetails = salaryTemplateDetails;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
