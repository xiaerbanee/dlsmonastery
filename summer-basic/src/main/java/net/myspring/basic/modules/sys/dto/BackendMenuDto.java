package net.myspring.basic.modules.sys.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Created by wangzm on 2017/4/19.
 */
public class BackendMenuDto {

    private String id;
    private String name;
    private String code;
    private List<BackendModuleMenuDto> backendModuleList = Lists.newArrayList();
    @JsonIgnore
    private Map<String,BackendModuleMenuDto> backendModuleMenuDtoMap = Maps.newTreeMap();

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<BackendModuleMenuDto> getBackendModuleList() {
        if(backendModuleMenuDtoMap.size()>0) {
            backendModuleList =  Lists.newArrayList(backendModuleMenuDtoMap.values());
        }
        return backendModuleList;
    }

    public void setBackendModuleList(List<BackendModuleMenuDto> backendModuleList) {
        this.backendModuleList = backendModuleList;
    }

    public Map<String, BackendModuleMenuDto> getBackendModuleMenuDtoMap() {
        return backendModuleMenuDtoMap;
    }

    public void setBackendModuleMenuDtoMap(Map<String, BackendModuleMenuDto> backendModuleMenuDtoMap) {
        this.backendModuleMenuDtoMap = backendModuleMenuDtoMap;
    }
}
