package net.myspring.future.modules.api.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.api.domain.CarrierShop;

/**
 * Created by wangzm on 2017/7/5.
 */
public class CarrierShopDto extends DataDto<CarrierShop>{
    private String name;
    private String code;
    private String type;
    private String enabled;
    private String locked;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }
}
