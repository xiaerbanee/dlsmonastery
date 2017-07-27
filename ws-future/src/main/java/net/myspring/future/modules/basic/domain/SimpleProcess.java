package net.myspring.future.modules.basic.domain;


import net.myspring.future.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="crm_simple_process")
public class SimpleProcess extends DataEntity<SimpleProcess> {

    private Integer version = 0;
    private String type;
    private String currentProcessStatus;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCurrentProcessStatus() {
        return currentProcessStatus;
    }

    public void setCurrentProcessStatus(String currentProcessStatus) {
        this.currentProcessStatus = currentProcessStatus;
    }
}
