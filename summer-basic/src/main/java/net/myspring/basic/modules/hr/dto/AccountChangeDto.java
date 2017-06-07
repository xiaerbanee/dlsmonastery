package net.myspring.basic.modules.hr.dto;


import net.myspring.common.dto.DataDto;
import net.myspring.basic.modules.hr.domain.AccountChange;
import net.myspring.util.cahe.annotation.CacheInput;

public class AccountChangeDto extends DataDto<AccountChange> {

    private String oldValue;
    private String newValue;
    private String oldLabel;
    private String newLabel;
    private String type;
    private String processStatus;
    private String areaId;
    private String officeId;
    @CacheInput(inputKey = "offices",inputInstance = "areaId",outputInstance = "name")
    private String areaName;

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

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
