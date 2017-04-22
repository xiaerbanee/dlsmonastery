package net.myspring.general.modules.sys.web.form;

import net.myspring.general.common.dto.IdDto;
import net.myspring.general.common.form.IdForm;
import net.myspring.general.modules.sys.domain.ProcessFlow;

/**
 * Created by admin on 2017/4/5.
 */
public class ProcessFlowForm extends IdForm<ProcessFlow> {
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
