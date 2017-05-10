package net.myspring.general.modules.sys.dto;


import com.google.common.collect.Lists;
import net.myspring.common.dto.DataDto;
import net.myspring.general.modules.sys.domain.ProcessType;

import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */
public class ProcessTypeDto extends DataDto<ProcessType> {
    private String type;
    private String name;
    private String viewPositionIds;
    private String createdPositionIds;
    private Boolean auditFileType;

    private List<ProcessFlowDto> processFlowDtoList = Lists.newArrayList();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getViewPositionIds() {
        return viewPositionIds;
    }

    public void setViewPositionIds(String viewPositionIds) {
        this.viewPositionIds = viewPositionIds;
    }

    public String getCreatedPositionIds() {
        return createdPositionIds;
    }

    public void setCreatedPositionIds(String createdPositionIds) {
        this.createdPositionIds = createdPositionIds;
    }

    public Boolean getAuditFileType() {
        return auditFileType;
    }

    public void setAuditFileType(Boolean auditFileType) {
        this.auditFileType = auditFileType;
    }

    public List<ProcessFlowDto> getProcessFlowDtoList() {
        return processFlowDtoList;
    }

    public void setProcessFlowDtoList(List<ProcessFlowDto> processFlowDtoList) {
        this.processFlowDtoList = processFlowDtoList;
    }
}
