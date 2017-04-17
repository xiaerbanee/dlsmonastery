package net.myspring.basic.modules.sys.domain;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.hr.domain.*;
import net.myspring.common.domain.CompanyEntity;
import net.myspring.common.domain.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="sys_process_flow")
public class ProcessFlow extends CompanyEntity<ProcessFlow> {
    private String name;
    private Integer sort;
    private String positionId;
    private String processTypeId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getProcessTypeId() {
        return processTypeId;
    }

    public void setProcessTypeId(String processTypeId) {
        this.processTypeId = processTypeId;
    }
}
