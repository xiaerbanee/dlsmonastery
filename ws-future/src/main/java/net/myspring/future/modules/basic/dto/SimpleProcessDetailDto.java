package net.myspring.future.modules.basic.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.SimpleProcessDetail;


public class SimpleProcessDetailDto extends DataDto<SimpleProcessDetail> {

    private String simpleProcessId;
    private String processStatus;
    private String opinion;

    public String getSimpleProcessId() {
        return simpleProcessId;
    }

    public void setSimpleProcessId(String simpleProcessId) {
        this.simpleProcessId = simpleProcessId;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }
}
