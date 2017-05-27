package net.myspring.basic.modules.sys.domain;

import net.myspring.basic.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "sys_backend")
public class Backend extends DataEntity<Backend> {
    private String name;
    private String code;
    private Integer version = 0;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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
