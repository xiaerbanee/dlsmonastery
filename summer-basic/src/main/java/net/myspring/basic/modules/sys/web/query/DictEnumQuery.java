package net.myspring.basic.modules.sys.web.query;

import com.google.common.collect.Lists;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateTimeUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by lihx on 2017/4/7.
 */
public class DictEnumQuery  {
    private String createdDate;
    private String category;
    private String value;
    private List<String> categoryList= Lists.newArrayList();

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
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
    public List<String> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<String> categoryList) {
        this.categoryList = categoryList;
    }

    public LocalDateTime getCreatedDateStart() {
        if(StringUtils.isNotBlank(createdDate)) {
            return LocalDateTimeUtils.parse(createdDate.split(" - ")[0] + " 00:00:00");
        } else {
            return null;
        }
    }

    public LocalDateTime getCreatedDateEnd() {
        if(StringUtils.isNotBlank(createdDate)) {
            return LocalDateTimeUtils.parse(createdDate.split(" - ")[1] + " 23:59:59");
        } else {
            return null;
        }
    }

}
