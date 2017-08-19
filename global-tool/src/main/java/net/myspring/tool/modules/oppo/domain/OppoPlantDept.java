package net.myspring.tool.modules.oppo.domain;

import net.myspring.tool.common.domain.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "oppo_plant_dept")
public class OppoPlantDept extends IdEntity<OppoPlantDept> {
    private String deptId;
    private String deptCode;
    private String deptDesrc;
    private String deptPid;
    private String deptLvl;

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptDesrc() {
        return deptDesrc;
    }

    public void setDeptDesrc(String deptDesrc) {
        this.deptDesrc = deptDesrc;
    }

    public String getDeptPid() {
        return deptPid;
    }

    public void setDeptPid(String deptPid) {
        this.deptPid = deptPid;
    }

    public String getDeptLvl() {
        return deptLvl;
    }

    public void setDeptLvl(String deptLvl) {
        this.deptLvl = deptLvl;
    }
}
