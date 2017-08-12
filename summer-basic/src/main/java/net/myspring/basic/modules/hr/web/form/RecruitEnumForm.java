package net.myspring.basic.modules.hr.web.form;

import com.google.common.collect.Lists;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.form.BaseForm;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;

import java.util.List;

public class RecruitEnumForm extends BaseForm<RecruitForm> {

    private String category;
    private String value;

    private List<String> list= Lists.newArrayList();

    public List<String> getList() {
        if(CollectionUtil.isEmpty(list)&& StringUtils.isNotBlank(value)){
            this.list=StringUtils.getSplitList(value, CharConstant.COMMA);
        }
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
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
