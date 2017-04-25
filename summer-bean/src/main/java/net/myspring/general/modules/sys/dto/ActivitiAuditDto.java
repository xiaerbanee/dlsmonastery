package net.myspring.general.modules.sys.dto;

/**
 * Created by wangzm on 2017/4/25.
 */
public class ActivitiAuditDto {
    private String processStatus;
    private String processFlowId;
    private String positionId;

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
}
