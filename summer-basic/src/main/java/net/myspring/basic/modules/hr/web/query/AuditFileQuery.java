package net.myspring.basic.modules.hr.web.query;

import com.google.common.collect.Lists;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateTimeUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by lihx on 2017/4/7.
 */
public class AuditFileQuery {
    private String id;
    private String positionId;
    private String auditType;
    private List<String> officeIds;
    private String createdDateBTW;
    private LocalDateTime createdDateStart;
    private LocalDateTime createdDateEnd;
    private String officeName;
    private String officeId;
    private String createdByName;
    private String stageName;
    private String processTypeId;
    private String content;
    private String title;
    private String processflowName;
    private List<String> processFlowIdList=Lists.newArrayList();

    public List<String> getProcessFlowIdList() {
        return processFlowIdList;
    }

    public void setProcessFlowIdList(List<String> processFlowIdList) {
        this.processFlowIdList = processFlowIdList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPositionId() {
        if(auditType == null || auditType.equals("1")) {
            this.positionId = SecurityUtils.getPositionId();
        }else {
            this.positionId="";
        }
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    public List<String> getOfficeIds() {
        return officeIds;
    }

    public void setOfficeIds(List<String> officeIds) {
        this.officeIds = officeIds;
    }

    public LocalDateTime getCreatedDateStart() {
        if(createdDateStart==null&& StringUtils.isNotBlank(createdDateBTW)){
            String[] tempParamValues = createdDateBTW.split(" - ");
            this.createdDateStart= LocalDateTimeUtils.parse(tempParamValues[0]+ " 00:00:00");
        }
        return createdDateStart;
    }

    public void setCreatedDateStart(LocalDateTime createdDateStart) {
        this.createdDateStart = createdDateStart;
    }

    public LocalDateTime getCreatedDateEnd() {
        if(createdDateEnd==null&& StringUtils.isNotBlank(createdDateBTW)){
            String[] tempParamValues = createdDateBTW.split(" - ");
            this.createdDateEnd= LocalDateTimeUtils.parse(tempParamValues[1]+ " 23:59:59");
        }
        return createdDateEnd;
    }

    public void setCreatedDateEnd(LocalDateTime createdDateEnd) {
        this.createdDateEnd = createdDateEnd;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getProcessTypeId() {
        return processTypeId;
    }

    public void setProcessTypeId(String processTypeId) {
        this.processTypeId = processTypeId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProcessflowName() {
        return processflowName;
    }

    public void setProcessflowName(String processflowName) {
        this.processflowName = processflowName;
    }

    public String getCreatedDateBTW() {
        return createdDateBTW;
    }

    public void setCreatedDateBTW(String createdDateBTW) {
        this.createdDateBTW = createdDateBTW;
    }
}
