package net.myspring.future.modules.basic.domain;


import net.myspring.future.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="crm_simple_process")
public class SimpleProcess extends DataEntity<SimpleProcess> {

    private Integer version = 0;
    private String simpleProcessTypeId;
    private String currentProcessStatus;
    private String currentPositionId;

    public String getCurrentPositionId() {
        return currentPositionId;
    }

    public void setCurrentPositionId(String currentPositionId) {
        this.currentPositionId = currentPositionId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getSimpleProcessTypeId() {
        return simpleProcessTypeId;
    }

    public void setSimpleProcessTypeId(String simpleProcessTypeId) {
        this.simpleProcessTypeId = simpleProcessTypeId;
    }

    public String getCurrentProcessStatus() {
        return currentProcessStatus;
    }

    public void setCurrentProcessStatus(String currentProcessStatus) {
        this.currentProcessStatus = currentProcessStatus;
    }
}
