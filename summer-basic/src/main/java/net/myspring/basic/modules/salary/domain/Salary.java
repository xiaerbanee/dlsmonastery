package net.myspring.basic.modules.salary.domain;



import net.myspring.basic.common.domain.DataEntity;

import javax.persistence.*;

@Entity
@Table(name="hr_salary")
public class Salary  extends DataEntity<Salary> {
	private String employeeId;
	private String month;
	private String projectName;
	private String projectValue;

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectValue() {
		return projectValue;
	}

	public void setProjectValue(String projectValue) {
		this.projectValue = projectValue;
	}
}
