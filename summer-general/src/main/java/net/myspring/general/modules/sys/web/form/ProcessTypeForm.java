package net.myspring.general.modules.sys.web.form;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.form.DataForm;
import net.myspring.basic.modules.hr.dto.PositionDto;
import net.myspring.basic.modules.sys.domain.ProcessType;
import net.myspring.basic.modules.sys.dto.ProcessFlowDto;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/4/6.
 */

public class ProcessTypeForm extends DataForm<ProcessType>{
    private String type;
    private String name;
    private String viewPermissionId;
    private String createPermissionId;
    private String companyId = "1";
    private Boolean auditFileType;
    private String remarks;
    private List<PositionDto> positionList= Lists.newArrayList();
    private List<ProcessFlowDto> processFlowDtoList = Lists.newArrayList();
    private Map<Boolean,String>  bools= Maps.newHashMap();

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

    public String getViewPermissionId() {
        return viewPermissionId;
    }

    public void setViewPermissionId(String viewPermissionId) {
        this.viewPermissionId = viewPermissionId;
    }

    public String getCreatePermissionId() {
        return createPermissionId;
    }

    public void setCreatePermissionId(String createPermissionId) {
        this.createPermissionId = createPermissionId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Boolean getAuditFileType() {
        return auditFileType;
    }

    public void setAuditFileType(Boolean auditFileType) {
        this.auditFileType = auditFileType;
    }

    public List<PositionDto> getPositionList() {
        return positionList;
    }

    public void setPositionList(List<PositionDto> positionList) {
        this.positionList = positionList;
    }

    public Map<Boolean, String> getBools() {
        return bools;
    }

    public void setBools(Map<Boolean, String> bools) {
        this.bools = bools;
    }

    public List<ProcessFlowDto> getProcessFlowDtoList() {
        return processFlowDtoList;
    }

    public void setProcessFlowDtoList(List<ProcessFlowDto> processFlowDtoList) {
        this.processFlowDtoList = processFlowDtoList;
    }
}
