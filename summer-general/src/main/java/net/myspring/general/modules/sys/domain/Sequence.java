package net.myspring.general.modules.sys.domain;

import net.myspring.general.common.domain.DataEntity;
import net.myspring.general.common.domain.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="sys_sequence")
public class Sequence extends DataEntity<Sequence> {

    private String seqName;
    private Long currentVal;
    private Integer version = 0;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getSeqName() {
        return seqName;
    }

    public void setSeqName(String seqName) {
        this.seqName = seqName;
    }

    public Long getCurrentVal() {
        return currentVal;
    }

    public void setCurrentVal(Long currentVal) {
        this.currentVal = currentVal;
    }
}
