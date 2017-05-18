package net.myspring.future.modules.basic.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.DepotShop;
import net.myspring.future.modules.basic.domain.DepotStore;
import net.myspring.util.cahe.annotation.CacheInput;

/**
 * Created by wangzm on 2017/5/13.
 */
public class DepotStoreDto extends DataDto<DepotStore>{

    private String depotId;
    private String type;
    private String officeId;
    private String taxName;
    private String delegateDepotId;
    //分组
    private String storeGroup;
    @CacheInput(inputKey = "depots",inputInstance = "depotId",outputInstance = "name")
    private String depotName;
    @CacheInput(inputKey = "offices",inputInstance = "officeId",outputInstance = "name")
    private String officeName;
    @CacheInput(inputKey = "depots",inputInstance = "delegateDepotId",outputInstance = "name")
    private String delegateDepotName;

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public String getDelegateDepotId() {
        return delegateDepotId;
    }

    public void setDelegateDepotId(String delegateDepotId) {
        this.delegateDepotId = delegateDepotId;
    }

    public String getStoreGroup() {
        return storeGroup;
    }

    public void setStoreGroup(String storeGroup) {
        this.storeGroup = storeGroup;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getDelegateDepotName() {
        return delegateDepotName;
    }

    public void setDelegateDepotName(String delegateDepotName) {
        this.delegateDepotName = delegateDepotName;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
