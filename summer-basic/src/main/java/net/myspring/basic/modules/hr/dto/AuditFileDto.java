package net.myspring.basic.modules.hr.dto;

import com.google.common.collect.Lists;
import com.sun.org.apache.regexp.internal.RE;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.common.dto.DataDto;
import net.myspring.basic.modules.hr.domain.AuditFile;
import net.myspring.common.enums.AuditTypeEnum;
import net.myspring.general.modules.sys.dto.ActivitiDetailDto;
import net.myspring.util.cahe.annotation.CacheInput;

import javax.persistence.Transient;
import java.util.List;

/**
 * Created by admin on 2017/4/5.
 */
public class AuditFileDto extends DataDto<AuditFile> {
    private String title;
    private String processStatus;
    private String memo;
    private String processTypeId;
    private String areaId;
    private String officeId;
    private String content;
    private List<ActivitiDetailDto> activitiDetailList= Lists.newArrayList();
    private String attachment;
    private String processFlowId;
    private String positionId;
    private boolean auditable;
    private boolean editable;

    @CacheInput(inputKey = "offices",inputInstance = "areaId",outputInstance = "name")
    private String areaName;
    @CacheInput(inputKey = "offices",inputInstance = "officeId",outputInstance = "name")
    private String officeName;
    @CacheInput(inputKey = "processTypes",inputInstance = "processTypeId",outputInstance = "name")
    private String processTypeName;
    private boolean locked;

    public boolean getLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
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

    public List<ActivitiDetailDto> getActivitiDetailList() {
        return activitiDetailList;
    }

    public void setActivitiDetailList(List<ActivitiDetailDto> activitiDetailList) {
        this.activitiDetailList = activitiDetailList;
    }

    public String getProcessTypeId() {
        return processTypeId;
    }

    public void setProcessTypeId(String processTypeId) {
        this.processTypeId = processTypeId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getProcessTypeName() {
        return processTypeName;
    }

    public void setProcessTypeName(String processTypeName) {
        this.processTypeName = processTypeName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
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

    public Boolean getAuditable() {
        if (processFlowId == null) {
            return false;
        } else {
            return RequestUtils.getPositionId().equals(positionId) || RequestUtils.getAdmin();
        }
    }

    public Boolean getEditable() {
        if ((!getLocked() && !getFinished()) && RequestUtils.getAccountId()!= null && (getCreatedBy().equals(RequestUtils.getAccountId()) || RequestUtils.getAdmin())) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean getFinished() {
        return AuditTypeEnum.PASS.getValue().equals(processStatus) || AuditTypeEnum.NOT_PASS.getValue().equals(processStatus);
    }

}
