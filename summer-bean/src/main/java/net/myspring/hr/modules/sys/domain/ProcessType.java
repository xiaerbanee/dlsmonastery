package net.myspring.hr.modules.sys.domain;

import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import net.myspring.common.domain.DataEntity;
import java.util.List;
import com.google.common.collect.Lists;
import net.myspring.hr.modules.hr.domain.AccountChange;
import net.myspring.hr.modules.hr.domain.AuditFile;
import net.myspring.hr.modules.hr.domain.EmployeeSalaryBasic;
import net.myspring.hr.modules.hr.domain.OfficeChange;

@Entity
@Table(name="sys_process_type")
public class ProcessType extends DataEntity<ProcessType> {
    private String type;
    private String name;
    private String viewPermissionId;
    private String createPermissionId;
    private Integer version = 0;
    private String companyId = "1";
    private Boolean auditFileType;
    private List<AccountChange> accountChangeList = Lists.newArrayList();
    private List<String> accountChangeIdList = Lists.newArrayList();
    private List<AuditFile> auditFileList = Lists.newArrayList();
    private List<String> auditFileIdList = Lists.newArrayList();
    private List<EmployeeSalaryBasic> employeeSalaryBasicList = Lists.newArrayList();
    private List<String> employeeSalaryBasicIdList = Lists.newArrayList();
    private List<OfficeChange> officeChangeList = Lists.newArrayList();
    private List<String> officeChangeIdList = Lists.newArrayList();
    private List<ProcessFlow> processFlowList = Lists.newArrayList();
    private List<String> processFlowIdList = Lists.newArrayList();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getViewPermissionId() {
        return viewPermissionId;
    }

    public void setViewPermissionId(String viewPermissionId) {
        this.viewPermissionId = viewPermissionId;
    }

    public String getCreatePermissionId() {
        return createPermissionId;
    }

    public void setCreatePermissionId(String createPermissionId) {
        this.createPermissionId = createPermissionId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Boolean getAuditFileType() {
        return auditFileType;
    }

    public void setAuditFileType(Boolean auditFileType) {
        this.auditFileType = auditFileType;
    }

    public List<AccountChange> getAccountChangeList() {
        return accountChangeList;
    }

    public void setAccountChangeList(List<AccountChange> accountChangeList) {
        this.accountChangeList = accountChangeList;
    }

    public List<String> getAccountChangeIdList() {
        return accountChangeIdList;
    }

    public void setAccountChangeIdList(List<String> accountChangeIdList) {
        this.accountChangeIdList = accountChangeIdList;
    }

    public List<AuditFile> getAuditFileList() {
        return auditFileList;
    }

    public void setAuditFileList(List<AuditFile> auditFileList) {
        this.auditFileList = auditFileList;
    }

    public List<String> getAuditFileIdList() {
        return auditFileIdList;
    }

    public void setAuditFileIdList(List<String> auditFileIdList) {
        this.auditFileIdList = auditFileIdList;
    }

    public List<EmployeeSalaryBasic> getEmployeeSalaryBasicList() {
        return employeeSalaryBasicList;
    }

    public void setEmployeeSalaryBasicList(List<EmployeeSalaryBasic> employeeSalaryBasicList) {
        this.employeeSalaryBasicList = employeeSalaryBasicList;
    }

    public List<String> getEmployeeSalaryBasicIdList() {
        return employeeSalaryBasicIdList;
    }

    public void setEmployeeSalaryBasicIdList(List<String> employeeSalaryBasicIdList) {
        this.employeeSalaryBasicIdList = employeeSalaryBasicIdList;
    }

    public List<OfficeChange> getOfficeChangeList() {
        return officeChangeList;
    }

    public void setOfficeChangeList(List<OfficeChange> officeChangeList) {
        this.officeChangeList = officeChangeList;
    }

    public List<String> getOfficeChangeIdList() {
        return officeChangeIdList;
    }

    public void setOfficeChangeIdList(List<String> officeChangeIdList) {
        this.officeChangeIdList = officeChangeIdList;
    }

    public List<ProcessFlow> getProcessFlowList() {
        return processFlowList;
    }

    public void setProcessFlowList(List<ProcessFlow> processFlowList) {
        this.processFlowList = processFlowList;
    }

    public List<String> getProcessFlowIdList() {
        return processFlowIdList;
    }

    public void setProcessFlowIdList(List<String> processFlowIdList) {
        this.processFlowIdList = processFlowIdList;
    }
}