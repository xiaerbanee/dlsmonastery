package net.myspring.basic.modules.hr.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.basic.modules.hr.domain.OfficeChange;
import net.myspring.util.cahe.annotation.CacheInput;

/**
 * Created by admin on 2017/4/5.
 */
public class OfficeChangeDto extends DataDto<OfficeChange> {

    private String type;
    private String officeId;
    @CacheInput(inputKey ="offices",inputInstance = "officeId",outputInstance = "name")
    private String officeName;
    private String oldLabel;
    private String newLabel;
    private String processStatus;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getOldLabel() {
        return oldLabel;
    }

    public void setOldLabel(String oldLabel) {
        this.oldLabel = oldLabel;
    }

    public String getNewLabel() {
        return newLabel;
    }

    public void setNewLabel(String newLabel) {
        this.newLabel = newLabel;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }
}
