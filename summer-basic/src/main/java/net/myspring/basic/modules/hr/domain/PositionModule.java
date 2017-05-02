package net.myspring.basic.modules.hr.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.myspring.basic.common.domain.CompanyEntity;
import net.myspring.basic.modules.sys.domain.Backend;
import net.myspring.common.domain.DataEntity;
import net.myspring.util.text.StringUtils;

@Entity
@Table(name="hr_position_module")
public class PositionModule extends DataEntity<PositionModule> {
    private Integer version = 0;
    private String positionId;
    private String backendModuleId;

    public PositionModule(){};

    public PositionModule(String positionId,String backendModuleId){
        this.positionId=positionId;
        this.backendModuleId=backendModuleId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getBackendModuleId() {
        return backendModuleId;
    }

    public void setBackendModuleId(String backendModuleId) {
        this.backendModuleId = backendModuleId;
    }
}
