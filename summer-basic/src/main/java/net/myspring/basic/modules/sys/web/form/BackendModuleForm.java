package net.myspring.basic.modules.sys.web.form;

import com.google.common.collect.Lists;
import net.myspring.common.form.BaseForm;
import net.myspring.basic.modules.sys.domain.BackendModule;
import net.myspring.basic.modules.sys.dto.BackendDto;

import java.util.List;

/**
 * Created by wangzm on 2017/4/19.
 */
public class BackendModuleForm extends BaseForm<BackendModule> {
    private String code;
    private String name;
    private String backendId;
    private List<BackendDto> backendList= Lists.newArrayList();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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
