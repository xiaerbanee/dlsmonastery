package net.myspring.basic.modules.salary.domain;



import net.myspring.basic.common.domain.DataEntity;

import javax.persistence.*;

@Entity
@Table(name = "hr_salary_template_detail")
public class SalaryTemplateDetail extends DataEntity<SalaryTemplateDetail> {
	
	private String salaryTemplateId;
	private String name;
	private Integer sort;

	public String getSalaryTemplateId() {
		return salaryTemplateId;
	}

	public void setSalaryTemplateId(String salaryTemplateId) {
		this.salaryTemplateId = salaryTemplateId;
	}

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
}
