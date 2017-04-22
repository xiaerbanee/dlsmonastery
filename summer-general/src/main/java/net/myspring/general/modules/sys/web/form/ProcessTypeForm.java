package net.myspring.general.modules.sys.web.form;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.general.common.form.DataForm;
import net.myspring.general.modules.sys.domain.ProcessType;
import net.myspring.general.modules.sys.dto.ProcessFlowDto;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/4/6.
 */

public class ProcessTypeForm extends DataForm<ProcessType> {
    private String type;
    private String name;
    private String viewPermissionId;
    private String createPermissionId;
    private Boolean auditFileType;
    private List<ProcessFlowForm> processFlowFormList = Lists.newArrayList();

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

    public Boolean getAuditFileType() {
        return auditFileType;
    }

    public void setAuditFileType(Boolean auditFileType) {
        this.auditFileType = auditFileType;
    }

    public List<ProcessFlowForm> getProcessFlowFormList() {
        return processFlowFormList;
    }

    public void setProcessFlowFormList(List<ProcessFlowForm> processFlowFormList) {
        this.processFlowFormList = processFlowFormList;
    }
}
