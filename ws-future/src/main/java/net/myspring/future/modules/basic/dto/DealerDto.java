package net.myspring.future.modules.basic.dto;

import com.google.common.collect.Lists;
import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.Dealer;
import net.myspring.future.modules.basic.domain.Depot;

import java.util.List;

/**
 * Created by lihx on 2017/4/17.
 */
public class DealerDto extends DataDto<Dealer> {
    private String name;
    private String mobilePhone;
    private Boolean locked;
    private Boolean enabled;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
