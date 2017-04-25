package net.myspring.basic.modules.hr.web.form;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.modules.hr.domain.AuditFile;
import net.myspring.basic.common.form.DataForm;
import net.myspring.general.modules.sys.dto.ProcessTypeDto;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/4/6.
 */

public class AuditFileForm extends DataForm<AuditFile> {
    private String processTypeId;
    private Map<Boolean, String> boolMap= Maps.newHashMap();
    private String title;
    private String content;
    private String attachment;
    private String processInstanceId;
    private String processStatus;
    private String processFlowId;
    private String positionId;
    private List<ProcessTypeDto> processTypeList=Lists.newArrayList();

    public List<ProcessTypeDto> getProcessTypeList() {
        return processTypeList;
    }

    public void setProcessTypeList(List<ProcessTypeDto> processTypeList) {
        this.processTypeList = processTypeList;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getProcessFlowId() {
        return processFlowId;
    }

    public void setProcessFlowId(String processFlowId) {
        this.processFlowId = processFlowId;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public Map<Boolean, String> getBoolMap() {
        return boolMap;
    }

    public void setBoolMap(Map<Boolean, String> boolMap) {
        this.boolMap = boolMap;
    }

    public String getProcessTypeId() {
        return processTypeId;
    }

    public void setProcessTypeId(String processTypeId) {
        this.processTypeId = processTypeId;
    }
}
