package net.myspring.tool.modules.vivo.dto;

import com.google.common.collect.Maps;

import java.util.Map;

public class FactoryOrderDto {
    private String factoryCode;
    private String factoryPassword;
    private String code;
    private String password;

    private Map<String,Object> extra = Maps.newHashMap();

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

    public String getFactoryPassword() {
        return factoryPassword;
    }

    public void setFactoryPassword(String factoryPassword) {
        this.factoryPassword = factoryPassword;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, Object> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
    }
}
