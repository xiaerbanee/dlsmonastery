/*******************************************************************************
 * Copyright (c) 2005, 2014 myspring.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package net.myspring.cloud.common.dto;


import net.myspring.util.cahe.annotation.CacheInput;

import java.time.LocalDateTime;

public class AuditDto<T> extends DataDto<T> {
	protected String auditBy;
	@CacheInput(inputKey = "account",inputInstance = "auditBy",outputInstance = "auditByName")
	protected String auditByName;
	protected LocalDateTime auditDate;
	protected String auditRemarks;
	protected String status;

	public String getAuditBy() {
		return auditBy;
	}

	public void setAuditBy(String auditBy) {
		this.auditBy = auditBy;
	}

	public String getAuditByName() {
		return auditByName;
	}

	public void setAuditByName(String auditByName) {
		this.auditByName = auditByName;
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
}
