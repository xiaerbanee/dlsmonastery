package net.myspring.basic.modules.sys.dto;

import com.google.common.collect.Maps;
import net.myspring.basic.modules.sys.domain.Backend;
import net.myspring.basic.modules.sys.domain.BackendModule;

import java.util.List;
import java.util.Map;

/**
 * Created by wangzm on 2017/4/26.
 */
public class FrontMenuDto {
    private List<BackendMenuDto> backendList;
    private Map<String,List<BackendModuleMenuDto>> backendModuleMap= Maps.newHashMap();

    public List<BackendMenuDto> getBackendList() {
        return backendList;
    }

    public void setBackendList(List<BackendMenuDto> backendList) {
        this.backendList = backendList;
    }

    public Map<String, List<BackendModuleMenuDto>> getBackendModuleMap() {
        return backendModuleMap;
    }

    public void setBackendModuleMap(Map<String, List<BackendModuleMenuDto>> backendModuleMap) {
        this.backendModuleMap = backendModuleMap;
    }
}
