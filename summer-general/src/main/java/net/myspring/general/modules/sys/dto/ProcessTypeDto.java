package net.myspring.general.modules.sys.dto;


import com.google.common.collect.Lists;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.dto.DataDto;
import net.myspring.general.modules.sys.domain.ProcessType;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;

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

    private List<ProcessFlowDto> processFlowList=Lists.newArrayList();
    private List<String> viewPositionIdList=Lists.newArrayList();
    private List<String> createdPositionIdList=Lists.newArrayList();

    public List<String> getViewPositionIdList() {
        if(CollectionUtil.isEmpty(viewPositionIdList)&& StringUtils.isNotBlank(viewPositionIds)){
            this.viewPositionIdList=StringUtils.getSplitList(viewPositionIds, CharConstant.COMMA);
        }
        return viewPositionIdList;
    }

    public void setViewPositionIdList(List<String> viewPositionIdList) {
        this.viewPositionIdList = viewPositionIdList;
    }

    public List<String> getCreatedPositionIdList() {
        if(CollectionUtil.isEmpty(createdPositionIdList)&& StringUtils.isNotBlank(createdPositionIds)){
            this.createdPositionIdList=StringUtils.getSplitList(createdPositionIds, CharConstant.COMMA);
        }
        return createdPositionIdList;
    }

    public void setCreatedPositionIdList(List<String> createdPositionIdList) {
        this.createdPositionIdList = createdPositionIdList;
    }

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

    public List<ProcessFlowDto> getProcessFlowList() {
        return processFlowList;
    }

    public void setProcessFlowList(List<ProcessFlowDto> processFlowList) {
        this.processFlowList = processFlowList;
    }
}
