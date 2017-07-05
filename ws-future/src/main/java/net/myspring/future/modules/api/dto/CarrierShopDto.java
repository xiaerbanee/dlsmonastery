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
    private boolean locked;

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

    public boolean getLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
