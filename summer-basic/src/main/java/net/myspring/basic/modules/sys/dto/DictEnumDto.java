package net.myspring.basic.modules.sys.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.basic.modules.sys.domain.DictEnum;

/**
 * Created by admin on 2017/4/5.
 */
public class DictEnumDto extends DataDto<DictEnum>{
    private Integer sort;
    private String category;
    private String value;

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
