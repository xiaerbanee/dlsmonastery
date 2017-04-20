package net.myspring.basic.modules.sys.domain;

import com.google.common.collect.Lists;
import net.myspring.common.domain.CompanyEntity;
import net.myspring.common.domain.TreeEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

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
