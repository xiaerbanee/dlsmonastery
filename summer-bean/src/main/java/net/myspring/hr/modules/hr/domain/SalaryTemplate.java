package net.myspring.hr.modules.hr.domain;


import com.google.common.collect.Lists;
import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="hr_salary_template")
public class SalaryTemplate extends DataEntity<SalaryTemplate> {
    private String name;
    private Integer version = 0;
    private List<Salary> salaryList = Lists.newArrayList();
    private List<String> salaryIdList = Lists.newArrayList();
    private List<SalaryTemplateDetail> salaryTemplateDetailList = Lists.newArrayList();
    private List<String> salaryTemplateDetailIdList = Lists.newArrayList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<Salary> getSalaryList() {
        return salaryList;
    }

    public void setSalaryList(List<Salary> salaryList) {
        this.salaryList = salaryList;
    }

    public List<String> getSalaryIdList() {
        return salaryIdList;
    }

    public void setSalaryIdList(List<String> salaryIdList) {
        this.salaryIdList = salaryIdList;
    }

    public List<SalaryTemplateDetail> getSalaryTemplateDetailList() {
        return salaryTemplateDetailList;
    }

    public void setSalaryTemplateDetailList(List<SalaryTemplateDetail> salaryTemplateDetailList) {
        this.salaryTemplateDetailList = salaryTemplateDetailList;
    }

    public List<String> getSalaryTemplateDetailIdList() {
        return salaryTemplateDetailIdList;
    }

    public void setSalaryTemplateDetailIdList(List<String> salaryTemplateDetailIdList) {
        this.salaryTemplateDetailIdList = salaryTemplateDetailIdList;
    }
}
