package net.myspring.basic.modules.salary.domain;


import net.myspring.basic.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="hr_salary_template")
public class SalaryTemplate  extends DataEntity<SalaryTemplate> {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
