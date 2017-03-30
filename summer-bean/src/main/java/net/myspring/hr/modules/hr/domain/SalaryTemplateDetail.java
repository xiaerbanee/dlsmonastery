package net.myspring.hr.modules.hr.domain;


import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="hr_salary_template_detail")
public class SalaryTemplateDetail extends DataEntity<SalaryTemplateDetail> {
    private String name;
    private Integer sort;
    private Integer version = 0;
    private SalaryTemplate salaryTemplate;
    private String salaryTemplateId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public SalaryTemplate getSalaryTemplate() {
        return salaryTemplate;
    }

    public void setSalaryTemplate(SalaryTemplate salaryTemplate) {
        this.salaryTemplate = salaryTemplate;
    }

    public String getSalaryTemplateId() {
        return salaryTemplateId;
    }

    public void setSalaryTemplateId(String salaryTemplateId) {
        this.salaryTemplateId = salaryTemplateId;
    }
}
