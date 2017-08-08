package net.myspring.general.modules.sys.domain;

import net.myspring.general.common.domain.DataEntity;
import net.myspring.general.common.domain.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="sys_sequence")
public class Sequence extends DataEntity<Sequence> {

    private String key;
    private Long currentVal;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getCurrentVal() {
        return currentVal;
    }

    public void setCurrentVal(Long currentVal) {
        this.currentVal = currentVal;
    }
}
