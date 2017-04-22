package net.myspring.basic.modules.sys.web.form;

import com.google.common.collect.Lists;
import net.myspring.basic.common.form.DataForm;
import net.myspring.basic.modules.sys.domain.Backend;
import net.myspring.basic.modules.sys.domain.BackendModule;
import net.myspring.basic.modules.sys.dto.BackendDto;
import net.myspring.util.cahe.annotation.CacheInput;

import java.util.List;

/**
 * Created by wangzm on 2017/4/19.
 */
public class BackendModuleForm extends DataForm<BackendModule> {
    private String name;
    private String backendId;
    private List<BackendDto> backendList= Lists.newArrayList();

    public List<BackendDto> getBackendList() {
        return backendList;
    }

    public void setBackendList(List<BackendDto> backendList) {
        this.backendList = backendList;
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
}
