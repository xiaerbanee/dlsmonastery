package net.myspring.future.modules.basic.dto;

import net.myspring.future.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.Pricesystem;

/**
 * Created by lihx on 2017/4/17.
 */
public class PricesystemDto extends DataDto<Pricesystem> {
    private String name;
    private Integer sort;
    private Boolean enabled;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
