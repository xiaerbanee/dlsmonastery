package net.myspring.basic.modules.sys.web.form;

import net.myspring.basic.common.form.DataForm;
import net.myspring.basic.modules.sys.domain.Backend;
import net.myspring.basic.modules.sys.domain.BackendModule;
import net.myspring.util.cahe.annotation.CacheInput;

/**
 * Created by wangzm on 2017/4/19.
 */
public class BackendModuleForm extends DataForm<BackendModule> {
    private String name;
    private String backendId;

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
}
