package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.crm.domain.DepotChange;
import net.myspring.util.cahe.annotation.CacheInput;

import java.time.LocalDate;

/**
 * Created by zhangyf on 2017/6/13.
 */
public class DepotChangeDto extends DataDto<DepotChange>{
    private String type;
    private LocalDate expiryDate;
    private String oldValue;
    private String newValue;
    private String depotId;
    @CacheInput(inputKey = "depots",inputInstance = "depotId",outputInstance = "name")
    private String depotName;
    private String status;
    private String auditRemarks;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuditRemarks() {
        return auditRemarks;
    }

    public void setAuditRemarks(String auditRemarks) {
        this.auditRemarks = auditRemarks;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
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

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }


    public Boolean getIsEditable(){
        if (RequestUtils.getAccountId().equals(getCreatedBy())|| RequestUtils.getAccountId().equalsIgnoreCase("1")){
            return true;
        }else {
            return false;
        }
    }
}
