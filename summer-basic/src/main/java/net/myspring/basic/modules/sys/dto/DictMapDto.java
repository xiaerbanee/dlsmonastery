package net.myspring.basic.modules.sys.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.basic.modules.sys.domain.DictMap;

/**
 * Created by admin on 2017/4/1.
 */
public class DictMapDto  extends DataDto<DictMap> {
    private String category;
    private String name;
    private String value;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
