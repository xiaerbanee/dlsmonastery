package net.myspring.future.modules.basic.web.query;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by lihx on 2017/4/19.
 */
public class ProductTypeQuery {
    private String code;
    private String name;
    private Map<Boolean,String> boolMap= Maps.newHashMap();

    public Map<Boolean, String> getBoolMap() {
        return boolMap;
    }

    public void setBoolMap(Map<Boolean, String> boolMap) {
        this.boolMap = boolMap;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
