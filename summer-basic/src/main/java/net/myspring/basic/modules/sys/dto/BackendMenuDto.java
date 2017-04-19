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
    private List<BackendModuleMenuItemDto> backendModuleMenuItemDtoList= Lists.newArrayList();

    public Backend getBackend() {
        return backend;
    }

    public void setBackend(Backend backend) {
        this.backend = backend;
    }

    public List<BackendModuleMenuItemDto> getBackendModuleMenuItemDtoList() {
        return backendModuleMenuItemDtoList;
    }

    public void setBackendModuleMenuItemDtoList(List<BackendModuleMenuItemDto> backendModuleMenuItemDtoList) {
        this.backendModuleMenuItemDtoList = backendModuleMenuItemDtoList;
    }
}
