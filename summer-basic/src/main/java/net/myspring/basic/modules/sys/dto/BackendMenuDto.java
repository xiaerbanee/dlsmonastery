package net.myspring.basic.modules.sys.dto;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.modules.sys.domain.Backend;
import net.myspring.basic.modules.sys.domain.BackendModule;

import java.util.List;
import java.util.Map;

/**
 * Created by wangzm on 2017/4/19.
 */
public class BackendMenuDto {

    private Backend backend;
    private Map<String,List<BackendModule>> backendModuleMap= Maps.newHashMap();

    public Backend getBackend() {
        return backend;
    }

    public void setBackend(Backend backend) {
        this.backend = backend;
    }

    public Map<String, List<BackendModule>> getBackendModuleMap() {
        return backendModuleMap;
    }

    public void setBackendModuleMap(Map<String, List<BackendModule>> backendModuleMap) {
        this.backendModuleMap = backendModuleMap;
    }
}
