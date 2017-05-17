package net.myspring.future.modules.basic.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.DepotShop;
import net.myspring.util.cahe.annotation.CacheInput;

/**
 * Created by wangzm on 2017/5/13.
 */
public class DepotStoreDto extends DataDto<DepotShop>{

    private String depotId;
    private String name;
    private String type;
    //分组
    private String group;
    @CacheInput(inputKey = "depots",inputInstance = "depotId",outputInstance = "name")
    private String depotName;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
