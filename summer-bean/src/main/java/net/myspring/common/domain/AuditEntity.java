/*******************************************************************************
 * Copyright (c) 2005, 2014 myspring.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package net.myspring.common.domain;


import javax.persistence.Transient;
import java.time.LocalDateTime;

public abstract class AuditEntity<T> extends DataEntity<T> {
	protected String auditBy;
	protected LocalDateTime auditDate;
	protected String auditRemarks;
	protected String status;
	@Transient
	protected Boolean pass;

	public String getAuditBy() {
		return auditBy;
	}

	public void setAuditBy(String auditBy) {
		this.auditBy = auditBy;
	}

	public LocalDateTime getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(LocalDateTime auditDate) {
		this.auditDate = auditDate;
	}

	public String getAuditRemarks() {
		return auditRemarks;
	}

	public void setAuditRemarks(String auditRemarks) {
		this.auditRemarks = auditRemarks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getPass() {
		return pass;
	}

	public void setPass(Boolean pass) {
		this.pass = pass;
	}
}