package net.myspring.basic.modules.hr.web.form;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.modules.hr.domain.AuditFile;
import net.myspring.common.form.DataForm;
import net.myspring.general.modules.sys.dto.ActivitiDetailDto;
import net.myspring.util.cahe.annotation.CacheInput;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/4/6.
 */

public class AuditFileForm extends DataForm<AuditFile> {
    private String processTypeName;
    private Map<String, String> boolMap= Maps.newHashMap();
    private String title;
    private String content;
    private String attachment;
    private String processInstanceId;
    private String processStatus;
    private String processFlowId;
    private String positionId;
    private String createdBy;
    @CacheInput(inputKey = "accounts",inputInstance = "createdBy",outputInstance = "loginName")
    private String createdByName;
    private LocalDateTime createdDate;
    private boolean locked;
    private List<ActivitiDetailDto> activitiDetailList= Lists.newArrayList();

    public List<ActivitiDetailDto> getActivitiDetailList() {
        return activitiDetailList;
    }

    public void setActivitiDetailList(List<ActivitiDetailDto> activitiDetailList) {
        this.activitiDetailList = activitiDetailList;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
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

    public Map<String, String> getBoolMap() {
        return boolMap;
    }

    public void setBoolMap(Map<String, String> boolMap) {
        this.boolMap = boolMap;
    }

    public String getProcessTypeName() {
        return processTypeName;
    }

    public void setProcessTypeName(String processTypeName) {
        this.processTypeName = processTypeName;
    }
}
