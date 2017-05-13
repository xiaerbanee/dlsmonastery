package net.myspring.future.modules.basic.domain;

import net.myspring.future.common.domain.CompanyEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by liuj on 2017/5/12.
 */

@Entity
@Table(name="crm_depot_store")
public class DepotStore extends CompanyEntity<DepotStore> {
    private String depotId;
    //仓库类型(好机库good，坏机库bad，寄存机库deposit，淘汰机库disuse)
    private String type;
    //寄售对应门店
    private String delegateShopId;
    //分组
    private String group;

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

    public String getDelegateShopId() {
        return delegateShopId;
    }

    public void setDelegateShopId(String delegateShopId) {
        this.delegateShopId = delegateShopId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
