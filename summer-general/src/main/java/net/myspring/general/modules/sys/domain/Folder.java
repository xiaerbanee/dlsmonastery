package net.myspring.general.modules.sys.domain;

import net.myspring.general.common.domain.TreeEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="sys_folder")
public class Folder extends TreeEntity<Folder> {
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
