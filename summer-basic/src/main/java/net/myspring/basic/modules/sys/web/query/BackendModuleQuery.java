package net.myspring.basic.modules.sys.web.query;

import net.myspring.basic.modules.sys.domain.Backend;

/**
 * Created by wangzm on 2017/4/19.
 */
public class BackendModuleQuery {
    private String name;
    private String backendName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBackendName() {
        return backendName;
    }

    public void setBackendName(String backendName) {
        this.backendName = backendName;
    }
}
