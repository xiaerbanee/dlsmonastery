package net.myspring.general.modules.sys.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.general.modules.sys.domain.ProcessTask;

/**
 * Created by liuj on 2017/4/22.
 */
public class ProcessTaskDto extends DataDto<ProcessTask> {
    private String name;
    private String extendId;
    private String status;
    private String officeId;
    private String positionId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtendId() {
        return extendId;
    }

    public void setExtendId(String extendId) {
        this.extendId = extendId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }
}
