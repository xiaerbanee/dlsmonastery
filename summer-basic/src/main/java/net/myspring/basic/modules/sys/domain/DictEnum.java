package net.myspring.basic.modules.sys.domain;

import net.myspring.basic.common.domain.DataEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="sys_dict_enum")
@Where(clause = "enabled=true")
public class DictEnum extends DataEntity<DictEnum> {
    private Integer sort;
    private String category;
    private String value;
    private Integer version = 0;

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
