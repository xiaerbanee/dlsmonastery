package net.myspring.future.modules.basic.domain;


import net.myspring.future.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="crm_chain")
public class Chain extends DataEntity<Chain> {
    private String name;
    private Integer version = 0;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}
