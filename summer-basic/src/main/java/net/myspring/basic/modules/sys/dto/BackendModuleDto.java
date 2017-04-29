package net.myspring.basic.modules.sys.dto;

import net.myspring.basic.common.dto.DataDto;
import net.myspring.basic.modules.sys.domain.Backend;
import net.myspring.basic.modules.sys.domain.BackendModule;
import net.myspring.util.cahe.annotation.CacheInput;

/**
 * Created by wangzm on 2017/4/19.
 */
public class BackendModuleDto extends DataDto<BackendModule> {
    private String code;
    private String name;
    private String backendId;
    @CacheInput(inputKey = "backends",inputInstance = "backendId",outputInstance = "name")
    private String backendName;

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

    public String getBackendId() {
        return backendId;
    }

    public void setBackendId(String backendId) {
        this.backendId = backendId;
    }

    public String getBackendName() {
        return backendName;
    }

    public void setBackendName(String backendName) {
        this.backendName = backendName;
    }
}
