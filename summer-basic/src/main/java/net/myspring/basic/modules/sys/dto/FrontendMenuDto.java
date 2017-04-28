package net.myspring.basic.modules.sys.dto;

import com.google.common.collect.Maps;
import net.myspring.basic.modules.sys.domain.Backend;
import net.myspring.basic.modules.sys.domain.BackendModule;

import java.util.List;
import java.util.Map;

/**
 * Created by wangzm on 2017/4/26.
 */
public class FrontendMenuDto {

    private String id;
    private String name;
    private String code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
