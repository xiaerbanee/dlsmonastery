package net.myspring.hr.modules.sys.domain;


import javax.persistence.Table;

import net.myspring.common.domain.DataEntity;
import javax.persistence.Entity;

@Entity
@Table(name="sys_dict_map")
public class DictMap extends DataEntity<DictMap> {
    private String category;
    private String name;
    private String value;
    private Integer version = 0;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
