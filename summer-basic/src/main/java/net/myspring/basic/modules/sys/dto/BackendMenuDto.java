package net.myspring.basic.modules.sys.dto;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.modules.sys.domain.Backend;

import java.util.List;
import java.util.Map;

/**
 * Created by wangzm on 2017/4/19.
 */
public class BackendMenuDto {

    private Backend backend;
    private Map<String,List<BackendModuleMenuItemDto>> backendModuleMenuItemDtoMap= Maps.newHashMap();

    public Backend getBackend() {
        return backend;
    }

    public void setBackend(Backend backend) {
        this.backend = backend;
    }

    public Map<String, List<BackendModuleMenuItemDto>> getBackendModuleMenuItemDtoMap() {
        return backendModuleMenuItemDtoMap;
    }

    public void setBackendModuleMenuItemDtoMap(Map<String, List<BackendModuleMenuItemDto>> backendModuleMenuItemDtoMap) {
        this.backendModuleMenuItemDtoMap = backendModuleMenuItemDtoMap;
    }
}
