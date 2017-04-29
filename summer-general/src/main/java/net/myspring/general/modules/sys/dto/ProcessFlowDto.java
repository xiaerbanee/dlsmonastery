package net.myspring.general.modules.sys.dto;

import com.google.common.collect.Lists;
import net.myspring.common.dto.DataDto;
import net.myspring.common.dto.IdDto;
import net.myspring.general.modules.sys.domain.ProcessFlow;

import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */
public class ProcessFlowDto extends IdDto<ProcessFlow> {
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

    public String getProcessTypeId() {
        return processTypeId;
    }

    public void setProcessTypeId(String processTypeId) {
        this.processTypeId = processTypeId;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }
}
