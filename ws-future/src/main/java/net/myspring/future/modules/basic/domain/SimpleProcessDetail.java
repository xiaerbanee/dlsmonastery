package net.myspring.future.modules.basic.domain;


import net.myspring.future.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="crm_simple_process_detail")
public class SimpleProcessDetail extends DataEntity<SimpleProcessDetail> {

    private Integer version = 0;
    private String simpleProcessId;
    private String processStatus;
    private String opinion;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getSimpleProcessId() {
        return simpleProcessId;
    }

    public void setSimpleProcessId(String simpleProcessId) {
        this.simpleProcessId = simpleProcessId;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }
}
