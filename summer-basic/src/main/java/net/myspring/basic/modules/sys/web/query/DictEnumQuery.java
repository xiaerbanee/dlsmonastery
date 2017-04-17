package net.myspring.basic.modules.sys.web.query;

import com.google.common.collect.Lists;
import net.myspring.basic.common.query.BaseQuery;
import net.myspring.basic.common.query.PageQuery;
import net.myspring.basic.modules.sys.domain.DictEnum;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateTimeUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by lihx on 2017/4/7.
 */
public class DictEnumQuery extends PageQuery<DictEnum> {
    private String createdDateBTW;
    private String category;
    private String value;
    private LocalDateTime createdDateStart;
    private LocalDateTime createdDateEnd;
    private List<String> categoryList= Lists.newArrayList();

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

    public LocalDateTime getCreatedDateStart() {
        return createdDateStart;
    }

    public void setCreatedDateStart(LocalDateTime createdDateStart) {
        if(createdDateEnd==null&& StringUtils.isNotBlank(createdDateBTW)){
            String[] tempParamValues = createdDateBTW.split(" - ");
            this.createdDateStart= LocalDateTimeUtils.parse(tempParamValues[0]+"00:00:00");
        }
        this.createdDateStart = createdDateStart;
    }

    public LocalDateTime getCreatedDateEnd() {
        if(createdDateEnd==null&& StringUtils.isNotBlank(createdDateBTW)){
            String[] tempParamValues = createdDateBTW.split(" - ");
            this.createdDateEnd= LocalDateTimeUtils.parse(tempParamValues[1]+ " 23:59:59");
        }
        return createdDateEnd;
    }

    public void setCreatedDateEnd(LocalDateTime createdDateEnd) {
        this.createdDateEnd = createdDateEnd;
    }

    public String getCreatedDateBTW() {
        return createdDateBTW;
    }

    public void setCreatedDateBTW(String createdDateBTW) {
        this.createdDateBTW = createdDateBTW;
    }

    public List<String> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<String> categoryList) {
        this.categoryList = categoryList;
    }
}
