package net.myspring.basic.modules.hr.web.form;

import com.google.common.collect.Maps;
import net.myspring.common.form.BaseForm;
import net.myspring.basic.modules.hr.dto.DutyDto;

import java.util.Map;

/**
 * Created by wangzm on 2017/4/17.
 */
public class DutyForm extends BaseForm<DutyDto> {
    private String dutyType;
    private Object item;
    private Map<String,String> boolMap= Maps.newHashMap();

    public String getDutyType() {
        return dutyType;
    }

    public void setDutyType(String dutyType) {
        this.dutyType = dutyType;
    }

    public Object getItem() {
        return item;
    }

    public void setItem(Object item) {
        this.item = item;
    }

    public Map<String, String> getBoolMap() {
        return boolMap;
    }

    public void setBoolMap(Map<String, String> boolMap) {
        this.boolMap = boolMap;
    }
}
