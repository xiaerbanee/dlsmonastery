package net.myspring.future.modules.basic.domain;


import net.myspring.future.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="crm_simple_process_step")
public class SimpleProcessStep extends DataEntity<SimpleProcessStep> {

    private Integer version = 0;
    private String simpleProcessTypeId;
    private String step;
    private Integer sort;
    private String positionId;

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

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }
}
