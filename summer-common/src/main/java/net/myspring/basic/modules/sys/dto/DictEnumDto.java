package net.myspring.basic.modules.sys.dto;


/**
 * Created by admin on 2017/4/5.
 */
public class DictEnumDto{
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
